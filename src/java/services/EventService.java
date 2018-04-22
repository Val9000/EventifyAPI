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
import javax.ws.rs.Produces;

/**
 *
 * @author Valon
 */
@Path("/events")
public class EventService 

{
    /*
    // URI:
    // /contextPath/servletPath/employees
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Employee> getEmployees_JSON() {
        List<Employee> listOfCountries = EmployeeDAO.getAllEmployees();
        return listOfCountries;
    }
 
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
    public String addEmployee(String content) {
        EventMongoConcrete emc = EventMongoConcrete.getInstance();
        Event temp = new Gson().fromJson(content, Event.class);
        return emc.add(temp);
    }
    
    // URI:
    // /contextPath/servletPath/employees
    /*@PUT
    @Produces({MediaType.APPLICATION_JSON})
    public String updateEmployee(String content) {
        EventMongoConcrete emc = new EventMongoConcrete();
        emc.update(filterQuery, updateObject);
        return ;
    }*/
 
   
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