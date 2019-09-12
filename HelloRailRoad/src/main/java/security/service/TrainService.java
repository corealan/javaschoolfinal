package security.service;

import security.model.Station;
import security.model.Train;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Service class contains business logic concerning {@link Train} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface TrainService {
    void saveTrain(Train train);
    void removeTrain(Train train);
    void updateFullRoute(Train train, Station newStation);
    String addNewTrain(Map<String, String> params);
    List<Station> getFullRoute(Train train);
    List<Train> findTrains(Station departure, Station arrival, Date from, Date to);
    List<Train> findTrains(Station departure, Station arrival, Date from, Date to, int start, int max);
    List<Train> getAllTrains();
    Train findTrainById(Long id);
    List<Train> findTrainsBetweenStations(Station departure, Station destination, Date date);
    List<Train> findTrainsByStation(Station station, int first, int max);
    Long getCountOfTrains(Station station);
    List<Map<Train, List<Station>>> findComplexRoute(Station departure, Station destination, Date from, Date to);
}
