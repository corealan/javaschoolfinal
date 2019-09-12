package security.dao;

import security.model.Schedule;
import security.model.Station;
import security.model.Train;
import java.util.Date;
import java.util.List;

/**
 * DAO-interface to provide operations with {@link Schedule} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface ScheduleDAO {
    void saveSchedule(Schedule schedule);
    Schedule findScheduleById(Long id);
    List<Schedule> findStationSchedule(Station station);
    List<Schedule> findScheduleOnDate(Station station, Date date, int first, int max);
    Long getCountOfSchedules(Station station, Date date);
    Schedule findTrainScheduleByStation(Train train, Station station);
}
