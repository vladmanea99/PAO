package Show;

import Service.SeatService.SeatService;
import Service.SeatService.SimpleSeatService;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public abstract class Show {

    SeatService seatService;
    String name;
    String location;
    LocalDateTime timeOfShow;

    public Show(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow) {
        seatService = new SimpleSeatService(nOfRows, nOfSeatsOnRow);
        this.name = name;
        this.location = location;
        this.timeOfShow = timeOfShow;
    }
}
