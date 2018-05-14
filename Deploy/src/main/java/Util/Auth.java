/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import data.models.User;
import org.bson.Document;
import services.IService;

/**
 *
 * @author Chris
 */
public class Auth implements IService{
    private static final String API_KEY = "dmFsaTEyMzRpMjMwOGhnaW9zZ2Rqb2lqY3hvaTgwN";   
    public static boolean Authenticate(String API_KEY){
        return Auth.API_KEY.equals(API_KEY.split("\"")[0]);
    }
    public static boolean Authenticate_User(String API_KEY, String uID){
        if(!Auth.API_KEY.equals(API_KEY.split("\"")[0])) return false;
        else {
            User oneFilter = umc.getOneFilter(new Document("uID",uID.split("\"")[0]), new Document("uID",1));
            return oneFilter != null;
        }
    }
}
