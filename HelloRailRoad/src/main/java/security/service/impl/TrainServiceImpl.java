package security.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.ScheduleDAO;
import security.dao.TrainDAO;
import security.messaging.MessageSender;
import security.model.Schedule;
import security.model.Station;
import security.model.Train;
import security.service.StationService;
import security.service.TicketService;
import security.service.TrainService;
import security.util.TrainValidator;
import security.util.Util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TrainServiceImpl implements TrainService {

    @Autowired
    private StationService stationService;
    @Autowired
    private TrainDAO trainDAO;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ScheduleDAO scheduleDAO;
    @Autowired
    private TicketService ticketService;


    private static final Logger log = Logger.getLogger(TrainServiceImpl.class);

    public String addNewTrain(Map<String, String> params) {

        Train train = new Train();
        if(params.get("trainNumber").equals("")){
            log.error("Train number didn't set");
            return "Не задан номер поезда!";
        }
        if(params.get("trainNumber").length() > 4) {
            log.error("Incorrect train number length");
            return "Номер поезда от 1 до 4 цифр!";
        }
        if(params.get("trainNumber").matches("\\D+")){
            log.error("Incorrect symbols in train number");
            return "Номер поезда должен содержать только цифры!";
        }
        if(params.get("numberOfSeats").equals("")){
            log.error("Number of seats not set");
            return "Не указано число мест в поезде!";
        }
        if(Integer.parseInt(params.get("numberOfSeats")) > 100 || Integer.parseInt(params.get("numberOfSeats")) < 10){
            log.error("Incorrect number of seats");
            return "Число мест в поезде от 10 до 100!";
        }
        train.setTrainNumber(Integer.parseInt(params.get("trainNumber")));
        train.setNumberOfSeats(Integer.parseInt(params.get("numberOfSeats")));
        List<Station> stops = new ArrayList<Station>();
        params.remove("trainNumber");
        params.remove("numberOfSeats");

        String[] ids = params.get("route").replaceAll("\\D+"," ").trim().split(" ");
        ArrayList<Station> fullRoute = new ArrayList<Station>();

        for(String id : ids){
            fullRoute.add(stationService.getStationById(Long.parseLong(id)));
        }

        train.setFullRoute(fullRoute);

        for(String s : params.keySet()){
            if(!s.equals("route") && s.matches("\\d+stop")) {
                Long id = Long.parseLong(s.replaceAll("\\D+",""));
                Station temp = stationService.getStationById(id);
                if (!stops.contains(temp)) {
                    stops.add(temp);
                }
            }
        }
        train.setRoute(stops);

        for (Station station : stops){
            Schedule schedule = new Schedule();
            schedule.setStation(station);
            schedule.setTrain(train);

            Date arrive = null, departure = null;
            for(String s : params.keySet()){
                if(s.equals(station.getId() + "arrive")){

                    if(!params.get(s).equals("")){
                        arrive = getDate(params.get(s));
                    }
                }
                if(s.equals(station.getId() + "departure")){
                    if(!params.get(s).equals("")){
                        departure = getDate(params.get(s));
                    }
                }
            }

            schedule.setArrivalTime(arrive);
            schedule.setDepartureTime(departure);
            train.addSchedule(schedule);
        }


        if(TrainValidator.trainScheduleValidate(train)) {
            String message = "";
            for(Station s : train.getRoute()){
                message += s.getName() + "#";
            }
            try{
                messageSender.sendMessage(message);
            } catch (UncategorizedJmsException exception){
                log.error("Exception: " + exception);
            }

            trainDAO.saveTrain(train);
            log.info("Train №" + train.getTrainNumber() + " successfully added");
        }
        return TrainValidator.getMessage();
    }



    public void saveTrain(Train train) {
        trainDAO.saveTrain(train);
    }

    public void removeTrain(Train train) {
        trainDAO.removeTrain(train);
        String message ="";
        for(Station s : train.getRoute()){
            message += s.getName() + "#";
        }
        try{
            messageSender.sendMessage(message);
        } catch (UncategorizedJmsException exception){
            log.error("Exception: " + exception);
        }
        log.info("Cancel Message send: " + message);

    }

    public List<Station> getFullRoute(Train train) {
        Train train1 = findTrainById(train.getId());

        LinkedList<Station> stopRoute = new LinkedList<Station>(train.getRoute());
        List<LinkedList<Station>> routes = stationService.getRoutes(stopRoute.getFirst().getName(), stopRoute.getLast().getName());

        for(LinkedList<Station> route : routes){
            if (route.containsAll(train1.getFullRoute()) && route.size() == train1.getFullRoute().size()){
                return route;
            }
        }
        return null;
    }

    public void updateFullRoute(Train train, Station newStation) {
        Train train1 = findTrainById(train.getId());
        LinkedList<Station> stopRoute = new LinkedList<Station>(train.getRoute());
        List<LinkedList<Station>> routes = stationService.getRoutes(stopRoute.getFirst().getName(), stopRoute.getLast().getName());
        for(LinkedList<Station> route : routes){
            if (route.containsAll(train1.getFullRoute()) && route.size() == train1.getFullRoute().size() + 1 && route.contains(newStation)){
                train1.setFullRoute(route);
                saveTrain(train1);
                log.info("Train №" + train1.getTrainNumber() + "Route update: station " + newStation.getName() +" added to full route" );
            }
        }
    }

    public List<Train> findTrains(Station departure, Station arrival, Date from, Date to) {
        return trainDAO.getTrainsBetweenStations(departure,arrival,from,to);
    }

    public List<Train> findTrains(Station departure, Station arrival, Date from, Date to, int start, int max) {
        List<Train> allTrains = trainDAO.getTrainsBetweenStations(departure,arrival,from,to);
        Collections.sort(allTrains);
        if(allTrains.size() < start + max){
            return allTrains.subList(start,allTrains.size());
        }
        return allTrains.subList(start,start+max);
    }

    public List<Train> getAllTrains() {
        return trainDAO.getAllTrains();
    }

    public Train findTrainById(Long id) {
        return trainDAO.findTrainById(id);
    }

    public List<Train> findTrainsBetweenStations(Station departure, Station destination, Date date) {
        List<Train> trains = new ArrayList<Train>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String depDate = "";
        if(date != null) {
            depDate = dateFormat.format(date);
        }
        for(Schedule schedule : departure.getSchedules()){
            boolean destinationAfterDeparture = schedule.getTrain().getRoute().indexOf(destination) > schedule.getTrain().getRoute().indexOf(departure);
            if(schedule.getTrain().getRoute().contains(destination) && destinationAfterDeparture && depDate.equals("")){
                trains.add(schedule.getTrain());
            }
            if(schedule.getDepartureTime() != null && !depDate.equals("") && dateFormat.format(schedule.getDepartureTime()).equals(depDate) && schedule.getTrain().getRoute().contains(destination)) {
                trains.add(schedule.getTrain());
            }
        }
        log.info("Number of trains between stations " + departure.getName() + " and " + destination.getName() + ": " + trains.size());
        return trains;
    }

    public List<Train> findTrainsByStation(Station station, int first, int max) {
        return trainDAO.findTrainsByStation(station, first,max);
    }

    public Long getCountOfTrains(Station station) {
        return trainDAO.getCountOfTrains(station);
    }

    public List<Map<Train, List<Station>>> findComplexRoute(Station departure, Station destination, Date from, Date to) {
        Map<Train, List<Station>> result = new LinkedHashMap<Train, List<Station>>();
        List<LinkedList<Station>> routes = stationService.getRoutes(departure.getName(), destination.getName());
        List<Train> trainsFromDepartureOnRoute = new ArrayList<Train>();
        Long start = new Date().getTime();
        for(List<Station> route : routes){
            trainsFromDepartureOnRoute.addAll(findTrainsOnRoute(route,from,to));
            }
        start = new Date().getTime();
        List<Map<Train, List<Station>>> cRoutes = new ArrayList<Map<Train, List<Station>>>();
        List<Train> checkedTrains = new ArrayList<Train>();
        for(Train train : trainsFromDepartureOnRoute){
            if (!checkedTrains.contains(train)) {
            for(Station station : train.getRoute()) {
                    checkedTrains.add(train);
                    List<Train> trains = trainDAO.findTrainsByStationBetweenDates(station, from, to);

                    for (Train train1 : trains) {
                        if (train1.getRoute().contains(destination)) {
                            List<Station> firstRoute = new ArrayList<Station>();
                            List<Station> secondRoute = new ArrayList<Station>();
                            Date firstArrive = scheduleDAO.findTrainScheduleByStation(train, station).getArrivalTime();
                            Date secondDeparture = scheduleDAO.findTrainScheduleByStation(train1, station).getDepartureTime();
                            if (firstArrive != null && secondDeparture != null) {
                                Long arrDepDiff = Util.getDateDiff(firstArrive, secondDeparture, TimeUnit.MINUTES);
                                if (!departure.equals(station) && !destination.equals(station) && (arrDepDiff >= 5 && arrDepDiff < 180) && !train.equals(train1)) {
                                    firstRoute.add(departure);
                                    firstRoute.add(station);
                                    secondRoute.add(station);
                                    secondRoute.add(destination);
                                    result.put(train, firstRoute);

                                    result.put(train1, secondRoute);
                                    if(ticketService.getNumOfTicketsOnSale(train,departure,station) != 0 && ticketService.getNumOfTicketsOnSale(train1,station,destination) !=0) {
                                        cRoutes.add(new LinkedHashMap<Train, List<Station>>(result));
                                    }
                                    result.clear();
                                }
                            }
                        }
                    }
                }
            }
        }
        return cRoutes;
    }


    private List<Train> findTrainsOnRoute(List<Station> route, Date from, Date to) {
        Long count = getCountOfTrains(route.get(0));
        List<Train> trains = findTrainsByStation(route.get(0),0, (int)(count*1.0));
        List<Train> result = new ArrayList<Train>();
        for (Train train : trains){
            List<Station> temp = new ArrayList<Station>();
            temp.addAll(route);
            temp.retainAll(train.getRoute());
            if (route.size() > 1 && train.getSchedules().get(0).getDepartureTime().after(from) && train.getSchedules().get(0).getDepartureTime().before(to) ){
                result.add(train);
            }
        }
        return result;
    }


    private Date getDate(String s){
        s = s.replace("T"," ");
        Date date = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            log.info("ParseException: " + e);
        }
        return date;
    }
}
