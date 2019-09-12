package security.service;

import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.model.security.Passenger;
import java.util.List;

/**
 * Service class contains business logic concerning {@link Ticket} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface TicketService {
    void saveTicket(Ticket ticket);
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    List<Ticket> getTicketOnRoute(Train train,Station departure, Station destination);
    String checkPurchaseConditions(Train train, Station departure, Station destination, Passenger passenger);
    int getNumOfTicketsOnSale(Train t, Station departure, Station destination);
    List<Ticket> getTicketsOnTrain(Train train);
    String buyTicket(Ticket ticket);
    Ticket reservTicket(Train train, Station departure, Station destination, Passenger passenger);
    void removeOldReservations();
}
