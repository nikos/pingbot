package jobs;

import models.PingPoint;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Checks in regular interval, if old measurements can be removed.
 *
 * @author Niko Schmuck
 */
@Every("1h")
public class CleanupPoints extends Job {

    public static final int TTL_DAYS = Integer.valueOf(Play.configuration.getProperty("pingbot.cleanup_points", "5"));

    public void doJob() throws Exception {
        Logger.info("clean up old ping points ...");
        PingPoint.removeOlderThan(new DateTime().minusDays(TTL_DAYS).toDate());
    }

}