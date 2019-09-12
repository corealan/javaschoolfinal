package security.dao;
import security.model.Station;
import java.util.List;

/**
 * DAO-interface to provide operations with {@link Station} entity
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public interface StationDAO {

    List<Station> getAllStations();

    void saveStation(Station station);

    Station getStationById(long id);

    Station getStationByName(String name);


}
