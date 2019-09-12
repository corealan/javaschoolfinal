package security.rest;
import java.util.Date;

/**
 * Class to encapsulate meaningful for timetable app information about train in timetable
 * @autor Arkhipov Sergei
 * @version 1.0
 */
public class TimetableTrain implements Comparable<TimetableTrain>{
    private Integer number;
    private String departure;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimetableTrain that = (TimetableTrain) o;

        if (!number.equals(that.number)) return false;
        return destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + destination.hashCode();
        return result;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public int compareTo(TimetableTrain train) {
        if(arrivalTime != null && train.arrivalTime != null){
            return arrivalTime.compareTo(train.arrivalTime);
        }
        if(departureTime != null && train.departureTime != null){
            return departureTime.compareTo(train.departureTime);
        }
        return 0;
    }
}
