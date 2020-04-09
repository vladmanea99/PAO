package Seat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Seat {

    int number;
    int row;
    SeatState state;

    public Seat(int number, int row, SeatState state) {
        this.number = number;
        this.row = row;
        this.state = state;
    }
}
