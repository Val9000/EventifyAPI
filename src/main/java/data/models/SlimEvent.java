/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;

import com.google.gson.Gson;
import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class SlimEvent implements Serializable{
    private String eID;
    private String name;
    private int totalLikes;
    private int maxParticipators;
    private int totalParticipators;

    public SlimEvent(String eID, String name, int totalLikes, int maxParticipators, int totalParticipators) {
        this.eID = eID;
        this.name = name;
        this.totalLikes = totalLikes;
        this.maxParticipators = maxParticipators;
        this.totalParticipators = totalParticipators;
    }

    public String geteID() {
        return eID;
    }

    public String getName() {
        return name;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public int getMaxParticipators() {
        return maxParticipators;
    }

    public int getTotalParticipators() {
        return totalParticipators;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void setMaxParticipators(int maxParticipators) {
        this.maxParticipators = maxParticipators;
    }

    public void setTotalParticipators(int totalParticipators) {
        this.totalParticipators = totalParticipators;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }   
}