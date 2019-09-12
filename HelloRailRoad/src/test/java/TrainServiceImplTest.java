import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import security.dao.TrainDAO;
import security.model.Schedule;
import security.model.Station;
import security.model.Train;
import security.service.StationService;
import security.service.impl.TrainServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TrainServiceImplTest {
    @InjectMocks
    private TrainServiceImpl trainService;

    @Mock
    private StationService stationServiceMock;
    @Mock
    private TrainDAO trainDAOMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addNewTrain() throws ParseException {
        Map<String, String> params = new LinkedHashMap<String, String>();
        Train train = new Train();
        train.setTrainNumber(56);
        params.put("trainNumber","92");
        params.put("numberOfSeats","40");
        params.put("route","[1, 15]");
        params.put("1stop","1");
        params.put("1departure", "2019-12-27T10:00");
        params.put("15stop", "15");
        params.put("15arrive", "2019-12-27T12:00");
        Station s1 = new Station();
        s1.setName("S1");
        List<Schedule> schedules = new ArrayList<Schedule>();
        Schedule depSchedule = new Schedule();
        depSchedule.setTrain(train);
        depSchedule.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-12-27 10:00"));
        schedules.add(depSchedule);
        s1.setSchedules(schedules);
        s1.setId(1l);

        Station s2 = new Station();
        s2.setName("S2");
        Schedule arrSchedule = new Schedule();
        arrSchedule.setTrain(train);
        arrSchedule.setDepartureTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-12-27 10:00"));
        schedules.clear();
        schedules.add(arrSchedule);
        s2.setSchedules(schedules);
        s2.setId(15l);

        Mockito.when(stationServiceMock.getStationById(1l)).thenReturn(s1);
        Mockito.when(stationServiceMock.getStationById(15l)).thenReturn(s2);
        assertEquals(trainService.addNewTrain(params),"В заданное время cо станции S1 отправляется поезд №56");
    }

    @Test
    void getFullRoute() {
        Station a = new Station();
        a.setId(1l);
        a.setName("A");

        Station b = new Station();
        b.setId(2l);
        b.setName("B");

        Station c = new Station();
        c.setId(3l);
        c.setName("C");

        a.addAdjacent(b);
        b.addAdjacent(a);
        b.addAdjacent(c);
        c.addAdjacent(b);
        a.addAdjacent(c);
        c.addAdjacent(a);
        Train train = new Train();

        Schedule schedule1 = new Schedule();
        schedule1.setStation(a);
        schedule1.setTrain(train);

        Schedule schedule2 = new Schedule();
        schedule2.setStation(c);
        schedule2.setTrain(train);

        List<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule1);
        schedules.add(schedule2);


        train.setId(1l);
        LinkedList<Station> route = new LinkedList<Station>();
        route.add(a);
        route.add(c);
        train.setRoute(route);
        train.setSchedules(schedules);

        List<Station> fullRoute = new LinkedList<Station>();
        fullRoute.add(b);
        fullRoute.add(a);
        fullRoute.add(c);
        train.setFullRoute(fullRoute);
        train.setTrainNumber(777);

        List<LinkedList<Station>> routes = new ArrayList<LinkedList<Station>>();
        LinkedList<Station> possibleRoute1 = new LinkedList<Station>();
        possibleRoute1.add(a);
        possibleRoute1.add(b);
        possibleRoute1.add(c);
        LinkedList<Station> possibleRoute2 = new LinkedList<Station>();
        possibleRoute2.add(a);
        possibleRoute2.add(c);
        routes.add(possibleRoute1);
        routes.add(possibleRoute2);
        Mockito.when(stationServiceMock.getRoutes("A","C")).thenReturn(routes);
        Mockito.when(trainDAOMock.findTrainById(1l)).thenReturn(train);
        Mockito.when(stationServiceMock.getStationByName("A")).thenReturn(a);

        assertEquals(trainService.getFullRoute(train).toString(),"[1, 2, 3]");
    }

    @Test
    void findTrains() throws ParseException {
        Station a = new Station();
        Station b = new Station();
        Date from = new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-19");
        Date to = new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-23");
        List<Train> result = new ArrayList<Train>();
        Mockito.when(trainDAOMock.getTrainsBetweenStations(a,b,from,to)).thenReturn(result);
        assertEquals(result, trainService.findTrains(a,b,from,to));
    }

    @Test
    void getAllTrains() {
        List<Train> result = new ArrayList<Train>();
        Mockito.when(trainDAOMock.getAllTrains()).thenReturn(result);
        assertEquals(result, trainService.getAllTrains());
    }

    @Test
    void findTrainById() {
        Train train = new Train();
        train.setId(1l);
        Mockito.when(trainDAOMock.findTrainById(1l)).thenReturn(train);
        assertEquals(trainService.findTrainById(1l),train);
    }



    @Test
    void findTrainsBetweenStations() {
        Station departure = new Station();
        departure.setId(1l);
        Station destination = new Station();
        destination.setId(2l);
        Train train1 = new Train();
        Train train2 = new Train();

        List<Schedule> depSchedules = scheduleInit(departure, train1, train2);
        List<Schedule> destSchedules = scheduleInit(destination,train1, train2);
        departure.setSchedules(depSchedules);
        destination.setSchedules(destSchedules);

        assertEquals(trainService.findTrainsBetweenStations(departure,destination,null).size(),2);

    }

    private List<Schedule> scheduleInit(Station station, Train train1, Train train2){
        Schedule s11 = new Schedule();
        Schedule s12 = new Schedule();

        s11.setStation(station);
        s11.setTrain(train1);
        train1.addSchedule(s11);
        s12.setStation(station);
        s12.setTrain(train2);
        train2.addSchedule(s12);

        List<Schedule> depSchedules = new ArrayList<Schedule>();
        depSchedules.add(s11);
        depSchedules.add(s12);
        return depSchedules;
    }
}