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
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;

/**
 *
 * @author Valon
 */
@Path("/events")
public class EventService 
{
    private EventMongoConcrete emc = null;
    public EventService(){
        emc = EventMongoConcrete.getInstance();
    }
    // URI:
    // /contextPath/servletPath/employees
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getEvents() {
        return new Gson().toJson(emc.getAllFilter(new Document()));
    }
    
    
    /*
    // URI:
    // /contextPath/servletPath/employees/{empNo}
    @GET
    @Path("/{empNo}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Employee getEmployee(@PathParam("empNo") String empNo) {
        return EmployeeDAO.getEmployee(empNo);
    }
    */
    
    // URI:
    // /contextPath/servletPath/employees
    
   @POST
    @Produces({MediaType.APPLICATION_JSON})
    public String addEvent(String content) {
        String eId = "";
        try {
            Event temp = new Gson().fromJson(content, Event.class);
            Event newEvent = new Event(temp.getName(),temp.getCreatorID(),temp.getState(), temp.getDescription(), temp.getMaxParticipants(), temp.getMinAge(), temp.getType(), temp.getCategory(), temp.getStartDate(), temp.getEndDate());
            eId = emc.add(temp);
            newEvent.setEID(eId);
            emc.update(Filters.eq("_id", new ObjectId(eId)), new Document("$set", new Document("eID", eId)));
            System.out.println("*******added new event from userID " + temp.getCreatorID() + "******");
        } catch (Exception e) {
            return "Add - Event - Error : " + e.getMessage(); // falsch verbessern : valid json !!
        }
        return eId;
    }
    
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
            return "";
        }
        return "";
    }
 
   
    // deaktivieren... 
    
     /*
    @DELETE
    @Path("/{empNo}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void deleteEmployee(@PathParam("empNo") String empNo) {
        EmployeeDAO.deleteEmployee(empNo);
    }
    */
}