/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
/**
 *
 * @author cstuser
 */
public class LorentzForce implements Initializable{
    @FXML
    protected AnchorPane  pane;
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
    private Circle c;
    
    // menu items 
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
    
    // Pictures 
    @FXML
    protected ImageView  backgroundSimulation;
    @FXML
    protected ImageView  MagneticForceDirection;
    @FXML
    protected ImageView  ElectricForceDirection;
    @FXML
    protected ImageView  VelocityDirection;
    @FXML
    protected ImageView  MagneticFieldDirection;
    
    //INPUTS    
    @FXML
    private TextField VelocityXinput;
    @FXML
    protected Slider  Voltage;
    @FXML
    protected Slider  ConstantMagneticField;
      
    // outputs
    private Vector2D cPosition;
    private Vector2D cVelocity;
    private Vector2D MagneticForce;
    private Vector2D ElectricForce;
    private Vector2D acceleration;
    private Vector2D lorentzforce;
    
    //CHART  and series of number in chart
    @FXML
    protected LineChart  PositionYForceChart;
    @FXML
    protected LineChart  PositionYtimeChart;
    XYChart.Series series = new XYChart.Series <Number, Number>();
    XYChart.Series series2 = new XYChart.Series <Number, Number>();
   
  
    
    //check input
    private double lastFrameTime = 0.0;
    protected boolean isStopClicked = false;
    protected boolean isPlayClicked = false;
    protected boolean isPauseClicked = false;
    protected boolean isResetClicked = false;
    protected boolean isHelpClicked = false;
    protected static boolean isExitClicked = false;
    protected boolean isAnimating = false;
       
    // used in time for animation and graphs
    long lastNow;
    long initialTime;   
    long resumeTime;
    long pausedTime;
    AnimationTimer anim ;
    

    
    public void disableInputs()
    {
       
        ConstantMagneticField.setDisable(true);
        Voltage.setDisable(true);
        VelocityXinput.setDisable(true);
 
    }
    
     public void enableInputs()
    {
        
        ConstantMagneticField.setDisable(false);
        Voltage.setDisable(false);
        VelocityXinput.setDisable(false);
        
    }
     
    
    @FXML
    public void Stop(ActionEvent event)
    {
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        resetButton.setDisable(false);
        exitButton.setDisable(false);
        helpButton.setDisable(false);
        disableInputs();
        
        pausedTime += System.nanoTime();
        //difference = System.nanoTime();  
        isPauseClicked = true;  
        anim.stop();  
    }
    
  
    
    @FXML
    public void start(ActionEvent event)
    {
          if (validateEntries())
        {
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            resetButton.setDisable(false); 
            exitButton.setDisable(false);
            helpButton.setDisable(false);
            
            
            resumeTime += System.nanoTime();
            isPlayClicked = true;
            
            VelocityXinput.setDisable(true);
            ConstantMagneticField.setDisable(false);
            Voltage.setDisable(false);
        
            //resets the charts 
            PositionYForceChart.getData().add(series);
            PositionYtimeChart.getData().add(series2);
           
            anim.start();
        }
    }
    
