package com.dragonfly.client.connection;

import com.dragonfly.entity.NotificationLog;
import com.dragonfly.entity.SensorLog;
import com.dragonfly.util.HTTPResponse;
import com.dragonfly.util.HTTPUtils;
import com.dragonfly.util.UserCache;
import com.google.gson.Gson;

import java.io.IOException;

public class Notification {

    private static final Gson gson = new Gson();

    public static void logSensor(SensorLog sl) throws IOException {
        String body = gson.toJson(sl);
        HTTPResponse r = HTTPUtils.post(HTTPUtils.REST_BASE + String.format("/unit/sensorlog?username=%s&tokenid=%s&unitid=%d",
                UserCache.getUsername(), UserCache.getTokenID(), UserCache.getUnitID()), body);

        if (r.getHttpCode() != 200)
            throw new IOException("Error when creating sensor log: " + r.getContent());
    }

    public static void logNotification(NotificationLog nl) throws IOException {
        String body = gson.toJson(nl);
        HTTPResponse r = HTTPUtils.post(HTTPUtils.REST_BASE + String.format("/unit/notificationlog?username=%s&tokenid=%s&unitid=%d",
                UserCache.getUsername(), UserCache.getTokenID(), UserCache.getUnitID()), body);

        if (r.getHttpCode() != 200)
            throw new IOException("Error when creating notification log: " + r.getContent());
    }
}
