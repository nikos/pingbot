package jobs;

import models.MonitorResource;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.ResourceChecker;

import java.util.List;

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
            ResourceChecker.check(resource);
        }
    }

}
