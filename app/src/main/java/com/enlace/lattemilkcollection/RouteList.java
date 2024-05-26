package com.enlace.lattemilkcollection;

public class RouteList {
    public String route_id;
    public String route_name;
    public String user_name;

    public RouteList(String route_id, String route_name, String user_name) {
        this.route_id = route_id;
        this.route_name = route_name;
        this.user_name = user_name;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public String getUser_name() {
        return user_name;
    }
}
