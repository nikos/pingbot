package unit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import models.MonitorResource;
import models.PingPoint;
import org.junit.Test;
import play.Play;
import play.test.UnitTest;
import utils.AggregatedJenkinsJsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

/**
 * @author Niko Schmuck
 */
public class AggreatedJenkinsJsonParserTest extends UnitTest {

    @Test
    public void failedJob() throws FileNotFoundException {
        MonitorResource resource = new MonitorResource("http://jenkins/view/BuildLights/api/json");
        PingPoint point = new PingPoint(resource, new Date(), 200, null, 123);

        File sampleFile = Play.getFile("test/data/jenkins-status-with-failed.json");

        BufferedReader br = new BufferedReader(new FileReader(sampleFile));
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(br);

        AggregatedJenkinsJsonParser.enrich(json.getAsJsonObject(), point);
        assertEquals(500, point.statusCode);
        assertEquals("Failed job: S_01.04_Basistests_CI", point.statusMessage);
    }

    @Test
    public void allJobsOK() throws FileNotFoundException {
        MonitorResource resource = new MonitorResource("http://jenkins/view/BuildLights/api/json");
        PingPoint point = new PingPoint(resource, new Date(), 200, null, 24);

        File sampleFile = Play.getFile("test/data/jenkins-status-all-ok.json");

        BufferedReader br = new BufferedReader(new FileReader(sampleFile));
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(br);

        AggregatedJenkinsJsonParser.enrich(json.getAsJsonObject(), point);
        assertEquals(200, point.statusCode);
        assertNull(point.statusMessage);
    }

}
