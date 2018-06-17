/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.Auth;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.UpdateResult;
import data.models.Event;
import data.models.EventCategory;
import data.models.EventType;
import data.models.Location;
import data.models.MinimalEvent;
import data.models.MinimalUser;
import data.models.ParticipationType;
import data.models.SlimEvent;
import data.models.User;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * REST Web Service
 *
 * @author Valon
 */
@Path("events")
public class EventService implements IService {   
    List<String> vaildSlimFieldToUpdate = Arrays.asList("name", "totalLikes", "maxParticipators", "totalParticipators", "category");
    
    // URI : /websources/events
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllSlimEvents(@Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            Document projection = new Document("eID", 1)
                    .append("name", 1)
                    .append("totalLikes", 1)
                    .append("maxParticipators", 1)
                    .append("totalParticipators", 1)
                    .append("category", 1);

            List<SlimEvent> allFilter = emc.getAllFilter(new Document(), projection, SlimEvent.class);

            if (allFilter == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: EventSerivce - getAllSlimEvents", "Empty list ! ").toJson()).build();
            }
            return Response.status(Response.Status.OK).entity(custom_gson.toJson(allFilter)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getAllSlimEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
    }

    // URI : /websources/events/{eID}
    @GET
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFullEvent(@PathParam("eID") String eID, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        Event oneFilter = emc.getOneFilter(eq("eID", eID), new Document());
        if (oneFilter == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getFullEvent", "Exception:  Can't find Event with the id : " + eID).toJson()).build();
        } else {
            return Response.status(Response.Status.OK).entity(custom_gson.toJson(oneFilter)).build();
        }
    }

    // URI : /websources/events/{eID}/participators 
    @GET
    @Path("/{eID}/participators")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEventParticipators(@PathParam("eID") String eID, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            Event oneFilter = emc.getOneFilter(Filters.eq("eID", eID), new Document("participators", 1));
            if (oneFilter == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getEventParticipators", "Exception:  Can't find Event with the id : " + eID).toJson()).build();
            } else {
                Collection<MinimalUser> participators = oneFilter.getParticipators();
                if (participators.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: EventSerivce - getEventParticipators", "Empty list ! ").toJson()).build();
                }
                return Response.status(Response.Status.OK).entity(custom_gson.toJson(participators)).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getEventParticipators", "Exception:  " + e.getMessage()).toJson()).build();
        }
    }

    // URI : /websources/events/minimal
    @GET
    @Path("/minimal")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMinimalEvents(@Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            Document projection = new Document("eID", 1).append("location", 1).append("category", 1);
            List<MinimalEvent> allFilter = emc.getAllFilter(new Document(), projection, MinimalEvent.class);
            if (allFilter == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: EventSerivce - getMinimalEvents", "Empty list ! ").toJson()).build();
            }
            return Response.status(Response.Status.OK).entity(custom_gson.toJson(allFilter)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
    }

    // URI : /websources/events
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response addEvent(String content, @Context HttpHeaders headers) {
        String eId = "";
        String uID = "";
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            HandleEventObject temp = custom_gson.fromJson(content, HandleEventObject.class);
            MinimalUser x = umc.getOneFilter(eq("uID", authID), new Document(), MinimalUser.class);
            Event newEvent = new Event(temp.getName(), temp.getDescription(), temp.getMaxParticipators(), temp.getMinAge(), temp.getStartDate(), temp.getEndDate(), temp.getLocation(), temp.getType(), temp.getCategory(), x);
            eId = emc.add(newEvent);
            newEvent.seteID(eId);
            emc.update(eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
            //uID = headers.getRequestHeader("uID").get(0);
            //SlimEvent se = new SlimEvent(eId, newEvent.getName(), newEvent.getTotalLikes(), newEvent.getMaxParticipators(), newEvent.getTotalParticipators(), newEvent.getCategory());
            //umc.update(Filters.eq("uID", uID), new Document("$push", new Document("participatesIn", Document.parse(custom_gson.toJson(se)))));
            //User y = umc.getOneFilter(eq("uID", uID), new Document(), User.class);
            //umc.update(eq("uID", authID), new Document("numberOfCreated", y.getNumberOfCreated() + 1));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.OK).entity(new Document("eID", eId).toJson()).build();
    }
    
    @PUT
    @Path("/{eID}/{action}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response takeAction(String content, @PathParam("eID") String eID, @PathParam("action") String action, @Context HttpHeaders headers){
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try{
            switch(action){
                case "like": 
                    return like(eID, authID);
                case "participate":
                    return participate(eID, authID, content);
                default: return Response.status(Response.Status.NOT_FOUND).entity(new Document("error", "Invalid Route").toJson()).build();
            }
        }
        catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", "Something went wrong").toJson()).build();
        }
    }

    
    private Response like(String eID, String authID)    {
        try {
            String msg = "";
            SlimEvent check_Event = emc.getOneFilter(eq("eID", eID), new Document(), SlimEvent.class);
            if (check_Event == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", "Event with this id : " + eID + " doesn't exist.").toJson()).build();
            }

            SlimEvent oldEvent = umc.getOneFilter(Filters.and(eq("uID", authID),
                    Filters.elemMatch("likes", new Document("eID", eID))),
                    new Document(), SlimEvent.class);
            
            if (oldEvent == null) { // we know that user didn't like this event yet so add it
                Document parse = Document.parse(custom_gson.toJson(check_Event));
                umc.update(eq("uID", authID), new Document("$push", new Document("likes", Document.parse(custom_gson.toJson(check_Event)))));
                emc.update(eq("eID", eID), new Document("$set", new Document("totalLikes", check_Event.getTotalLikes() + 1)));
                msg = "Event has been liked";
            } else {
                umc.update(eq("uID", authID), new Document("$pull", new Document("likes", new Document("eID", eID))));
                int newLikesNr = check_Event.getTotalLikes() - 1; // i hab keinen plan warum es so geht...  wenn i des gleich im update eine schreib is es aufamal -1 ? wtf
                emc.update(eq("eID", eID), new Document("$set", new Document("totalLikes", newLikesNr)));
                msg = "Event has been de-liked";
            }
            return Response.status(Response.Status.OK).entity(new Document("success", msg).toJson()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", e.getMessage()).toJson()).build();
        }      
    }

    private Response participate(String eID, String authID, String content) {
        try {
            String msg = "";
            String type = content.split("type\": \"")[1].split("\"")[0];
            MinimalUser tempUser = null;
            SlimEvent tempEvent = emc.getOneFilter(eq("eID", eID), new Document(), SlimEvent.class);
    
            tempUser = emc.getOneFilter(Filters.and(eq("eID", eID),
                        Filters.elemMatch("participators", new Document("uID", authID))),
                        new Document(), MinimalUser.class);
            
            if (tempUser == null) { // we know that he yet didn't participate so let's add it to the list     
                if(tempEvent.getTotalParticipators() >= tempEvent.getMaxParticipators()) 
                    return Response.status(Response.Status.BAD_REQUEST).entity(new Document("info: ", "Event is already full").toJson()).build();
                
                if (type.equals(ParticipationType.Anonymous.toString())) {
                    tempUser = umc.getOneFilter(eq("uID", authID), new Document(), MinimalUser.class);
                    tempUser.setFirstName("Anonymous");
                    tempUser.setLastName("User");
                    tempUser.setProfilePicture("anonymous.pic");
                    msg = "Event has been participated anonymously";
                } else{
                    tempUser = umc.getOneFilter(eq("uID", authID), new Document(), MinimalUser.class);
                }
                umc.update(eq("uID", authID), new Document("$push", new Document("participatesIn", Document.parse(custom_gson.toJson(tempEvent)))));
                emc.update(eq("eID", eID), new Document("$push", new Document("participators", Document.parse(custom_gson.toJson(tempUser)))));
                emc.update(eq("eID", eID), new Document("$set", new Document("totalParticipators", tempEvent.getTotalParticipators() + 1)));
                
                if(!tempUser.getFirstName().equals("Anonymous")) msg = "Event has been participated";
                
            } else {
                umc.update(eq("uID", authID), new Document("$pull", new Document("participatesIn", new Document("eID", eID))));
                emc.update(eq("eID", eID), new Document("$pull", new Document("participators", Document.parse(custom_gson.toJson(tempUser)))));
                emc.update(eq("eID", eID), new Document("$set", new Document("totalParticipators", tempEvent.getTotalParticipators() - 1)));
                msg = "Event has been de - participated";
            } 
            
            return Response.status(Response.Status.ACCEPTED).entity(new Document("success", msg).toJson()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - de_participate", e.getMessage()).toJson()).build();
        }
    }
    
    @PUT
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateEventFields(String content, @PathParam("eID") String eID, @Context HttpHeaders httpHeaders) {
        if(httpHeaders.getRequestHeader("API_KEY") == null || httpHeaders.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = httpHeaders.getRequestHeader("API_KEY").get(0);
        String authID = httpHeaders.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();  
        try {
            Type type = new TypeToken<Map<String, ?>>() {}.getType();
            Map<String, ?> myMap = custom_gson.fromJson(content, type);
            Document toUpdateEvent = new Document();
            myMap.forEach((key, val) -> toUpdateEvent.append(key, val));
            emc.update(eq("eID", eID), new Document("$set", toUpdateEvent));         
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - updateEventFields", e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.OK).entity(new Document("success", "fields updated").toJson()).build();
    }

    public class HandleEventObject {

        private String name;
        private String description;
        private int maxParticipators;
        private int minAge;
        private Instant startDate;
        private Instant endDate;
        private Location location;
        private EventType type;
        private EventCategory category;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
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

        public Instant getStartDate() {
            return startDate;
        }

        public Instant getEndDate() {
            return endDate;
        }

        public EventType getType() {
            return type;
        }

        public EventCategory getCategory() {
            return category;
        }
    }
}
       