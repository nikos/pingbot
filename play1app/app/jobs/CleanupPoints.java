package jobs;

import models.PingPoint;
import org.joda.time.DateTime;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * Checks in regular interval, if old measurements can be removed.
 *
 * @author Niko Schmuck
 */
@Every("1h")
public class CleanupPoints extends Job {

    public static final int TTL_DAYS = 5; // TODO: make configurable

    public void doJob() throws Exception {
        Logger.info("clean up old ping points ...");
        PingPoint.removeOlderThan(new DateTime().minusDays(TTL_DAYS).toDate());
    }

}