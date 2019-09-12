package security.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.PassengerDAO;
import security.dao.RoleDAO;
import security.model.security.Passenger;
import security.model.security.Role;
import security.service.PassengerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    PassengerDAO passengerDAO;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    RoleDAO roleDAO;

    private static final Logger log = Logger.getLogger(PassengerServiceImpl.class);

    @Transactional
    public Passenger savePassenger(Passenger passenger) {
        return passengerDAO.savePassenger(passenger);
    }

    @Transactional
    public Passenger findByUsername(String username) {
        return passengerDAO.findByUsername(username);
    }

    @Transactional
    public void passengerRegistration(String username, String password, String firstName, String lastName, String dateOfBirth) throws ParseException {
        Passenger passenger = new Passenger();
        passenger.setUsername(username);
        passenger.setPassword(encoder.encode(password));
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleDAO.getRoleById(2L));
        passenger.setRoles(roles);
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth));
        savePassenger(passenger);
        log.info("New passenger + " + username + " registered!");
    }
}
