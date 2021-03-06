package jobs;

import models.MonitorResource;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Initialize database from YAML definition, if no resources yet defined.
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if (MonitorResource.count() == 0) {
            Fixtures.deleteDatabase();
            Logger.info("Importing bootstrap data ...");
            Fixtures.loadModels("data/test-data.yml");
        }
    }

}