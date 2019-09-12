package security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.dao.ScheduleDAO;
import security.model.Schedule;
import security.model.Station;
import security.service.ScheduleService;
import java.util.Date;
import java.util.List;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Transactional
    public void saveSchedule(Schedule schedule) {
        scheduleDAO.saveSchedule(schedule);

    }
    @Transactional
    public Schedule findScheduleById(Long id) {
        return scheduleDAO.findScheduleById(id);
    }

    @Transactional
    public List<Schedule> findScheduleOnDate(Station station, Date date, int first, int max) {
        return  scheduleDAO.findScheduleOnDate(station,date, first, max);
    }
    @Transactional
    public Long getCountOfSchedules(Station station, Date date) {
        return scheduleDAO.getCountOfSchedules(station,date);
    }

}
