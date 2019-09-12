package security.util;

import security.model.Schedule;
import security.model.Train;
import java.util.Date;
import java.util.List;

public class TrainValidator {
    private static String message;
    public static boolean trainScheduleValidate(Train train){
        List<Schedule> trainSchedules = train.getSchedules();

        if(trainSchedules.get(0).getDepartureTime().after(trainSchedules.get(1).getArrivalTime())){
            message = "Некорректно заданы времена прибытия/отправления!";
            return false;
        }
        for(int i = 1; i < trainSchedules.size()-1; i++){
            boolean arrivalBeforeDeparture = trainSchedules.get(i+1).getArrivalTime().before(trainSchedules.get(i).getDepartureTime());
            boolean departureBeforeArrival = trainSchedules.get(i).getDepartureTime().before(trainSchedules.get(i).getArrivalTime());

            if(arrivalBeforeDeparture || departureBeforeArrival){
                message = "Некорректно заданы времена прибытия/отправления!";
                return false;
            }
        }


        for(Schedule schedule : trainSchedules) {
            List<Schedule> stationSchedules = schedule.getStation().getSchedules();
            for (Schedule schedule1 : stationSchedules) {
                if((schedule.getDepartureTime() !=null && schedule.getDepartureTime().before(new Date())) || (schedule.getArrivalTime() != null && schedule.getArrivalTime().before(new Date()))){
                    message = "В расписании задано прошедшее время";
                    return false;
                }
                if (schedule1.getArrivalTime() != null && schedule.getArrivalTime() != null && schedule1.getArrivalTime().compareTo(schedule.getArrivalTime()) == 0) {
                    message = "В заданное время на станцию " + schedule.getStation().getName() + " прибывает поезд №" +
                            schedule1.getTrain().getTrainNumber();
                    return false;
                } else if (schedule.getDepartureTime() != null && schedule1.getDepartureTime() != null && schedule1.getDepartureTime().compareTo(schedule.getDepartureTime()) == 0) {
                    message = "В заданное время cо станции " + schedule.getStation().getName() + " отправляется поезд №" +
                            schedule1.getTrain().getTrainNumber();
                    return false;
                }
            }
        }

        message = null;
        return true;
    }

    public static String getMessage() {
        return message;
    }
}
