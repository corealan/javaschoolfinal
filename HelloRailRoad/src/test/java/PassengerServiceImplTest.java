import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import security.dao.PassengerDAO;
import security.model.security.Passenger;
import security.service.impl.PassengerServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class PassengerServiceImplTest {

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private PassengerDAO passengerDAOMock;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void savePassenger() {
        Passenger passenger = new Passenger();
        Mockito.when(passengerDAOMock.savePassenger(passenger)).thenReturn(passenger);
        assertEquals(passengerService.savePassenger(passenger),passenger);
    }

    @Test
    void findByUsername() throws ParseException {
        Passenger passenger = new Passenger();
        passenger.setUsername("username");
        passenger.setFirstName("Вася");
        passenger.setLastName("Пупкин");
        passenger.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-10-23"));
        Mockito.when(passengerDAOMock.findByUsername("username")).thenReturn(passenger);
        assertEquals(passengerService.findByUsername("username").getUsername(), "username");
    }
}