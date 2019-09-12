import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import security.dao.TicketDAO;
import security.model.Schedule;
import security.model.Station;
import security.model.Ticket;
import security.model.Train;
import security.model.security.Passenger;
import security.service.PassengerService;

import security.service.impl.TicketServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketDAO ticketDAOMock;

    @Mock
    private PassengerService passengerServiceMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllTickets() {
        List<Ticket> result = new ArrayList<Ticket>();
        Mockito.when(ticketDAOMock.getAllTickets()).thenReturn(result);
        List<Ticket> allTickets = ticketService.getAllTickets();
        assertEquals(result, allTickets);
    }

    @Test
    void getTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId(23L);
        Mockito.when(ticketDAOMock.getTicketById(23L)).thenReturn(ticket);
        Ticket result = ticketService.getTicketById(23L);
        assertEquals(result.getId(), ticket.getId());
    }

    @Test
    void getTicketOnRoute() {
        List<Ticket> ticketsOnRoute = new ArrayList<Ticket>();
        Train train = new Train();
        Station departure = new Station();
        Station destination = new Station();

        Mockito.when(ticketDAOMock.getTicketOnRoute(train,departure,destination)).thenReturn(ticketsOnRoute);
        assertEquals(ticketService.getTicketOnRoute(train,departure,destination),ticketsOnRoute);

    }

    @Test
    void checkPurchaseConditions() throws ParseException {
        Train train = new Train();
        train.setId(1l);
        train.setTrainNumber(22);
        train.setNumberOfSeats(10);

        Station departure = new Station();
        Station destination = new Station();

        departure.setId(1l);
        destination.setId(2l);

        departure.setName("A");
        destination.setName("C");

        Date departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-10-02 23:50");
        Date arrivalTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-10-03 03:50");

        Passenger passenger = new Passenger();
        passenger.setFirstName("First");
        passenger.setLastName("Last");
        passenger.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-10-23"));

        Schedule scheduleDep = new Schedule();
        scheduleDep.setTrain(train);
        scheduleDep.setStation(departure);
        scheduleDep.setDepartureTime(departureTime);

        train.addSchedule(scheduleDep);
        List<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(scheduleDep);
        departure.setSchedules(schedules);

        Schedule scheduleDest = new Schedule();
        scheduleDest.setTrain(train);
        scheduleDest.setStation(destination);
        scheduleDest.setArrivalTime(arrivalTime);

        List<Station> route = new ArrayList<Station>();
        route.add(departure);
        route.add(destination);
        train.setRoute(route);
        train.setFullRoute(route);

        Ticket ticket = new Ticket();
        ticket.setId(1l);
        ticket.setDeparture(departure);
        ticket.setDestination(destination);
        ticket.setDepartureTime(departureTime);
        ticket.setArrivalTime(arrivalTime);
        ticket.setTrain(train);
        ticket.setPassenger(passenger);

        List<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(ticket);
        passenger.setTickets(tickets);

        Mockito.when(ticketDAOMock.getTicketsOnTrain(train)).thenReturn(tickets);

        assertEquals(ticketService.checkPurchaseConditions(train,departure,destination,passenger),"duplicate");
    }


    @Test
    void getTicketsOnTrain() {
        Train train = new Train();
        List<Ticket> tickets = new ArrayList<Ticket>();
        Mockito.when(ticketDAOMock.getTicketsOnTrain(train)).thenReturn(tickets);
        assertEquals(tickets, ticketService.getTicketsOnTrain(train));
    }

}