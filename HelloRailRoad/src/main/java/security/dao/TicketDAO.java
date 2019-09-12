package security.dao;

import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.model.security.Passenger;
import java.util.List;

/**
 * DAO-interface to provide operations with {@link Station} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface TicketDAO {
    void saveTicket(Ticket ticket);
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    List<Ticket> getTicketOnRoute(Train t,Station departure, Station destination);
    List<Ticket> getTicketsOnTrain(Train train);
    void removeTicket(Ticket ticket);
    Ticket findTicketByPassengerAndTrain(Passenger passenger, Train train);
}
