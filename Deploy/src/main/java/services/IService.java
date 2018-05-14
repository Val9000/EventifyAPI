/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author Chris
 */
import Util.InstantTypeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.dao.*;
import java.time.Instant;

/**
 *
 * @author Valon
 */
public interface IService {
    public static final  EventMongoConcrete emc = EventMongoConcrete.getInstance();
    public static final  UserMongoConcrete umc = UserMongoConcrete.getInstance();
    public static final Gson custom_gson  = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantTypeConverter()).create();
}