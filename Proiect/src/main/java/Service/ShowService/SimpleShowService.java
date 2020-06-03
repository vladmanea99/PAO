package Service.ShowService;

import Gala.Gala;
import Seat.Seat;
import Seat.SeatState;
import Service.GalaService.GalaService;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public Show getShowDB(String name, String gala_name) {
        Show show = null;
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from shows " +
                                                            "where show_name = " + "'" + name + "'" + " and " +
                                                            "gala_name = " +  "'" + gala_name + "'" + ";")){
            while(resultSet.next()){
                int number_of_rows = resultSet.getInt("number_of_rows");
                int number_of_seats_per_row = resultSet.getInt("number_of_seats_per_row");
                String show_seats = resultSet.getString("seats");
                String show_type = resultSet.getString("show_type");
                String actor_names = resultSet.getString("actor_names");

                List<String>names = Arrays.stream(actor_names.split(";")).collect(Collectors.toList());
                String[] states = show_seats.split(";");
                LocalDateTime show_date = LocalDateTime.parse(resultSet.getString("show_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (show_type.toLowerCase().equals(OperaShow.class.toString().toLowerCase())){
                    show = new OperaShow(number_of_rows, number_of_seats_per_row, name, gala_name, show_date, names);
                    System.out.println("O");
                }
                else if(show_type.toLowerCase().equals(TheaterShow.class.toString().toLowerCase())){
                        show = new TheaterShow(number_of_rows, number_of_seats_per_row, name, gala_name, show_date, names);
                        System.out.println("T");
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
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void insertShowDB(Show show) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement()){
            int result = statement.executeUpdate("Insert into shows values(" +
                                                "'" + show.getName() + "'"  + "," +
                                                "'" + show.getLocation() + "'"+ "," +
                                                "'" + show.getTimeOfShow().toString() + "'" + "," +
                                                show.getSeatService().getNumberOfRows() + "," +
                                                show.getSeatService().getNumberOfSeatsPerRow() + "," +
                                                "'" + show.getSeatService().getSeatsForCSV() + "'" + "," +
                                                "'" + show.getClass().toString() + "'" + "," +
                                                "'" + show.getNames() + "'" +
                                                ")" + ";");
            System.out.println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Show> getShowsOfAGalaDB(String gala_name) {
        List<Show> shows = new ArrayList<Show>();
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from shows " +
                    "where gala_name = " + "'" + gala_name + "'" + ";")){
            while(resultSet.next()){
                String show_name = resultSet.getString("show_name");
                int number_of_rows = resultSet.getInt("number_of_rows");
                int number_of_seats_per_row = resultSet.getInt("number_of_seats_per_row");
                String show_seats = resultSet.getString("seats");
                String show_type = resultSet.getString("show_type");
                String actor_names = resultSet.getString("actor_names");

                List<String>names = Arrays.stream(actor_names.split(";")).collect(Collectors.toList());
                String[] states = show_seats.split(";");
                Show show = null;
                LocalDateTime show_date = LocalDateTime.parse(resultSet.getString("show_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (show_type.toLowerCase().equals(OperaShow.class.toString().toLowerCase())){
                    show = new OperaShow(number_of_rows, number_of_seats_per_row, show_name, gala_name, show_date, names);
                    System.out.println("O");
                }
                else if(show_type.toLowerCase().equals(TheaterShow.class.toString().toLowerCase())){
                    show = new TheaterShow(number_of_rows, number_of_seats_per_row, show_name, gala_name, show_date, names);
                    System.out.println("T");
                }
                Seat[][] seats = show.getSeatService().getSeats();
                for(int i = 0; i < seats.length; i++){
                    for(int j = 0; j < seats[0].length; j++){
                        seats[i][j].setState(SeatState.valueOf(states[i*seats[0].length + j]));
                    }
                }
                if(shows != null){
                    shows.add(show);
                }

            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return shows;
    }

    @Override
    public void deleteShowDB(Show show) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
             Statement statement = connection.createStatement()){
             int result = statement.executeUpdate("Delete from shows where " +
                                                     "show_name = " + "'" + show.getName() + "'" +
                                                     "and " + "gala_name = " + "'" + show.getLocation() + "'" +
                                                     ";");
            System.out.println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteShowDB(String show_name, String gala_name) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
             Statement statement = connection.createStatement()){
            int result = statement.executeUpdate("Delete from shows where " +
                    "show_name = " + "'" + show_name + "'" +
                    "and " + "gala_name = " + "'" + gala_name + "'" +
                    ";");
            System.out.println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateShowDB(Show show, String column_name, String newData, boolean isDate) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
             Statement statement = connection.createStatement()){
            int result = statement.executeUpdate("update shows set " +
                    column_name + "=" + "'" + (isDate ? LocalDateTime.parse(newData, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):newData) + "'" +
                    "where " + "show_name" + " = " + "'" + show.getName() + "'" +
                    " and " + "gala_name" + " = " + "'" + show.getLocation() + "'" +
                    ";");
            System.out.println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateShowDB(Show show, String column_name, int newData) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pao_proiect?useSSL=false", "root", "root");
             Statement statement = connection.createStatement()){
            int result = statement.executeUpdate("update shows set " +
                    column_name  + "=" + newData +
                    "where " + "show_name" + " = " + "'" + show.getName() + "'" +
                    " and " + "gala_name" + " = " + "'" + show.getLocation() + "'" +
                    ";");
            System.out.println(result);
        }
        catch (SQLException e){
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
