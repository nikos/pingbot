package unit;

import models.MonitorResource;
import models.PingPoint;
import org.junit.Test;
import play.test.UnitTest;
import utils.ResourceChecker;

public class ResourceCheckerTest extends UnitTest {

    @Test
    public void count() {
        MonitorResource resource = new MonitorResource("http://www.google.de");
        resource.save();
        PingPoint point = ResourceChecker.check(resource);
        assertEquals(200, point.statusCode);
        assertTrue(point.isOK());
        assertTrue(point.responseTime < 2000);
    }

}
