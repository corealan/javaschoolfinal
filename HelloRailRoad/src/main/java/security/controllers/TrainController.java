package security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.model.Station;
import security.model.Train;
import security.service.StationService;
import security.service.TrainService;
import java.util.*;

@Controller
public class TrainController {

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationService stationService;

    @PostMapping(value = "/admin/findTrains")
    public ModelAndView findTrains(ModelAndView model,
                                   @RequestParam("station") String stationName){
        Station station = stationService.getStationByName(stationName);
        if(station==null){
            model.addObject("message","Название станции указано неверно!");
            model.setViewName("admin");
            return model;
        }
        int numOfPages = (int)Math.ceil(trainService.getCountOfTrains(station)*1.0/10);
        model.addObject("numOfPages",numOfPages);
        model.addObject("trains",trainService.findTrainsByStation(station,0,10));
        model.addObject("page",1);
        model.addObject("numOfPages",numOfPages);
        model.addObject("station",stationName);
        model.setViewName("trainList");
        return model;
    }

    @GetMapping(value = "/admin/findTrains/{station}/{page}/{numOfPages}")
    public ModelAndView findTrains(ModelAndView model,
                                   @PathVariable("station") String stationName,
                                   @PathVariable("page") int page,
                                   @PathVariable("numOfPages") int numOfPages){
        int numOfRows = 10;
        Station station = stationService.getStationByName(stationName);
        model.addObject("numOfPages",numOfPages);
        model.addObject("page",page);
        model.addObject("trains",trainService.findTrainsByStation(station,numOfRows*(page-1), numOfRows));
        model.setViewName("trainList");
        return model;
    }

    @PostMapping(value = "/admin/setTrainRoute")
    public ModelAndView setTrainRoute(ModelAndView model,
                                      @RequestParam Map<String, String> params){
        String trainNumber = params.get("trainNumber");
        String message = trainService.addNewTrain(params);
        if(message != null){
            model.addObject("message", message);
            model.addObject("route", stationService.getStationsListFromString(params.get("route")));
            model.setViewName("routeStationSelect");
            return model;
        }
        model.addObject("newTrainMessage","Поезд №" + trainNumber + " добавлен в график!");
        model.setViewName("admin");
        return model;
    }

    @GetMapping(value = "cancelTrain/{trainId}")
    public ModelAndView cancelTrain(ModelAndView model,
                                    @PathVariable(value = "trainId") Long trainId){
        Train train = trainService.findTrainById(trainId);
        trainService.removeTrain(train);
        model.setViewName("admin");
        model.addObject("message","Поезд №" + train.getTrainNumber() + " отменен!");
        return model;
    }


    @GetMapping(value = "/routeInfo/{trainId}")
    public ModelAndView getRouteInfo(ModelAndView model,
                                     @PathVariable(value = "trainId")Long trainId){
        Train train = trainService.findTrainById(trainId);
        List<Station> fullRoute = trainService.getFullRoute(train);
        model.addObject("fullRoute", fullRoute);
        model.addObject("route", train.getRoute());
        model.addObject("train",train);
        model.setViewName("routeInfo");
        return model;
    }
}
