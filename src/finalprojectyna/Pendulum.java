/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author cstuser
 */
public class Pendulum implements Initializable{
    
    //Layout
    @FXML
    protected VBox  mainLayout;
    @FXML
    protected AnchorPane  pane;
    @FXML
    protected Pane  topSimulation;
    
    @FXML
    protected Pane  pendulum;
    
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
    private Label lengthLabel;
    
    @FXML
    private Label massLabel;
    
    @FXML
    private Label angleLabel;
    
    @FXML 
    private Label lengthUnitLabel;
    
    @FXML
    private Label massUnitLabel;
    
    @FXML
    private Label angleUnitLabel;
    
    @FXML
    private Label periodLabel;
    
    @FXML
    private Label energyLabel;
    
    @FXML
    private Label angularFrequencyLabel;
    
    @FXML
    private Label equationLabel;
    
    //Text field
    @FXML
    protected TextField  lengthField;
    
    @FXML
    protected TextField  massField;
    
    @FXML
    protected TextField  angleField;
    
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
    

    //Inputs values
    private double length;
    private double mass;
    private double startAngle;
    
    //Calculated values
    private double period;
    private double energy;
    private double angleFreq;
    private double height;
    private double equation;
    private double startAngleRad;
    double x;
    
    //Constant value
    final static private double GRAVITY = 9.8;
    
    //Need for animation
    protected boolean isStopClicked = false;
    protected boolean isStartClicked = false;
    protected boolean isPauseClicked = false;
    protected boolean isResetClicked = false;
    
    long initialTime;
    double extraTime = 0.0;
    
