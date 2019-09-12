package com.tsystems.javaschool.controller;

import com.tsystems.javaschool.data.TimetableStorage;
import com.tsystems.javaschool.model.Timetable;
import com.tsystems.javaschool.model.TimetableTrain;
import com.tsystems.javaschool.rest.RestClient;


import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Named
@SessionScoped
public class TimetableController implements Serializable {
    @Inject
    private RestClient restClient;
    @Inject
    private TimetableStorage timetableStorage;

    private String station;

    private Timetable timetable = new Timetable();

    @PostConstruct
    public void initTimetable() {
        if(timetableStorage.getStations().size()>0) {
            station = timetableStorage.getStations().get(0);
            timetableStorage.setTimetable(station, restClient.getTimetable(station));
            timetable = timetableStorage.getTimetable(station);
            removeOldTrains(timetable);
        }
    }

    public void updateTimetable(){
        if (timetableStorage.getStations().size()==0){
            timetableStorage.init();
            initTimetable();
        }
        timetable = timetableStorage.getTimetable(station);
        removeOldTrains(timetable);
    }

    public List<String> getStations(){
        return timetableStorage.getStations();
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }


    public void setStation(String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    private void removeOldTrains(Timetable timetable){
        List<TimetableTrain> removeList = new ArrayList<TimetableTrain>();
        for(TimetableTrain train : timetable.getArrivalTable()){
            if(new Date().after(train.getArrivalTime())){
                removeList.add(train);
            }
        }
        timetable.getArrivalTable().removeAll(removeList);
        removeList.clear();
        for(TimetableTrain train : timetable.getDepartureTable()){
            if(new Date().after(train.getDepartureTime())){
                removeList.add(train);
            }
        }
        timetable.getDepartureTable().removeAll(removeList);
    }

    public void findTimetable() {
        timetableStorage.setTimetable(station,restClient.getTimetable(station));
        timetable = timetableStorage.getTimetable(station);
        removeOldTrains(timetable);
    }
}
