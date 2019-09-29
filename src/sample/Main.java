package sample;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.AI.PropositionalLogicResolution;
import sample.utils.AgentPercept;
import sample.utils.WmpsWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    AgentPercept agentPercept = new AgentPercept();
    GridPane gp = new GridPane();
    String worldEnv[][];
    Circle circle = new Circle();
    PropositionalLogicResolution propositionalLogicResolution = new PropositionalLogicResolution();
    Thread simulationThread;
    @Override
    public void start(Stage primaryStage) throws Exception {

        Random rand = new Random();
        Color[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED};

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane();
        BorderPane bpane = new BorderPane();
        Label gameName = new Label("Wumpus World!");
        gameName.setTextFill(Color.valueOf("white"));

        Button startBtn = new Button("Start");
        bpane.setRight(startBtn);


        bpane.setLeft(gameName);
        bpane.setStyle("-fx-background-color:darkslateblue;-fx-padding: 10px;\n" +
                "    -fx-font-size: 20px;-fx-color:white");
        root.setTop(bpane);

        VBox vbox = new VBox();

        vbox.setStyle("-fx-padding: 8 0 15 15;\n" +
                "    -fx-border-width: 1;\n" +
                "    -fx-border-color: transparent #E8E8E8 transparent transparent;\n" +
                "    -fx-background-color: #E8E8E8;");

        gp.setStyle("-fx-padding: 8 15 15 15");
        gp.setVgap(2.5);
        gp.setHgap(2.5);

        WmpsWorld myWorld = new WmpsWorld();
        worldEnv = myWorld.generateWorld();
        Map<String, Integer> list = new HashMap<String, Integer>();
        list.put("", 0);
        list.put("W", 1);
        list.put("S", 2);
        list.put("P", 3);
        list.put("B", 4);
        list.put("G", 5);
        list.put("Gl", 6);


        int[][] listOfStuff = new int[10][10];

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {

                boolean[] status = {false, false, false, false, false, false, false};

                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.valueOf("beige"));
                String[] input = worldEnv[row][col].split(" ");
                String output = "";

                for (int i = 0; i < input.length; i++) {
                    if (status[list.get(input[i])] == false) {
                        output = output + " " + input[i];
                        status[list.get(input[i])] = true;
                    }
                }

                Text text = new Text(output);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                GridPane.setRowIndex(text, row);
                GridPane.setColumnIndex(text, col);
                text.setVisible(true);

//


                if (text.getText().equals(" ")) {
                    listOfStuff[row][col] = 1;
                } else listOfStuff[row][col] = 2;
//                text.setVisible(false);
//                gp.getChildren().addAll(rec, text, circle);
                gp.getChildren().addAll(rec, text);
            }
        }

//        gp.setGridLinesVisible(true);
        Image im =new Image("agentbg.png");
        circle.setFill(new ImagePattern(im));
