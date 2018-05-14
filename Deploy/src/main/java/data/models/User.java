/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.models;

import com.google.gson.Gson;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Valon
 */
public class User {

    private String uID;
    private String firstName;
    private String lastName;
    private Instant birthDate ;
    private String email;
    private String password;
    private String profilePicture;
    private UserState state;
    private UserType type;
    private int numberOfCreated;
    private int numberOfParticipated;
    private int totalFollowers;
    private Instant created;
    private Instant lastEdited;
    private ArrayList<Integer> ratings;
    private ArrayList<SlimUser> follows;
    private ArrayList<SlimEvent> likes;
    private ArrayList<SlimEvent> participatesIn;

    public User(String firstName, String lastName, Instant birthDate, String email, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.state = UserState.Activated;
        this.type = UserType.User;
        this.numberOfCreated = 0;
        this.numberOfParticipated = 0;
        this.ratings = new ArrayList<>();
        this.totalFollowers = 0;
        this.created = Instant.now();
        this.lastEdited = Instant.now();
        this.follows = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.participatesIn = new ArrayList<>();
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

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getLastEdited() {
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

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
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

    public void setCreated(Instant created) {
        this.created = created;
    }

    public void setLastEdited(Instant lastEdited) {
        this.lastEdited = lastEdited;
    }

    public ArrayList<SlimEvent> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<SlimEvent> likes) {
        this.likes = likes;
    }

    public ArrayList<SlimEvent> getParticipatesIn() {
        return participatesIn;
    }

    public void setParticipatesIn(ArrayList<SlimEvent> participatesIn) {
        this.participatesIn = participatesIn;
    }

    public ArrayList<SlimUser> getFollows() {
        return follows;
    }

    public void setFollows(ArrayList<SlimUser> follows) {
        this.follows = follows;
    }

    public void setRatings(ArrayList<Integer> rates) {
        this.ratings = ratings;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
