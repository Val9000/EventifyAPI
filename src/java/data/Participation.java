/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class Participation implements Serializable {
    private final String uID;
    private final String eID;
    private final ParticipationType type;

    public Participation(String uID, String eID, ParticipationType type) {
        this.uID = uID;
        this.eID = eID;
        this.type = type;
    }

    public String getuID() {
        return uID;
    }

    public String geteID() {
        return eID;
    }

    public ParticipationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{ 'uID': '" + uID + "', 'eID': '" + eID + "', 'type': '" + type + "' }";
    }
}