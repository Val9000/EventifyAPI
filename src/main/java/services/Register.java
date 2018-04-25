/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.Filters;
import data.dao.MongoConcrete;
import data.dao.UserMongoConcrete;
import data.models.User;
import java.time.LocalDate;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Valon
 */
@Path("register")
public class Register {

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public String newUser(String content) throws Exception {
        String uId = "";
        try {             
            User temp = new Gson().fromJson(content, User.class);
            String x = BCrypt.hashpw(temp.getPassword(), "$2a$07$2dq0/4gdywDsSSZnTcUVWu"); // for simulation , gehört zum client 
            User newUser = new User(temp.getFirstName(), temp.getLastName(), temp.getBirthDate(), temp.getUsername(), temp.getEmail(), BCrypt.hashpw(x, BCrypt.gensalt(7)), temp.getProfilePicture());
            UserMongoConcrete umc = UserMongoConcrete.getInstance();
            uId = umc.add(newUser);
            newUser.setUID(uId);
            umc.update(Filters.eq("_id", new ObjectId(uId)), new Document("$set", new Document("uID", uId))); 
            System.out.println("*******added new user product*******");
        } catch (Exception e) {
            return new Document("error", e.getMessage()).toJson();
        }
        return new Document("uID", uId).toJson();
    }
}
