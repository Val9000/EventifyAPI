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
/**
 *
 * @author Valon
 */
@Path("Login")
public class Login {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean login(String content) throws Exception {
        try {
            HandleObjectLogin hol = new Gson().fromJson(content, HandleObjectLogin.class);
            UserMongoConcrete umc = new UserMongoConcrete();
            User temp = umc.getOneFilter(Filters.and(Filters.eq("username", hol.getUsername()), Filters.eq("password", hol.getPassword())));
            
            if(temp==null) return false;
            
        }catch (Exception ex) {
            printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
}

class HandleObjectLogin {

    private String username;
    private String password;

    public HandleObjectLogin(String _name, String _password) {
        username = _name;
        password = _password;
    }

    public String getUsername() {
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
