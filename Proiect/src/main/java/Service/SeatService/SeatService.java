package Service.SeatService;

import Seat.Seat;

public interface SeatService {

    boolean validation(int beginRow, int endRow, int beginSeat, int endSeat);
    void buySeats(int beginRow, int endRow, int beginSeat, int endSeat);
    void reserveSeats(int beginRow, int endRow, int beginSeat, int endSeat);
    void cancelSeats(int beginRow, int endRow, int beginSeat, int endSeat);
    Seat[][] getSeats();
}
