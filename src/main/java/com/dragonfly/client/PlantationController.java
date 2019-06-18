package com.dragonfly.client;

import com.dragonfly.client.connection.Notification;
import com.dragonfly.client.hardware.DHTSensor;
import com.dragonfly.client.hardware.Hygrometer;
import com.dragonfly.client.hardware.Pump;
import com.dragonfly.client.hardware.Rotor;
import com.dragonfly.entity.NotificationLog;
import com.dragonfly.entity.Plant;
import com.dragonfly.entity.Plantation;
import com.dragonfly.entity.SensorLog;
import com.dragonfly.meteorology.ForecastResult;
import com.dragonfly.meteorology.MetereologyService;
import com.dragonfly.meteorology.entity.Forecast;
import com.dragonfly.util.HTTPResponse;
import com.dragonfly.util.HTTPUtils;
import com.dragonfly.util.UserCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlantationController {
    private static final Gson gson = new Gson();
    private static final Type plantationListType = new TypeToken<ArrayList<Plantation>>(){}.getType();
    private static final Type plantListType = new TypeToken<ArrayList<Plant>>(){}.getType();

    private static final float IN_WATER_PLANT_MINIMUM_HUMIDITY = 94.0f;
    private static final float WET_PLANT_MINIMUM_HUMIDITY = 85.0f;
    private static final float WET_MESIC_PLANT_MINIMUM_HUMIDITY = 67.0f;
    private static final float MESIC_PLANT_MINIMUM_HUMIDITY = 60.0f;
    private static final float MESIC_DRY_PLANT_MINIMUM_HUMIDITY = 48.0f;
    private static final float DRY_PLANT_MINIMUM_HUMIDITY = 40.0f;

    private static final float UNIT_OVERHEAT_TEMPERATURE = 30.0f;

    private static final String STORM_NOTIFICATION_MSG = "A storm is predicted, please provide shelter to your plants";
    private static final String UNIT_OVERHEAT_NOTIFICATION_LOG = "The unit is overheating, please do not let the unit under direct sun.";

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // The temperature and humidity sensor attached to the unit
    private DHTSensor dhtSensor;
    // The rotor that carries the tube
    private Rotor rotor;
    // The pump
    private Pump pump;
    // Maps each plant name to the corresponding plant name
    private Map<String, Plant> plantMap;
    // Maps each hygrometer to the corresponding sector
    private Map<Integer, Hygrometer> hygrometerMap;
    private List<Plantation> currentPlantation;
    private boolean forceWater;

    public PlantationController(Rotor r, Pump p, boolean forceWater) throws IOException {
        LOG.info("Initializing plant controller");

        rotor = r;
        pump = p;
        LOG.info("Initializing DHT sensor");
        dhtSensor = new DHTSensor();
        plantMap = new HashMap<>();
        hygrometerMap = new HashMap<>();

        // Get current weather and humidity to calibrate dht sensor
        LOG.info("Fetching meteorology to calibrate dht sensor");
        Forecast f = MetereologyService.getCurrentWeather();
        dhtSensor.calibrate(f.getTemperature(), (float)f.getHumidity());
        LOG.info("Done");
        // Fetch all plants from server
        getAllPlantsFromServer();
        // Fetch plantations for this unit and update the hygrometer map
        getPlantationsFromServer();
        this.forceWater = forceWater;
    }

    public void update() throws IOException {
        executeSmartWaterProcedure();
        readUnitDHTSensor();

        LOG.info("End of cycle, going to sleep.");

        if (forceWater)
            forceWater = false;
    }

    private void executeSmartWaterProcedure() throws IOException {
        LOG.info("Executing smart watering procedure.");


        ForecastResult fr = MetereologyService.isItGoingToRainInNext24h();

        if (fr == ForecastResult.STORM_PREDICTED) {
            NotificationLog nl = new NotificationLog(UserCache.getUnitID(), STORM_NOTIFICATION_MSG, 3, false);
            Notification.logNotification(nl);
        }

        // For every plantation, check if plant requires watering
        for (Plantation p : currentPlantation)
        {
            Plant plant = plantMap.get(p.getSowedPlant());
            boolean willWater = forceWater;
            float plantHumidity = hygrometerMap.get(p.getSector()).readHumidity();
            float targetHumidity = 0.0f;

            switch (plant.getWaterRequirementsEnum())
            {
                case IN_WATER:
                    if (plantHumidity <= IN_WATER_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = IN_WATER_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
                case WET:
                    if (plantHumidity <= WET_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = WET_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
                case WET_MESIC:
                    if (plantHumidity <= WET_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = WET_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
                case MESIC:
                    // If it rains, don't bother watering
                    if (fr == ForecastResult.RAIN_PREDICTED) continue;

                    if (plantHumidity <= MESIC_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = MESIC_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
                case DRY_MESIC:
                    // If it rains, don't bother watering
                    if (fr == ForecastResult.RAIN_PREDICTED) continue;

                    if (plantHumidity <= MESIC_DRY_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = MESIC_DRY_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
                case DRY:
                    // If it rains or drizzles, don't bother watering
                    if (fr == ForecastResult.RAIN_PREDICTED || fr == ForecastResult.DRIZZLE_PREDICTED) continue;

                    if (plantHumidity <= DRY_PLANT_MINIMUM_HUMIDITY)
                        willWater = true;
                    targetHumidity = DRY_PLANT_MINIMUM_HUMIDITY + 5.0f;
                    break;
            }

            if (willWater) waterPlant(p.getSector(), plantHumidity, targetHumidity);
            logSensorDataForPlantation(willWater, p.getSector());
            LOG.info(String.format("Updating plant %s at sector %d. Has watered: %s", p.getSowedPlant(), p.getSector(), willWater));
        }
    }

    private void waterPlant(int sector, float curHumidity, float targetHumidity) throws IOException {
        LOG.info(String.format("Watering plant at sector %d.", sector));
        switch (sector)
        {
            case 0:
                rotor.rotate(-90);
                break;
            case 1:
                break;
            case 2:
                rotor.rotate(90);
                break;
            case 3:
                rotor.rotate(180);
                break;
        }

        float readHumidity;
        do {
            pump.turnOn();
            LOG.info("Turning pump on.");
            // Leave pump turned on for 4 seconds
            sleep(4000);
            LOG.info("Turning pump off.");
            pump.turnOff();

            readHumidity = hygrometerMap.get(sector).readHumidity();
            sleep(200);
        } while (readHumidity >= targetHumidity);

        rotor.reset();
        rotor.turnOffPins();
    }

    private void logSensorDataForPlantation(boolean wasJustWatered, int sector) throws IOException {
        SensorLog sl = new SensorLog(UserCache.getUnitID(), dhtSensor.getTemperature(), dhtSensor.getHumidity(), wasJustWatered, sector);
        Notification.logSensor(sl);
    }

    private void readUnitDHTSensor() throws IOException {
        if (dhtSensor.getTemperature() > UNIT_OVERHEAT_TEMPERATURE)
        {
            LOG.warning(UNIT_OVERHEAT_NOTIFICATION_LOG);
            NotificationLog nl = new NotificationLog(UserCache.getUnitID(), UNIT_OVERHEAT_NOTIFICATION_LOG, 3, false);
            Notification.logNotification(nl);
        }
    }

    private void getAllPlantsFromServer() throws IOException {
        LOG.info("Fetching all plants from server");
        HTTPResponse r = HTTPUtils.get(HTTPUtils.REST_BASE + "/plants");

        if (r.getHttpCode() != 200)
            throw new IOException("Could not fetch plants: " + r.getContent());

        List<Plant> plants = gson.fromJson(r.getContent(), plantListType);
        for (Plant p : plants)
            plantMap.put(p.getName(), p);
    }

    private void getPlantationsFromServer() throws IOException {
        LOG.info("Fetching all plantations from server");

        HTTPResponse r = HTTPUtils.get(HTTPUtils.REST_BASE + String.format("/plantation?username=%s&tokenid=%s&unitid=%d",
                UserCache.getUsername(), UserCache.getTokenID(), UserCache.getUnitID()));

        if (r.getHttpCode() != 200)
            throw new IOException("Could not fetch plantations: " + r.getContent());

        currentPlantation = gson.fromJson(r.getContent(), plantationListType);

        hygrometerMap.clear();
        for (Plantation p : currentPlantation)
            hygrometerMap.put(p.getSector(), new Hygrometer(p.getSector()));
    }

    private void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {}
    }
}
