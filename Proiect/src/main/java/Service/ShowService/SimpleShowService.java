package Service.ShowService;

import Gala.Gala;
import Seat.Seat;
import Seat.SeatState;
import Service.GalaService.GalaService;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Show.*;
@Getter
@ToString
public class SimpleShowService implements ShowService {

    List<Show> shows;
    static List<Show> allShowsFromCSV = new ArrayList<>();
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

    public void readCSV(String CSVFileName) {
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(CSVFileName))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] dataForShow = line.split(cvsSplitBy);
                Show show = showConverter(dataForShow);
                allShowsFromCSV.add(show);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void writeShowCSV(String CSVFileName, GalaService galaService){
        List<String> showDatas = new ArrayList<>();
        for(Gala gala: galaService.getGalas()){
            for (Show show : gala.getShowService().getShows()){
                showDatas.add(show.getDataForCSV());
            }
        }

        File csvOutputFile = new File(CSVFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            showDatas.forEach(pw::println);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Show showConverter(String[] data) {
        Show show;
        LocalDateTime dateTime = LocalDateTime.parse(data[2]);
        List<String>names = Arrays.stream(data[7].split(";")).collect(Collectors.toList());
        String[] states = data[5].split(";");
        if(data[6].equals(OperaShow.class.toString())){
            show = new OperaShow(Integer.parseInt(data[3]), Integer.parseInt(data[4]), data[0], data[1], dateTime, names);
            System.out.println("O");
        }
        else if(data[6].equals(TheaterShow.class.toString())){
            show = new TheaterShow(Integer.parseInt(data[3]), Integer.parseInt(data[4]), data[0], data[1], dateTime, names);
            System.out.println("T");
        }
        else{
            show = null;
            System.out.println("nu");
            return show;
        }
        Seat[][] seats = show.getSeatService().getSeats();
        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[0].length; j++){
                seats[i][j].setState(SeatState.valueOf(states[i*seats[0].length + j]));
            }
        }
        return show;

    }

}
