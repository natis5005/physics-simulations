/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;

//import com.sun.corba.se.impl.orbutil.closure.Constant;
import static finalprojectyna.Generator.round;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author cstuser
 */
public class PhotoEffect implements Initializable{
    
    @FXML
    protected AnchorPane pane;
    @FXML
    protected VBox  mainLayot;
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

    @FXML
    protected Slider  intensitySlider;
    @FXML
    protected Slider  wavelengthSlider;
    @FXML
    protected Slider  voltageSlider;
    
    @FXML
    protected ChoiceBox materialOptions;
    @FXML
    protected ChoiceBox graphOptions;
    
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
    @FXML
    private Label messageLabel;
    @FXML
    protected LineChart <Number, Number> graph;
    @FXML
    protected ImageView  lamp;
    @FXML
    protected ImageView  capacitor;
    @FXML
    protected ImageView  multimeter;
    @FXML
    protected Rectangle  light;
    @FXML
    protected Label  currentLabel;
    
    //time variables
    private double lastFrameTime = 0.0;
    long lastNow;
    long initialTime;
    double difference = 0.0;                // difference in seconds
    double extraTime = 0.0;
    boolean doItOnce = true;
    boolean doItOnceEgen = true;
    
    
    XYChart.Series <Number, Number>  series = new XYChart.Series <>();
    XYChart.Series <Number, Number>  dataPoint = new XYChart.Series<>();
    static private double Gamma = 0.80;
    static private double IntensityMax = 255;
    
    //SIMULATION 
    protected ArrayList <GameObject> electronsArray;
    protected ArrayList <Vector2D> electronPositions; 
    protected ArrayList <Vector2D> electronVelocities ;
    Vector2D electronVel = new Vector2D (1,0);
    protected boolean isElectronGenerated = false;
    double currentElectronTime = 0;
    int numOfElectrons;
    double electronGenTimeInterval;
    
    AnimationTimer anim ;
    protected boolean isPauseClicked = false;
    protected boolean isPlayClicked = false;
    protected boolean isResetClicked = false;
    
    //material constants
    double workFunction = 0;
    double peakWave = 0;
    Vector2D efficiency1 = new Vector2D(0,0);
    Vector2D efficiency2 = new Vector2D(0,0);
    
    //calculation variables
    final double JOULES_TO_EV = 1/Constants.ELECTRON_CHARGE;
    final double EV_TO_JOUULES = Constants.ELECTRON_CHARGE;
    double photonEnergy = 0.0;
    double frequency = 0.0;
    double current = 0.0;
    double power = 0;
    double waveLength = 0;
    
    int temporaryNumOfElectrons = 0;
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
   
    public static int[] waveLengthToRGB(double Wavelength)
    {
        double factor;
        double Red,Green,Blue;

        if((Wavelength >= 380) && (Wavelength<440))
        {
            Red = -(Wavelength - 440) / (440 - 380);
            Green = 0.0;
            Blue = 1.0;
        }
        else if((Wavelength >= 440) && (Wavelength<490))
        {
            Red = 0.0;
            Green = (Wavelength - 440) / (490 - 440);
            Blue = 1.0;
        }else if((Wavelength >= 490) && (Wavelength<510))
        {
            Red = 0.0;
            Green = 1.0;
            Blue = -(Wavelength - 510) / (510 - 490);
        }
        else if((Wavelength >= 510) && (Wavelength<580))
        {
            Red = (Wavelength - 510) / (580 - 510);
            Green = 1.0;
            Blue = 0.0;
        }
        else if((Wavelength >= 580) && (Wavelength<645))
        {
            Red = 1.0;
            Green = -(Wavelength - 645) / (645 - 580);
            Blue = 0.0;
        }
        else if((Wavelength >= 645) && (Wavelength<781))
        {
            Red = 1.0;
            Green = 0.0;
            Blue = 0.0;
        }
        else
        {
            Red = 0.0;
            Green = 0.0;
            Blue = 0.0;
        };

        // Let the intensity fall off near the vision limits

        if((Wavelength >= 380) && (Wavelength<420))
        {
            factor = 0.3 + 0.7*(Wavelength - 380) / (420 - 380);
        }
        else if((Wavelength >= 420) && (Wavelength<701))
        {
            factor = 1.0;
        }
        else if((Wavelength >= 701) && (Wavelength<781)){
            factor = 0.3 + 0.7*(780 - Wavelength) / (780 - 700);
        }
        else
        {
            factor = 0.0;
        };


        int[] rgb = new int[3];

        // Don't want 0^x = 1 for x <> 0
        rgb[0] = Red==0.0 ? 0 : (int) Math.round(IntensityMax * Math.pow(Red * factor, Gamma));
        rgb[1] = Green==0.0 ? 0 : (int) Math.round(IntensityMax * Math.pow(Green * factor, Gamma));
        rgb[2] = Blue==0.0 ? 0 : (int) Math.round(IntensityMax * Math.pow(Blue * factor, Gamma));

        return rgb;
    }
    
