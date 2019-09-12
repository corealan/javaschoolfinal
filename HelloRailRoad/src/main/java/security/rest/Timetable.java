package security.rest;

import security.model.Schedule;
import security.model.Station;
import security.model.Train;
import java.util.*;
/**
 * Timetable class to encapsulate meaningful for timetable app information about station timetable
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public class Timetable {

    private String station;
    private TreeSet<TimetableTrain> departureTable;
    private TreeSet<TimetableTrain> arrivalTable;


    public Timetable() {
        arrivalTable = new TreeSet<TimetableTrain>();
        departureTable = new TreeSet<TimetableTrain>();
    }

    public void addDepartureSchedule(Schedule schedule)  {
        if(schedule.getDepartureTime()!=null) {
            TimetableTrain timetableTrain = createTimetableTrain(schedule);
            timetableTrain.setDepartureTime(schedule.getDepartureTime());
            departureTable.add(timetableTrain);
        }
    }

    public void addArrivalSchedule(Schedule schedule) {
        if(schedule.getArrivalTime()!=null) {
            TimetableTrain timetableTrain = createTimetableTrain(schedule);
            timetableTrain.setArrivalTime(schedule.getArrivalTime());
            arrivalTable.add(timetableTrain);
        }
    }

    private TimetableTrain createTimetableTrain(Schedule schedule){
        TimetableTrain timetableTrain = new TimetableTrain();
        Train train = schedule.getTrain();
        List<Station> route = train.getRoute();
        timetableTrain.setNumber(train.getTrainNumber());
        timetableTrain.setDeparture(route.get(0).getName());
        timetableTrain.setDestination(route.get(route.size()-1).getName());
        return timetableTrain;
    }
    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public TreeSet<TimetableTrain> getDepartureTable() {
        return departureTable;
    }

    public void setDepartureTable(TreeSet<TimetableTrain> departureTable) {
        this.departureTable = departureTable;
    }

    public TreeSet<TimetableTrain> getArrivalTable() {
        return arrivalTable;
    }

    public void setArrivalTable(TreeSet<TimetableTrain> arrivalTable) {
        this.arrivalTable = arrivalTable;
    }
}
