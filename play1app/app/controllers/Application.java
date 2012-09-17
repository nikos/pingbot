package controllers;

import models.MonitorEnvironment;
import models.MonitorResource;
import models.PingPoint;
import models.serializers.PingPointFreeMemoryJsonSerializer;
import models.serializers.PingPointNrThreadsJsonSerializer;
import models.serializers.PingPointResponseTimeJsonSerializer;
import org.joda.time.DateTime;
import play.Logger;
import play.mvc.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void dashboard() {
        List<MonitorEnvironment> environments = MonitorEnvironment.findAllSorted();
        render(environments);
    }

    public static void details(Long resourceId) {
        notFoundIfNull(resourceId);
        MonitorResource resource = MonitorResource.findById(resourceId);
        render(resource);
    }

    public static void availability() {
        List<MonitorEnvironment> environments = MonitorEnvironment.findAllSorted();
        render(environments);
    }

    public static void ajaxPoints(Long resourceId, Date starttime, Date endtime, Character y) {
        notFoundIfNull(resourceId, "Please specify resourceId parameter");
        // set defaults if no valid start- or endtime specified
        if (starttime == null) {
            starttime = new DateTime().minusHours(8).toDate();  // TODO: make configurable
        }
        if (endtime == null) {
            endtime = new Date();
        }
        MonitorResource resource = MonitorResource.findById(resourceId);
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