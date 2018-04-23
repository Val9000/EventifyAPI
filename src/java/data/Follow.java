/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Chris
 */
public class Follow implements Serializable{
    private final String User1ID;
    private final String User2ID;
    private final LocalDate created;

    public Follow(String User1ID, String User2ID) {
        this.User1ID = User1ID;
        this.User2ID = User2ID;
        this.created = LocalDate.now();
    }

    public String getUser1ID() {
        return User1ID;
    }

    public String getUser2ID() {
        return User2ID;
    }

    public LocalDate getCreated() {
        return created;
    }

    @Override
    public String toString(){
        return "{ 'User1ID': '" + User1ID + "', 'User2ID': '" + User2ID + "', 'created': '" + created + "' }";
    }
}