//        circle.setUserData("Player");
//        circle.setFill(Color.valueOf("Red"));
        circle.setRadius(18);
        circle.setUserData("Player");
        GridPane.setHalignment(circle, HPos.CENTER);
        GridPane.setRowIndex(circle, agentPercept.getCurrRow());
        GridPane.setColumnIndex(circle, agentPercept.getCurrCol());
        gp.getChildren().add(circle);
        //GridPane.setMargin(circle, new Insets(12, 0, 0, 17));
        vbox.getChildren().add(gp);
        root.setLeft(vbox);
        GridPane sidewindow =new GridPane();
        sidewindow.setStyle("-fx-padding: 10 10 10 10;");
        sidewindow.setHgap(10);
        sidewindow.setVgap(10);
        Label scoreText = new Label("Score:");
        scoreText.setStyle("-fx-font-size: 18;");
        scoreText.setTextFill(Color.valueOf("Green"));
        Label score = new Label("0");
        score.setStyle("-fx-font-size:18;");
        sidewindow.add(scoreText,1,1);
        sidewindow.add(score,2,1);
        BorderPane sidePane = new BorderPane();
        sidePane.setTop(sidewindow);

        TextArea textArea =new TextArea();
        textArea.setPrefRowCount(80);
        textArea.setPrefColumnCount(40);
        sidePane.setCenter(textArea);
        sidePane.setStyle("-fx-padding: 10");
        root.setCenter(sidePane);
        primaryStage.setTitle("Wumpus World!");
        primaryStage.setScene(new Scene(root, 750, 630));
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(600);
        primaryStage.setResizable(false);
        primaryStage.show();
        changePlayerPosition(0, 0);
        agentPercept.getKnowledgeBaseSingleton().printKB();

        startSimulation();

    }


    private void startSimulation() {
        simulationThread = new Thread(() -> { //use another thread so long process does not block gui
            changePlayerPosition(agentPercept.getCurrCol(), agentPercept.getCurrRow());
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                int dir = pickMove();
                if (dir == 0) changePlayerPosition(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1);
                else if (dir == 1) changePlayerPosition(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow());
                else if (dir == 2) changePlayerPosition(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1);
                else if (dir == 3) changePlayerPosition(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());

            }

        });
        simulationThread.start();
    }


    private void changePlayerPosition(int x, int y) {


        agentPercept.setLastVisitedCol(agentPercept.getCurrCol());
        agentPercept.setLastVisitedRow(agentPercept.getCurrRow());

        agentPercept.setCurrCol(x);
        agentPercept.setCurrRow(y);
        agentPercept.addToVisitedList(x, y);

        GridPane.setRowIndex(circle, y);
        GridPane.setColumnIndex(circle, x);
        String info = agentPercept.addKnowledgeFromPercept(worldEnv[y][x]);

        try {
            if (info.equals("Wumpus")) {
                System.out.println("WUMPUS KILLED YOU!!!!!!");
                simulationThread.join();
            } else if (info.equals("Pit")) {
                System.out.println("YOU FELL IN PIT!!!!!!");
                simulationThread.join();
            } else if (info.equals("Gold")) {
                System.out.println("YOU FOUND THE GOLD!!!!!!");
                simulationThread.join();
            }
        }catch (InterruptedException e ){
            e.printStackTrace();
        }

    }


    private int pickMove() {
        int priority = -1000;
        int dangerDir = -1;
        int moveDir = ThreadLocalRandom.current().nextInt(0, 4 + 1);

//        if(agentPercept.getCurrCol() == 0 || agentPercept.getCurrCol()== 9) moveDir = 1;
        if (agentPercept.getCurrRow() == 0 || agentPercept.getCurrRow() == 9) {
            if (agentPercept.getCurrCol() == 0) moveDir = 1;
            else moveDir = 3;
        }
        if (agentPercept.getCurrCol() == 0 || agentPercept.getCurrCol() == 9) {
            if (agentPercept.getCurrRow() == 0) moveDir = 2;
            else moveDir = 0;
        }

        if (agentPercept.getCurrCol() == 0 && agentPercept.getCurrRow() == 9) moveDir = 1;
        if (agentPercept.getCurrCol() == 9 && agentPercept.getCurrRow() == 9) moveDir = 0;

//        if(worldEnv[agentPercept.getCurrCol()][agentPercept.getCurrRow()].equals("")) return moveDir;


        if (agentPercept.getCurrCol() - 1 > -1) {
            int p = getPriority(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());
            if (priority < p) {
                if(p==-100 || p == -10) dangerDir = 3;
                priority = p;
                moveDir = 3;
            }

        }

        if (agentPercept.getCurrCol() + 1 < 10) {
            int p = getPriority(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow());
            if (priority < p) {
                if(p==-100 || p == -10) dangerDir = 1;

                priority = p;
                moveDir = 1;
            }

        }

        if (agentPercept.getCurrRow() - 1 > -1) {
            int p = getPriority(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1);
            if (priority < p ) {
                if(p==-100 || p == -10) dangerDir = 0;

                priority = p;
                moveDir = 0;
            }

        }


        if (agentPercept.getCurrRow() + 1 < 10) {
            int p = getPriority(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1);
            if (priority < p ) {
                if(p==-100 || p == -10) dangerDir = 2;

                priority = p;
                moveDir = 2;
            }

        }

        if (moveDir == 0) {
            if (agentPercept.getCurrRow() -1>-1&& !propositionalLogicResolution.getResolutionResult("W" + agentPercept.getCurrCol()+ (agentPercept.getCurrRow() -1)) && !propositionalLogicResolution.getResolutionResult("P" + agentPercept.getCurrCol()+ (agentPercept.getCurrRow() -1))   && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() -1)>0 &&agentPercept.getLastVisitedRow() == agentPercept.getCurrRow() - 1
                    && agentPercept.getLastVisitedCol() == agentPercept.getCurrCol()) {

                int min = 100000000;

                if (agentPercept.getCurrRow() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1);
                    moveDir = 2;
                }


                if (agentPercept.getCurrCol() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow());
                    moveDir = 1;
                }

                if (agentPercept.getCurrCol() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());
                    moveDir = 3;
                }


            }
        }


        if (moveDir == 1) {
            if (agentPercept.getCurrCol() + 1<10
                    && !propositionalLogicResolution.getResolutionResult("W" + (agentPercept.getCurrCol()+1)+ (agentPercept.getCurrRow())) && !propositionalLogicResolution.getResolutionResult("P" + (agentPercept.getCurrCol()+1)+ (agentPercept.getCurrRow())) &&
                    agentPercept.getVisitedCount(agentPercept.getCurrCol()+1,agentPercept.getLastVisitedRow())>0 &&agentPercept.getLastVisitedRow() == agentPercept.getCurrRow()
                    && agentPercept.getLastVisitedCol() == agentPercept.getCurrCol() + 1) {


                int min = 100000000;

                if (agentPercept.getCurrRow() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1);
                    moveDir = 2;
                }


                if (agentPercept.getCurrRow() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1);
                    moveDir = 0;
                }

                if (agentPercept.getCurrCol() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());
                    moveDir = 3;
                }

            }
        }


        if (moveDir == 2) {
            if (agentPercept.getLastVisitedRow() + 1<10 &&
                    !propositionalLogicResolution.getResolutionResult("W" + (agentPercept.getCurrCol())+ (agentPercept.getCurrRow()+1)) && !propositionalLogicResolution.getResolutionResult("P" + (agentPercept.getCurrCol())+ (agentPercept.getCurrRow()+1))
                    && agentPercept.getVisitedCount(agentPercept.getCurrCol(),agentPercept.getLastVisitedRow() + 1)>0 &&agentPercept.getLastVisitedRow() + 1 == agentPercept.getCurrRow()
                    && agentPercept.getLastVisitedCol() == agentPercept.getCurrCol()) {

                int min = 100000000;

                if (agentPercept.getCurrCol() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow());
                    moveDir = 1;
                }


                if (agentPercept.getCurrRow() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1);
                    moveDir = 0;
                }

                if (agentPercept.getCurrCol() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());
                    moveDir = 3;
                }

            }
        }


        if (moveDir == 3) {
            if (agentPercept.getCurrCol() - 1>-1 &&
                    !propositionalLogicResolution.getResolutionResult("W" + (agentPercept.getCurrCol()-1)+ (agentPercept.getCurrRow())) && !propositionalLogicResolution.getResolutionResult("P" + (agentPercept.getCurrCol()-1)+ (agentPercept.getCurrRow())) &&

                    agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow())>0 || agentPercept.getLastVisitedRow() == agentPercept.getCurrRow()
                    && agentPercept.getLastVisitedCol() == agentPercept.getCurrCol() - 1) {

                int min = agentPercept.getVisitedCount(agentPercept.getCurrCol() - 1, agentPercept.getCurrRow());

                if (agentPercept.getCurrCol() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow())) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol() + 1, agentPercept.getCurrRow());
                    moveDir = 1;
                }


                if (agentPercept.getCurrRow() - 1 > -1 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() - 1);
                    moveDir = 0;
                }

                if (agentPercept.getCurrRow() + 1 < 10 && agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)>0 && min > agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1)) {
                    min = agentPercept.getVisitedCount(agentPercept.getCurrCol(), agentPercept.getCurrRow() + 1);
                    moveDir = 2;
                }

            }
        }

        System.out.println("priority: "+priority);
        System.out.println("Dir: "+moveDir);

