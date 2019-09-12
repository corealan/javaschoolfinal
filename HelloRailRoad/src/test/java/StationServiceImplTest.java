import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import security.dao.StationDAO;
import security.model.Schedule;
import security.model.Station;
import security.service.TrainService;
import security.service.impl.StationServiceImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationServiceImplTest {

    @InjectMocks
    private StationServiceImpl stationService;

    @Mock
    private TrainService trainServiceMock;

    @Mock
    private StationDAO stationDAOMock;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getStationById() {
        Station station = new Station();
        station.setId(3l);
        Mockito.when(stationDAOMock.getStationById(3l)).thenReturn(station);
        assertEquals(stationService.getStationById(3l),station);
    }

    @Test
    void getStationByName() {
        Station station = new Station();
        station.setName("Волковская");
        Mockito.when(stationDAOMock.getStationByName("Волковская")).thenReturn(station);
        assertEquals(stationService.getStationByName("Волковская"), station);
    }

    @Test
    void getAllStations() {
        List<Station> stations = new ArrayList<Station>();
        Mockito.when(stationDAOMock.getAllStations()).thenReturn(stations);
        assertEquals(stationService.getAllStations(),stations);
    }

    @Test
    void getRoutes() {
        Station a = new Station();
        a.setName("A");
        a.setId(1l);

        Station b = new Station();
        b.setName("B");
        b.setId(2l);

        Station c = new Station();
        c.setName("C");
        c.setId(3l);

        Station d = new Station();
        d.setName("D");
        d.setId(4l);

        a.addAdjacent(b);
        b.addAdjacent(a);

        b.addAdjacent(c);
        c.addAdjacent(b);

        c.addAdjacent(d);
        d.addAdjacent(c);

        a.addAdjacent(d);
        d.addAdjacent(a);

        a.addAdjacent(c);
        c.addAdjacent(a);

        b.addAdjacent(d);
        d.addAdjacent(b);

        Mockito.when(stationDAOMock.getStationByName("A")).thenReturn(a);

        assertEquals(stationService.getRoutes("A","D").size(),5);
    }

    @Test
    void getScheduleOnDate() throws ParseException {
        Station station = new Station();
        Schedule schedule = new Schedule();
        schedule.setId(1l);
        schedule.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-10-23 14:50"));
        schedule.setStation(station);

        Schedule schedule1 = new Schedule();
        schedule1.setId(2l);
        schedule1.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-10-24 14:50"));
        schedule1.setStation(station);

        Schedule schedule2 = new Schedule();
        schedule2.setId(3l);
        schedule2.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-10-24 19:50"));
        schedule2.setStation(station);

        List<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);
        schedules.add(schedule1);
        schedules.add(schedule2);

        station.setSchedules(schedules);
        assertEquals(stationService.getScheduleOnDate(station,new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-24")).get(0).getDepartureTime(),schedule1.getDepartureTime());
    }
}