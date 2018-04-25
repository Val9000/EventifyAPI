/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import Util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Valon
 */
@Path("login")
public class Login {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(String content) throws Exception {
        try {
           
            //* TEST  Conclusion : Salt on the client side needs to be constant
            /*String val = "val";
            String clientConstantSalt = BCrypt.gensalt(7);
            // Register
            String clientHash = BCrypt.hashpw(val, clientConstantSalt);
            String RegisterHash = BCrypt.hashpw(clientHash, BCrypt.gensalt(7)); // gets stored to the db.
            
            // Login
            String clientHash2 = BCrypt.hashpw(val, clientConstantSalt);
            
            
            System.out.println("check : " + BCrypt.checkpw(clientHash2,RegisterHash));
            */
            
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
            HandleObjectLogin hol = gson.fromJson(content, HandleObjectLogin.class);
            hol.setPassword(BCrypt.hashpw(hol.getPassword(), "$2a$07$2dq0/4gdywDsSSZnTcUVWu")); // simulation : pretend this was done by the client xD boi
            System.out.println("password * " + hol.getPassword());
            UserMongoConcrete umc = UserMongoConcrete.getInstance();
            User temp = umc.getOneFilter(Filters.eq("username", hol.getUsername()));

            if (temp == null) {
                temp = umc.getOneFilter(Filters.eq("email", hol.getEmail()));
                if (temp == null) {
                    return (new Document("error", "Invalid username/email or password")).toJson();
                }
            }
           
            System.out.println("services.Login.login() : " + BCrypt.checkpw(hol.getPassword(),temp.getPassword()));
            if (BCrypt.checkpw(hol.getPassword(),temp.getPassword())) {
                return (new Document("uID", temp.getUID())).toJson();
            }

        } catch (Exception ex) {
            printStackTrace();
            System.out.println(ex.getMessage());
            return (new Document("error", ex.getMessage())).toJson();
        }
        return (new Document("error", "Invalid username/email or password")).toJson();
    }
}

class HandleObjectLogin {

    private String email;
    private String password;
    private String username;

    public HandleObjectLogin(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "HandleObjectLogin{" + "email=" + email + ", password=" + password + ", username=" + username + '}';
    }
    

}
