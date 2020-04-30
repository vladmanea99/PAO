package Service.GalaService;

import Gala.Gala;

import java.util.List;

public interface GalaService {

    public List<Gala> getGalas();
    public void readCSV(String CSVFileName);
    public void writeCSV(String CSVFileName);
}
