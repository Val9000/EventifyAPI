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
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
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
@Path("events")
public class EventService implements IService {

    public EventService() {

    }

    // URI : /websources/events
    @GET
    @Path("/s")
    @Produces({MediaType.APPLICATION_JSON})
    public String getEvents() {
        return new Gson().toJson(emc.getAllFilter(new Document(), new Document()));
    }

    // URI : /websources/events/{eID}
    @GET
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getFullEvent(@PathParam("eID") String eID) {
        return new Gson().toJson(emc.getOneFilter(eq("eID", eID), new Document()));
    }

    // URI : /websources/events/{eID}/participators 
    @GET
    @Path("/{eID}/participators")
    @Produces({MediaType.APPLICATION_JSON})
    public String getEventParticipators(@PathParam("eID") String eID) {
        try {
            Collection<MinimalUser> participators = emc.getOneFilter(Filters.eq("eID", eID), new Document("participators", 1)).getParticipators();
            if (participators.isEmpty()) {
                return new Document("Warning: EventSerivce - getEventParticipators", "Empty list ! ").toJson();
            }
            return new Gson().toJson(participators);
        } catch (Exception e) {
            e.printStackTrace();
            return new Document("Error: EventSerivce - getEventParticipators", "Exception:  " + e.getMessage()).toJson();
        }

    }

    // URI : /websources/events/minimal
    @GET
    @Path("/minimal")
    @Produces({MediaType.APPLICATION_JSON})
    public String getMinimalEvents() {
        try {
            Document projection = new Document("eID", 1)
                    .append("name", 1)
                    .append("totalLikes", 1)
                    .append("maxParticipators", 1)
                    .append("totalParticipators", 1);
            List<Event> allFilter = emc.getAllFilter(new Document(), projection);
            if (allFilter.isEmpty()) {
                return new Document("Warning: EventSerivce - getMinimalEvents", "Empty list ! ").toJson();
            }
            return new Gson().toJson(allFilter);
        } catch (Exception e) {
            e.printStackTrace();
            return new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson();
        }

    }

    // URI : /websources/events
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String addEvent(String content, @Context HttpHeaders headers) {
        String eId = "";
        String uID = "";
        try {
            HandleEventObject temp = new Gson().fromJson(content, HandleEventObject.class);
            Event newEvent = new Event(temp.getName(), temp.getDescription(), temp.getMaxParticipators(), temp.getMinAge(), temp.getStartDate(), temp.getEndDate(), temp.getType(), temp.getCategory(), temp.getCreator());
            eId = emc.add(newEvent);
            newEvent.seteID(eId);
            emc.update(eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
            uID = headers.getRequestHeader("uID").get(0);
            SlimEvent se = new SlimEvent(eId, newEvent.getName(), newEvent.getTotalLikes(), newEvent.getMaxParticipators(), newEvent.getTotalParticipators());
            umc.update(Filters.eq("uID", uID), new Document("$push", new Document("participatesIn", Document.parse(new Gson().toJson(se)))));

        } catch (Exception e) {
            return new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson();
        }
        return new Document("eID", eId).toJson();
    }

    @PUT
    @Path("/{eID}/{listName}")
    @Produces({MediaType.APPLICATION_JSON})
    public String updateEvent(String content, @PathParam("eID") String eID, @PathParam("listName") String listName) {
        
        UpdateResult s = null;
        try {
            MinimalUser temp = new Gson().fromJson(content, MinimalUser.class);
            
            switch (listName) {
                case "like":
                    break;
                case "participate":
                    System.out.println("im here");
                    s = emc.update(eq("eID",eID), new Document("$addToSet", new Document("participators", Document.parse(new Gson().toJson(temp)))));
                    if(s.wasAcknowledged())
                        emc.remove(new Document("participators", Document.parse(new Gson().toJson(temp)))); // löscht das ganze object...gotta fix tho
                    break;
                case "rate":
                    break;
                default:
                    return new Document("error", "listName : " + listName + " doesn't exist").toJson();
            }
            //emc.update(Filters.eq("eID", temp.getEID()), new Document("$set", content));
        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Document("success", s.wasAcknowledged()).toJson();
    }

//    // URI : /websources/events/{eID}
//    @GET
//    @Path("/{eID}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public String getEmployee(@PathParam("eID") String eID) {
//        return new Gson().toJson(emc.getOneFilter(Filters.eq("eID", eID), new Document()));
//    }
//    @GET
//    @Path("/{eID}/like")
//    @Produces({MediaType.APPLICATION_JSON})
//    public String dis_like(@PathParam("eID") String eID, @Context HttpHeaders headers) {
//        try {
//            Event e = emc.getOneFilter(Filters.eq("eID", eID));
//            emc.update(Filters.eq("eID", eID), new Document("$set", new Document("totalLikes", e.getTotalLikes() + 1)));
//
//            String uID = headers.getRequestHeader("uID").get(0);
//            HashMap<String, SlimEvent> likes = umc.getOneFilter(Filters.eq("uID", uID)).getLikes();
//            if (likes.containsKey(eID)) {
//                likes.remove(uID);
//            } else {
//                likes.put(uID, new SlimEvent(eID, e.getName(), e.getTotalLikes(), e.getMaxParticipators(), e.getTotalParticipators()));
//            }
//            emc.update(Filters.eq("uID", uID), new Document("$set", new Document("likes", likes)));
//
//        } catch (Exception e) {
//            return new Document("error", e.getMessage()).toJson();
//        }
//        return new Gson().toJson("Success: Event =  " + eID + " liked / disliked");
//    }
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

//{
//    "name": "Peter",
//    "follows": [
//        {
//            "uID": "5ae481d36570441d54c90f1b",
//            "firstName": "Peter",
//            "lastName": "Peter",
//            "profilePicture": "mypic.com/x.png"
//        }
//    ],
//    "password": "$2a$07$8dzyMMB3Jl6v.8EaY5byg.6RyQQf0HGuM2VRz2EYDP7lpSy3Qy9si",
//    "participatesIn": [],
//    "ratings": [],
//    "likes": [],
//}
