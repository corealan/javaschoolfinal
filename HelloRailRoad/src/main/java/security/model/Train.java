package security.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing train
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Entity
public class Train implements Comparable<Train>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_number")
    private Integer trainNumber;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy(value = "departureTime")
    private List<Schedule> schedules;

    @OrderBy(value = "schedules")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Station> route;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "train_full_route",joinColumns = @JoinColumn(name = "train_id"),inverseJoinColumns = @JoinColumn(name = "station_id"))
    private List<Station> fullRoute;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Ticket> tickets;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        if (!id.equals(train.id)) return false;
        return trainNumber.equals(train.trainNumber);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + trainNumber.hashCode();
        return result;
    }

    public Schedule getScheduleByStation(Station station){
        for(Schedule schedule : this.getSchedules()){
            if (schedule.getStation().equals(station))
                return schedule;
        }
        return null;
    }

    public List<Station> getFullRoute() {
        return fullRoute;
    }

    public void setFullRoute(List<Station> fullRoute) {
        this.fullRoute = fullRoute;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Integer getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(Integer trainNumber) {
        this.trainNumber = trainNumber;
    }

    public List<Station> getRoute() {
        route = new ArrayList<Station>();
        for(Schedule schedule: schedules){
            route.add(schedule.getStation());
        }
        return route;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    public void setRoute(List<Station> route) {
        this.route = route;
    }

    public Train() {
        schedules = new ArrayList<Schedule>();
    }

    public int compareTo(Train o) {
        return schedules.get(0).getDepartureTime().compareTo(o.schedules.get(0).getDepartureTime());
    }


}
