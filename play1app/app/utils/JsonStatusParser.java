package utils;

import com.google.gson.JsonObject;
import models.PingPoint;

/**
 * @author Niko Schmuck
 *
 */
public final class JsonStatusParser {

    public static void enrich(final JsonObject root, PingPoint point) {
        if (root.has("localName"))   point.hostname   = root.get("localName").getAsString();
        if (root.has("maxMemory"))   point.maxMemory  = root.get("maxMemory").getAsInt();
        if (root.has("freeMemory"))  point.freeMemory = root.get("freeMemory").getAsInt();
        if (root.has("nrThreads"))   point.nrThreads  = root.get("nrThreads").getAsInt();
    }

}
