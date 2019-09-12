package security.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import security.model.Station;
import security.service.PassengerService;
import security.service.StationService;
import java.text.ParseException;
import java.util.List;

@Controller
public class AppController {

    private static final String LOGIN_PAGE = "loginPage";
    private static final String REGISTRATION = "registration";
    private static final String ADMIN_PAGE = "admin";
    private static final String PASSENGER_PAGE = "passenger";

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private StationService stationService;
    private static final Logger log = Logger.getLogger(AppController.class);


    @GetMapping(value = "/admin")
    public ModelAndView admin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Station> stations = stationService.getAllStations();
        return new ModelAndView("admin", "stations", stations);
    }


    @GetMapping(value = "/error")
    public String error(ModelMap model) {
        model.addAttribute("error", "true");
        return LOGIN_PAGE;
    }

    @GetMapping(value = "/logout")
    public String logout(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
        model.addAttribute("logout", "true");
        return LOGIN_PAGE;
    }

    @PostMapping(value = "/registration")
    public String registration(Model model,
                               @RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "passwordConfirm") String passwordConfirm,
                               @RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName") String lastName,
                               @RequestParam(name = "DOB") String dateOfBirth) {
        if(!password.equals(passwordConfirm)){
            model.addAttribute("message", "Пароли не совпали!");
            return REGISTRATION;
        }
        if(passengerService.findByUsername(username)!=null){
            model.addAttribute("message", "Пользователь с таким логином уже зарегистрирован!");
            return REGISTRATION;
        }
        try {
            passengerService.passengerRegistration(username,password,firstName,lastName,dateOfBirth);
        } catch (ParseException e) {
            log.info("ParseException: " + e);
        }
        return LOGIN_PAGE;
    }

    @GetMapping(value = "/login")
    public String login() {
        return LOGIN_PAGE;
    }

    @GetMapping(value = "/")
    public ModelAndView loginPage(ModelAndView model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Station> stations = stationService.getAllStations();
        model.addObject("stations", stations);
        if(authentication.getAuthorities().toString().contains("ROLE_ADMIN")) {
            model.setViewName(ADMIN_PAGE);
            log.info("Logged as administrator");
            return model;
        }
        model.setViewName(PASSENGER_PAGE);
        return model;
    }

    @GetMapping("/registration")
    public String registration() {
        return REGISTRATION;
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){
        log.error("Access denied!");
        return "accessDenied";
    }
}