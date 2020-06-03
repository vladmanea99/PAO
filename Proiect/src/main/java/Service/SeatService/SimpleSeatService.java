package Service.SeatService;

import Seat.Seat;
import Seat.SeatState;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SimpleSeatService implements SeatService{

    Seat[][] seats;

    public SimpleSeatService(int nOfRows, int nOfSeatsOnRow) {
        seats = new Seat[nOfRows][nOfSeatsOnRow];
        for (int i = 0; i < nOfRows; i++){
            for (int j = 0; j < nOfSeatsOnRow; j++){
                seats[i][j] = new Seat(j, i, SeatState.AVAILABLE);
            }
        }
    }

    @Override
    public boolean validation(int beginRow, int endRow, int beginSeat, int endSeat){
        if (beginRow > endRow){
            return false;
        }
        if (beginSeat > endSeat){
            return false;
        }
        if (beginSeat < 0 || endSeat >= seats[0].length){
            return false;
        }
        if (beginRow < 0 || endRow >= seats.length){
            return false;
        }

        for (int i = beginRow; i <= endRow; i++){
            for (int j = beginSeat; j <= endSeat; j++){
                if (seats[i][j].getState() != SeatState.AVAILABLE){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void buySeats(int beginRow, int endRow, int beginSeat, int endSeat){

        //TODO CREEAZA BILET SI PUNE PE BILET DETALIILE
        if (validation(beginRow, endRow, beginSeat, endSeat)){
            for (int i = beginRow; i <= endRow; i++){
                for (int j = beginSeat; j <= endSeat; j++){
                    seats[i][j].setState(SeatState.BOUGHT);
                }
            }
        }
        else{
            System.out.println("not nice");
        }
    }

    @Override
    public void reserveSeats(int beginRow, int endRow, int beginSeat, int endSeat) {
        //TODO CREEAZA BILET SI PUNE PE BILET DETALIILE
        if (validation(beginRow, endRow, beginSeat, endSeat)){
            for (int i = beginRow; i <= endRow; i++){
                for (int j = beginSeat; j <= endSeat; j++){
                    seats[i][j].setState(SeatState.RESERVED);
                }
            }
        }
        else{
            System.out.println("not nice");
        }
    }

    @Override
    public void cancelSeats(int beginRow, int endRow, int beginSeat, int endSeat) {
        for (int i = beginRow; i <= endRow; i++){
            for (int j = beginSeat; j <= endSeat; j++){
                seats[i][j].setState(SeatState.AVAILABLE);
            }
        }
    }

    @Override
    public Seat[][] getSeats() {
        return seats;
    }

    @Override
    public int getNumberOfRows() {
        return seats.length;
    }

    @Override
    public int getNumberOfSeatsPerRow() {
        return seats[0].length;
    }

    @Override
    public String getSeatsForCSV() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < seats.length; i++){
            for (int j = 0; j < seats[0].length; j++){
                stringBuilder.append(seats[i][j].getState().toString()).append(";");
            }
        }
        return stringBuilder.toString();
    }
}
