package security.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import security.dao.ScheduleDAO;
import security.model.Schedule;
import security.model.Station;
import security.model.Train;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Repository
public class ScheduleDAOImpl implements ScheduleDAO {

    @Autowired
    private SessionFactory sessionFactory;
    public void saveSchedule(Schedule schedule) {
        if(schedule.getId() == null){
            sessionFactory.getCurrentSession().persist(schedule);
        } else sessionFactory.getCurrentSession().merge(schedule);
    }

    public Schedule findScheduleById(Long id) {
        Query<Schedule> query = sessionFactory.getCurrentSession().createQuery("FROM Schedule s where s.id=:id", Schedule.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    public List<Schedule> findStationSchedule(Station station) {
        Query<Schedule> query = sessionFactory.getCurrentSession().createQuery("FROM Schedule s where s.station=:station ORDER BY " +
                "CASE " +
                "WHEN s.departureTime IS NULL THEN s.arrivalTime WHEN s.arrivalTime IS NULL THEN s.departureTime ELSE s.arrivalTime END",Schedule.class);
        query.setParameter("station", station);
        return query.getResultList();
    }

    public List<Schedule> findScheduleOnDate(Station station, Date date, int first, int max) {
        Query<Schedule> query = sessionFactory.getCurrentSession().createQuery("FROM Schedule s where s.station=:station AND " +
                "((s.departureTime > :date AND s.departureTime < :date2 ) OR" +
                "(s.arrivalTime > :date AND s.arrivalTime < :date2)) ORDER BY " +
                "CASE " +
                "WHEN s.departureTime IS NULL THEN s.arrivalTime WHEN s.arrivalTime IS NULL THEN s.departureTime ELSE s.arrivalTime END ",Schedule.class);
        query.setParameter("date", date);
        query.setParameter("station", station);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        query.setParameter("date2", calendar.getTime());
        query.setFirstResult(first);
        query.setMaxResults(max);
        return query.getResultList();
    }

    public Long getCountOfSchedules(Station station, Date date) {
        Query countQuery = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM Schedule s WHERE s.station=:station AND ((s.departureTime > :date AND s.departureTime < :date2 ) OR (s.arrivalTime > :date AND s.arrivalTime < :date2))");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, +1);
        countQuery.setParameter("station",station);
        countQuery.setParameter("date", date);
        countQuery.setParameter("date2",calendar.getTime());
        return (Long)countQuery.uniqueResult();
    }

    public Schedule findTrainScheduleByStation(Train train, Station station) {
        Query<Schedule> query = sessionFactory.getCurrentSession().createQuery("FROM Schedule s WHERE s.train = :train AND s.station = :station");
        query.setParameter("train", train);
        query.setParameter("station", station);
        return query.getSingleResult();
    }

}
