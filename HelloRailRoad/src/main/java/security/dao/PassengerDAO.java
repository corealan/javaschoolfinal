package security.dao;


import security.model.security.Passenger;
/**
 * DAO-interface to provide operations with {@link Passenger} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface PassengerDAO {

    Passenger savePassenger(Passenger passenger);
    Passenger findByUsername(String username);
}
