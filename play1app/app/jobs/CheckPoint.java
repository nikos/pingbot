package jobs;

import com.google.gson.JsonObject;
import models.AvailabilityChange;
import models.MonitorResource;
import models.PingPoint;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.WS;
import utils.JsonStatusParser;

import java.util.Date;
import java.util.List;

import static models.AvailabilityChange.ChangeType.FROM_OK_TO_UNAVAILABLE;
import static models.AvailabilityChange.ChangeType.FROM_UNAVAILABLE_TO_OK;

/**
 * The 'heart' of pingbot: checks in regular intervals if
 * a resource is still available and persists in DB result.
 *
 * @author Niko Schmuck
 */
@OnApplicationStart
@Every("1mn")
public class CheckPoint extends Job {

    public void doJob() throws Exception {
        Logger.info("pinging all resources ...");
        List<MonitorResource> resources = MonitorResource.findAll();
        for (MonitorResource resource : resources) {
            WS.WSRequest request = WS.url(resource.url);
            request.timeout = 3; // in seconds  // TODO: make it configurable
            //
            Logger.info("Retrieve status for '%s' ...", resource.url);
            Integer statusCode = 500;
            String statusMessage = null;
            int elapsed;
            JsonObject responseJson = null;
            WS.HttpResponse response = null;
            long startTime = System.currentTimeMillis();
            try {
                if (resource.cookie != null) {
                    request = request.setHeader("Cookie", resource.cookie);
                }
                if (resource.needsAuthentication()) {
                    request = request.authenticate(resource.username, resource.password);
                }
                // execute GET request
                response = request.get();
                statusCode = response.getStatus();
                responseJson = response.getJson().getAsJsonObject();
            } catch (Exception e) {
                Logger.warn("Problem while requesting %s: %s", resource.url, e);
                if (response != null) {
                    statusMessage = response.getString();
                } else {
                    statusMessage = "Exception: " + e.getMessage();
                }
            } finally {
                elapsed = (int) (System.currentTimeMillis() - startTime);
            }
            PingPoint previous = PingPoint.getLatest(resource);
            PingPoint current = new PingPoint(resource, new Date(startTime), statusCode, statusMessage, elapsed);
            if (current.isOK() && responseJson != null) {
                JsonStatusParser.enrich(responseJson, current);
            }
            if (previous != null) {
                // has the status changed compared to previous state?
                AvailabilityChange change = null;
                if (current.isOK() && !previous.isOK()) {
                    change = new AvailabilityChange(resource, new Date(startTime), FROM_UNAVAILABLE_TO_OK);
                } else if (!current.isOK() && previous.isOK()) {
                    change = new AvailabilityChange(resource, new Date(startTime), FROM_OK_TO_UNAVAILABLE);
                }
                if (change != null) {
                    change.save();
                }
            }
            current.save();
        }
    }

}
