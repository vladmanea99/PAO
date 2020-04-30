package Service.GalaService;

import Gala.Gala;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleGalaService implements GalaService{

    List<Gala> galas;

    public SimpleGalaService() {
        galas = new ArrayList<>();
    }

    public List<Gala> getGalas() {
        return galas;
    }

    @Override
    public void readCSV(String CSVFileName) {
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(CSVFileName))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                List<String> names = Arrays.stream(line.split(cvsSplitBy)).collect(Collectors.toList());
                for (String name : names){
                    galas.add(new Gala(name));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeCSV(String CSVFileName) {
        String names = galas.stream().map(Gala::getName).collect(Collectors.joining(","));
        File csvOutputFile = new File(CSVFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(names);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
