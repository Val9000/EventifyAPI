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
public class Location implements Serializable {
    private final String lat;
    private final String lon;
    
    public Location(String lat, String lon){
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "{ 'lat': '" + lat + "', 'lon': '" + lon + "' }";
    }
}