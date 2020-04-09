package Service.ShowService;

import Show.Show;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowService {
    void addShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, String type, List<String> names);
    void addShow(Show show);
    void cancelShow(Show show);
    List<Show> getShows();
}
