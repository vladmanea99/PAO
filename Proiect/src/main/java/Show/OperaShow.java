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
public class OperaShow extends Show {

    List<String> singerNames;

    public OperaShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, List<String> singerNames) {
        super(nOfRows, nOfSeatsOnRow, name, location, timeOfShow);
        this.singerNames = singerNames;
    }

    @Override
    public String toString() {
        return "OperaShow{" +
                "singerNames=" + singerNames +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", timeOfShow=" + timeOfShow +
                '}';
    }
    public String getDataForCSV(){
        String data =  name + "," + location + "," + timeOfShow.toString() + "," + seatService.getSeatsForCSV() + "," + OperaShow.class.toString() + ",";

        for(String name : singerNames){
            data += name + ";";
        }

        return data;
    }

}
