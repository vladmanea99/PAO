import Gala.Gala;
import Seat.Seat;
import Service.GalaService.GalaService;
import Show.Show;
import Show.*;
import Seat.*;

import javax.print.attribute.standard.JobName;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class UIThread implements Runnable{
    static JFrame fGalas = null;
    static GalaService galaService;
    static String chosenGala = null;
    static JList<String> jGalas;
    static JButton addGalaButton;
    static String[] galaNames;

    static JFrame fShows = null;
    static JLabel jLabelGalaName;
    static JButton goBackButton;
    static List<Show> shows;
    static Gala currentGala;
    static JList<String> jShows;
    static JButton addShowButton;
    static JButton removeShowButton;
    static String chosenShow = null;

    static JFrame fAddShow = null;
    static JLabel jLabelShowName;
    static JTextArea jTextAreaShowName;
    static JLabel jLabelNumberOfRows;
    static JTextArea jTextAreaNumberOfRows;
    static JLabel jLabelNumberOfSeatsPerRow;
    static JTextArea jTextAreaNumberOfSeatsPerRow;
    static JLabel jLabelShowType;
    static JTextArea jTextAreaShowType;
    static JLabel jLabelNames;
    static JTextArea jTextAreaNames;
    static JButton confirmAddShowButton;
    static JButton cancelAddShowButton;

    static  JFrame fAddGala = null;
    static JLabel jLabelAddGala;
    static JTextArea jTextAreaAddGala;
    static JButton cancelAddGalaButton;
    static JButton confirmAddGalaButton;

    static boolean isGalasUISet = false;
    static boolean isShowsUISet = false;
    static boolean isAddShowUISet = false;
    static boolean isAddGalaUISet = false;
    @Override
    public void run() {
        runUIGalas();
    }

    public static void runAddGalaUI(){
        if(!isAddGalaUISet){
            initAddGalaUI();
        }
        else{
            jTextAreaAddGala.setText("");
            fAddGala.setVisible(true);
            fGalas.setVisible(false);
        }
    }

    public static void runUIGalas(){
        if (!isGalasUISet){
            initGalaUI();
        }
        else{
            chosenGala = null;
            initJGalasData();
            fGalas.setVisible(true);
            if(fShows != null){
                fShows.setVisible(false);
            }
            if(fAddGala != null){
                fAddGala.setVisible(false);
            }

        }
    }

    public static void runUIShows(){
        if(!isShowsUISet){
            System.out.println("Aici");
            initShowsUI();
        }
        else{
            jLabelGalaName.setText(chosenGala);
            currentGala = galaService.getGalaDB(chosenGala);
            shows = currentGala.getShowService().getShowsOfAGalaDB(currentGala.getName());
            initJShowsData();
            fShows.setVisible(true);
            fGalas.setVisible(false);
            if(fAddShow != null){
                fAddShow.setVisible(false);
            }
        }
    }

    public static void runAddShowUI(){
        if(!isAddShowUISet){
            initAddShowUI();
        }
        else{
            jTextAreaShowName.setText("");
            jTextAreaNumberOfRows.setText("");
            jTextAreaNumberOfSeatsPerRow.setText("");
            jTextAreaNames.setText("");
            jTextAreaShowType.setText("");

            fShows.setVisible(false);
            fAddShow.setVisible(true);
        }
    }

    static boolean isDataOk(){

        if(jTextAreaShowName.getText() == null || jTextAreaShowName.getText().equals("")){
            System.out.println("Name not good");
            return false;
        }
        if(jTextAreaShowType.getText() == null || (!jTextAreaShowType.getText().toLowerCase().equals("opera") && !jTextAreaShowType.getText().toLowerCase().equals("theater"))){
            System.out.println("Type not good");
            return false;
        }
        if(jTextAreaNumberOfRows.getText() == null || !jTextAreaNumberOfRows.getText().matches("^[0-9]")){
            System.out.println("Number of rows not good");
            return false;
        }
        if(jTextAreaNumberOfSeatsPerRow.getText() == null || !jTextAreaNumberOfSeatsPerRow.getText().matches("^[0-9]")){
            System.out.println("Number of seats not good");
            return false;
        }
        if(jTextAreaNames.getText() == null || jTextAreaNames.getText().equals("")){
            System.out.println("Names not good");
            return false;
        }
        return true;
    }

    protected static void initAddGalaUI(){
        fGalas.setVisible(false);
        fAddGala = new JFrame();

        jLabelAddGala = new JLabel("Add name for new gala");
        jLabelAddGala.setBounds(100, 100, 200, 16);
        jTextAreaAddGala = new JTextArea("");
        jTextAreaAddGala.setBounds(100, 120, 200, 16);

        confirmAddGalaButton = new JButton("Add gala");
        confirmAddGalaButton.setBounds(400, 200, 200, 100);
        confirmAddGalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                galaService.insertGalaDB(new Gala(jTextAreaAddGala.getText()));
                runUIGalas();
            }
        });

        cancelAddGalaButton = new JButton("Cancel");
        cancelAddGalaButton.setBounds(400, 400, 200, 100);
        cancelAddGalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runUIGalas();
            }
        });

        fAddGala.add(jLabelAddGala);
        fAddGala.add(jTextAreaAddGala);
        fAddGala.add(confirmAddGalaButton);
        fAddGala.add(cancelAddGalaButton);

        fAddGala.setSize(1336, 768);
        fAddGala.setLayout(null);
        fAddGala.setVisible(true);
        isAddGalaUISet = true;

    }

    public static void initAddShowUI(){
        fAddShow = new JFrame();

        jLabelShowName = new JLabel("Add show name");
        jLabelShowName.setBounds(100, 100, 200, 16);
        jTextAreaShowName = new JTextArea();
        jTextAreaShowName.setBounds(100, 120, 200, 16);

        jLabelNumberOfRows = new JLabel("Add number of rows");
        jLabelNumberOfRows.setBounds(100, 160, 200, 16);
        jTextAreaNumberOfRows = new JTextArea();
        jTextAreaNumberOfRows.setBounds(100, 180, 200, 16);

        jLabelNumberOfSeatsPerRow = new JLabel("Add number of seats per row");
        jLabelNumberOfSeatsPerRow.setBounds(100, 220, 200, 16);
        jTextAreaNumberOfSeatsPerRow = new JTextArea();
        jTextAreaNumberOfSeatsPerRow.setBounds(100, 240, 200,16);

        jLabelShowType = new JLabel("Add show type");
        jLabelShowType.setBounds(100, 280, 200, 16);
        jTextAreaShowType = new JTextArea();
        jTextAreaShowType.setBounds(100, 300, 200, 16);

        jLabelNames = new JLabel("Add names");
        jLabelNames.setBounds(100, 340, 200, 16);
        jTextAreaNames = new JTextArea();
        jTextAreaNames.setBounds(100, 360, 200, 16);

        confirmAddShowButton = new JButton("Add show");
        confirmAddShowButton.setBounds(400, 300, 200, 50);

        cancelAddShowButton = new JButton("Cancel adding show");
        cancelAddShowButton.setBounds(400, 500, 200, 50);

        confirmAddShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(isDataOk()){
                    Show show = null;
                   String showName = jTextAreaShowName.getText();
                   String showType = jTextAreaShowType.getText();
                   int numberOfRows = Integer.parseInt(jTextAreaNumberOfRows.getText());
                   int numberOfSeatsPerRow = Integer.parseInt(jTextAreaNumberOfSeatsPerRow.getText());
                   String names = jTextAreaNames.getText();
                    System.out.println(names);
                   List<String> namesList = Arrays.stream(names.split(",")).collect(Collectors.toList());
                   if(showType.toLowerCase().equals("opera")){
                       show = new OperaShow(numberOfRows, numberOfSeatsPerRow, showName, chosenGala, LocalDateTime.now().plusMonths(2), namesList);
                   }
                   else if(showType.toLowerCase().equals("theater")){
                       show = new TheaterShow(numberOfRows, numberOfSeatsPerRow, showName, chosenGala, LocalDateTime.now().plusMonths(2), namesList);
                   }

                    Seat[][] seats = show.getSeatService().getSeats();
                    for(int i = 0; i < seats.length; i++){
                        for(int j = 0; j < seats[0].length; j++){
                            seats[i][j].setState(SeatState.AVAILABLE);
                        }
                    }

                   currentGala.getShowService().insertShowDB(show);
                   runUIShows();
                }
            }
        });

        cancelAddShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runUIShows();
            }
        });

        fAddShow.add(jLabelShowName);
        fAddShow.add(jLabelNames);
        fAddShow.add(jLabelNumberOfRows);
        fAddShow.add(jLabelNumberOfSeatsPerRow);
        fAddShow.add(jLabelShowType);

        fAddShow.add(jTextAreaShowName);
        fAddShow.add(jTextAreaNames);
        fAddShow.add(jTextAreaNumberOfRows);
        fAddShow.add(jTextAreaNumberOfSeatsPerRow);
        fAddShow.add(jTextAreaShowType);

        fAddShow.add(confirmAddShowButton);
        fAddShow.add(cancelAddShowButton);


        fAddShow.setSize(1336, 768);
        fAddShow.setLayout(null);
        fAddShow.setVisible(true);
        fShows.setVisible(false);
        isAddShowUISet = true;
    }

    private static void initJShowsData(){
        String[] showsDisplay= shows.stream().map(s -> s.getName() + ", " + s.getTimeOfShow().toString() + ", " + s.getNames()).toArray(String[]::new);
        jShows.setListData(showsDisplay);
    }

    private static void initJGalasData(){
        String[] galasDisplay = galaService.getAllGalasDB().stream().map(g -> g.getName()).toArray(String[]::new);
        galaNames = galasDisplay;
        jGalas.setListData(galasDisplay);
    }

    public static void initShowsUI(){
        fGalas.setVisible(false);
        fShows = new JFrame();

        jLabelGalaName = new JLabel();
        jLabelGalaName.setBounds(0, 0, 100, 15);
        jLabelGalaName.setText(chosenGala);

        goBackButton = new JButton("Go Back");
        goBackButton.setBounds(1000, 0, 200, 100);
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runUIGalas();
            }
        });
        currentGala = galaService.getGalaDB(chosenGala);
        shows = currentGala.getShowService().getShowsOfAGalaDB(currentGala.getName());

        jShows = new JList<>();
        initJShowsData();
        jShows.setBounds(0, 15, 400, 200);
        jShows.setBackground(fShows.getBackground());
        jShows.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                chosenShow = jShows.getSelectedValue();
            }
        });

        addShowButton = new JButton("Add show");
        addShowButton.setBounds(1000, 300 ,200 ,100);
        addShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAddShowUI();
            }
        });

        removeShowButton = new JButton("Remove selected show");
        removeShowButton.setBounds(1000, 500, 200, 100);
        removeShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (chosenShow != null){
                    System.out.println(chosenGala + " " + chosenShow.split(",")[0]);
                    currentGala.getShowService().deleteShowDB( chosenShow.split(",")[0], currentGala.getName());
                    chosenShow = null;
                    runUIShows();
                }
            }
        });
        fShows.add(addShowButton);
        fShows.add(jLabelGalaName);
        fShows.add(goBackButton);
        fShows.add(jShows);
        fShows.add(removeShowButton);

        fShows.setSize(1336, 768);
        fShows.setLayout(null);
        fShows.setVisible(true);
        isShowsUISet = true;
    }

    public static void initGalaUI(){
        if(fShows != null){
            fShows.setVisible(false);
        }
        chosenGala = null;
            fGalas = new JFrame();

        galaService = Main.getGalaService();
        List<Gala> galas = galaService.getAllGalasDB();
        galaNames = galas.stream().map(g -> g.getName()).toArray(String[]::new);
        jGalas = new JList<String>(galaNames);
        jGalas.setBounds(0, 0, 300, 300);
        jGalas.setBackground(fGalas.getBackground());

        jGalas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(chosenGala == null && jGalas.getSelectedIndex() != -1) {
                    System.out.println(galaNames);
                    System.out.println(jGalas.getSelectedIndex());
                    chosenGala = galaNames[jGalas.getSelectedIndex()];
                    runUIShows();
                }
            }
        });

        addGalaButton = new JButton("Add gala");
        addGalaButton.setBounds(500, 500, 200, 100);
        addGalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                runAddGalaUI();
            }
        });

        fGalas.add(jGalas);
        fGalas.add(addGalaButton);

        fGalas.setSize(1336, 768);
        fGalas.setLayout(null);
        fGalas.setVisible(true);
        isGalasUISet = true;

    }
}
