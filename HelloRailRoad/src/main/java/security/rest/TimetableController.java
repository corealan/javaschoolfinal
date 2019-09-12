package security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import security.messaging.MessageSender;
import security.model.Station;
import security.service.StationService;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * RestController class to provide timetable information by request
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@RestController
public class TimetableController {

    @Autowired
    private StationService stationService;
    @Autowired
    private MessageSender messageSender;

    @GetMapping(value = "/timetable")
    public Timetable getSchedule(@RequestParam(value = "station") String stationName,
                                 @RequestParam(value = "date") String dateStr) throws ParseException {
        Station station = stationService.getStationByName(stationName);
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
        Timetable timetable = stationService.getTimetable(station,date);
        return timetable;
    }
    @GetMapping(value = "/station_names")
    public List<String> getStations(){
        List<String> stationNames = new ArrayList<String>();
        for(Station station : stationService.getAllStations()){
            stationNames.add(station.getName());
        }
        return stationNames;
    }

}
