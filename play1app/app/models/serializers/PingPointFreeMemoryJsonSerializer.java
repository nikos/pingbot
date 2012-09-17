package models.serializers;

import com.google.gson.*;
import models.PingPoint;

import java.lang.reflect.Type;

/**
 * @author Niko Schmuck
 */
public class PingPointFreeMemoryJsonSerializer implements JsonSerializer<PingPoint> {

    public static PingPointFreeMemoryJsonSerializer instance;

    private PingPointFreeMemoryJsonSerializer() {
    }

    public static PingPointFreeMemoryJsonSerializer get() {
        if (instance == null)
            instance = new PingPointFreeMemoryJsonSerializer();
        return instance;
    }

    @Override
    public JsonElement serialize(PingPoint point, Type type, JsonSerializationContext context) {
        JsonArray obj = new JsonArray();
        obj.add(new JsonPrimitive(point.timestamp.getTime()));
        obj.add(new JsonPrimitive(point.freeMemory));
        return obj;
    }
    
}