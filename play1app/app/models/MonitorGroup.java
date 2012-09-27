package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

/**
 * One monitoring group belongs to exactly one environment,
 * and can refer to one or more resources.
 *
 * @author Niko Schmuck
 */
@Entity
@Table(name = "monitor_groups")
public class MonitorGroup extends Model {

    @Required
    public String name;

    public String sortKey;

    @ManyToOne
    public MonitorEnvironment environment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    @OrderBy("sortKey")
    public List<MonitorResource> resources;

    // ~~

    @Override
    public String toString() {
        return name + " (" + (environment != null ? environment.name : "N/A") + ')';
    }

}
