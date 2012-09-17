package unit;

import models.MonitorResource;
import models.PingPoint;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;
import sun.security.krb5.internal.KerberosTime;

import java.util.Date;
import java.util.List;

/**
 * @author Niko Schmuck
 */
public class PingPointTest extends UnitTest {

    @Before
    public void setUpData() {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data/test-data.yml");
    }

    @Test
    public void count() {
        long count = PingPoint.count();
        assertEquals(7, count);
    }

    @Test
    public void findOKandFailed() {
        MonitorResource resource = MonitorResource.findByName("Monitor B");
        assertNotNull(resource);
        DateTime start = new DateTime(2012, 9, 5, 23, 40);
        DateTime end = start.plusHours(2).plusMinutes(10);
        // ~~
        List<PingPoint> points_ok = PingPoint.findOK(resource, start.toDate(), end.toDate());
        assertEquals(5, points_ok.size());
        List<PingPoint> points_failed = PingPoint.findFailed(resource, start.toDate(), end.toDate());
        assertEquals(2, points_failed.size());
    }

}
