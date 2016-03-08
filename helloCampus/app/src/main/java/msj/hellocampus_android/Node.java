package msj.hellocampus_android;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mason on 3/6/16.
 */

@JsonIgnoreProperties({ "memos" })
public class Node {

    private String id;
    private String name;
    private String type;
    private double latitude;
    private double longitude;

    public Node() {
        // empty default constructor, necessary for Firebase to be able to deserialize
    }

    public Node(String id, String type, String name, GeoLocation location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
