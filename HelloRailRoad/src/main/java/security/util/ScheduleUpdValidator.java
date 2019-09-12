package security.util;

import security.model.Schedule;
import security.model.Station;

import java.util.Date;
import java.util.List;

public class ScheduleUpdValidator {
    private static final String DEP_BEFORE_ARR = "Заданное время отправлнеия раньше времени прибытия";
    private static final String PREV_DEP_AFTER_ARR = "Заданное время прибытия прибытия, раньше времени отправления с предыдущей станции";
    private static final String NEXT_ARR_BEFORE_DEP = "Заданное время отправления позже времени прбытия на следующую станцию";
    public static String updateValidation(Schedule schedule){
        String message = "SUCCESS";
        Date depTime = schedule.getDepartureTime(),
                arrTime = schedule.getArrivalTime();
        if(depTime != null && arrTime != null && depTime.before(arrTime)){
            message = DEP_BEFORE_ARR;
        }
        List<Schedule> trainSchedules = schedule.getTrain().getSchedules();
        int currentScheduleIdx = trainSchedules.indexOf(schedule);
        if(currentScheduleIdx != 0) {
            Date prevStationDep = trainSchedules.get(currentScheduleIdx - 1).getDepartureTime();
            if (prevStationDep.after(arrTime)) {
                message = PREV_DEP_AFTER_ARR;
            }
        }
        if(currentScheduleIdx != trainSchedules.size()-1){
            Date nextStationArr = trainSchedules.get(currentScheduleIdx+1).getArrivalTime();
            if(nextStationArr.before(depTime)){
                message = NEXT_ARR_BEFORE_DEP;
            }
        }
        return message;
    }
}