//        Scanner sc = new Scanner(System.in);
//        sc.nextLine();
        if(moveDir == dangerDir){
            moveDir= (moveDir+2)%4;
        }

        return moveDir;
    }

    private int getPriority(int x, int y) {
        int p = 0;
        //check if visited
        if (agentPercept.getVisitedCount(x, y) > 0) p = 1;
        else p = 5;


        if (propositionalLogicResolution.getResolutionResult("~P" + x + y)) {
            System.out.println("No Pit at " + x + " " + y);
            if (agentPercept.getVisitedCount(x, y) > 0) p = 1;
            else p = 3;

        } else {
            System.out.println("Pit at " + x + " " + y);
            return -100;
        }


        if (propositionalLogicResolution.getResolutionResult("~W" + x + y)) {
            if (agentPercept.getVisitedCount(x, y) > 0) p = 1;
            else p = 5;
        } else {
            return -10;
//            System.out.println("WUMPUS at " + x + " " + y);

        }


        if (propositionalLogicResolution.getResolutionResult("G" + x + y)) {
            System.out.println("GOLD at " + x + " " + y);
            p = 100;
        } else{
            System.out.println("No GOLD at " + x + " " + y);
        }



        return p;
    }


    public static void main(String[] args) {
        launch(args);
    }


}


