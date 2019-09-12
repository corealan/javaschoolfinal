package security.model;
import security.model.security.Passenger;
import javax.persistence.*;
import java.util.Date;

/**
 * Entity class representing train ticket
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationTime;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_station_id")
    private Station departure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_station_id")
    private Station destination;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;


    public Ticket() {
    }

    public Ticket(Train train, Station departure, Station destination, Passenger passenger, Date departureTime, Date arrivalTime) {
        this.train = train;
        this.departure = departure;
        this.destination = destination;
        this.passenger = passenger;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }


    public Station getDeparture() {
        return departure;
    }

    public void setDeparture(Station departure) {
        this.departure = departure;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservTime) {
        this.reservationTime = reservTime;
    }
}
