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
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author Valon
 */
@Path("Login")
public class Login {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(String content) throws Exception {
        try {
            HandleObjectLogin hol = new Gson().fromJson(content, HandleObjectLogin.class);
            UserMongoConcrete umc = UserMongoConcrete.getInstance();
            User temp = umc.getOneFilter(Filters.and(Filters.eq("username", hol.getIdentifier()), Filters.eq("password", BCrypt.hashpw(hol.getPassword(), BCrypt.gensalt(7)))));
            if(temp==null){
                temp = umc.getOneFilter(Filters.and(Filters.eq("email", hol.getIdentifier()), Filters.eq("password", BCrypt.hashpw(hol.getPassword(), BCrypt.gensalt(7)))));
                if(temp==null) return (new Document("error", "Invalid username/email or password")).toJson();
            } else return (new Document("uID", temp.getUID())).toJson();
        } catch (Exception ex) {
            printStackTrace();
            System.out.println(ex.getMessage());
            return (new Document("error", ex.getMessage())).toJson();
        }
        return (new Document("error", "Invalid username/email or password")).toJson();
    }
}

class HandleObjectLogin {

    private String username;
    private String password;

    public HandleObjectLogin(String _name, String _password) {
        username = _name;
        password = _password;
    }

    public String getIdentifier() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "HandleObjectLogin{" + "username=" + username + ", password=" + password + '}';
    }
}