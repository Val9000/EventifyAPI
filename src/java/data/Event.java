/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Chris
 */
public class Event implements Serializable {
    private String name;
    private String creatorID;
    private EventState state;
    private String description;
    private int maxParticipants;
    private int minAge;
    private EventType type;
    private EventCategory category;
    private LocalDate startDate;
    private LocalDate endDate;
    private final LocalDate created;
    private LocalDate lastEdited;
    private Location location;

    public Event(String name, String creatorID, EventState state, String description, int maxParticipants, int minAge, EventType type, EventCategory category, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.creatorID = creatorID;
        this.state = state;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.minAge = minAge;
        this.type = type;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.created = LocalDate.now();
        this.lastEdited = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public EventState getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getMinAge() {
        return minAge;
    }

    public EventType getType() {
        return type;
    }

    public EventCategory getCategory() {
        return category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getLastEdited() {
        return lastEdited;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setLastEdited(LocalDate lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "{ 'name': '" + name + "', 'creatorID': '" + creatorID + "', 'state': '" + state + "', 'description': '" + description + "', 'maxParticipants': '" + maxParticipants + "', 'minAge': '" + minAge + "', 'type': '" + type + "', 'category': '" + category + "', 'startDate': '" + startDate + "', 'endDate': '" + endDate + "', 'created': '" + created + "', 'lastEdited': '" + lastEdited + "', 'location': '" + location + "' }";
    }
}