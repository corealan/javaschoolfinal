package security.model;

import javax.persistence.*;
import java.util.*;
/**
 * Entity class representing station
 * @autor Arkhipov Sergei
 * @version 1.0
 */
@Entity
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;
    @Column(name = "station_name", unique = true)
    private String name;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Station> adjacent;

    @OneToMany(mappedBy = "station",fetch = FetchType.EAGER)
    private List<Schedule> schedules;

    public List<Station> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(List<Station> adjacent) {
        this.adjacent = adjacent;
    }

    public void removeAdjacent(Station adjacentToRemove){
        adjacent.remove(adjacentToRemove);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAdjacent(Station station){
        adjacent.add(station);
    }
    public Station() {
        adjacent = new ArrayList<Station>();
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public String toString() {
        return id + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (!id.equals(station.id)) return false;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
