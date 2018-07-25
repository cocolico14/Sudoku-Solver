/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sudokusolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author soheilchangizi
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button solveButton;
    
    @FXML
    private Button loadButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private RadioButton forwardChecking;
    
    @FXML
    private Label status;
    
    @FXML
    private Label time;
    
    @FXML
    private GridPane table;
    
    private int[][] init;
    
    private Label[][] numbers;
    
    private HBox[][] numberCont;
    
    private long startTime= 0, endTime = 0, totalTime = 0;
    
    @FXML
    private void handleSolveButtonAction(ActionEvent event) {
        
        State start = new State(numbers);
        ProblemTree pt = new ProblemTree();
        NumberFormat formatter = new DecimalFormat("#0.00000");
        Platform.runLater(() -> {
            status.setText("Calculating...");
            status.setTextFill(Color.DARKORANGE);
        });
        
        Thread job = new Thread(() -> {
            startTime = System.currentTimeMillis();
            pt.dfs(start, forwardChecking.isSelected());
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            if(pt.getCurrent().getBlank() <= 1){
                Platform.runLater(() -> {
                    status.setText("Solved");
                    status.setTextFill(Color.DARKGREEN);
                });
                Platform.runLater(() -> {
                    updateTable(pt.getCurrent());
                    time.setText("Solved in: " + formatter.format((totalTime) / 1000d) + "s");
                });
            }else{
                System.out.println(pt.getCurrent().getBlank());
                Platform.runLater(() -> {
                    status.setText("Not Solvable");
                    status.setTextFill(Color.RED);
                });
            }
        });
        job.start();
        
        Thread thread = new Thread(() -> {
            
            
            Runnable updater = () -> {
                try{
                    updateTable(pt.getCurrent());
                }catch(NullPointerException e){
                    
                }
            };
            while(endTime == 0){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.runLater(updater);
            }
            
        });
        thread.start();
    }
    
    @FXML
    private void handleLoadButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        Stage primaryStage = new Stage();
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null){
            String s = "";
            try {
                int i = 0;
                BufferedReader input = new BufferedReader(new FileReader(file));
                while ((s = input.readLine()) != null) {
                    String[] split = s.split("\\s+");
                    for (int j = 0; j < split.length; j++) {
                        numberCont[j][i].getChildren().remove(numbers[j][i]);
                        numbers[j][i] = new Label(split[j] + "");
                        numberCont[j][i].getChildren().add(numbers[j][i]);
                        init[j][i] = Integer.valueOf(split[j].trim() + "");
                    }
                    i++;
                }
            } catch (Exception e) {
                status.setText("Error: Bad Input File");
                status.setTextFill(Color.RED);
            }
        }
        
    }
    
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        startTime = 0;
        endTime = 0;
        totalTime = 0;
        status.setText("");
        time.setText("");
        for (int i = 0; i < init.length; i++) {
            for (int j = 0; j < init[i].length; j++) {
                numberCont[j][i].getChildren().remove(numbers[j][i]);
                numbers[j][i] = new Label(init[j][i] + "");
                numberCont[j][i].getChildren().add(numbers[j][i]);
            }
        }
    }
    
    private void updateTable(State s){
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                numberCont[j][i].getChildren().remove(numbers[j][i]);
                numbers[j][i] = new Label(s.getBoard()[j][i].getValue() + "");
                numberCont[j][i].getChildren().add(numbers[j][i]);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        numbers = new Label[9][9];
        numberCont = new HBox[9][9];
        table.setGridLinesVisible(true);
        
        int[][] sample = {{0,6,0,0,0,5,0,0,0},
            {0,0,0,3,8,0,5,0,0},
            {2,0,7,9,0,0,0,0,0},
            {0,0,0,0,0,4,0,2,7},
            {0,1,5,0,0,0,0,9,0},
            {7,0,0,0,0,0,8,0,0},
            {0,0,0,0,0,0,0,0,0},
            {6,8,0,0,0,0,9,4,0},
            {9,4,0,1,0,3,0,0,0}
        };
        init = sample;
        
        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample[i].length; j++) {
                numbers[i][j] = new Label(sample[j][i] + "");
                numberCont[i][j] = new HBox();
                numberCont[i][j].setAlignment(Pos.CENTER);
                numberCont[i][j].getChildren().add(numbers[i][j]);
                table.add(numberCont[i][j], i, j);
            }
        }
        
    }
    
}
