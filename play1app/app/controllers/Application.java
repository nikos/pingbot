package controllers;

import models.MonitorEnvironment;
import models.MonitorResource;
import models.PingPoint;
import models.serializers.PingPointFreeMemoryJsonSerializer;
import models.serializers.PingPointNrThreadsJsonSerializer;
import models.serializers.PingPointResponseTimeJsonSerializer;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.mvc.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static final int DEFAULT_TIME_RANGE_IN_HRS = Integer.valueOf(Play.configuration.getProperty("pingbot.default_timerange", "8"));


    public static void dashboard() {
        List<MonitorEnvironment> environments = MonitorEnvironment.findAllSorted();
        render(environments);
    }

    public static void details(Long resourceId) {
        notFoundIfNull(resourceId);
        MonitorResource resource = MonitorResource.findById(resourceId);
        render(resource);
    }

    public static void query() {
        List<MonitorEnvironment> environments = MonitorEnvironment.findAllSorted();
        render(environments);
    }

    /**
     * Returns a JSON array of measure points for the specified time range and data type.
     *
     * @param resourceId which {@link MonitorResource} should be considered
     * @param starttime start of the time range to deliver
     * @param endtime end of the time range
     * @param y R (default): response time (ms), M: free memory (MB), T: nr. of threads
     */
    public static void ajaxPoints(Long resourceId, Date starttime, Date endtime, Character y) {
        notFoundIfNull(resourceId, "Please specify resourceId parameter");
        // set defaults if no valid start- or endtime specified
        if (starttime == null) {
            starttime = new DateTime().minusHours(DEFAULT_TIME_RANGE_IN_HRS).toDate();
        }
        if (endtime == null) {
            endtime = new Date();
        }
        MonitorResource resource = MonitorResource.findById(resourceId);
        notFoundIfNull(resource, "No MonitoResource found for resourceId: " + resourceId);
        Logger.info("Get PingPoints for '%s' between %s and %s ...", resource.url, starttime, endtime);
        List<PingPoint> points_ok     = PingPoint.findOK(resource, starttime, endtime);
        List<PingPoint> points_failed = PingPoint.findFailed(resource, starttime, endtime);
        Map<String, List<PingPoint>> points = new HashMap<String, List<PingPoint>>();
        points.put("ok", points_ok);
        points.put("failed", points_failed);

        // generate JSON (depending on y axis character type)
        if (y == null) {
            y = 'R';
        }
        switch (y) {
            case 'M':
                renderJSON(points, PingPointFreeMemoryJsonSerializer.get());
                break;
            case 'T':
                renderJSON(points, PingPointNrThreadsJsonSerializer.get());
                break;
            case 'R':
            default:
                renderJSON(points, PingPointResponseTimeJsonSerializer.get());
        }
    }

}