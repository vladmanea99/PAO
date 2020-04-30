package Service.ShowService;

import Gala.Gala;
import Service.GalaService.GalaService;
import Show.Show;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Service.ShowService.SimpleShowService.allShowsFromCSV;
import static Service.ShowService.SimpleShowService.showConverter;

public interface ShowService {
    void addShow(int nOfRows, int nOfSeatsOnRow, String name, String location, LocalDateTime timeOfShow, String type, List<String> names);
    void addShow(Show show);
    void cancelShow(Show show);
    List<Show> getShows();
    public static List<Show> readCSV(String CSVFileName) {
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
        return allShowsFromCSV;
    }
    public static  void writeShowCSV(String CSVFileName, GalaService galaService){
        List<String> showDatas = new ArrayList<>();
        for(Gala gala: galaService.getGalas()){
            for (Show show : gala.getShowService().getShows()){
                showDatas.add(show.getDataForCSV());
            }
        }

        File csvOutputFile = new File(CSVFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            showDatas.stream().forEach(pw::println);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
