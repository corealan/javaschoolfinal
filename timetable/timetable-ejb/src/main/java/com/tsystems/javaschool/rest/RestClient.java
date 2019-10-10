package com.tsystems.javaschool.rest;

import com.tsystems.javaschool.model.Timetable;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestScoped
public class RestClient implements Serializable {
    public Timetable getTimetable(String station){
        Client client = ClientBuilder.newClient();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String targetURL = "http://localhost:8081/HelloRailRoad/timetable?station=" + station + "&date=" + date;

        WebTarget target = client.target(targetURL);
        Timetable timetable = target
                .request()
                .get(Timetable.class);
        client.close();
        return timetable;
    }

    public List<String> getStations(){
        Client client = ClientBuilder.newClient();
        String targetURL = "http://localhost:8081/HelloRailRoad/station_names";
        WebTarget target = client.target(targetURL);
        List stations = new ArrayList();
        try {
            stations = target
                    .request()
                    .get(List.class);
            client.close();
        } catch (Exception ex){
        }
        return stations;
    }
}
