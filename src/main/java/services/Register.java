/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.Utilities;
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

/**
 *
 * @author Valon
 */
@Path("Register")
public class Register {

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public String newUser(String content) throws Exception {
        String uId = "";
        try {             
            User temp = new Gson().fromJson(content, User.class);
            User newUser = new User(temp.getFirstName(), temp.getLastName(), temp.getBirthDate(), temp.getUsername(), temp.getEmail(), temp.getPassword(), temp.getProfilePicture());
            UserMongoConcrete umc = UserMongoConcrete.getInstance();
            uId = umc.add(newUser);
            newUser.setUID(uId);
            umc.update(Filters.eq("_id", new ObjectId(uId)), new Document("$set", new Document("uID", uId))); 
            System.out.println("*******added new user product*******");
        } catch (Exception e) {
            return "Register - Error : " + e.getMessage();
        }
        return uId;
    }
}
