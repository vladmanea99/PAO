package Service.GalaService;

import Gala.Gala;
import lombok.Getter;

import java.io.*;
import java.sql.*;
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

    @Override
    public Gala getGalaDB(String name) {
        Gala gala = null;
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from galas where gala_name = " + "'" + name + "'"+ ";");){

            while(resultSet.next()) {
                gala = new Gala(resultSet.getString("gala_name"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return gala;
    }

    @Override
    public void insertGalaDB(Gala gala) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();){
            int result = statement.executeUpdate( "Insert into galas values(" + "'" + gala.getName() + "'"+ ");" );
            System.out.println("Insert result: " + result);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public List<Gala> getAllGalasDB() {
        List<Gala> galas = new ArrayList<Gala>();
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from galas;");){

            while(resultSet.next()) {
                galas.add(new Gala(resultSet.getString("gala_name")));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return galas;
    }

    @Override
    public void deleteGalaDB(Gala gala) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();){
            int result = statement.executeUpdate( "Delete from galas where gala_name = " + "'" + gala.getName() + "'"+ ";");
            System.out.println("Delete result: " + result);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateGalaDB(Gala gala, String newName) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();
            ){
            int resultShow = statement.executeUpdate("Update shows set gala_name = " + "'" + newName + "'"
                    + "where gala_name = " + "'" + gala.getName() + "'" + ";");
            int result = statement.executeUpdate( "Update galas set gala_name = " + "'" + newName + "'"
                    + "where gala_name = " + "'" + gala.getName() + "'" + ";");
            gala.setName(newName);
        }

        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
