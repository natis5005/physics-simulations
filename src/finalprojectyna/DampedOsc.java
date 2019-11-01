/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author cstuser
 */
public class DampedOsc implements Initializable{
    
    //Layout
    @FXML
    protected VBox  mainLayout;
    
    @FXML
    protected Pane  dampedOsc;
    
    @FXML
    protected Pane  topSimulation;
    
    @FXML
    protected HBox  middleBox;
    
    @FXML
    protected Pane  bottomBox;
    
    @FXML
    protected Pane  inputPane;
    
    @FXML
    protected Pane  graphPane;
    
    //Label
    @FXML
    private Label messageLabel;
    
    @FXML 
    private Label positionLabel;
    
    @FXML
    private Label massLabel;
    
    @FXML
    private Label springLabel;
    
    @FXML
    private Label dampingLabel;
    
    @FXML 
    private Label positionUnitLabel;
    
    @FXML
    private Label massUnitLabel;
    
    @FXML
    private Label springUnitLabel;
    
    @FXML
    private Label dampingUnitLabel;
    
    @FXML
    private Label equationLabel;
    
    @FXML
    private Label valueLabel;
    
    //Text field
    @FXML
    protected TextField  positionField;
    
    @FXML
    protected TextField  massField;
    
    @FXML
    protected TextField  springField;
    
    @FXML
    protected TextField  dampingField;
    
    //Button
    @FXML
    protected Button  startButton;
    
    @FXML
    protected Button  pauseButton;
    
    @FXML
    protected Button  resetButton;
    
    @FXML
    protected Button  helpButton;
    
    @FXML
    protected Button  exitButton;
    
    //Graph
    @FXML
    protected LineChart positionVsTimeGraph;
    
    @FXML
    protected ImageView ball;
    
    @FXML
    protected ImageView spring;
    
    //Need for animation
    protected boolean isStopClicked = false;
    protected boolean isStartClicked = false;
    protected boolean isPauseClicked = false;
    protected boolean isResetClicked = false;
    
    //Inputs values
    private double position;
    private double mass;
    private double springConst;
    private double dampingConst;
    
    //Constant value
    final static private double GRAVITY = 9.8;
    
    //Calculated values
    private double angular;
    private double v;
    private double y;
    private double currentPosition;
    private double value;
    private double maxExt;
    private double force;
    
    AnimationTimer anim ;
    
    @FXML
    protected AnchorPane pane;
    
    XYChart.Series series = new XYChart.Series <Number, Number>();
    
    Timeline secondTime;
    PathTransition pathTransition;
    
    Line pendulumHand;
    Circle circle1;
    Circle circle2;
    Path arcPath;
    Rotate secondRotate;
    
    //Disable inputs
    public void disableInputs()
    {
        positionField.setDisable(true);
        massField.setDisable(true);
        springField.setDisable(true);
        dampingField.setDisable(true);
    }
    
    //Enable inputs
    public void enableInputs()
    {
        positionField.setDisable(false);
        massField.setDisable(false);
        springField.setDisable(false);
        dampingField.setDisable(false);
    }
    
    //Verify if all text field have entries
    public boolean areValuesEntered()
    {
        if (positionField.getText().isEmpty() || massField.getText().isEmpty() || springField.getText().isEmpty() || dampingField.getText().isEmpty())
        {
            messageLabel.setVisible(true);
            messageLabel.setText("Missing values. Please verify that the values of position, mass, spring constant & damping constant are entered");
            return false;
        }
        return true;
    }
    
    //Verify if a string is a number
    public boolean isDouble(String str)
    {
        try{
            Double.parseDouble(str);
            return true;
        }
        catch(Exception e){
            return false;
        }         
    }
    
    //Verify if all entries are numbers
    public boolean areEntriesNumbers()
    {
        if(isDouble(positionField.getText()) && isDouble(massField.getText()) && isDouble(springField.getText()) && isDouble(dampingField.getText())){
            return true;
        }
        else{
            messageLabel.setVisible(true);
            messageLabel.setText("One or more entrie(s) are not numbers. Please make sure your entries are numbers.");
            return false;
        }
    }
    
