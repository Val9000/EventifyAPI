/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.UpdateResult;
import data.models.Event;
import data.models.EventCategory;
import data.models.EventType;
import data.models.MinimalUser;
import data.models.SlimEvent;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * REST Web Service
 *
 * @author Chris
 */
@Path("events")
public class EventService implements IService{

    @Context
    private UriInfo context;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    Logger logger = Logger.getLogger(getClass().getName());

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

    // URI : /websources/events/{eID}/participate
    @PUT
    @Path("/{eID}/participate")
    @Produces({MediaType.APPLICATION_JSON})
    public String de_participate(String content, @PathParam("eID") String eID) {
        UpdateResult updateResult = null;

        try {
            MinimalUser temp = new Gson().fromJson(content, MinimalUser.class);
            updateResult = emc.update(eq("eID", eID), new Document("$addToSet", new Document("participators", Document.parse(gson.toJson(temp)))));
            // if the count is 0 we know that it already exits. if it does.. then delete the object.
            if (updateResult.getModifiedCount() == 0) {
                emc.update(eq("eID", eID), new Document("$pull", new Document("participators", Document.parse(gson.toJson(temp)))));
            }
        } catch (Exception e) {
            return new Document("Error: EventSerivce - de_participate", e.getMessage()).toJson();
        }
        return new Document("success", updateResult.wasAcknowledged() + " count: " + updateResult.getModifiedCount()).toJson();
    }

    @PUT
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public String updateEventFields(String content, @PathParam("eID") String eID, @Context HttpHeaders httpHeaders) {
        try {
            String  jsonToUpdate = httpHeaders.getRequestHeader("fieldsToUpdate").get(0);
            Type type = new TypeToken<Map<String, ?>>(){}.getType();
            Map<String, ?> myMap = gson.fromJson(jsonToUpdate, type);
            Document toUpdate = new Document();
            myMap.forEach((key,val)-> toUpdate.append(key,val));
            emc.update(eq("eID",eID), new Document("$set", toUpdate));      
        } catch (Exception e) {
            return new Document("Error: EventSerivce - updateEventFields", e.getMessage()).toJson();
        }
        return new Document("success", "ka").toJson();
    }

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
