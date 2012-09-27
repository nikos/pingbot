package models;

import org.hibernate.annotations.ForeignKey;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The measure result for one monitored resource.
 *
 * @author Niko Schmuck
 */
@Entity
@Table(name = "points")
public class PingPoint extends Model {

    /** Time test was made (including timezone) */
    @Required
    public Date timestamp;

    @Required
    @ManyToOne
    @JoinColumn(name="monitor_resource_id")
    @ForeignKey(name="FK_PINGPOINT_MONITORRESOURCE")
    public MonitorResource monitorResource;

    /** HTTP Status Codes: 200 == OK */
    @Required
    public int statusCode;

    @Column(length = 2000)
    public String statusMessage;

    /** In Milliseconds */
    @Required
    public int responseTime;


    // -- TODO: model attributes into own entity

    // Results from Status JSON
    public String hostname;
    public int nrThreads;

    public int freeMemory;
    public int maxMemory;


    // ~~

    public PingPoint(MonitorResource resource, Date startTime, int statusCode, String statusMessage, int elapsed) {
        this.monitorResource = resource;
        this.timestamp = startTime;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseTime = elapsed;
    }

    // ~~

    public boolean isOK() {
        return statusCode < 400;
    }

    // ~~

    public static PingPoint getLatest(MonitorResource monitorResource) {
        return find("monitorResource = ? ORDER BY timestamp DESC", monitorResource).first();
    }

    public static PingPoint getNearest(MonitorResource monitorResource, Date timestamp) {
        return find("monitorResource = ? AND timestamp < ? ORDER BY timestamp DESC", monitorResource, timestamp).first();
    }


    public static List<PingPoint> findOK(MonitorResource monitorResource, Date start, Date end) {
        if (end != null) {
            return find("monitorResource = ? AND statusCode < 400 AND timestamp > ? AND timestamp < ? ORDER BY timestamp ASC",
                        monitorResource, start, end).fetch();
        } else {
            return find("monitorResource = ? AND statusCode < 400 AND timestamp > ? ORDER BY timestamp ASC",
                        monitorResource, start, end).fetch();
        }
    }

    public static List<PingPoint> findFailed(MonitorResource monitorResource, Date start, Date end) {
        if (end != null) {
            return find("monitorResource = ? AND statusCode >= 400 AND timestamp > ? AND timestamp < ? ORDER BY timestamp ASC",
                        monitorResource, start, end).fetch();
        } else {
            return find("monitorResource = ? AND statusCode >= 400 AND timestamp > ? ORDER BY timestamp ASC",
                        monitorResource, start, end).fetch();
        }
    }

    public static void removeOlderThan(Date timeOffset) {
        delete("timestamp < ?", timeOffset);
    }

    //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PingPoint");
        sb.append("{timestamp=").append(timestamp);
        sb.append(", monitorResource='").append(monitorResource).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append(", responseTime=").append(responseTime);
        sb.append('}');
        return sb.toString();
    }

}
