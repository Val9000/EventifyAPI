/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.time.LocalDate;

/**
 *
 * @author Chris
 */
public class Rating {
    private final String uID;
    private final String eID;
    private final int value;
    private final LocalDate created;

    public Rating(String uID, String eID, int value) {
        this.uID = uID;
        this.eID = eID;
        this.value = value;
        this.created = LocalDate.now();
    }

    public String getUID() {
        return uID;
    }

    public String getEID() {
        return eID;
    }

    public int getValue() {
        return value;
    }

    public LocalDate getCreated() {
        return created;
    }

    @Override
    public String toString(){
        return "{ 'uID': '" + uID + "', 'eID': '" + eID + "', 'value': '" + value + "', 'created': '" + created + "' }";
    }
}