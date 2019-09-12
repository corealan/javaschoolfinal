package security.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import security.dao.TrainDAO;
import security.model.Schedule;
import security.model.Station;
import security.model.Train;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class TrainDAOImpl implements TrainDAO {
    @Autowired
    private SessionFactory sessionFactory;

    public void saveTrain(Train train) {
        if (train.getId() == null) {
            sessionFactory.getCurrentSession().persist(train);
        } else sessionFactory.getCurrentSession().merge(train);
    }

    public Train findTrainById(Long id) {
        Query<Train> query = sessionFactory.getCurrentSession().createQuery("FROM Train t where t.id = :id",Train.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public void removeTrain(Train train) {
        sessionFactory.getCurrentSession().remove(train);
    }

    public List<Train> getAllTrains() {
        Query<Train> query = sessionFactory.getCurrentSession().createQuery("FROM Train t", Train.class);
        return query.list();
    }

    public List<Train> getTrainsBetweenStations(Station a, Station b, Date fromm, Date to) {
        Query<Schedule> query = sessionFactory.getCurrentSession().createQuery("FROM Schedule s where s.station.id = :aId and " +
                "s.departureTime > :aDeparture",Schedule.class);
        query.setParameter("aId", a.getId());
        query.setParameter("aDeparture", fromm);

        Query<Schedule> query1 = sessionFactory.getCurrentSession().createQuery("FROM Schedule s where s.station.id = :bId and " +
                "s.arrivalTime < :bArrival",Schedule.class);
        query1.setParameter("bId", b.getId());
        query1.setParameter("bArrival", to);

        List<Schedule> schedules1 = query.getResultList();
        List<Schedule> schedules2 = query1.getResultList();
        List<Train> resultTrainList = new ArrayList<Train>();

        for(Schedule s1 : schedules1){
         for(Schedule s2 : schedules2){
             if(s1.getTrain().equals(s2.getTrain())){
                 resultTrainList.add(s1.getTrain());
             }
         }
        }
        return resultTrainList;
    }

    public List<Train> findTrainByNumber(Integer number) {
        Query<Train> query = sessionFactory.getCurrentSession().createQuery("FROM Train t where t.trainNumber = :number",Train.class);
        query.setParameter("number", number);
        return query.getResultList();
    }

    public List<Train> depFromStation(Station station, Date date) {
        Query<Train> query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s where s.station=:station AND (s.departureTime > :date AND s.departureTime < :date2) ORDER BY s.departureTime",Train.class);

        query.setParameter("station",station);
        query.setParameter("date", date);
        query.setParameter("date2", getNextDay(date));

        return query.getResultList();
    }

    public List<Train> filterTrains(Station departure, Station destination, Date date, int start, int max) {
        Query<Train> query = null;
        if(destination == null){
            query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s where s.station=:station AND (s.departureTime > :date AND s.departureTime < :date2) ORDER BY s.departureTime",Train.class);
            query.setParameter("station",departure);
        }
        if(departure == null){
            query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s where s.station=:station AND (s.arrivalTime > :date AND s.arrivalTime < :date2) ORDER BY s.arrivalTime",Train.class);
            query.setParameter("station",destination);
        }
        if(departure == null && destination == null){
            query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s WHERE (s.arrivalTime > :date AND s.arrivalTime < :date2) OR(s.departureTime > :date AND s.departureTime < :date2)  ORDER BY s.arrivalTime,s.departureTime",Train.class);
        }
        if (departure != null && destination != null)
        {
            query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s where (s.station=:departure AND (s.departureTime > :date AND s.departureTime < :date2)) OR  (s.station =:destination  AND (s.arrivalTime > :date AND s.arrivalTime < :date2)) ORDER BY s.departureTime, s.arrivalTime",Train.class);
            query.setParameter("departure",departure);
            query.setParameter("destination",destination);
        }
        query.setParameter("date", date);
        query.setParameter("date2", getNextDay(date));
        query.setFirstResult(start);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public List<Train> findTrainsByStation(Station station, int first, int max) {
        Query<Train> query = sessionFactory.getCurrentSession().createQuery("SELECT s.train from Schedule s where s.station =:station ORDER BY CASE WHEN s.departureTime IS NULL THEN s.arrivalTime WHEN s.arrivalTime IS NULL THEN s.departureTime ELSE s.departureTime END DESC ", Train.class);
        query.setParameter("station", station);
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public Long getCountOfTrains(Station station) {
        Query countQuery = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(s.train) FROM Schedule s WHERE s.station=:station");
        countQuery.setParameter("station", station);
        return (Long)countQuery.uniqueResult();
    }

    public List<Train> findTrainsByStationBetweenDates(Station station, Date timeFrom, Date timeTo) {
        Query query = sessionFactory.getCurrentSession().createQuery("SELECT s.train FROM Schedule s WHERE s.station =:station AND s.departureTime > :timeFrom AND s.departureTime < :timeTo");
        query.setParameter("station", station);
        query.setParameter("timeFrom", timeFrom);
        query.setParameter("timeTo", timeTo);
        return query.getResultList();
    }

    private Date getNextDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,+1);
        return calendar.getTime();
    }

}
