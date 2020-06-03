import ActionHistory.ActionHistory;
import Gala.Gala;
import Seat.Seat;
import Service.GalaService.GalaService;
import Service.GalaService.SimpleGalaService;
import Service.SeatService.SimpleSeatService;

import Service.ShowService.ShowService;
import Service.ShowService.SimpleShowService;
import Show.Show;
import Show.TheaterShow;

import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import Show.*;

public class Main {

    static GalaService galaService;
    public static void main(String[] args) {
        galaService = new SimpleGalaService();
        UIThread uiThread = new UIThread();
        Thread thread = new Thread(uiThread, "UIThread");
        thread.start();


    }
    static GalaService getGalaService(){
        return galaService;
    }
    /*
    static GalaService galaService;

    static void addShowsFromCSVToGalas(){
        List<Show> allShows = ShowService.readCSV("shows.csv");
        for(Show show : allShows){
            for(Gala gala : galaService.getGalas()){

                if (show.getLocation().toLowerCase().equals(gala.getName().toLowerCase())){

                        gala.getShowService().addShow(show);
                        break;
                }
            }
        }
    }

    static int getNumber(){
        Scanner scanner = new Scanner(System.in);
        boolean goodFormat = false;
        int number = 0;
        while(!goodFormat) {
            try {
                String input = scanner.nextLine();
                number = Integer.parseInt(input);
                goodFormat = true;
            } catch (NumberFormatException e) {
                System.out.println("What you have entered is not a command please try again");
                goodFormat = false;
            }
        }
        return number;
    }

    static void init(){
        galaService =  new SimpleGalaService();


        galaService.readCSV("galas.csv");
        addShowsFromCSVToGalas();
        galaService.getGalas().sort(new Comparator<Gala>() {
            @Override
            public int compare(Gala gala, Gala t1) {
                return gala.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        });
        //Just because I need a different collection besides lists
        HashMap<String, Gala> hashGalas = new HashMap<>();
        for (Gala currGala : galaService.getGalas()){
            hashGalas.put(currGala.getName(), currGala);
        }
        Iterator it = hashGalas.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
        System.out.println("");

        ActionHistory.readCSVActions("actions.csv");
    }
    public static void main(String[] args) {

        init();

        System.out.println("Available commands: ");
        System.out.println("1. Show available galas");
        System.out.println("2. Pick a gala by it's number in the list");
        System.out.println("3. Exit");

        Scanner scanner = new Scanner(System.in);

        String input;
        int number = 0;
        Show currentShow;
        Gala currentGala;


        number = getNumber();

        while (number == 1){

            if (number == 1) {
                for (int i = 1; i <= galaService.getGalas().size(); i++) {
                    System.out.println(i + " " + galaService.getGalas().get(i - 1).getName());
                }
                ActionHistory.addAction("See galas");
            }
            System.out.println("Available commands: ");
            System.out.println("1. Show available galas");
            System.out.println("2. Pick a gala by it's number in the list");
            System.out.println("3. Exit");
            number = getNumber();
        }
        if (number == 2) {
            ActionHistory.addAction("Choose gala");
            System.out.println("Choose a gala by its number");
            number = getNumber();
            currentGala = galaService.getGalas().get(number - 1);
            System.out.println("Available commands: ");
            System.out.println("1. View shows");
            System.out.println("2. Pick a show by it's number in the list");
            System.out.println("3. Add a show");
            System.out.println("4. Cancel a show by its number in the list");
            System.out.println("5. Exit");
            number = getNumber();

            while (number != 2){

                if (number == 1) {
                    ActionHistory.addAction("View shows of current gala");
                    if(currentGala.getShowService().getShows().size() == 0){
                        System.out.println("There are no current shows at this gala");
                    }
                    else {
                        for (int i = 1; i <= currentGala.getShowService().getShows().size(); i++) {
                            System.out.println(i + " " + currentGala.getShowService().getShows().get(i - 1));
                        }
                    }
                }
                else if (number == 3) {
                    ActionHistory.addAction("Add show to current gala");
                    System.out.println("Number of rows:");
                    int numberOfRows = getNumber();
                    System.out.println("Number of seats per row:");
                    int numberOfSeats = getNumber();
                    System.out.println("Name of the show:");
                    input = scanner.nextLine();
                    String nameShow = input;
                    System.out.println("Names (Enter 0 to stop):");
                    List<String> names = new ArrayList<>();
                    while (true) {
                        input = scanner.nextLine();
                        String name = input;
                        if (name.equals("0")) {
                            break;
                        } else {
                            names.add(name);
                        }
                    }
                    System.out.println("Type of show:");
                    for(ShowType showType : ShowType.values()){
                        System.out.print(showType.name() + ", ");
                    }
                    System.out.println("");
                    input = scanner.nextLine();
                    String type = input;
                    Show addedShow = null;
                    if (ShowType.valueOf(type.toUpperCase()) == ShowType.THEATER) {
                        addedShow = new TheaterShow(numberOfRows, numberOfSeats, nameShow, currentGala.getName(), LocalDateTime.now().plusMonths(2), names);
                    } else if (ShowType.valueOf(type.toUpperCase()) == ShowType.OPERA) {
                        addedShow = new OperaShow(numberOfRows, numberOfSeats, nameShow, currentGala.getName(), LocalDateTime.now().plusMonths(2), names);
                    }
                    if (addedShow != null) {
                        currentGala.getShowService().addShow(addedShow);
                    }
                    System.out.println(currentGala.getShowService().getShows());
                } else if (number == 4) {
                    ActionHistory.addAction("Remove show from current gala");
                    number = getNumber();
                    //TODO FA VERIFICARI
                    currentGala.getShowService().cancelShow(currentGala.getShowService().getShows().get(number - 1));
                } else if (number == 5) {
                    ActionHistory.addAction("Exit");
                    ShowService.writeShowCSV("shows.csv", galaService);
                    ActionHistory.writeCSV("actions.csv");
                    System.exit(0);
                }
                System.out.println("Available commands: ");
                System.out.println("1. View shows");
                System.out.println("2. Pick a show by it's number in the list");
                System.out.println("3. Add a show");
                System.out.println("4. Cancel a show by its number in the list");
                System.out.println("5. Exit");
                number = getNumber();
            }
                if (number == 2) {
                    ActionHistory.addAction("Choose show from current gala");
                    System.out.println("Choose show by number:");
                    number = getNumber();
                    currentShow = currentGala.getShowService().getShows().get(number - 1);
                    System.out.println("Available commands: ");
                    System.out.println("1. View seats");
                    System.out.println("2. Buy seats");
                    System.out.println("3. Reserve seats");
                    System.out.println("4. Cancel seats");
                    System.out.println("5. Exit");

                    number = getNumber();
                    while(number != 5) {
                        if (number == 1) {
                            ActionHistory.addAction("View seats");
                            Seat[][] seats = currentShow.getSeatService().getSeats();
                            for (int i = 0; i < seats.length; i++) {
                                System.out.print("Row " + i + ": ");
                                for (int j = 0; j < seats[0].length; j++) {
                                    System.out.print(j + "-" + seats[i][j].getState() + " ");
                                }
                                System.out.println("");
                            }
                        }
                        else if(number == 2 || number == 3 || number == 4){
                            System.out.println("Specify begin row: ");
                            int beginRow = getNumber();
                            System.out.println("Specify end row: ");
                            int endRow = getNumber();
                            System.out.println("Specify begin seat: ");
                            int beginSeat = getNumber();
                            System.out.println("Specify end seat: ");
                            int endSeat = getNumber();

                            if(number == 2) {
                                ActionHistory.addAction("Buy seats");
                                currentShow.getSeatService().buySeats(beginRow, endRow, beginSeat, endSeat);
                            }
                            else if(number == 3){
                                ActionHistory.addAction("Reserve seats");
                                currentShow.getSeatService().reserveSeats(beginRow, endRow, beginSeat, endSeat);
                            }
                            else if(number == 4){
                                ActionHistory.addAction("Cancel seats");
                                currentShow.getSeatService().cancelSeats(beginRow, endRow, beginSeat, endSeat);
                            }
                        }
                        System.out.println("Available commands: ");
                        System.out.println("1. View seats");
                        System.out.println("2. Buy seats");
                        System.out.println("3. Reserve seats");
                        System.out.println("4. Cancel seats");
                        System.out.println("5. Exit");

                        number = getNumber();
                    }
                    if(number == 5){
                        ActionHistory.addAction("Exit");
                        ActionHistory.writeCSV("actions.csv");
                        ShowService.writeShowCSV("shows.csv", galaService);
                    }
            }
        } else if (number == 3) {
            ActionHistory.addAction("Exit");
            ActionHistory.writeCSV("actions.csv");
            galaService.writeCSV("galas.csv");
            ShowService.writeShowCSV("shows.csv", galaService);
            System.exit(0);
        } else {
            ActionHistory.addAction("Unknown command");
            System.out.println("Command not found");
            ActionHistory.writeCSV("actions.csv");
        }

    }
    */
}

