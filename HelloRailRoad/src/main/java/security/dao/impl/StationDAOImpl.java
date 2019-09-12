package security.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import security.dao.StationDAO;
import security.model.Station;
import java.util.List;


@Repository
public class StationDAOImpl implements StationDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<Station> getAllStations() {
        Query<Station> query = sessionFactory.getCurrentSession().createQuery("FROM Station s", Station.class);
        return query.getResultList();
    }

    public void saveStation(Station station) {
        if(station.getId() == null){
            sessionFactory.getCurrentSession().persist(station);
        } else sessionFactory.getCurrentSession().merge(station);
    }

    public Station getStationById(long id) {
        Query<Station> query = sessionFactory.getCurrentSession().createQuery("FROM Station s where s.id=:id", Station.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    public Station getStationByName(String name) {
        Query<Station> query = sessionFactory.getCurrentSession().createQuery("FROM Station s where s.name=:name", Station.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

}
