package security.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import security.dao.PassengerDAO;
import security.model.security.Passenger;

@Repository
public class PassengerDAOImpl implements PassengerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Passenger savePassenger(Passenger passenger) {
        sessionFactory.getCurrentSession().persist(passenger);
        return passenger;
    }

    public Passenger findByUsername(String username){
        Query<Passenger> query = sessionFactory.getCurrentSession().createQuery("FROM Passenger u where u.username=:username", Passenger.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

}
