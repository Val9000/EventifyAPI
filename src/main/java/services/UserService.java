/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import data.models.Event;
import data.models.MinimalUser;
import data.models.SlimEvent;
import data.models.User;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import org.bson.types.ObjectId;
import static services.IService.emc;
import static services.IService.umc;

/**
 *
 * @author Valon
 */
@Path("users")
public class UserService implements IService {
    List<String> userListNames;
    
    public UserService() {
        userListNames = Arrays.asList("follows", "participatesIn","ratings","likes");
    }

    // URI : /websources/users/
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllUsers() {
        return new Gson().toJson(umc.getAllFilter(new Document(), new Document()));
    }

    // URI : /websources/users/{uID}
    @GET
    @Path("/{uID}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUser(@PathParam("uID") String uID) {
        return new Gson().toJson(umc.getOneFilter(Filters.eq("uID", uID), new Document()));
    }

    // URI : /websources/users/{uID}/{listName} get specifiec list of user
    @GET
    @Path("/{uID}/{listName}") // Bug for Ratings, doesn't return an warning just [], in the if we also need to check if object.size != 0. 
    @Produces({MediaType.APPLICATION_JSON})
    public String getList(@PathParam("uID") String uID, @PathParam("listName") String listName) throws IntrospectionException {
        if(!userListNames.contains(listName)) return new Document("Warning: UserSerivce - getList", "Invalid List - Name! ").toJson(); 
        User filtered = umc.getOneFilter(Filters.eq("uID", uID), new Document(listName, 1));
        Object result = invokeGetter(filtered, listName);
        if(result instanceof Exception) return new Document("Error: UserSerivce - getList", "Exception:  " + ((Exception) result).getMessage()).toJson();
        if(result == null) return new Document("Warning: UserSerivce - getList", "Empty list ! ").toJson();
        return new Gson().toJson(result);
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
    public String updateUser(String content) {
        try {
            //Event temp = new Gson().fromJson(content, Event.class);
            
            //emc.update(Filters.eq("eID", temp.getEID()), new Document("$set", content));
        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Document("success", "Event has been modified").toJson();
    }
    
    
    @PUT
    @Path("/{uID}/follows")
    @Produces({MediaType.APPLICATION_JSON}) 
    public String followUser(String content, @PathParam("uID") String uID) {
        try {
            
            // first 
            MinimalUser temp = new Gson().fromJson(content, MinimalUser.class);
            umc.update(Filters.eq("uID", uID), new Document("$push", new Document("follows", Document.parse(new Gson().toJson(temp)))));
        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Document("success", "Event has been modified").toJson();
    }
    
    
    
}
