package unit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import models.MonitorResource;
import models.PingPoint;
import org.junit.Test;
import play.Play;
import play.test.UnitTest;
import utils.JsonStatusParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

/**
 * @author Niko Schmuck
 */
public class StatusParserTest extends UnitTest {

    @Test
    public void testEnrich() throws FileNotFoundException {
        MonitorResource resource = new MonitorResource("http://example.com");
        PingPoint point = new PingPoint(resource, new Date(), 200, null, 23);

        File sampleFile = Play.getFile("test/data/status-details.json");

        BufferedReader br = new BufferedReader(new FileReader(sampleFile));
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(br);

        JsonStatusParser.enrich(json.getAsJsonObject(), point);
        assertEquals(2035, point.freeMemory);
        assertEquals(57, point.nrThreads);
        assertEquals("qa-42.demo.example.com", point.hostname);
        assertEquals(2822, point.maxMemory);
    }
}
