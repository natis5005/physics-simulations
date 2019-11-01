/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author cstuser
 */
public class Generator implements Initializable{
    
    //Containers and panes
    @FXML
    protected VBox  mainLayot;
    @FXML
    protected AnchorPane  pane;
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
    
    //labels
    @FXML
    private Label messageLabel;
    @FXML 
    private Label waterFlowLabel;
    @FXML
    private Label magnetStrengthLabel;
    @FXML
    private Label loopsLabel;
    @FXML
    private Label areaLabel;
    @FXML
    private Label timeLabel;
    
    //sliders
    @FXML
    protected Slider  flowSlider;
    @FXML
    protected Slider  strengthSlider;
    
    //textFields
    @FXML
    protected TextField  loopsField;
    @FXML
    protected TextField  areaField;
  
    //Buttons
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
    
    //Images
    @FXML
    protected ImageView  backgroundSimulation;
    @FXML
    protected ImageView  faucet;
    @FXML
    protected ImageView  barMagnet;
    @FXML
    protected ImageView wheel;
    @FXML
    protected ImageView coil;
    @FXML
    protected ImageView voltmeter;
    @FXML
    protected ImageView clock;
    
    //rectangles
    @FXML
    protected Rectangle water;
    @FXML
    protected Rectangle voltmeterArrow;
    
    //charts
    @FXML
    protected LineChart currentVStimeGraph;
    
    //user entries
    protected float waterFlow = 0.0f;
    protected float magnetStregth = Constants.MAX_MAGNETIC_FIELD;
    protected int loopsNum = 1;
    protected float loopArea = 100;
    
    //calculated values
    protected float angularVelocity = 0; //in Rad/s
    protected float magnetRotationAngle = 0;           // in rad
    protected float totalField = Constants.MAX_MAGNETIC_FIELD;         // with percentage of magnet Strength
    protected double inducedVoltage = 0.0;
    protected double inducedCurrent = 0.0;
    
    //booleans
    protected boolean isStopClicked = false;
    protected boolean isPlayClicked = false;
    protected boolean isPauseClicked = false;
    protected boolean isResetClicked = false;
    protected boolean isHelpClicked = false;
    protected boolean isExitClicked = false;
    protected boolean loopCounter = true;
    protected boolean isAngleAdjusted = true;
    
    //time variables
    private double lastFrameTime = 0.0;
    long lastNow;
    long initialTime;
    double difference = 0.0;                // difference in seconds
    double extraTime = 0.0;
    boolean doItOnce = true;
     
    //animation variables
    AnimationTimer anim ;   
    XYChart.Series series = new XYChart.Series <>();
    double voltmeterAngle = 0;
    Rotate voltmeterRotation = new Rotate(0, 3, 0);
     
    public void disableInputs()
    {
        
        loopsField.setDisable(true);
        areaField.setDisable(true);
        flowSlider.setDisable(true);
        strengthSlider.setDisable(true);
    }
    