     @FXML
    public void RESET(ActionEvent event)
    {
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        enableInputs();
        c.setLayoutX(40);
        c.setLayoutY(200);
        
        pausedTime = 0;                // difference in seconds
        resumeTime = 0;
        
        messageLabel.setText("");
                
      
        series2.getData().clear();   
        series.getData().clear();       
       
        PositionYForceChart.getData().clear();
        PositionYtimeChart.getData().clear();
    }
    
    
    @FXML
    public void Help(ActionEvent event)
    {       
      messageLabel.setText("Enter the values that are in the range inside of the boxes."
              + "\nIn physics (particularly in electromagnetism) the Lorentz force is the combination of electric and magnetic force on a point charge due to electromagnetic fields."
              + "\n A particle of charge q moving with velocity v in the presence of an electric field E and a magnetic field B experiences a force"
              + "\nhttps://en.wikipedia.org/wiki/Lorentz_force");
            
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        backgroundSimulation.setImage(new Image(fileURL("./assets/images/lorentzforceBackground.jpg")));
        MagneticForceDirection.setImage(new Image(fileURL("./assets/images/arrowLorentz.png")));
        ElectricForceDirection.setImage(new Image(fileURL("./assets/images/arrowLorentz.png")));
        VelocityDirection.setImage(new Image(fileURL("./assets/images/arrowLorentz.png")));
        MagneticFieldDirection.setImage(new Image(fileURL("./assets/images/pointingOutLorentz.png")));
        
        String cssLayout = "-fx-border-color: black;\n" +
                   "-fx-border-insets: 0;\n" +
                   "-fx-border-width: 3;\n" +
                   "-fx-border-style: solid;\n";
      
        topSimulation.setStyle(cssLayout);
        inputPane.setStyle(cssLayout);
        graphPane.setStyle(cssLayout);
        bottomBox.setStyle(cssLayout);

        lastFrameTime = 0.0f;

        anim = new AnimationTimer()
        {
            @Override
            public void handle(long now) 
            {
                double currentTime = (now - initialTime-(resumeTime - pausedTime)) / 1000000000.0;             
                double MagneticField = ConstantMagneticField.getValue();   
                double ElectricField = Voltage.getValue();   
                double VelocityX = Double.parseDouble(VelocityXinput.getText());                   
                double distancebetweenplates = 5;
                
                MagneticForce = new Vector2D(0,((16)*(VelocityX)*MagneticField));              // using 16 as electron charge since number is too big to process  // going down
                ElectricForce =  new Vector2D(0,(-1*16*(ElectricField)/(distancebetweenplates))); //electron charge = 1.60217662*Math.pow(10,-19) //  going up 
                
                double totalForce = ElectricForce.getY() + MagneticForce.getY(); // electric down positive number and mangetic up so positive
                
                // using I replaced as electron mass is too big to process 
                acceleration = new Vector2D(0.0 , totalForce/(91)); // mass of electron = (9.10938356)*(Math.pow(10,-31))
                
                lorentzforce = new Vector2D(totalForce , totalForce );
                
                // position of particle 
                double circlePosY = c.getLayoutY();
                double circlePosX = c.getLayoutX();                                     
                cPosition = new Vector2D(circlePosX, circlePosY);                                        
                
                //  velocity of particle  
                double velocityX = (VelocityX);   // right answer 
                double velocityY = acceleration.getY()*currentTime;   
                cVelocity = new Vector2D(velocityX, velocityY);

                // Euler Integration
                // calculations of position
                Vector2D frameAccelerationReal = acceleration.mul(currentTime);     //  ax = 0                      / ay = (totalForce/mass) * time  
                cVelocity = cVelocity.add(frameAccelerationReal);                   //  vx = Vox(cos(2pi))          / vy = Voy(sin(2pi))+ (at)
                cPosition = (cPosition.add(cVelocity.mul(currentTime)));            //  x = xo + Vox(cos(2pi))(t)   / y = yo + voy(sin(2pi))(t) + (ay)(t)(t)
                
                // Update animation position
                c.setLayoutX(cPosition.getX());
                c.setLayoutY(cPosition.getY());
                
                if (totalForce == 0 )
                {
                    messageLabel.setText("Equilibrium is reached therefor it will never hit the plate");                    
                }
                if (totalForce > 0 )
                {
                    messageLabel.setText("Electric Force is stronger than Magnetic force");
                }
                if (totalForce < 0 )
                {
                    messageLabel.setText("Magnetic Force is stronger than Electric force");
                }     
                
                PositionYtimeChart.setCreateSymbols(false);
                PositionYForceChart.setCreateSymbols(false);
                if (currentTime > 0 )
                {                    
                    XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (-lorentzforce.getY(), (-cPosition.getY()+200));
                    series.getData().add(data); 
                    
                    XYChart.Data<Number, Number> data2 = new XYChart.Data <Number, Number> ( currentTime, (-cPosition.getY()+200));
                    series2.getData().add(data2); 
                    
                }
                

                if ((cPosition.getY() > topSimulation.getPrefHeight()-50 && (cPosition.getX() > 0  || cPosition.getX() < 900)) )
                {
                    messageLabel.setText("Hit bottom plate, Magnetic Force is stronger than the Electric force");
                    anim.stop();
                }
                if(cPosition.getY() < 0+50 && (cPosition.getX() > 0  || cPosition.getX() < 900))
                {
                    messageLabel.setText("Hit top plate, Electric Force is stronger than the Magnetic force");
                    anim.stop();
                }
                if(cPosition.getX() > 1000 )
                {
                     messageLabel.setText("Will not hit the plates ");
                    anim.stop();
                }     
            }           
        };
    }

      public boolean isAllEntered()
    {
        if (VelocityXinput.getText().isEmpty())
        {
            messageLabel.setText("Missing values. Please verify that the Velocity has been entered");
            return false;
        }
        return true;
    }
    
    public boolean isInteger()
    {
        if (VelocityXinput.getText().matches("\\d+"))      
        {
            return true;
        }
        else 
        {
            messageLabel.setText("Invalid values. Please verify that the Velocity is an integers");
            return false;
        }
        
    }
    
     public boolean validateEntries ()
    {
        if ( isAllEntered() && isInteger())
        {
            if (Double.parseDouble(VelocityXinput.getText()) < 0 ||Double.parseDouble(VelocityXinput.getText()) > 15)
            {
                messageLabel.setText("Invalid values. The Velocity  is out of the range");
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