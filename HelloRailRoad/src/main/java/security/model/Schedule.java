package security.model;

import javax.persistence.*;
import java.util.Date;
/**
 * Entity class representing schedule
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Entity
public class Schedule implements Comparable<Schedule>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "train_number")
    private Train train;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_time")
    private Date arrivalTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    private Date departureTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        return id.equals(schedule.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int compareTo(Schedule o) {
        if(arrivalTime != null && o.arrivalTime != null){
            return arrivalTime.compareTo(o.arrivalTime);
        } else if(arrivalTime != null){
            return arrivalTime.compareTo(o.departureTime);
        }
        if (departureTime != null && o.departureTime != null) {
            return departureTime.compareTo(o.departureTime);
        } else if (departureTime != null) {
            return departureTime.compareTo(o.arrivalTime);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", station=" + station +
                ", train=" + train +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
