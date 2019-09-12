package security.dao;

import security.model.Station;
import security.model.Train;

import java.util.Date;
import java.util.List;
/**
 * DAO-interface to provide operations with {@link Train} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface TrainDAO {
    void saveTrain(Train train);
    Train findTrainById(Long id);
    void removeTrain(Train id);
    List<Train> getAllTrains();
    List<Train> getTrainsBetweenStations(Station a, Station b, Date from, Date to);
    List<Train> findTrainByNumber(Integer number);
    List<Train> depFromStation(Station station,Date date);
    List<Train> filterTrains(Station departure, Station destination, Date date, int start, int max);
    List<Train> findTrainsByStation(Station station, int first, int max);
    Long getCountOfTrains(Station station);
    List<Train> findTrainsByStationBetweenDates(Station station, Date timeFrom, Date TimeTo);
}
