/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import data.dao.UserMongoConcrete;
import data.models.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import static com.sun.corba.se.impl.util.Utility.printStackTrace;
import data.dao.EventMongoConcrete;
import data.models.Event;
import data.models.EventCategory;
import data.models.EventType;
import data.models.MinimalUser;
import data.models.SlimEvent;
import java.lang.reflect.Method;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Valon
 */
@Path("/events")
public class EventService {

    private EventMongoConcrete emc = null;
    private UserMongoConcrete umc = null;

    public EventService() {
        emc = EventMongoConcrete.getInstance();
        umc = UserMongoConcrete.getInstance();
    }

    // URI : /websources/events
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getEvents() {
        return new Gson().toJson(emc.getAllFilter(new Document()));
    }

    // URI : /websources/events
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String addEvent(String content, @Context HttpHeaders headers) {
        String eId = "";
        String uId = "";
        try {
            
            HandleEventObject temp = new Gson().fromJson(content, HandleEventObject.class);
            Event newEvent = new Event(temp.getName(), temp.getDescription(), temp.getMaxParticipators(), temp.getMinAge(), temp.getStartDate(), temp.getEndDate(), temp.getType(), temp.getCategory(), temp.getCreator());
            eId = emc.add(newEvent);
            newEvent.seteID(eId);
            emc.update(Filters.eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
            uId = headers.getRequestHeader("uID").get(0);
            
            HashMap<String, SlimEvent> participatesIn = umc.getOneFilter(Filters.eq("uId",uId)).getParticipatesIn();
            participatesIn.put(eId, new SlimEvent(eId, newEvent.getName(), newEvent.getTotalLikes(), newEvent.getMaxParticipators(), newEvent.getTotalParticipators()));
            umc.update(Filters.eq("uId", uId), new Document("$set", new Document("participatesIn",participatesIn)));
            System.out.println("*******added new event from userID " + newEvent.getCreator() + "******");
        } catch (Exception e) {
            return new Document("error", "Add - Event - Error : " + e.getMessage()).toJson(); // falsch verbessern : valid json !!
        }
        return new Document("eID", eId).toJson();
    }

    // URI : /websources/events/{eID}
    @GET
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getEmployee(@PathParam("eID") String eID) {
        return new Gson().toJson(emc.getOneFilter(Filters.eq("eID", eID)));
    }

    @GET
    @Path("/{eID}/like")
    @Produces({MediaType.APPLICATION_JSON})
    public String dis_like(@PathParam("eID") String eID, @Context HttpHeaders headers) {
        try {
            Event e = emc.getOneFilter(Filters.eq("eID", eID));
            emc.update(Filters.eq("eID", eID), new Document("$set", new Document("totalLikes", e.getTotalLikes() + 1)));

            String uID = headers.getRequestHeader("uID").get(0);
            HashMap<String, SlimEvent> likes = umc.getOneFilter(Filters.eq("uID", uID)).getLikes();
            if (likes.containsKey(eID)) {
                likes.remove(uID);
            } else {
                likes.put(uID, new SlimEvent(eID, e.getName(), e.getTotalLikes(), e.getMaxParticipators(), e.getTotalParticipators()));
            }
            emc.update(Filters.eq("uID", uID), new Document("$set", new Document("likes", likes)));

        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Gson().toJson("Success: Event =  " + eID + " liked / disliked");
    }

    // URI : /websources/events
//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    public String addUserToEvent(String content) {
//        String eId = "";
//        try {
//            HandleEventObject temp = new Gson().fromJson(content, HandleEventObject.class);
//            Event newEvent = new Event(temp.getName(), temp.getDescription(), temp.getMaxParticipators(), temp.getMinAge(), temp.getStartDate(), temp.getEndDate(), temp.getType(), temp.getCategory(), temp.getCreator());
//            eId = emc.add(newEvent);
//            newEvent.seteID(eId);
//            emc.update(Filters.eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
//            System.out.println("*******added new event from userID " + newEvent.getCreator() + "******");
//        } catch (Exception e) {
//            return new Document("error", "Add - Event - Error : " + e.getMessage()).toJson(); // falsch verbessern : valid json !!
//        }
//        return new Document("eID", eId).toJson();
//    }

    /*
    //URI:
    // /contextPath/servletPath/employees
    @PUT
    @Produces({MediaType.APPLICATION_JSON}) // später evtl. nur bestimmte fields mitschicken und nicht alles.. mom. irrelevant
    public String updateEmployee(String content) {
        try {
            Event temp = new Gson().fromJson(content, Event.class);
            EventMongoConcrete emc = EventMongoConcrete.getInstance();
            emc.update(Filters.eq("eID", temp.getEID()), new Document("$set", content));
        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Document("success", "Event has been modified").toJson();
    }
     */
    // deaktivieren... 
    /*
    @DELETE
    @Path("/{empNo}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void deleteEmployee(@PathParam("empNo") String empNo) {
        EmployeeDAO.deleteEmployee(empNo);
    }
     */
    public class HandleEventObject {

        private String name;
        private String description;
        private int maxParticipators;
        private int minAge;
        private LocalDate startDate;
        private LocalDate endDate;
        private EventType type;
        private EventCategory category;
        private MinimalUser creator;

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

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public EventType getType() {
            return type;
        }

        public EventCategory getCategory() {
            return category;
        }

        public MinimalUser getCreator() {
            return creator;
        }

    }
}
