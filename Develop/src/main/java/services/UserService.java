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
import data.models.MinimalUser;
import data.models.SlimUser;
import data.models.User;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;

/**
 * REST Web Service
 *
 * @author Valon
 */
@Path("users")
public class UserService implements IService {
       
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UserService
     */

   List<String> userListNames;
    
    public UserService() {
        userListNames = Arrays.asList("follows", "participatesIn","ratings","likes");
    }

    // URI : /websources/users/
    //REMOVE
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllUsers(@Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        if(!Auth.Authenticate(API_KEY)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String uID = headers.getRequestHeader("uID").get(0);
        User u = umc.getOneFilter(new Document("uID", uID), new Document());
        if(u != null) return Response.status(Response.Status.OK).entity(custom_gson.toJson(u)).build();
        else return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", "User not found").toJson()).build();
    }

    // URI : /websources/users/{uID}
    @GET
    @Path("/{uID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("uID") String uID, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        SlimUser u = umc.getOneFilter(eq("uID", uID), new Document(), SlimUser.class);
        if(u == null) Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", "User not found").toJson()).build();
        return Response.status(Response.Status.OK).entity(custom_gson.toJson(u)).build();
    }

    // URI : /websources/users/{uID}/{listName} get specifiec list of user
    //TODO  "NEED TO RE- WRTIE THIS... I've solved this way to stupid... No need for reflections". 
 
    @GET
    @Path("/{uID}/{listName}") // Bug for Ratings, doesn't return an warning just [], in the if we also need to check if object.size != 0. 
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@PathParam("uID") String uID, @PathParam("listName") String listName, @Context HttpHeaders headers) throws IntrospectionException {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        if(!userListNames.contains(listName)) return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: UserSerivce - getList", "Invalid List - Name! ").toJson()).build(); 
        User filtered = umc.getOneFilter(eq("uID", uID), new Document(listName, 1));
        Object result = invokeGetter(filtered, listName);
        if(result instanceof Exception) return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: UserSerivce - getList", "Exception:  " + ((Exception) result).getMessage()).toJson()).build();
        if(result == null) return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Warning: UserSerivce - getList", "Empty list ! ").toJson()).build();
        return Response.status(Response.Status.OK).entity(custom_gson.toJson(result)).build();
    }

    private Object invokeGetter(Object obj, String variableName) {
        try {
            // Note: To use PropertyDescriptor on any field/variable, the field must have both `Setter` and `Getter` method.           
            PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(variableName, obj.getClass());
            Object variableValue = objPropertyDescriptor.getReadMethod().invoke(obj);
            if(variableValue == null) return null;
            return variableValue;
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {  
            return e;
        } 
    }
    
    @PUT
    @Path("/{uID}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateUser(String content, @PathParam("uID") String eID, @Context HttpHeaders httpHeaders, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            String  jsonToUpdate = httpHeaders.getRequestHeader("fieldsToUpdate").get(0);
            Type type = new TypeToken<Map<String, ?>>(){}.getType();
            Map<String, ?> myMap = custom_gson.fromJson(jsonToUpdate, type);
            Document toUpdate = new Document();
            myMap.forEach((key,val)-> toUpdate.append(key,val));
            umc.update(eq("uID",eID), new Document("$set", toUpdate));      
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("Error: EventSerivce - updateEventFields", e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(new Document("success", "Successfully changed").toJson()).build();
    }
    
    @PUT
    @Path("/{uID}/follows")
    @Produces({MediaType.APPLICATION_JSON}) 
    public Response followUser(String content, @PathParam("uID") String uID, @Context HttpHeaders headers) {
        if(headers.getRequestHeader("API_KEY") == null || headers.getRequestHeader("uID") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        String authID = headers.getRequestHeader("uID").get(0);
        if(!Auth.Authenticate_User(API_KEY, authID)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            // first 
            MinimalUser temp = custom_gson.fromJson(content, MinimalUser.class);
            umc.update(eq("uID", uID), new Document("$push", new Document("follows", Document.parse(custom_gson.toJson(temp)))));
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(new Document("success", "Event has been modified").toJson()).build();
    } 
}