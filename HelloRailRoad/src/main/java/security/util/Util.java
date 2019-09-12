package security.util;

import security.model.Station;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Util {
    public static List<List<Station>> getSubRoutes(List<Station> route){
        List<List<Station>> subroutes = new ArrayList<List<Station>>();

        for(Station s : route){
            ArrayList<Station> subroute = new ArrayList<Station>();
            int i = route.size();
            while (i > route.indexOf(s) + 1){
                subroutes.add(new ArrayList<Station>(route.subList(route.indexOf(s), i--)));
            }
        }
        return subroutes;
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
