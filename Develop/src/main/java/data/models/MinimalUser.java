/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;

import com.google.gson.Gson;

/**
 *
 * @author Chris
 */
public class MinimalUser {
    private String uID;
    private String firstName;
    private String lastName;
    private String profilePicture;

    public MinimalUser(String uID, String firstName, String lastName, String profilePicture) {
        this.uID = uID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }

    public String getuID() {
        return uID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }   
}