    AnimationTimer anim ;
    
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
        lengthField.setDisable(true);
        massField.setDisable(true);
        angleField.setDisable(true);
    }
    
    //Enable inputs
    public void enableInputs()
    {
        lengthField.setDisable(false);
        massField.setDisable(false);
        angleField.setDisable(false);
    }
    
    //Verify if all text field have entries
    public boolean areValuesEntered()
    {
        if (lengthField.getText().isEmpty() || massField.getText().isEmpty() || angleField.getText().isEmpty())
        {
            messageLabel.setVisible(true);
            messageLabel.setText("Missing values. Please verify that the values of Length, mass & angle are entered");
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
        if(isDouble(lengthField.getText()) && isDouble(massField.getText()) && isDouble(angleField.getText())){
            return true;
        }
        else{
            messageLabel.setVisible(true);
            messageLabel.setText("One or more entrie(s) are not numbers. Please make your entries are numbers.");
            return false;
        }
    }
    
    //Verify if all the inputs are valid
    public boolean areEntriesValid()
    {
        if (areValuesEntered() && areEntriesNumbers())
        {
            length = Double.parseDouble(lengthField.getText());
            mass = Double.parseDouble(massField.getText());
            startAngle = Double.parseDouble(angleField.getText());
            
            if (length <= 0 || length > 2){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid length value. Please enter a positive number below 2 m.");
                return false;
            }
            if(mass <= 0 || mass > 20){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid mass value. Please enter a positive number below 20 kg.");
                return false;
            }
            if(startAngle <= 0 || startAngle > 90){
                messageLabel.setVisible(true);
                messageLabel.setText("Invalid angle value. Please enter a positive number below 90 degrees.");
                return false;
            }
            else{
                return true;
            }    
        }
        return false;
    }                
 
    /*
    Start the calculations and diplay the results 
    and start the animation of the pendulum and the graph when the start button is pressed
    */
    @FXML
    public void handleStartButton(ActionEvent event)
    {
        if(areEntriesValid()){
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false);
            
            disableInputs();
            
            //Convert angle degree into radian
            startAngleRad = Math.toRadians(startAngle);
            
            //Calculations round to 2 decimals
            period = 2 * Math.PI * Math.sqrt(length/GRAVITY);
            period = period*100;
            period = Math.round(period);
            period = period /100;
            
            energy = mass * length * GRAVITY * (1 - Math.cos(Math.toRadians(startAngle)));
            energy = energy*100;
            energy = Math.round(energy);
            energy = energy /100;
            
            angleFreq = (2 * Math.PI) / period;
            angleFreq = angleFreq*100;
            angleFreq = Math.round(angleFreq);
            angleFreq = angleFreq /100;
            
            //θ(t)=θmax cos(2PI/T t)
            equation = (2 * Math.PI)/period;
            equation = equation*100;
            equation = Math.round(equation);
            equation = equation /100;
            
            
            //Display the calculated values
            periodLabel.setVisible(true);
            equationLabel.setVisible(true);
            energyLabel.setVisible(true);
            angularFrequencyLabel.setVisible(true);
            
            periodLabel.setText("Period (T) = " + period + " s");
            energyLabel.setText("Total energy (E) = " + energy + " J");
            angularFrequencyLabel.setText("Angular frequency (w) = " + angleFreq + " rad/s");
            equationLabel.setText("θ(t)= " + startAngle + "cos(" + equation + "t)");
            
            //Graph
            for(double t=0; t<=10; t+=0.01){
                double currentAngle = startAngle * Math.cos(equation * t);
                XYChart.Data<Number, Number> data = new Data <Number, Number> (t, currentAngle);
                series.getData().add(data);
            }
            positionVsTimeGraph.setCreateSymbols(false);
            positionVsTimeGraph.getData().add(series);
            
            //Pendulum Line
            pendulumHand = new Line(100, 10, 100, 10);
            
            pendulumHand.setStrokeWidth(3);
            pendulumHand.setStroke(Color.BLACK);
            
            //Pendulum Ball
            circle1 = new Circle(550, 100, mass * 1.5);
            circle1.setFill(Color.BLACK);

            // Binding the line and the circle1 together, so they move synchronized
            pendulumHand.startXProperty().bind(circle1.centerXProperty().add(circle1.translateXProperty()));
            pendulumHand.startYProperty().bind(circle1.centerYProperty().add(circle1.translateYProperty()));

            pendulum.getChildren().addAll(pendulumHand, circle1);

            // Add a path to have a semicircle movement
            int lineLength = (int)(length * 100);
            Arc path = new Arc(100,10,lineLength,lineLength, 270 - startAngle,startAngle * 2);
            path.setType(ArcType.OPEN);

            // Create a path transition and set specifications
            pathTransition = new PathTransition();
            pathTransition.setRate(1/period);
            pathTransition.setPath(path);
            pathTransition.setNode(circle1);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(Timeline.INDEFINITE);
            pathTransition.setAutoReverse(true);

            pendulum.setVisible(true);
            pathTransition.play();
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
        lengthField.setText(null);
        massField.setText(null);
        angleField.setText(null);
        
        periodLabel.setVisible(false);
        equationLabel.setVisible(false);
        energyLabel.setVisible(false);
        angularFrequencyLabel.setVisible(false);
        messageLabel.setVisible(false);
        
        positionVsTimeGraph.getData().clear();
        series.getData().clear();
        
        pathTransition.stop();
        pendulum.setVisible(false);
        
        enableInputs();
        
        pendulum.getChildren().clear();
    }
    
    //Display a message box with instructions when the help button is pressed
    @FXML
    public void handleHelpButton(ActionEvent event)
    {
        messageLabel.setVisible(true);
        messageLabel.setText("- The start button displays all the results with the graph and the simulation.\n" +
        "- The pause button pause the animation of the pendulum.\n" +
        "- The reset button resets all the inputs and outputs.\n" +
        "- The exit button makes you return to the main menu where you can choose another program.");
        
         
    }
    
    //Return to the main window when the exit button is pressed
   @FXML
    protected void handleExit(ActionEvent event) throws IOException 
    {
        //pathTransition.stop();
        //secondTime.stop();
        //pendulum.setVisible(false);
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        pane.getChildren().setAll(pane2);
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
        periodLabel.setVisible(false);
        equationLabel.setVisible(false);
        energyLabel.setVisible(false);
        angularFrequencyLabel.setVisible(false);
        messageLabel.setVisible(false);
        
        //Disable buttons at beginning
        pauseButton.setDisable(true);
        resetButton.setDisable(true);

        pendulum.setVisible(false);
    }
}
