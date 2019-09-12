package security.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.PassengerDAO;
import security.model.security.Passenger;

@Service
public class UserDAODetailsService implements UserDetailsService {

    @Autowired
    private PassengerDAO passengerDAO;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Passenger passenger = passengerDAO.findByUsername(username);
        if (passenger != null){
            return passenger;
        }
        throw new UsernameNotFoundException("Passenger " + username + " not found");
    }
}