     public boolean validateEntries ()
    {
        if ( materialOptions.getValue().toString().equals("-") || graphOptions.getValue().toString().equals("-"))
        {
            messageLabel.setText("Missing values. Please select a graph option and a target material.");
            return false;
        }    
        else 
        {
            messageLabel.setText("Values validated. Good to go");
            return true;
        }
 
    }
     
    @FXML
    public void handlestartButton(ActionEvent event) 
    {
        
        if (validateEntries())
        {
            graphOptions.setDisable(true);
            materialOptions.setDisable(true);
            
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false);
            
            determineConstants();
                    
            if (graphOptions.getValue().toString().equals("Frequency Vs Energy"))
            {
                intensitySlider.setDisable(true);
                wavelengthSlider.setDisable(false);
                voltageSlider.setDisable(true);
                anim.start(); 
            }
            else if (graphOptions.getValue().toString().equals("Light Intensity Vs Current"))
            {
                intensitySlider.setDisable(false);
                wavelengthSlider.setDisable(true);
                voltageSlider.setDisable(true);
                anim.start(); 
            }
            else if(graphOptions.getValue().toString().equals("Voltage Vs current"))
            {
                intensitySlider.setDisable(true);
                wavelengthSlider.setDisable(true);
                voltageSlider.setDisable(false);
                anim.start(); 
            }
             
        }
    }
    @FXML
    public void handlPauseButton(ActionEvent event) 
    {
        difference = System.nanoTime();
        pauseButton.setDisable(true);

        startButton.setDisable(false);
        resetButton.setDisable(false);
           
        isPauseClicked = true; 
        isElectronGenerated = false;
        anim.stop();
          
    }
    @FXML
    protected void handleResetButton(ActionEvent event) 
    {
        isResetClicked = true;
        doItOnce = true;
        
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        
        intensitySlider.setDisable(false);
        wavelengthSlider.setDisable(false);
        voltageSlider.setDisable(false);
        graphOptions.setDisable(false);
        materialOptions.setDisable(false);
        
        
        graph.getData().clear();
        series.getData().clear();
        dataPoint.getData().clear();
        
        doItOnceEgen = true;
        isElectronGenerated = false;
        currentLabel.setText("0.00");
        
        
        
        for ( GameObject obj : electronsArray)
        {
            topSimulation.getChildren().remove(obj.getCircle());
        }
        electronsArray.clear();
        anim.stop();
       
    }
    @FXML
    protected void handleHelpButton(ActionEvent event) 
    {
        messageLabel.setText("Here is some insights about the experiment you are about to simulate in order to help you. Light’s nature can not be fully defined if considering a particle or a wave. Particularly light behaviour is a duality that is"
                + "\n limited by Planck’s constant. In the case evaluated, which is the photoelectric effect, light behaves as a particle that carries discrete amounts of energies and at the same time exhibit wave properties."
                + "\nThose energies are being carried by photons whichare the particles that carry the electromagnetic wave of light. With the right amount of energy electrons will be ejected from the metal and captured by an adjacent plate,"
                + "\n thus generating a current. The simulation will try to represent the photoelectric effect by directing a beam of monochromatic light to a metal plate that can have materials of Sodium, Calcium, Zinc, Copper and platinum. ");
        
    }
    
    @FXML
    protected void handleExit(ActionEvent event) throws IOException 
    {
        anim.stop();
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        pane.getChildren().setAll(pane2);
        
    }
     
    public void determineConstants()
    {
        if (materialOptions.getValue().toString().equals("Sodium"))
        {
            workFunction = Constants.SODIUM_WORK_FUNCTION;
            peakWave = Constants.SODIUM_PEAK_LAMBDA;
            efficiency1.setX(Constants.SODIUM_QUANT_EFECT1.getX());
            efficiency1.setY(Constants.SODIUM_QUANT_EFECT1.getY());
            
            efficiency2.setX(Constants.SODIUM_QUANT_EFECT2.getX());
            efficiency2.setY(Constants.SODIUM_QUANT_EFECT2.getY());
        }
        else if (materialOptions.getValue().toString().equals("Calcium"))
        {
            workFunction = Constants.CALCIUM_WORK_FUNCTION;
            peakWave = Constants.CALCIUM_PEAK_LAMBDA;
            efficiency1.setX(Constants.CALCIUM_QUANT_EFECT1.getX());
            efficiency1.setY(Constants.CALCIUM_QUANT_EFECT1.getY());
            
            efficiency2.setX(Constants.CALCIUM_QUANT_EFECT2.getX());
            efficiency2.setY(Constants.CALCIUM_QUANT_EFECT2.getY());
        }
        else if (materialOptions.getValue().toString().equals("Zinc"))
        {
            workFunction = Constants.ZINC_WORK_FUNCTION;
            peakWave = Constants.ZINC_PEAK_LAMBDA;
            efficiency1.setX(Constants.ZINC_QUANT_EFECT1.getX());
            efficiency1.setY(Constants.ZINC_QUANT_EFECT1.getY());
            
            efficiency2.setX(Constants.ZINC_QUANT_EFECT2.getX());
            efficiency2.setY(Constants.ZINC_QUANT_EFECT2.getY());
        }
        else if (materialOptions.getValue().toString().equals("Copper"))
        {
            workFunction = Constants.COPPER_WORK_FUNCTION;
            peakWave = Constants.COPPER_PEAK_LAMBDA;
            efficiency1.setX(Constants.COPPER_QUANT_EFECT1.getX());
            efficiency1.setY(Constants.COPPER_QUANT_EFECT1.getY());
            
            efficiency2.setX(Constants.COPPER_QUANT_EFECT2.getX());
            efficiency2.setY(Constants.COPPER_QUANT_EFECT2.getY());
        }
        else if (materialOptions.getValue().toString().equals("Platinum"))
        {
            workFunction = Constants.PLATINUM_WORK_FUNCTION;
            peakWave = Constants.PLATINUM_PEAK_LAMBDA;
            efficiency1.setX(Constants.PLATINUM_QUANT_EFECT1.getX());
            efficiency1.setY(Constants.PLATINUM_QUANT_EFECT1.getY());
            
            efficiency2.setX(Constants.PLATINUM_QUANT_EFECT2.getX());
            efficiency2.setY(Constants.PLATINUM_QUANT_EFECT2.getY());
        }
    }
     
    public void handleFrequencyGraph()
    {
        waveLength = wavelengthSlider.getValue()*Math.pow(10, -9);
        photonEnergy =( ( Constants.PLANCKS_CONSTANT* Constants.SPEED_OF_LIGHT /  waveLength ) - workFunction);         //JOULES
        photonEnergy = photonEnergy*JOULES_TO_EV;
        frequency = Constants.SPEED_OF_LIGHT / waveLength;
        frequency = frequency/Math.pow(10, 14);

        graph.setTitle("Frequency Vs Energy");


        graph.getXAxis().setLabel("Frequency (10\u00B9\u2074 Hz)");
        graph.getYAxis().setLabel("Energy (eV)");

        if (photonEnergy <= 0)
        {
            XYChart.Data<Number, Number> data = new XYChart.Data <> (frequency, 0 );
            XYChart.Data<Number, Number> data2 = new XYChart.Data <> (frequency, 0 );
            series.getData().add(data);
            if (!dataPoint.getData().contains(data2))
            {
                dataPoint.getData().clear();
                dataPoint.getData().add(data2);
            }
            
        }
        else
        {
            XYChart.Data<Number, Number> data = new XYChart.Data <> (frequency, photonEnergy );
            XYChart.Data<Number, Number> data2 = new XYChart.Data <> (frequency, photonEnergy );
            series.getData().add(data);
            if (!dataPoint.getData().contains(data2))
            {
                dataPoint.getData().clear();
                dataPoint.getData().add(data2);
            }
        }
        
            //in loop take all series
            for (XYChart.Series<Number, Number> seriesIterator : graph.getData() ) 
            {
                if (seriesIterator.getName().equals("currentPoint"))                                                                 //if Name is "currentPoint" then continue
                continue;

              //for all series, take date, each data has Node (symbol) for representing point
              for (XYChart.Data<Number, Number> dataIterator : seriesIterator.getData()) {
                // this node is StackPane
                StackPane stackPane = (StackPane) dataIterator.getNode();
                stackPane.setVisible(false);
              }
            }

        
       
        
    }
    
    public void handleIntensityGraph()
    {
        graph.setTitle("Light Intensity Vs Current");
        graph.getXAxis().setLabel("Light Intensity (% Lumens)");
        graph.getYAxis().setLabel("Current (dA)");

        waveLength = wavelengthSlider.getValue()*Math.pow(10, -9);
        photonEnergy =( ( Constants.PLANCKS_CONSTANT* Constants.SPEED_OF_LIGHT /  waveLength ) - workFunction);         //JOULES
        if (waveLength <= peakWave)
        {
            power = (efficiency1.getX() * waveLength ) + efficiency1.getY();

        }
        else
        {
            power = efficiency2.getX() * Math.exp(efficiency2.getY() * waveLength);

        }
        current = ( (Constants.ELECTRON_CHARGE * intensitySlider.getValue() * power / 100 ) / photonEnergy );
        
        XYChart.Data<Number, Number> data = new XYChart.Data <> ();
        XYChart.Data<Number, Number> data2 = new XYChart.Data <> ();
        if (current <= 0)
        {
            data.setXValue(intensitySlider.getValue());
            data.setYValue(0);
            data2.setXValue(intensitySlider.getValue());
            data2.setYValue(0);
        }
        else
        {
            data.setXValue(intensitySlider.getValue());
            data.setYValue(current);
            data2.setXValue(intensitySlider.getValue());
            data2.setYValue(current);
        }

        series.getData().add(data);
        if (!dataPoint.getData().contains(data2))
        {
            dataPoint.getData().clear();
            dataPoint.getData().add(data2);

        }
        //in loop take all series
        for (XYChart.Series<Number, Number> seriesIterator : graph.getData() ) 
        {

          if (seriesIterator.getName().equals("currentPoint")) //if Name is "blue" then continue
            continue;

          //for all series, take date, each data has Node (symbol) for representing point
          for (XYChart.Data<Number, Number> dataIterator : seriesIterator.getData()) {
            // this node is StackPane
            StackPane stackPane = (StackPane) dataIterator.getNode();
            stackPane.setVisible(false);
          }
        }
    }
    public void handleVoltageGraph()
    {          
        graph.setTitle("Voltage Vs Current");
        graph.getXAxis().setLabel("Voltage (V)");
        graph.getYAxis().setLabel("Current (A)");

        waveLength = wavelengthSlider.getValue()*Math.pow(10, -9);
        photonEnergy =( ( Constants.PLANCKS_CONSTANT* Constants.SPEED_OF_LIGHT /  waveLength ) - workFunction);         //JOULES
        if (waveLength <= peakWave)
        {
            power = (efficiency1.getX() * waveLength ) + efficiency1.getY();

        }
        else
        {
            power = efficiency2.getX() * Math.exp(efficiency2.getY() * waveLength);

        }
        current = ( (Constants.ELECTRON_CHARGE * intensitySlider.getValue() * power / 100 ) / photonEnergy );
        
        if (voltageSlider.getValue() < 0 )
        {
            if (materialOptions.getValue().toString().equals("Sodium") || materialOptions.getValue().toString().equals("Calcium"))
            {
                current = current - 0.40*Math.abs(voltageSlider.getValue());
            }
            else
            {
                current = current - 0.24*Math.abs(voltageSlider.getValue());
            }
        }
        
        XYChart.Data<Number, Number> data = new XYChart.Data <> ();
        XYChart.Data<Number, Number> data2 = new XYChart.Data <> ();
        
        if (current <= 0)
        {
            data.setXValue(voltageSlider.getValue());
            data.setYValue(0);
            data2.setXValue(voltageSlider.getValue());
            data2.setYValue(0);
        }
        else
        {
            data.setXValue(voltageSlider.getValue());
            data.setYValue(current);
            data2.setXValue(voltageSlider.getValue());
            data2.setYValue(current);
        }

        series.getData().add(data);
        if (!dataPoint.getData().contains(data2))
        {
            dataPoint.getData().clear();
            dataPoint.getData().add(data2);

        }
        //in loop take all series
        for (XYChart.Series<Number, Number> seriesIterator : graph.getData() ) 
        {

          if (seriesIterator.getName().equals("currentPoint")) //if Name is "blue" then continue
            continue;

          //for all series, take date, each data has Node (symbol) for representing point
          for (XYChart.Data<Number, Number> dataIterator : seriesIterator.getData()) {
            // this node is StackPane
            StackPane stackPane = (StackPane) dataIterator.getNode();
            stackPane.setVisible(false);
          }
        }
    }

    public void addToPane(Node node)
    {
        topSimulation.getChildren().add(node);
    }
     
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        graph.getData().addAll(series, dataPoint);
        series.setName("trend");
        dataPoint.setName("currentPoint");
        String cssLayout = "-fx-border-color: black;\n" +
                   "-fx-border-insets: 0;\n" +
                   "-fx-border-width: 3;\n" +
                   "-fx-border-style: solid;\n";
      
        topSimulation.setStyle(cssLayout + "-fx-background-color: #ffffff");
        inputPane.setStyle(cssLayout);
        graphPane.setStyle(cssLayout);
        bottomBox.setStyle(cssLayout);
        
        materialOptions.getItems().addAll("-", "Sodium", "Calcium", "Zinc", "Copper", "Platinum");
        graphOptions.getItems().addAll("-", "Frequency Vs Energy", "Light Intensity Vs Current", "Voltage Vs current");
        
        materialOptions.setValue(materialOptions.getItems().get(0));
        graphOptions.setValue(graphOptions.getItems().get(0));
        
        lamp.setImage(new Image(fileURL("./assets/images/lamp.png")));
        capacitor.setImage(new Image(fileURL("./assets/images/capacitor.png")));
        multimeter.setImage(new Image(fileURL("./assets/images/multimeter.png")));
        
        wavelengthSlider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) 
            {
                light.setFill(Color.rgb(waveLengthToRGB(wavelengthSlider.getValue())[0], 
                                        waveLengthToRGB(wavelengthSlider.getValue())[1], 
                                        waveLengthToRGB(wavelengthSlider.getValue())[2]));
            }
        });
        
        intensitySlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (wavelengthSlider.getValue() < 380 || wavelengthSlider.getValue() > 781)
                {
                    light.setOpacity(.2);
                }
                else
                {
                    light.setOpacity(intensitySlider.getValue() / 100);
                }
                
            }
        });

        
        isPlayClicked = false;
        lastFrameTime = 0.0f;
        
        
        
        pauseButton.setDisable(true);
        resetButton.setDisable (true);
        
        electronsArray = new ArrayList<>();
        electronPositions = new ArrayList<>();
        electronVelocities = new ArrayList<>();
        
        electronVel.setMagnitude(100);

        
        anim = new AnimationTimer()
        {
            @Override
            public void handle(long now) 
            {
                
                // Time calculation   
                if (doItOnce)
                {
                     initialTime = System.nanoTime();
                     doItOnce = false;
                }
                if (!isPauseClicked)                    // enters when pause has been clicked
                {
                    lastNow = System.nanoTime();   
                }
                if (isResetClicked)
                {
                    lastNow = System.nanoTime();
                    extraTime = 0;
                    graph.getData().addAll(dataPoint, series);
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
                double frameDeltaTime = currentTime - lastFrameTime;                 //running handle loops each frameDeltaTime
                lastFrameTime = currentTime;
                
                currentTime = round(currentTime, 2);
                
                
                int integerTime = (int)currentTime;
                int decimalTime = (int)((currentTime - integerTime)*100);
 
                
                
                //graph calculations
                graph.setCreateSymbols(true);
                
                //velocity calculation
                
                double waveLengthV = wavelengthSlider.getValue()*Math.pow(10, -9);
                double waveTreshold = ( (Constants.PLANCKS_CONSTANT * Constants.SPEED_OF_LIGHT) /  workFunction);
                double currentV = 0.0;
                
                if(waveLengthV > waveTreshold)      // no e ejected
                {
                    numOfElectrons = 0;
                    currentV = 0.0;
                    electronGenTimeInterval = 0;
                    currentElectronTime = currentTime;               //initial time of interval for generation
                    isElectronGenerated = true;
                }
                else 
                {
                    double acceleration = (( Constants.ELECTRON_CHARGE * voltageSlider.getValue() ) / ( Constants.PLATE_SEPARATION * Constants.ELECTRON_MASS ));
                    acceleration = round(acceleration,2);
                    
                    double photonEnergyV =( ( Constants.PLANCKS_CONSTANT* Constants.SPEED_OF_LIGHT /  waveLengthV ) - workFunction);         //JOULES
                    double initialVelocity = Math.sqrt(2 * photonEnergyV / Constants.ELECTRON_MASS);
                    
                    double powerV = 0;
                        if (waveLengthV <= peakWave)
                        {
                            powerV = (efficiency1.getX() * waveLengthV ) + efficiency1.getY();
                        }
                        else
                        {
                            powerV = efficiency2.getX() * Math.exp(efficiency2.getY() * waveLength);
                        }
                        
                        currentV = ( (Constants.ELECTRON_CHARGE * intensitySlider.getValue() * powerV / 100 ) / photonEnergyV );
                        
                        if (voltageSlider.getValue() < 0 )
                        {
                            
                           if (materialOptions.getValue().toString().equals("Sodium") || materialOptions.getValue().toString().equals("Calcium"))
                           {
                               currentV = currentV - 0.40*Math.abs(voltageSlider.getValue());
                           }
                           else
                           {
                               currentV = currentV - 0.24*Math.abs(voltageSlider.getValue());
                           }
   
                        }
                    
                        numOfElectrons  = (int)( (currentV / Constants.ELECTRON_CHARGE) / Math.pow(10, 18) + 0.5 ) ;
                    
                        if (currentV <= 0.005 || numOfElectrons <= 0.005  )
                        {

                           // currentV = round(currentV,2);
                            currentV = 0.0;
                            electronGenTimeInterval = 0;
                            currentElectronTime = currentTime;               //initial time of interval for generation
                            isElectronGenerated = true;
                           // System.out.println("electrons are above treshold but not ejected");
                        }
                        else
                        {
                            currentV = round(currentV,2);
                            
                            electronGenTimeInterval = 1.0/numOfElectrons;                                      //every that many seconds I have to generate an e
                            electronGenTimeInterval = round(electronGenTimeInterval, 2);   

                            if (!isElectronGenerated )            //enters when isElectronGenerated = false
                            {
                                currentElectronTime = currentTime;               //initial time of interval for generation
                                isElectronGenerated = true;
                               // System.out.println("it happened once");
                            }

                            double longerTimeToRelease = currentElectronTime + electronGenTimeInterval;
 
                            longerTimeToRelease = round (longerTimeToRelease, 2);
                           // System.out.println("current electron time is " + currentElectronTime + " time to release is " + longerTimeToRelease + " electron time interval is " + electronGenTimeInterval);
                           
                            if (doItOnceEgen)
                            {
                                temporaryNumOfElectrons = numOfElectrons;
                                doItOnceEgen = false;
                            }
                            if (temporaryNumOfElectrons != numOfElectrons)
                            {
                                Random rand = new Random();
                                int randomYposition =  128 + (int)(Math.random() * ((348 - 128) + 1));
                                Vector2D electronPos = new Vector2D (369, randomYposition);
                                electronPositions.add(electronPos);
                                Vector2D electronVelo = new Vector2D ((initialVelocity / Math.pow(10, 4)+100 ), 0 );
                                Vector2D electronAcc = new Vector2D (0,0);
                                if (voltageSlider.getValue() > 0 )
                                {
                                    electronAcc = new Vector2D (acceleration / Math.pow(10, 11), 0);
                                }
                                else
                                {
                                    electronAcc = new Vector2D (acceleration / Math.pow(10, 12), 0);
                                }

                                GameObject newElectron = new GameObject(electronPos, electronVelo, electronAcc, 6);
                                
                                newElectron.getCircle().setFill(Color.BLUEVIOLET);
                                electronsArray.add(newElectron);
                                topSimulation.getChildren().add(newElectron.getCircle());
                                isElectronGenerated = false;
                                
                                doItOnceEgen = true;

                            } 
                            if ( ( longerTimeToRelease >= (currentTime - 0.04) ) && ( longerTimeToRelease <= ( currentTime + 0.04 ) ))
                            {
                                Random rand = new Random();
                                int randomYposition =  128 + (int)(Math.random() * ((348 - 128) + 1));
                                Vector2D electronPos = new Vector2D (369, randomYposition);
                                Vector2D electronVelo = new Vector2D ((initialVelocity / Math.pow(10, 4)+100 ), 0 );
                                Vector2D electronAcc = new Vector2D (0,0);
                                
                                if (voltageSlider.getValue() > 0 )
                                {
                                    electronAcc = new Vector2D (acceleration / Math.pow(10, 11), 0);
                                }
                                else
                                {
                                    electronAcc = new Vector2D (acceleration / Math.pow(10, 12), 0);
                                }
                              
                                isElectronGenerated = false;
                                doItOnceEgen = true;
                                
                                GameObject newElectron = new GameObject(electronPos, electronVelo, electronAcc, 6);
                                
                                newElectron.getCircle().setFill(Color.BLUEVIOLET);
                                electronsArray.add(newElectron);
                                topSimulation.getChildren().add(newElectron.getCircle());
                          
                            } 
                        }
                    
                    if (currentV < 0.08)
                    {
                        currentV = 0.0;
                    }
                    
                }
                currentLabel.setText(String.valueOf(currentV));

               for (GameObject obj : electronsArray)
                {
                    try
                    {
                        obj.update(frameDeltaTime);
                        if(obj.position.getX() > 935 || obj.position.getX() < 368)
                        {
                            electronsArray.remove(obj);
                           topSimulation.getChildren().remove(obj.getCircle());

                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Exception catched");
                    }
                    
                    
                }  
               
                // plot graphs
                if (currentTime > 0)
                {
                    if (graphOptions.getValue().toString().equals("Frequency Vs Energy"))
                    {
                        handleFrequencyGraph();   
                    }
                    else if (graphOptions.getValue().toString().equals("Light Intensity Vs Current"))
                    {
                        handleIntensityGraph();  
                    }
                    else if(graphOptions.getValue().toString().equals("Voltage Vs current"))
                    {
                        handleVoltageGraph();
                    }
                    
                }
                
                
            }
        };   
       
    }
    
}
