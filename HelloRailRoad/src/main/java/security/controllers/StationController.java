package security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.model.Station;
import security.model.Train;
import security.service.StationService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class StationController {

    @Autowired
    StationService stationService;

    @PostMapping(value = "/addNewStation")
    public ModelAndView addNewStation(ModelAndView model, @RequestParam Map<String, String> paramMap){
        String message = "";
        boolean error = false;
        if(paramMap.get("stationName").equals("")){
            message = "Заполнены не все обязательные поля!";
            error = true;
        }
        if(stationService.getStationByName(paramMap.get("stationName")) != null){
            message = "Станция с таким названием уже зарегистрирована!";
            error = true;
        }
        if(stationService.getStationByName(paramMap.get("adjacentStation")) == null && !paramMap.get("adjacentStation").equals("")){
            message = "Такая станция примыкания не зарегистрирована!";
            error = true;
        }
        if(!error){
            stationService.addNewStation(paramMap.get("stationName"), paramMap.get("adjacentStation"), paramMap.get("adjacentStation2"));
            message = "Станция добавлена!";
        }
        model.addObject("message", message);
        model.addObject("error",error);
        model.setViewName("addNewStation");
        return model;
    }

    @GetMapping(value = "/addNewStation")
    public ModelAndView addNewStation(ModelAndView model){
        List<Station> stations = stationService.getAllStations();
        model.addObject("stations", stations);
        model.setViewName("addNewStation");
        return model;
    }

    @GetMapping(value = "/routesRequest")
    public ModelAndView routesRequest(ModelAndView model){
        model.addObject("stations", stationService.getAllStations());
        model.setViewName("routesRequest");
        return model;
    }

    @PostMapping(value = "/getRoutes")
    public ModelAndView getRoutes(ModelAndView model, @RequestParam Map<String, String> paramMap){
        String message = "";
        if(stationService.getStationByName(paramMap.get("departure")) == null || stationService.getStationByName(paramMap.get("destination")) == null){
            message = "Вы ввели некорректные данные!";
            model.addObject("message", message);
            model.setViewName("routesRequest");
            return model;
        }
        model.addObject("routes", stationService.getRoutes(paramMap.get("departure"), paramMap.get("destination")));
        model.setViewName("posibleRoutes");
        return model;
    }

    @PostMapping(value = "/routeStationSelect")
    public ModelAndView routeStationSelect(ModelAndView model, @RequestParam Map<String,String> params){
        model.addObject("route",stationService.getStationsListFromString(params.get("route")));
        model.setViewName("routeStationSelect");
        return model;
    }
}
