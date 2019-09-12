package com.tsystems.javaschool.model;
import java.util.TreeSet;


public class Timetable {
    private String station;
    private TreeSet<TimetableTrain> arrivalTable;
    private TreeSet<TimetableTrain> departureTable;


    public TreeSet<TimetableTrain> getDepartureTable() {
        return departureTable;
    }

    public void setDepartureTable(TreeSet<TimetableTrain> departureTable) {
        this.departureTable = departureTable;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public TreeSet<TimetableTrain> getArrivalTable() {
        return arrivalTable;
    }

    public void setArrivalTable(TreeSet<TimetableTrain> arrivalTable) {
        this.arrivalTable = arrivalTable;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "station='" + station + '\'' +
                ", arrivalTable=" + arrivalTable +
                ", departureTable=" + departureTable +
                '}';
    }
}