     public void enableInputs()
    {
        loopsField.setDisable(false);
        areaField.setDisable(false);
        flowSlider.setDisable(false);
        strengthSlider.setDisable(false);
    }
     
    
    @FXML
    public void handlestartButton(ActionEvent event) 
    {
        
        if (validateEntries())
        {
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false);
            
            disableInputs();
            
            isPlayClicked = true;
            
            water.setScaleX(flowSlider.getValue()/100);
            waterFlow = (float)flowSlider.getValue();
            
            if (loopCounter)
            {
                positionCoilAndVoltmeter();
                loopCounter = false;
            }
            
            anim.start();
              
        }
    }
    public void positionCoilAndVoltmeter()
    {
        if (loopsField.getText().equals("2"))
        {
            coil.setImage(new Image(fileURL("./assets/images/2Loop.png")));
            loopsNum = 2;
        }
        else if (loopsField.getText().equals("3"))
        {
            coil.setImage(new Image(fileURL("./assets/images/3Loop.png")));
            loopsNum = 3;
        }

        double resizePercentage = Double.parseDouble(areaField.getText())/100.0;

        if (resizePercentage >= 0.7)
        {
            coil.setFitHeight(290 * resizePercentage);
            coil.setFitWidth(127* resizePercentage);
        }
        else if (resizePercentage < 0.7 && resizePercentage > 0.4)
        {
           coil.setFitHeight( 290*resizePercentage *1.1);
           coil.setFitWidth(127 *resizePercentage *1.1);
        }
        else
        {
            coil.setFitHeight (290*0.4 );
            coil.setFitWidth(127*0.4 );    
        }


        coil.setLayoutY( 256.0 - (coil.getFitHeight() / 2) ); 
        voltmeter.setLayoutY( coil.getLayoutY() - voltmeter.getFitHeight() ) ;
        voltmeter.setLayoutX(coil.getLayoutX() - ( (voltmeter.getFitWidth() / 2 ) - (coil.getFitWidth() / 2 ) ));
        double xPositionArrow = voltmeter.getLayoutX() + (voltmeter.getFitWidth()/2) -16;
        double yPositionArrow = voltmeter.getLayoutY() + 35;
        voltmeterArrow.setLayoutX(xPositionArrow);
        voltmeterArrow.setLayoutY(yPositionArrow);
    }
    
    @FXML
    public void handlPauseButton(ActionEvent event) 
    {
        pauseButton.setDisable(true);

        startButton.setDisable(false);
        resetButton.setDisable(false);
        disableInputs();
        
        
        difference = System.nanoTime();
           
        isPauseClicked = true;  
        anim.stop();
       
    }
    @FXML
    protected void handleResetButton(ActionEvent event) 
    {
        timeLabel.setText("0:00" );
        isResetClicked = true;
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        enableInputs();
        loopCounter = true;
         
        doItOnce = true;
        currentVStimeGraph.getData().clear();
        series.getData().clear();
        anim.stop();
       
        barMagnet.setRotate(0);
    }
    @FXML
    protected void handleHelp(ActionEvent event) 
    {
        messageLabel.setText("Here is some insights about the experiment you are about to simulate in order to help you. Almost all the electricity that is accessed in most homes comes from generators. Alternating current generators function "
                + "\nthanks to Faraday and Lenz’s law. Lenz’s law was the first one to be discovered and it was an observation that an induced current in a closed conducting loop will appear in such a direction that it opposes"
                + "\nthe change that produce it. Later on Faraday came with a mathematical equation to model this behaviour and identified that the change in magnetic field flux was the responsible of the induced electromotive force (battery)"
                + "\nWith those two principles the generator can be explained as a big magnet whose north and south pole are rotating thus changing the angle between the magnetic field and the normal of the surface of the loop"
                + "\n where the current is being generated.");
        
    }
    
    @FXML
    protected void handleExit(ActionEvent event) throws IOException 
    {
        anim.stop();
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        pane.getChildren().setAll(pane2);
    }
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        
        backgroundSimulation.setImage(new Image(fileURL("./assets/images/generatorBackground.jpg")));
        faucet.setImage(new Image(fileURL("./assets/images/faucet1.png")));
        barMagnet.setImage(new Image(fileURL("./assets/images/barMagnet.png")));
        wheel.setImage(new Image(fileURL("./assets/images/wheel.png")));
        coil.setImage(new Image(fileURL("./assets/images/1Loop.png")));
        voltmeter.setImage(new Image(fileURL("./assets/images/Voltmeter.png")));
        clock.setImage(new Image(fileURL("./assets/images/clock.png")));
        areaLabel.setText("Loop Area (1-100)cm\u00B2");
        
        String cssLayout = "-fx-border-color: black;\n" +
                   "-fx-border-insets: 0;\n" +
                   "-fx-border-width: 3;\n" +
                   "-fx-border-style: solid;\n";
        
        topSimulation.setStyle(cssLayout);
        inputPane.setStyle(cssLayout);
        graphPane.setStyle(cssLayout);
        bottomBox.setStyle(cssLayout);
        
        
        currentVStimeGraph.getData().add(series);
        
        lastFrameTime = 0.0f;
        
        
        isPlayClicked = false;
        pauseButton.setDisable(true);
        resetButton.setDisable (true);
        
        Rotate rotationArrow = new Rotate(0 , 2, 52 );    
        voltmeterArrow.getTransforms().add(rotationArrow );
        
        currentVStimeGraph.getXAxis().setLabel("Time (s)");
        currentVStimeGraph.getYAxis().setLabel("Current (A)");
        
        anim = new AnimationTimer()
        {
           
            
            @Override
            public void handle(long now) 
            {
                final double RADIANS_TO_DEGREES = 180 / Math.PI;
                final double DEGREES_TO_RADIANS = Math.PI / 180;
                
                // Time calculation   
                if (doItOnce)
                {
                     initialTime = System.nanoTime();
                     doItOnce = false;
                }
                if (!isPauseClicked)                    // enters when pause is clicked
                {
                    lastNow = System.nanoTime();  
                }
                if (isResetClicked)
                {
                    lastNow = System.nanoTime();
                    extraTime = 0;
                    currentVStimeGraph.getData().add(series);
                    isResetClicked = false;
                }
                else
                {
                    difference = ((now - lastNow) / 1000000000.0);
                    extraTime  = extraTime  + difference;
                    isPauseClicked = false;
                }
                
                double currentTime = (now - initialTime) / 1000000000.0;
                currentTime = currentTime - extraTime;
                
                
                currentTime = round(currentTime, 2);
                double frameDeltaTime = currentTime - lastFrameTime;                 //running handle loops each frameDeltaTime
                lastFrameTime = currentTime;
                
                int integerTime = (int)currentTime;
                int decimalTime = (int)((currentTime - integerTime)*100);
                timeLabel.setText(String.valueOf(integerTime) +":" + String.valueOf(decimalTime));
 
                //rotation of wheel and magnet           
                angularVelocity = (float)(waterFlow * Math.PI / 30);                 //convert from rev/min to rad/s, 1% of waterFlow is 1rev/min
                magnetRotationAngle = (float)((angularVelocity * currentTime));        // angle in radians THETA = W*t
                barMagnet.setRotate(-magnetRotationAngle * RADIANS_TO_DEGREES);
                wheel.setRotate(-magnetRotationAngle * RADIANS_TO_DEGREES);
                
                //graph calculations - current and flux
                currentVStimeGraph.setCreateSymbols(false);
                inducedVoltage = (-1.0 * loopsNum * totalField * strengthSlider.getValue() * loopArea/10000 *Math.sin(magnetRotationAngle) );
              
                //voltmeter calculations
                voltmeterAngle = (56*inducedVoltage/25);
                rotationArrow.setAngle(voltmeterAngle);
                
                inducedCurrent = (inducedVoltage / Constants.RESISTENCE); 
                
                if (currentTime > 0)
                {
                    XYChart.Data<Number, Number> data = new Data <> (currentTime, inducedCurrent);
                    series.getData().add(data);
                }

            }
        };   
        
        
    }
    
    public boolean isAllEntered()
    {
        if (loopsField.getText().isEmpty() || areaField.getText().isEmpty())
        {
            messageLabel.setText("Missing values. Please verify that the number of "
                                 + "loops and area has been entered");
            return false;  
        }
        return true;
    }
    public boolean isInteger()
    {
        if (loopsField.getText().matches("\\d+") && areaField.getText().matches("\\d+"))
        {
            return true;
        }
        else 
        {
            messageLabel.setText("Invalid values. Please verify that the number of loops and area are integers");
            return false;
        }
    }
    public boolean validateEntries ()
    {
        if ( isAllEntered() && isInteger())
        {
            if (Integer.parseInt(loopsField.getText()) > 3 ||Integer.parseInt(areaField.getText()) > 100 )
            {
                messageLabel.setText("Invalid values. Number of loops or area are out of the range");
                return false;
            }
            else 
            {
                messageLabel.setText("Values validated. Good to go");
                return true;
            }   
        }
        return false;
    }
    
}
