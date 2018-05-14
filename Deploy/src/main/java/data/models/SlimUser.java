/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.time.Instant;

/**
 *
 * @author Chris
 */
public class SlimUser {
    private String uID;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private UserType type;
    private int numberOfCreated;
    private int numberOfParticipated;
    private double rating;
    private int totalFollowers;
    private Instant created;

    public SlimUser(String uID, String firstName, String lastName, String profilePicture, UserType type, int numberOfCreated, int numberOfParticipated, double rating, int totalFollowers, Instant created) {
        this.uID = uID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.type = type;
        this.numberOfCreated = numberOfCreated;
        this.numberOfParticipated = numberOfParticipated;
        this.rating = rating;
        this.totalFollowers = totalFollowers;
        this.created = created;
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

    public UserType getType() {
        return type;
    }

    public int getNumberOfCreated() {
        return numberOfCreated;
    }

    public int getNumberOfParticipated() {
        return numberOfParticipated;
    }

    public double getRating() {
        return rating;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public Instant getCreated() {
        return created;
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

    public void setType(UserType type) {
        this.type = type;
    }

    public void setNumberOfCreated(int numberOfCreated) {
        this.numberOfCreated = numberOfCreated;
    }

    public void setNumberOfParticipated(int numberOfParticipated) {
        this.numberOfParticipated = numberOfParticipated;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }   
}