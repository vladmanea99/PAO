package Show;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OperaShow extends Show {

    List<String> singerNames;

    public OperaShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, List<String> singerNames) {
        super(nOfRows, nOfSeatsOnRow, name, location, timeOfShow);
        singerNames = singerNames;
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
}
