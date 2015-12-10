package org.wildfly.swarm.examples.netflix.ribbon.events;

import java.util.Map;

/**
 * @author Lance Ball
 */
public class Event {
    private int id;
    private String name;
    private Map timestamp;


    public Map getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
