package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * One monitoring resource is the configuration object for an
 * URL endpoint which is going to be checked in regular intervals.
 *
 * @author Niko Schmuck
 */
@Entity
@Table(name = "monitor_resources")
public class MonitorResource extends Model {

    @Required
    public String name;

    public String sortKey;

    @Required
    public String url;

    public String username;
    public String password;
    public String cookie;

    // TODO: make frequency configurable

    @ManyToOne
    public MonitorGroup group;

    public MonitorResource(String url) {
        this.url = url;
    }

    // ~~

    public static MonitorResource findByName(String name) {
        return find("name = ?", name).first();
    }

    // ~~

    public MonitorEnvironment getEnvironment() {
        return group != null ? group.environment : null;
    }

    public String getFullName() {
        return (group != null ? group.toString() : "") + " " + name;
    }


    public boolean needsAuthentication() {
        return username != null;
    }

    @Override
    public String toString() {
        return name;
    }

}
