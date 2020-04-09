package Service.ShowService;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import Show.*;
@Getter
@ToString
public class SimpleShowService implements ShowService {

    List<Show> shows;

    public SimpleShowService(List<Show> shows) {
        this.shows = shows;
    }

    @Override
    public void addShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, String type, List<String> names){
        ShowType showType = ShowType.valueOf(type.toUpperCase());
        Show show = null;
        if(showType == ShowType.OPERA){
            show = new OperaShow(nOfRows, nOfSeatsOnRow, name, location, timeOfShow, names);
        }
        else if (showType == ShowType.THEATER){
             show = new TheaterShow(nOfRows, nOfSeatsOnRow, name, location, timeOfShow, names);
        }

        shows.add(show);
    }

    @Override
    public void addShow(Show show) {
        shows.add(show);
    }

    @Override
    public void cancelShow(Show show){
        shows.remove(show);
    }

    @Override
    public List<Show> getShows() {
        return shows;
    }

    //TODO ADD A SHOW TO THE LIST
}
