package security.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.model.Schedule;
import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.model.security.Passenger;
import security.service.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PassengerController {
    private static final Logger log = Logger.getLogger(PassengerController.class);
    private static final int NUM_OF_ROWS = 10;

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationService stationService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TicketService ticketService;

    @GetMapping(value = "/myTickets")
    public ModelAndView myTickets(ModelAndView model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Passenger passenger = passengerService.findByUsername(authentication.getName());
        List<Ticket> tickets = passenger.getTickets();
        Collections.reverse(tickets);
        model.addObject("tickets", tickets);
        model.setViewName("myTickets");
        return model;
    }

    @PostMapping(value = "/findTrains")
    public ModelAndView findTrains(ModelAndView modelAndView,@RequestParam Map<String, String> params) throws ParseException {
        Station departure = stationService.getStationByName(params.get("departure"));
        Station destination = stationService.getStationByName(params.get("destination"));
        Date from = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("after"));
        Date to = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("before"));
        int numOfPages = (int) Math.ceil(trainService.findTrains(departure,destination,from,to).size() * 1.0 / 10);
        List<Train> trains = trainService.findTrains(departure,destination,from,to,0, 10);
        return getModelAndView(modelAndView, trains, 1, departure, destination, from, to, numOfPages);
    }


    @GetMapping(value = "/findTrains/{departure}/{depTime}/{destination}/{arrTime}/{page}")
    public ModelAndView findTrains(ModelAndView model,
                                   @PathVariable("departure") Long depId,
                                   @PathVariable("depTime") String depTime,
                                   @PathVariable("destination") Long destId,
                                   @PathVariable("arrTime") String arrTime,
                                   @PathVariable("page") Integer page) throws ParseException {
        Station departure = stationService.getStationById(depId);
        Station destination = stationService.getStationById(destId);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy h:mm:ss");
        Date depDate = format.parse(depTime);
        Date arrDate = format.parse(arrTime);
        int numOfPages = (int) Math.ceil(trainService.findTrains(departure,destination,depDate,arrDate).size() * 1.0 / NUM_OF_ROWS);
        List<Train> trains = trainService.findTrains(departure,destination,depDate,arrDate, NUM_OF_ROWS*(page-1), NUM_OF_ROWS);
        return getModelAndView(model, trains, page, departure, destination, depDate, arrDate, numOfPages);
    }


    @GetMapping(value = "/getSchedule")
    public ModelAndView scheduleRequest(ModelAndView model){
        model.addObject("stations", stationService.getAllStations());
        model.setViewName("stationSchedule");
        return model;
    }


    @GetMapping(value = "passenger/buyComplexRouteTickets")
    public ModelAndView buyComplexTickets(ModelAndView model,
                                          @RequestParam("0trainId") Long firstTrainId,
                                          @RequestParam("1trainId") Long secondTrainId,
                                          @RequestParam("0departure") Long departureId,
                                          @RequestParam("0destination") Long breakId,
                                          @RequestParam("1destination") Long destinationId){
        String message = "";
        Passenger passenger = passengerService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Station departure = stationService.getStationById(departureId),
                breakStation = stationService.getStationById(breakId),
                destination = stationService.getStationById(destinationId);
        Train firstTrain = trainService.findTrainById(firstTrainId),
                secondTrain = trainService.findTrainById(secondTrainId);
        message = ticketService.buyTicket(ticketService.reservTicket(firstTrain,departure,breakStation,passenger));
        if(message.equals("success")){
            message = ticketService.buyTicket(ticketService.reservTicket(secondTrain,breakStation,destination,passenger));
        }
        model.addObject("message", message);
        model.setViewName("purchaseResult");
        return model;
    }
    @GetMapping(value = "/findComplexRoutes")
    public ModelAndView findComplexRoutes(ModelAndView model,
                                          @RequestParam("departure") Long depId,
                                          @RequestParam("destination") Long destId,
                                          @RequestParam("departureTime") String depTime,
                                          @RequestParam("arrivalTime") String arrTime) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Date depDate = format.parse(depTime);
        Date arrDate = format.parse(arrTime);
        Station departure = stationService.getStationById(depId);
        Station destination = stationService.getStationById(destId);
        model.addObject("complexRoutes", trainService.findComplexRoute(departure,destination,depDate,arrDate));
        model.setViewName("complexRoutes");
        return model;

    }

    @GetMapping(value = {"/getSchedule/{station}/{date}/{page}", "/admin/getSchedule/{station}/{date}/{page}"})
    public ModelAndView getScheduleOnDate(ModelAndView model,
                                          @PathVariable("station") Long stationId,
                                          @PathVariable("date") String date,
                                          @PathVariable("page") int page) throws ParseException {
        Station station = stationService.getStationById(stationId);
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        int numOfPages = (int) Math.ceil(scheduleService.getCountOfSchedules(station,date1) * 1.0 / NUM_OF_ROWS);
        List<Schedule> schedules = scheduleService.findScheduleOnDate(station,date1, NUM_OF_ROWS*(page-1), NUM_OF_ROWS);
        model.addObject("schedules",schedules);
        model.addObject("numOfPages",numOfPages);
        model.addObject("page",page);
        model.setViewName("stationSchedule");
        return model;
    }
    private ModelAndView getModelAndView(ModelAndView model,List<Train> trains, Integer page, Station departure, Station destination, Date depDate, Date arrDate, int numOfPages) {
        model.addObject("trains",trains);
        model.addObject("departure", departure);
        model.addObject("destination",destination);
        model.addObject("currentTime",new Date().getTime());
        model.addObject("page",page);
        model.addObject("numOfPages",numOfPages);
        model.addObject("departureTime",depDate);
        model.addObject("arrivalTime",arrDate);
        model.setViewName("trainList");
        return model;
    }
}

