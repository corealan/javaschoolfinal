package security.service;

import security.model.Schedule;
import security.model.Station;
import java.util.Date;
import java.util.List;

/**
 * Service class contains business logic concerning {@link Schedule} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    Schedule findScheduleById(Long id);
    List<Schedule> findScheduleOnDate(Station station, Date date, int first, int max);
    Long getCountOfSchedules(Station station, Date date);
}
