/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;

import com.google.gson.Gson;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Chris
 */
public class Event {
    private String eID;
    private String name;
    private String description;
    private int maxParticipators;
    private int minAge;
    private int totalLikes;
    private int totalParticipators;
    private Instant startDate;
    private Instant endDate;
    private Instant created;
    private Instant lastEdited;
    private Location location;
    private EventType type;
    private EventState state;
    private EventCategory category;
    private ArrayList<MinimalUser> participators;
    private MinimalUser creator;

    public Event(String name, String description, int maxParticipators, int minAge, Instant startDate, Instant endDate,Location location, EventType type, EventCategory category, MinimalUser creator) {
        this.name = name;
        this.description = description;
        this.maxParticipators = maxParticipators;
        this.minAge = minAge;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.type = type;
        this.category = category;
        this.creator = creator;
        this.totalLikes = 0;
        this.totalParticipators = 0;
        this.created = Instant.now();
        this.lastEdited = Instant.now();
        this.state = EventState.Unconfirmed;
        this.participators = new ArrayList<>();
    }

    public String geteID() {
        return eID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxParticipators() {
        return maxParticipators;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public int getTotalParticipators() {
        return totalParticipators;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getLastEdited() {
        return lastEdited;
    }

    public EventType getType() {
        return type;
    }

    public EventState getState() {
        return state;
    }

    public EventCategory getCategory() {
        return category;
    }

    public Collection<MinimalUser> getParticipators() {
        return participators;
    }

    public MinimalUser getCreator() {
        return creator;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxParticipators(int maxParticipators) {
        this.maxParticipators = maxParticipators;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void setTotalParticipators(int totalParticipators) {
        this.totalParticipators = totalParticipators;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public void setLastEdited(Instant lastEdited) {
        this.lastEdited = lastEdited;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public void setParticipators(Collection<MinimalUser> participators) {
        this.participators = (ArrayList<MinimalUser>) participators;
    }

    public void setCreator(MinimalUser creator) {
        this.creator = creator;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

}

//
//{	
//	"name":"UltrFetteParty",
//	"description":"keine ahnung",
//	"maxParticipators":"18",
//	"minAge":"18",
//	"startDate":{"year":2018,"month":4,"day":22},
//	"endDate":{"year":2018,"month":4,"day":22},
//	"type":"Public",
//	"category":"Party",
//	"creator":{
//			   "uID": "5adf87606570440928a6aa9e",
//			   "firstName": "Peter",
//			   "lastName": "Berisa",
//			   "profilePicture": "mypic.com/x.png"
//			 }
//}
