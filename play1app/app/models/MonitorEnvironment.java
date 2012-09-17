package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "monitor_envs")
public class MonitorEnvironment extends Model {

    @Required
    @Unique
    @Column(nullable=false, unique=true)
    public String name;

    public String sortKey;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "environment")
    @OrderBy("sortKey")
    public List<MonitorGroup> groups;

    public static List<MonitorEnvironment> findAllSorted() {
        return find("ORDER BY sortKey").fetch();
    }

    // ~~

    @Override
    public String toString() {
        return name;
    }

}
