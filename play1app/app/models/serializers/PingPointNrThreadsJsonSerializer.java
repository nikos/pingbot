package models.serializers;

import com.google.gson.*;
import models.PingPoint;

import java.lang.reflect.Type;

/**
 * @author Niko Schmuck
 */
public class PingPointNrThreadsJsonSerializer implements JsonSerializer<PingPoint> {

    public static PingPointNrThreadsJsonSerializer instance;

    private PingPointNrThreadsJsonSerializer() {
    }

    public static PingPointNrThreadsJsonSerializer get() {
        if (instance == null)
            instance = new PingPointNrThreadsJsonSerializer();
        return instance;
    }

    @Override
    public JsonElement serialize(PingPoint point, Type type, JsonSerializationContext context) {
        JsonArray obj = new JsonArray();
        obj.add(new JsonPrimitive(point.timestamp.getTime()));
        obj.add(new JsonPrimitive(point.nrThreads));
        return obj;
    }
    
}