    //Verify if all the inputs are valid
    public boolean areEntriesValid()
    {
        if (areValuesEntered() && areEntriesNumbers())
        {
            position = Double.parseDouble(positionField.getText());
            mass = Double.parseDouble(massField.getText());
            springConst = Double.parseDouble(springField.getText());
            dampingConst = Double.parseDouble(dampingField.getText());
            
            if (position < 0){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid position value. Please enter a positive number.");
                return false;
            }
            if(mass <= 0 || mass > 50){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid mass value. Please enter a positive number below 100 kg.");
                return false;
            }
            if(springConst <= 0 || springConst > 500){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid spring constant value. Please enter a positive number below 5000 N/m.");
                return false;
            }
            if(dampingConst <= 0 || dampingConst > 200){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid damping constant value. Please enter a positive number below 2000 kg/s.");
                return false;
            }
            else{
                return true;
            }    
        }
        return false;
    }
    
    public void addToPane(Node node)
    {
        dampedOsc.getChildren().add(node);
    }
    
    /*
    Start the calculations and diplay the results 
    and start the animation of the oscillation and the graph when the start button is pressed
    */
    @FXML
    public void handleStartButton(ActionEvent event)
    {
        if(areEntriesValid()){
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false);
            
            disableInputs();
            
            //Calculations
            value = (dampingConst * dampingConst) - (4 * mass * springConst);
            y = dampingConst / (2 * mass);
            angular = Math.sqrt((springConst/mass) - ((dampingConst*dampingConst)/(4 * (mass * mass))));  
            force = mass * GRAVITY;
            v = (-force)/dampingConst;
            maxExt = force/springConst;
            
            int lineLength = 150;
            
            //Pendulum Line
            pendulumHand = new Line(0, 10, 250, lineLength);
            pendulumHand.setStrokeWidth(3);
            pendulumHand.setStroke(Color.WHITE);

            //Pendulum Ball
            circle1 = new Circle(550, 100, mass * 5);
            circle1.setFill(Color.BLACK);

            // Binding the line and the circle1 together, so they move synchronized
            pendulumHand.startXProperty().bind(circle1.centerXProperty().add(circle1.translateXProperty()));
            pendulumHand.startYProperty().bind(circle1.centerYProperty().add(circle1.translateYProperty()));

            Line spring =  new Line(0, 10, 0, lineLength);
            spring.setStrokeWidth(3);
            spring.setStroke(Color.BLACK);
            
            //Dashed line
            spring.getStrokeDashArray().addAll(5.0,mass * 0.09);

            // Binding the line and the circle1 together, so they move synchronized
            spring.endYProperty().bind(circle1.centerYProperty().add(circle1.translateYProperty()));
            
            // Add circles and line to the pane
            dampedOsc.getChildren().addAll(pendulumHand, circle1,spring);

            // Add a path to have a semicircle movement
            Arc path = new Arc(0,50,0,lineLength, 270,90);
            path.setType(ArcType.OPEN);

            // Create a path transition and set specifications
            pathTransition = new PathTransition();
            
           
             
            
            //Display the calculated values
            if(value != 0)
            {
                if(value < 0){
                    valueLabel.setVisible(true);
                    valueLabel.setText("Underdamped \n" + "b2 - 4mk = " + value);

                    for(double t=0; t<=20; t+=0.1){
                        currentPosition = maxExt * Math.exp((-y) * t) * Math.cos((angular * t));
                        XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (t, currentPosition);
                        series.getData().add(data);
                        positionVsTimeGraph.setCreateSymbols(false);
                        positionVsTimeGraph.getData().add(series);
                    }
                    
                    //Change the period of oscillations
                    pathTransition.setRate(0.5/mass);

                    pathTransition.setPath(path);
                    pathTransition.setNode(circle1);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setCycleCount(3);
                    pathTransition.setAutoReverse(true);

                    dampedOsc.setVisible(true);
                    pathTransition.play(); 

                }
                if(value > 0){
                    valueLabel.setVisible(true);
                    valueLabel.setText("Overdamped \n" + "b2 - 4mk = " + value);

                    for(double t=0; t<=20; t+=0.1){
                        currentPosition = 2 * (Math.exp((-y) * t) * (position + (v + (y * position)) * t));
                        XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (t, currentPosition);
                        series.getData().add(data);
                        positionVsTimeGraph.setCreateSymbols(false);
                        positionVsTimeGraph.getData().add(series);
                    }

                    //Change the period of oscillations
                    pathTransition.setRate(0.5/mass);

                    pathTransition.setPath(path);
                    pathTransition.setNode(circle1);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setCycleCount(1);
                    pathTransition.setAutoReverse(true);

                    dampedOsc.setVisible(true);
                    pathTransition.play();
                }
            }
            
            else{
                valueLabel.setVisible(true);
                valueLabel.setText("Critical damping \n" + "b2 - 4mk = " + value);
                
                for(double t=0; t<=20; t+=0.1){
                    currentPosition = Math.exp((-y) * t) * (position + (v + (y * position)) * t);
                    XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (t, currentPosition);
                    series.getData().add(data);
                    positionVsTimeGraph.setCreateSymbols(false);
                    positionVsTimeGraph.getData().add(series);
                }
                
                //Change the period of oscillations
                    pathTransition.setRate(0.5/mass);

                    pathTransition.setPath(path);
                    pathTransition.setNode(circle1);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setCycleCount(1);
                    pathTransition.setAutoReverse(true);

                    dampedOsc.setVisible(true);
                    pathTransition.play();
                
            }
            /*
            if(value >= 0)
            {
                messageLabel.setText("The spring is not oscillating try to put other values.");
            }
            else
            {
                valueLabel.setVisible(true);
                    valueLabel.setText("Underdamped \n" + "b2 - 4mk = " + value);

                    for(double t=0; t<=20; t+=0.1){
                        currentPosition = maxExt * Math.exp((-y) * t) * Math.cos((angular * t));
                        XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (t, currentPosition);
                        series.getData().add(data);
                        positionVsTimeGraph.getData().add(series);
                    }
            }
            */
        }
    }
    
    //Pause the animation of the pendulum and the graph when the pause button is pressed
    @FXML
    public void handlePauseButton(ActionEvent event)
    {
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        resetButton.setDisable(false);
        
        pathTransition.stop();
        
        disableInputs();
    }
    
    //Reset all the inputs of the user when the reset button is pressed
    @FXML
    public void handleResetButton(ActionEvent event)
    {
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        
        //Reset text field
        positionField.setText(null);
        massField.setText(null);
        springField.setText(null);
        dampingField.setText(null);
        
        valueLabel.setVisible(false);
        equationLabel.setVisible(false);
        messageLabel.setVisible(false);
        
        positionVsTimeGraph.getData().clear();
        series.getData().clear();
        
        pathTransition.stop();
        dampedOsc.setVisible(false);
        
        enableInputs();
        
        dampedOsc.getChildren().clear();
    }
    
    //Display a message box with instructions when the help button is pressed
    @FXML
    public void handleHelpButton(ActionEvent event)
    {
        messageLabel.setVisible(true);
        messageLabel.setText("- The start button displays all the results with the graph and the simulation.\n" +
        "- The pause button pause the animation of the spring oscillating.\n" +
        "- The reset button resets all the inputs and outputs.\n" +
        "- The exit button makes you return to the main menu where you can choose another program.");        
    }
    
    //Return to the main window when the exit button is pressed
     @FXML
    protected void handleExit(ActionEvent event) throws IOException 
    {
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        pane.getChildren().setAll(pane2);
    }
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    //Initial look of the window
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        String cssLayout = "-fx-border-color: black;\n" +
                   "-fx-border-insets: 0;\n" +
                   "-fx-border-width: 3;\n" +
                   "-fx-border-style: solid;\n";
      
        topSimulation.setStyle(cssLayout + "-fx-background-color: #ffffff");
        inputPane.setStyle(cssLayout);
        graphPane.setStyle(cssLayout);
        bottomBox.setStyle(cssLayout);
        
        //Hide the label of calculated values at beginning
        valueLabel.setVisible(false);
        equationLabel.setVisible(false);
        messageLabel.setVisible(false);
        
        //Disable buttons at beginning
        pauseButton.setDisable(true);
        resetButton.setDisable(true);
        
        //ball.setImage(new Image(fileURL("./assets/images/ball.png")));
        //spring.setImage(new Image(fileURL("./assets/images/spring.png")));
    }
    
}
