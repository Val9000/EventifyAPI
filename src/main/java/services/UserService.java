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
import data.models.User;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.Document;

/**
 *
 * @author Valon
 */
@Path("users")
public class UserService implements IService {
    
    public UserService(){
        
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
    @Path("/{uID}/{listName}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUser(@PathParam("uID") String uID,@PathParam("listName") String list) {
       User x = umc.getOneFilter(Filters.eq("uID", uID),new Document(list, 1));
      
       return new Gson().toJson(" ");
    }
}
