package com.enlace.lattemilkcollection;

public class FarmersList {
    public String farmer_id;
    public String name;
    public String lat;
    public String lng;
    public String rate_per_litre;
    public String pnumber;
    public String route_id;
    public String route_name;
    public String username;
    public FarmersList(String farmer_id, String name, String lat, String lng, String rate_per_litre, String pnumber, String route_id, String route_name,String username) {
        this.farmer_id = farmer_id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rate_per_litre = rate_per_litre;
        this.pnumber = pnumber;
        this.route_id = route_id;
        this.route_name = route_name;
        this.username = username;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getRate_per_litre() {
        return rate_per_litre;
    }

    public String getPnumber() {
        return pnumber;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public String getUsername() {
        return username;
    }
}
