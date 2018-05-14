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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import data.models.Event;
import data.models.EventCategory;
import data.models.EventType;
import data.models.Location;
import data.models.MinimalEvent;
import data.models.MinimalUser;
import data.models.ParticipationType;
import data.models.SlimEvent;
import java.lang.reflect.Type;
import java.time.Instant;
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
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;
import static services.IService.custom_gson;
import static services.IService.emc;
import static services.IService.umc;

/**
 * REST Web Service
 *
 * @author Chris
 */
@Path("events")
@Api( value = "/events", description = "Manage events" )
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
    @ApiOperation( value = "Get all events", notes = "Returns events", response = SlimEvent.class )
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

            if (allFilter.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: EventSerivce - getAllSlimEvents", "Empty list ! ").toJson()).build();
            }
            return Response.status(Response.Status.OK).entity(custom_gson.toJson(allFilter)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getAllSlimEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
    }

    // URI : /websources/events/{eID}
    @GET
    @ApiOperation( value = "Get specific event by eID", notes = "Returns one specific event", response = Event.class )
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
    @ApiOperation( value = "Get participators of specific event", notes = "Returns all participators of one specific event", response = MinimalUser.class )
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
    @ApiOperation( value = "Get all events as minimal version", notes = "Returns all minimalEvents", response = MinimalEvent.class )
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
            if (allFilter.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: EventSerivce - getMinimalEvents", "Empty list ! ").toJson()).build();
            }
            return Response.status(Response.Status.OK).entity(custom_gson.toJson(allFilter)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
    }

    // URI : /websources/events
    @POST
    @ApiOperation( value = "Create an event", notes = "Event" )
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
            Event newEvent = new Event(temp.getName(), temp.getDescription(), temp.getMaxParticipators(), temp.getMinAge(), temp.getStartDate(), temp.getEndDate(), temp.getLocation(), temp.getType(), temp.getCategory(), temp.getCreator());
            eId = emc.add(newEvent);
            newEvent.seteID(eId);
            emc.update(eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
            uID = headers.getRequestHeader("uID").get(0);
            SlimEvent se = new SlimEvent(eId, newEvent.getName(), newEvent.getTotalLikes(), newEvent.getMaxParticipators(), newEvent.getTotalParticipators(), newEvent.getCategory());
            umc.update(Filters.eq("uID", uID), new Document("$push", new Document("participatesIn", Document.parse(custom_gson.toJson(se)))));

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - getMinimalEvents", "Exception:  " + e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.OK).entity(new Document("eID", eId).toJson()).build();
    }

    // URI : /websources/events/{eID}/participate
    @PUT
    @ApiOperation( value = "Participate/Unparticipate in an event", notes = "eID" )    
    @Path("/{eID}/participate")
    @Produces({MediaType.APPLICATION_JSON})
    public Response de_participate(String content, @PathParam("eID") String eID, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        UpdateResult updateResult = null;
        try {
            String type = content.split("type\": \"")[1].split("\"")[0];
            MinimalUser temp;
            if(type.equals(ParticipationType.Anonymous.toString())){
                temp = new MinimalUser(authID, "Anonymous", "User", "anonymous");
            } else {
                temp = umc.getOneFilter(eq("uID", authID), new Document(), MinimalUser.class);
            }
            updateResult = emc.update(eq("eID", eID), new Document("$addToSet", new Document("participators", Document.parse(custom_gson.toJson(temp)))));
            // if the count is 0 we know that it already exits. if it does.. then delete the object.
            if (updateResult.getModifiedCount() == 0) {
                emc.update(eq("eID", eID), new Document("$pull", new Document("participators", Document.parse(custom_gson.toJson(temp)))));
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - de_participate", e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.OK).entity(new Document("success", updateResult.wasAcknowledged() + " count: " + updateResult.getModifiedCount()).toJson()).build();
    }

    @PUT
    @ApiOperation( value = "Change details of one specific event", notes = "fieldToChange" )
    @Path("/{eID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateEventFields(String content, @PathParam("eID") String eID, @Context HttpHeaders httpHeaders) {
        if(httpHeaders.getRequestHeader("API_KEY") == null || httpHeaders.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = httpHeaders.getRequestHeader("API_KEY").get(0);
        String authID = httpHeaders.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            Type type = new TypeToken<Map<String, ?>>() {
            }.getType();
            Map<String, ?> myMap = custom_gson.fromJson(content, type);
            Document toUpdate = new Document();
            myMap.forEach((key, val) -> toUpdate.append(key, val));
            emc.update(eq("eID", eID), new Document("$set", toUpdate));
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
        private MinimalUser creator;

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

        public MinimalUser getCreator() {
            return creator;
        }
    }
}