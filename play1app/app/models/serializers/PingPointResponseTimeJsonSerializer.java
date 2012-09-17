package models.serializers;

import com.google.gson.*;
import models.PingPoint;

import java.lang.reflect.Type;

/**
 * @author Niko Schmuck
 */
public class PingPointResponseTimeJsonSerializer implements JsonSerializer<PingPoint> {

    public static PingPointResponseTimeJsonSerializer instance;

    private PingPointResponseTimeJsonSerializer() {
    }

    public static PingPointResponseTimeJsonSerializer get() {
        if (instance == null)
            instance = new PingPointResponseTimeJsonSerializer();
        return instance;
    }

    @Override
    public JsonElement serialize(PingPoint point, Type type, JsonSerializationContext context) {
        JsonArray obj = new JsonArray();
        obj.add(new JsonPrimitive(point.timestamp.getTime()));
        obj.add(new JsonPrimitive(point.responseTime));
        return obj;
    }
    
}