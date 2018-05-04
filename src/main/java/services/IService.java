/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.google.gson.Gson;
import data.dao.*;

/**
 *
 * @author Valon
 */
public interface IService {
    public static final  EventMongoConcrete emc = EventMongoConcrete.getInstance();
    public static final  UserMongoConcrete umc = UserMongoConcrete.getInstance();
    public static final  Gson gson = new Gson();
}
