/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.Auth;
import com.mongodb.client.model.Filters;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import data.dao.UserMongoConcrete;
import data.models.User;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import static services.IService.custom_gson;

/**
 * REST Web Service
 *
 * @author Chris
 */
@Path("register")
@Api( value = "/register", description = "Sign up for Eventify" )
public class Register {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Register
     */
    public Register() {
    }

    @POST
    @ApiOperation( value = "Register new User", notes = "User object" )
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response newUser(String content, @Context HttpHeaders headers) throws Exception {
        String uId = "";
        if(headers.getRequestHeader("API_KEY") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        if(!Auth.Authenticate(API_KEY)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try { 
            User temp = custom_gson.fromJson(content, User.class);
            User newUser = new User(temp.getFirstName(), temp.getLastName(), temp.getBirthDate(), temp.getEmail(), BCrypt.hashpw(temp.getPassword(), "$2a$07$2dq0/4gdywDsSSZnTcUVWu"), temp.getProfilePicture());
            UserMongoConcrete umc = UserMongoConcrete.getInstance();
            uId = umc.add(newUser);
            newUser.setuID(uId);
            umc.update(Filters.eq("_id", new ObjectId(uId)), new Document("$set", new Document("uID", uId))); 
            System.out.println("*******added new user product*******");
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Document("error", e.getMessage()).toJson()).build();
        }
        return Response.status(Response.Status.CREATED).entity(new Document("uID", uId).toJson()).build();
    }
}