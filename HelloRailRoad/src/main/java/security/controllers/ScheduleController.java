package security.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.messaging.MessageSender;
import security.model.Schedule;
import security.model.Station;
import security.service.ScheduleService;
import security.service.StationService;
import security.util.ScheduleUpdValidator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private StationService stationService;

    private static final Logger log = Logger.getLogger(ScheduleController.class);


    @GetMapping(value = "/admin/update_schedule/{id}")
    public ModelAndView updateSchedule(ModelAndView model,
                                       @PathVariable("id") Long id){
        Schedule schedule = scheduleService.findScheduleById(id);
        model.addObject(schedule);
        model.setViewName("scheduleUpdate");
        return model;
    }


    @PostMapping(value = {"/getSchedule", "/admin/getSchedule"})
    public ModelAndView getSchedule(ModelAndView model,
                                    @RequestParam(value = "station") String stationName,
                                    @RequestParam(value = "date") String date) throws ParseException {
        Station station = stationService.getStationByName(stationName);
        if(date.equals("") || station == null){
            model.addObject("message", "Указаны не все необходимые параметры");
            model.addObject("stations", stationService.getAllStations());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                model.setViewName("admin");
            }
            else model.setViewName("passenger");
            return model;
        }
        Date scheduleDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.replace("T", " "));
        long tableSize = scheduleService.getCountOfSchedules(station, scheduleDate);
        int numOfRows = 10;
        int numOfPages = (int) Math.ceil(tableSize * 1.0 / numOfRows);
        List<Schedule> schedules = scheduleService.findScheduleOnDate(station,scheduleDate, 0, numOfRows);
        model.addObject("currentTime", new Date().getTime());
        model.addObject("date",date);
        model.addObject("schedules",schedules);
        model.addObject("numOfPages",numOfPages);
        model.addObject("page",1);
        model.addObject("schedules", scheduleService.findScheduleOnDate(station,scheduleDate,0,numOfRows));
        model.setViewName("stationSchedule");
        return model;
    }


    @PostMapping(value = "/admin/update_schedule")
    public ModelAndView updateSchedule(ModelAndView model,
                                       @RequestParam(value = "id") Long id,
                                       @RequestParam Map<String, String> params) throws ParseException {
        Schedule schedule = scheduleService.findScheduleById(id);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Schedule> schedules = new ArrayList<Schedule>();

        if(params.get("arrivalTime")!=null && !params.get("arrivalTime").equals("")){
            String arrivalDate = params.get("arrivalTime").replace("T", " ");
            Date arrival = format.parse(arrivalDate);
            schedule.setArrivalTime(arrival);
            schedules = stationService.getScheduleOnDate(schedule.getStation(),schedule.getArrivalTime());
        }
        if(params.get("departureTime")!=null && !params.get("departureTime").equals("")){
            String departureDate = params.get("departureTime").replace("T", " ");
            Date departure = format.parse(departureDate);
            schedule.setDepartureTime(departure);
            schedules = stationService.getScheduleOnDate(schedule.getStation(),schedule.getDepartureTime());
        }
        String msg = ScheduleUpdValidator.updateValidation(schedule);
        if (msg.equals("SUCCESS")) {
            scheduleService.saveSchedule(schedule);
            model.addObject("schedules", schedules);
            model.setViewName("stationSchedule");
            try {
                messageSender.sendMessage(schedule.getStation().getName());
            } catch (Exception e){
                log.error("Exception while sending message: " + e);
            }
        } else {
            schedule = scheduleService.findScheduleById(id);
            model.addObject("schedule", schedule);
            model.addObject("message",msg);
            model.setViewName("scheduleUpdate");
        }
        return model;
    }
}
