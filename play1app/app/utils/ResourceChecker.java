package utils;

import models.AvailabilityChange;
import models.MonitorResource;
import models.PingPoint;
import play.Logger;
import play.Play;
import play.libs.WS;

import java.util.Date;

import static models.AvailabilityChange.ChangeType.FROM_OK_TO_UNAVAILABLE;
import static models.AvailabilityChange.ChangeType.FROM_UNAVAILABLE_TO_OK;

public final class ResourceChecker {

    public static final int DEFAULT_TIMEOUT_IN_S = Integer.valueOf(Play.configuration.getProperty("pingbot.request_timeout", "3"));


    public static PingPoint check(MonitorResource resource) {
        WS.WSRequest request = WS.url(resource.url);
        request.timeout = DEFAULT_TIMEOUT_IN_S;
        //
        Logger.info("Retrieve status for '%s' ...", resource.url);
        Integer statusCode = 500;
        String statusMessage = null;
        int elapsed;
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
        // TODO: Enable to plugin different response parsers
        if (current.isOK() && response != null && response.getContentType().contains("json")) {
            JsonStatusParser.enrich(response.getJson().getAsJsonObject(), current);
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
        Logger.debug("    [%s] response time: %d ms, status code: %d", resource.url, elapsed, statusCode);
        current.save();
        return current;
    }

}
