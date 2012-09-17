package models;

import org.hibernate.annotations.ForeignKey;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Track availability changes to ease summarizing a lot of {@link PingPoint} instances.
 *
 * @author Niko Schmuck
 */
@Entity
@Table(name = "changes")
public class AvailabilityChange extends Model {

    /** Time change happened (including timezone) */
    public Date timestamp;

    @Required
    @ManyToOne
    @JoinColumn(name="monitor_resource_id")
    @ForeignKey(name="FK_CHANGE_MONITORRESOURCE")
    public MonitorResource monitorResource;

    @Enumerated(EnumType.STRING)
    public ChangeType type;

    public AvailabilityChange(MonitorResource resource, Date startTime, ChangeType changeType) {
        this.monitorResource = resource;
        this.timestamp = startTime;
        this.type = changeType;
    }

    // ~~

    public enum ChangeType {

        FROM_OK_TO_UNAVAILABLE,
        FROM_UNAVAILABLE_TO_OK

    }

}
