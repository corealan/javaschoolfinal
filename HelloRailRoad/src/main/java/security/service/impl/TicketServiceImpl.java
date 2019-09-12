package security.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.TicketDAO;
import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.model.security.Passenger;
import security.service.TicketService;
import security.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {


    private static final String TOO_LATE = "late";
    private static final String NOT_ENOUGH_TICKETS = "Нет доступных для покупки билетов.";
    private static final String ONE_PASSENGER_ONE_TICKET = "duplicate";
    private static final String SUCCESS = "success";

    @Autowired
    private TicketDAO ticketDAO;

    private static final Logger log = Logger.getLogger(TicketServiceImpl.class);


    public void saveTicket(Ticket ticket) {
        ticketDAO.saveTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public Ticket getTicketById(Long id) {
        return ticketDAO.getTicketById(id);
    }

    public List<Ticket> getTicketOnRoute(Train train, Station departure, Station destination) {
        return ticketDAO.getTicketOnRoute(train,departure,destination);
    }

    public int getNumOfTicketsOnSale(Train train, Station departure, Station destination) {
        List<List<Station>> subroutes = Util.getSubRoutes(train.getRoute());
        int result = train.getNumberOfSeats();

        List<List<Station>> removeList = new ArrayList<List<Station>>();
        for (List<Station> subroute : subroutes) {
            if (subroute.get(0).equals(destination) || subroute.get(subroute.size() - 1).equals(departure) || (!subroute.contains(departure) && !subroute.contains(destination) && !train.getRoute().containsAll(subroute))) {
                removeList.add(subroute);
            }
        }
        subroutes.removeAll(removeList);
        for (List<Station> subroute : subroutes) {
            if(getTicketOnRoute(train,subroute.get(0), subroute.get(subroute.size()-1)).size() != 0)
            result -= getTicketOnRoute(train,subroute.get(0), subroute.get(subroute.size()-1)).size();
        }
        log.info("Train №" + train.getTrainNumber() + "Number of tickets on route " + departure.getName() + "--->" + destination.getName() + ": " + result);
        return result;
    }

    public String checkPurchaseConditions(Train train, Station departure, Station destination, Passenger passenger){
        if(getNumOfTicketsOnSale(train, departure, destination) <= 0){
            log.error("Train №" + train.getTrainNumber() + ": Not enough tickets on route " + departure.getName() + "-->" + destination.getName());
            return NOT_ENOUGH_TICKETS;
        }
        if(train.getSchedules().get(0).getDepartureTime().getTime() - new Date().getTime() < 600000){
            log.error("Train №" + train.getTrainNumber() + ": attempting to purchase ticket when < 10 mins before departure ");
            return TOO_LATE;
        }

        for(Ticket ticket : getTicketsOnTrain(train)){
            if(ticket.getPassenger().equals(passenger) && ticket.getTrain().equals(train)){
                log.error("Train №" + train.getTrainNumber() + ": passenger " + passenger.getFirstName() + " " + passenger.getLastName() + " attempting to purchase 2nd ticket");
                return ONE_PASSENGER_ONE_TICKET;
            }
        }

        return SUCCESS;
    }

    public List<Ticket> getTicketsOnTrain(Train train) {
        List<Ticket> result = new ArrayList<Ticket>();
        for(Ticket ticket : ticketDAO.getTicketsOnTrain(train)){
            if(ticket.getReservationTime() == null){
                result.add(ticket);
            }
        }
        return result;
    }

    public String buyTicket(Ticket ticket) {
        String conditions = checkPurchaseConditions(ticket.getTrain(),ticket.getDeparture(),ticket.getDestination(), ticket.getPassenger());
        if(ticket.getReservationTime() != null){
            ticket.setReservationTime(null);
            saveTicket(ticket);
            log.info("Passenger " + ticket.getPassenger().getFirstName() + " " + ticket.getPassenger().getLastName() + " successfully purchased ticket." +
                    "Train № " + ticket.getTrain().getTrainNumber() + " from " + ticket.getDeparture().getName() + " to " + ticket.getDestination().getName());
        }
        return conditions;
    }

    public Ticket reservTicket(Train train, Station departure, Station destination, Passenger passenger){
        Ticket ticket = null;

        Date depTime = train.getSchedules().get(0).getDepartureTime();
        Date arrTime = train.getSchedules().get(train.getSchedules().size()-1).getArrivalTime();
        String purchaseConditions = checkPurchaseConditions(train,departure,destination,passenger);

        if(purchaseConditions.equals(SUCCESS)){
            ticket = new Ticket(train,departure,destination,passenger,depTime,arrTime);
            ticket.setReservationTime(new Date());
            saveTicket(ticket);
        } else if(checkPurchaseConditions(train,departure,destination,passenger).equals(ONE_PASSENGER_ONE_TICKET)){
            ticket = ticketDAO.findTicketByPassengerAndTrain(passenger,train);
        }
        return ticket;
    }

    public void removeOldReservations() {
        for(Ticket ticket : ticketDAO.getAllTickets()){
            Date reservationTime = ticket.getReservationTime();
            if(reservationTime != null && new Date().getTime()- reservationTime.getTime() > 300000){
                ticketDAO.removeTicket(ticket);
            }
        }
    }
}
