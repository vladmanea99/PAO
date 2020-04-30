package ActionHistory;

import Gala.Gala;
import Show.Show;
import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActionHistory {

    static List<Pair<String, LocalDateTime>> actions = new ArrayList<>();

    public static void addAction(String action){
        actions.add(new Pair(action, LocalDateTime.now()));
    }

    public static void readCSVActions(String CSVFileName){
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(CSVFileName))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data =line.split(cvsSplitBy);
                actions.add(new Pair(data[0], LocalDateTime.parse(data[1])));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String actionConverter(Pair<String, LocalDateTime> action){
        return action.getKey() + "," + action.getValue().toString();
    }

    public static void writeCSV(String CSVFileName) {
        List<String> showDatas;
        showDatas = actions.stream().map(a -> actionConverter(a)).collect(Collectors.toList());

        File csvOutputFile = new File(CSVFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            showDatas.forEach(pw::println);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
