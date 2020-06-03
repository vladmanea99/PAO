package Show;

import Gala.Gala;
import Service.GalaService.GalaService;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TheaterShow extends Show {

    List<String> actorNames;

    public TheaterShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, List<String> actorNames) {
        super(nOfRows, nOfSeatsOnRow, name, location, timeOfShow);
        this.actorNames = actorNames;
    }

    @Override
    public String toString() {
        return "TheaterShow{" +
                "actorNames=" + actorNames +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", timeOfShow=" + timeOfShow +
                '}';
    }
    public String getDataForCSV(){
        String data =  name + "," + location + "," + timeOfShow.toString() + "," + seatService.getSeatsForCSV() + "," + TheaterShow.class.toString() + ",";

        for(String name : actorNames){
            data += name + ";";
        }

        return data;
    }

    public String getNames(){
        String names = "";
        for (String name : actorNames){
            names += name + ";";
        }
        return names;
    }

}
