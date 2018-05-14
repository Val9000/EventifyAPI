/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.Auth;
import com.mongodb.client.model.Filters;

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
import org.mindrot.jbcrypt.BCrypt;

/**
 * REST Web Service
 *
 * @author Valon
 */
@Path("login")
public class Login implements IService{

    @Context
    private UriInfo context;

    public Login() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String content, @Context HttpHeaders headers) throws Exception {
        if(headers.getRequestHeader("API_KEY") == null) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        String API_KEY = headers.getRequestHeader("API_KEY").get(0);
        if(!Auth.Authenticate(API_KEY)) return Response.status(Response.Status.UNAUTHORIZED).entity(new Document("error", "Unauthorized").toJson()).build();
        try {
            HandleObjectLogin hol = custom_gson.fromJson(content, HandleObjectLogin.class);
            User temp = umc.getOneFilter(Filters.eq("email", hol.getEmail()), new Document());
            if (temp == null) return Response.status(Response.Status.BAD_REQUEST).entity((new Document("error", "email doesn't exist")).toJson()).build();         
            if (BCrypt.checkpw(hol.getPassword(), temp.getPassword())) return Response.status(Response.Status.OK).entity((new Document("uID", temp.getuID())).toJson()).build();

        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity((new Document("Error: login", "Exception : " + ex.getMessage())).toJson()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity((new Document("Info: login", "Wrong password")).toJson()).build();
    }
}

class HandleObjectLogin {

    private String email;
    private String password;

    public HandleObjectLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}