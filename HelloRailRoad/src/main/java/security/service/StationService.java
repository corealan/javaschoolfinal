package security.service;

import security.model.Schedule;
import security.model.Station;
import security.rest.Timetable;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Service class contains business logic concerning {@link Station} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface StationService {
    void saveStation(Station station);
    Station getStationById(long id);
    void addNewStation(String name, String adjacentName, String adjacentName2);
    List<Station> getAllStations();
    Station getStationByName(String name);
    List<LinkedList<Station>> getRoutes(String departure, String destination);
    List<Station> getStationsListFromString(String ids);
    List<Schedule> getScheduleOnDate(Station station, Date date);
    Timetable getTimetable(Station station, Date date) throws ParseException;
}
