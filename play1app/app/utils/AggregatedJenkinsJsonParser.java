package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.PingPoint;

/**
 * @author Niko Schmuck
 *
 */
public final class AggregatedJenkinsJsonParser {

    public static void enrich(final JsonObject root, PingPoint point) {
        JsonArray jobs = root.getAsJsonArray("jobs");
        boolean statusOK = true;
        String failedJobName = null;
        for (JsonElement job : jobs) {
            JsonObject jsonObj = job.getAsJsonObject();
            String color = jsonObj.get("color").getAsString();
            String jobName = jsonObj.get("name").getAsString();
            if (color.equalsIgnoreCase("red")) {
                statusOK = false;
                failedJobName = jobName;
                break;
            }
        }

        if (!statusOK) {
            point.statusCode = 500;
            point.statusMessage = "Failed job: " + failedJobName;
        }
    }

}
