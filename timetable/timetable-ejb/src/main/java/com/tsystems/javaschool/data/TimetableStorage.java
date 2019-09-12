package com.tsystems.javaschool.data;

import com.tsystems.javaschool.model.Timetable;
import com.tsystems.javaschool.rest.RestClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TimetableStorage implements Serializable {
    private Map<String, Timetable> storage = new HashMap<String, Timetable>();
    private List<String> stations = new ArrayList<String>();

    @Inject
    private RestClient restClient;

    @PostConstruct
    public void init(){
        stations = restClient.getStations();
    }

    public List<String> getStations() {
        return stations;
    }

    public void addStation(String station){
        stations.add(station);
    }

    public Timetable getTimetable(String stationName) {
        return storage.get(stationName);
    }

    public void setTimetable(String station, Timetable timetable) {
        storage.put(station,timetable);
    }
}
