package security.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.StationDAO;
import security.model.Schedule;
import security.model.Station;
import security.rest.Timetable;
import security.model.Train;
import security.service.StationService;
import security.service.TrainService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StationServiceImpl implements StationService {
    private static final Logger log = Logger.getLogger(StationServiceImpl.class);

    private static List<LinkedList<Station>> routes = new LinkedList<LinkedList<Station>>();

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationDAO stationDAO;

    @Transactional
    public void saveStation(Station station) {
        stationDAO.saveStation(station);
    }

    @Transactional
    public Station getStationById(long id) {
        return stationDAO.getStationById(id);
    }

    @Transactional
    public  Station getStationByName(String name){
        return stationDAO.getStationByName(name);
    }

    @Transactional
    public List<Station> getAllStations() {
        return stationDAO.getAllStations();
    }

    @Transactional
    public void addNewStation(String name, String adjacentName, String adjacentName2) {
        Station station = getStationByName(name);
        Station adjacent = getStationByName(adjacentName);
        Station adjacent2 = getStationByName(adjacentName2);

        if(station == null) {
            station = new Station();
            station.setName(name);
            log.info("new station + " + name + " created");

            if (adjacent != null && adjacent2 == null) {
                station.addAdjacent(adjacent);
                adjacent.addAdjacent(station);
                saveStation(station);
                log.info("new station " + name + " with 1 adjacent station (" + adjacentName +") saved");
            } else
                if(adjacent != null && adjacent2 != null){
                    addStationBetween(station, adjacent, adjacent2);
            }
            else saveStation(station);
        } else
            if(!station.getAdjacent().contains(adjacent) && !station.equals(adjacent) && adjacent!=null){
            station.addAdjacent(adjacent);
            adjacent.addAdjacent(station);
            saveStation(station);
            log.info("connection between stations " + name + " and " + adjacentName + " created successfully");
            }
    }

    public Timetable getTimetable(Station station, Date date) throws ParseException {
        Timetable timetable = new Timetable();
        timetable.setStation(station.getName());
        for(Schedule schedule : getScheduleOnDate(station,date)){
            if(schedule != null) {
                timetable.addDepartureSchedule(schedule);
                timetable.addArrivalSchedule(schedule);
            }
        }
        return timetable;
    }

    private void addStationBetween(Station newStation, Station adjacent1, Station adjacent2){
        newStation.addAdjacent(adjacent1);
        adjacent1.addAdjacent(newStation);
        adjacent1.removeAdjacent(adjacent2);

        newStation.addAdjacent(adjacent2);
        adjacent2.addAdjacent(newStation);
        adjacent2.removeAdjacent(adjacent1);
        saveStation(newStation);
        log.info("New station " + newStation.getName() + " between stations " + adjacent1.getName() + " and " + adjacent2.getName() + " added successfully");

        for(Train t : trainService.getAllTrains()){
            trainService.updateFullRoute(t, newStation);
        }
    }

    @Transactional
    public List<LinkedList<Station>> getRoutes(String departure, String destination){
        routes.clear();
        Station departureStation = getStationByName(departure);
        LinkedList<Station> visited = new LinkedList<Station>();
        visited.add(departureStation);
        depthFirst(destination, visited);
        log.info("All routes between stations " + departure + " and " + destination + " found");
        return routes;
    }

    @Transactional
    public List<Station> getStationsListFromString(String ids) {
        List<Station> result = new ArrayList<Station>();
        String str = ids.substring(1, ids.length()-1);
        List<String> strings = Arrays.asList(str.split(", "));

        for(String s : strings){
            result.add(getStationById(Long.parseLong(s)));
        }
        return result;
    }

    public List<Schedule> getScheduleOnDate(Station station, Date date) {
        List<Schedule> schedulesOnDate = new ArrayList<Schedule>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String requestDate = formatter.format(date);
        for(Schedule schedule : station.getSchedules()){
            String arriveDate = "";
            String departureDate = "";
            if (schedule.getArrivalTime() != null){
                arriveDate = formatter.format(schedule.getArrivalTime());
            }
            if(schedule.getDepartureTime() != null){
                departureDate = formatter.format(schedule.getDepartureTime());
            }
            if((departureDate.equals(requestDate) || arriveDate.equals(requestDate)) && !schedulesOnDate.contains(schedule)){
                schedulesOnDate.add(schedule);
            }
        }
        log.info("Schedule for station " + station.getName() + " on date " + requestDate + " found. Number: " + schedulesOnDate.size());
        return schedulesOnDate;
    }

    private static void depthFirst(String destination,LinkedList<Station> visited){

        LinkedList<Station> nodes = new LinkedList<Station>(visited.getLast().getAdjacent());
        for(Station node : nodes){
            if(visited.contains(node)){
                continue;
            }
            if(node.getName().equals(destination)){
                visited.add(node);
                routes.add(new LinkedList<Station>(visited));
                visited.removeLast();
                break;
            }
        }
        for(Station node : nodes){
            if(visited.contains(node) || node.getName().equals(destination)){
                continue;
            }
            visited.addLast(node);
            depthFirst(destination, visited);
            visited.removeLast();
        }
    }
}
