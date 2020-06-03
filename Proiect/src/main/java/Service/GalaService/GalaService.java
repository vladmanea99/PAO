package Service.GalaService;

import Gala.Gala;

import java.util.List;

public interface GalaService {

    public List<Gala> getGalas();
    public void readCSV(String CSVFileName);
    public void writeCSV(String CSVFileName);
    public Gala getGalaDB(String name);
    public void updateGalaDB(Gala gala, String newName);
    public void deleteGalaDB(Gala gala);
    public void insertGalaDB(Gala gala);
    public List<Gala> getAllGalasDB();

}
