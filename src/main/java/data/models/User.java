/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;


/**
 *
 * @author Chris
 */

import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public class User {
    private String uID;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;
    private String password;
    private String profilePicture;
    private UserState state;
    private UserType type;
    private int numberOfCreated;
    private int numberOfParticipated;
    private ArrayList<Integer> ratings;
    private int totalFollowers;
    private LocalDate created;
    private LocalDate lastEdited;
    private HashMap<String, SlimUser> follows;
    private HashMap<String, SlimEvent> likes;
    private HashMap<String, SlimEvent> participatesIn;

    public User(String firstName, String lastName, LocalDate birthdate, String email, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.state = UserState.Activated;
        this.type = UserType.User;
        this.numberOfCreated = 0;
        this.numberOfParticipated = 0;
        this.ratings = new ArrayList<>();
        this.totalFollowers = 0;
        this.created = LocalDate.now();
        this.lastEdited = LocalDate.now();
        this.follows = new HashMap<>();
        this.likes = new HashMap<>();
        this.participatesIn = new HashMap<>();
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public UserState getState() {
        return state;
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

    public Collection<Integer> getRatings() {
        return ratings;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getLastEdited() {
        return lastEdited;
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

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setState(UserState state) {
        this.state = state;
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

    public void setRatings(Collection<Integer> ratings) {
        this.ratings = (ArrayList<Integer>) ratings;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setLastEdited(LocalDate lastEdited) {
        this.lastEdited = lastEdited;
    }

   
    public HashMap<String, SlimEvent> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, SlimEvent> likes) {
        this.likes = likes;
    }

    public HashMap<String, SlimEvent> getParticipatesIn() {
        return participatesIn;
    }

    public void setParticipatesIn(HashMap<String, SlimEvent> participatesIn) {
        this.participatesIn = participatesIn;
    }
    

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }   
}