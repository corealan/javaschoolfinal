package security.service;
import security.model.security.Passenger;
import java.text.ParseException;

/**
 * Service class contains business logic concerning {@link Passenger} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface PassengerService {
    Passenger savePassenger(Passenger passenger);
    Passenger findByUsername(String username);
    void passengerRegistration(String username, String password, String firstName, String lastName, String dateOfBirth) throws ParseException;
}
