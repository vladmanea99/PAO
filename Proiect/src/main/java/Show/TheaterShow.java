package Show;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
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
}
