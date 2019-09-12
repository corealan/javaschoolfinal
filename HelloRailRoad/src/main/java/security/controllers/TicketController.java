package security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.service.PassengerService;
import security.service.StationService;
import security.service.TicketService;
import security.service.TrainService;
import java.util.Map;

@Controller
public class TicketController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationService stationService;

    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "/passenger/ticketPurchase/{trainId}/{departure}/{destination}", method = RequestMethod.GET)
    public ModelAndView ticketPurchaseForm(ModelAndView model,
                                           @PathVariable("trainId") Long trainId,
                                           @PathVariable("departure")Long depId,
                                           @PathVariable("destination")Long destId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Train train = trainService.findTrainById(trainId);
        Station departure = stationService.getStationById(depId),
                destination = stationService.getStationById(destId);
        ticketService.removeOldReservations();
        model.addObject("route", train.getRoute());
        model.addObject("train", train);
        model.addObject("departure", departure);
        model.addObject("destination",destination);
        model.addObject("numOfTickets", ticketService.getNumOfTicketsOnSale(trainService.findTrainById(trainId),stationService.getStationById(depId),stationService.getStationById(destId)));
        model.addObject("passenger",passengerService.findByUsername(authentication.getName()));
        Ticket ticket = ticketService.reservTicket(train,departure,destination,passengerService.findByUsername(authentication.getName()));
        if (ticket != null) {
            model.addObject("ticketId",ticket.getId());
        }
        model.setViewName("ticketPurchase");
        return model;
    }

    @PostMapping(value = "/passenger/purchaseTicket")
    public ModelAndView purchaseTicket(ModelAndView model,
                                       @RequestParam Map<String, String> params,
                                       @RequestParam Long ticketId){

        Station destination = stationService.getStationByName(params.get("destinationStation"));
        if(!params.get("subroute").contains(destination+"")){
            model.addObject("message", "wrong destination");
            model.setViewName("purchaseResult");
            return model;
        }
        model.addObject("message",ticketService.buyTicket(ticketService.getTicketById(ticketId)));
        model.setViewName("purchaseResult");
        return model;
    }

    @GetMapping(value = "/admin/passengersOnTrain")
    public ModelAndView passengersOnTrain(ModelAndView model){
        model.addObject("trains",trainService.getAllTrains());
        model.setViewName("passengersOnTrain");
        return model;
    }

    @GetMapping(value = "/admin/passengersOnTrain/{trainId}")
    public ModelAndView passengersOnTrain(@PathVariable(value = "trainId") Long trainId){
        ModelAndView model = new ModelAndView("passengersOnTrain");
        model.addObject("tickets", ticketService.getTicketsOnTrain(trainService.findTrainById(trainId)));
        return model;
    }
}
