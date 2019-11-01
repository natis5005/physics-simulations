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

/**
 *
 * @author cstuser
 */
public class ProjMotion implements Initializable{
    
    
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
    protected ImageView   VolcanoSimulation;
      
    //INPUTS    
    @FXML
    protected Slider  GravitySlider;
    @FXML
    private TextField VelocityInput;
    @FXML
    private TextField AngleInput;
    @FXML
    private TextField PosYinput;
    @FXML
    protected Button  Mars;
    @FXML
    protected Button  Earth;
    @FXML
    protected Button  Jupiter;
    
    // outputs
    private Vector2D cPosition;
    private Vector2D cVelocity;
    private Vector2D acceleration;
    private Vector2D RealVelocity;
    private Vector2D Range;
    private Vector2D MaxHeight;
    
    //CHART  and series of number in chart
    @FXML
    protected LineChart  PositionTimeChart;
    @FXML
    protected LineChart  VelocityTimeChart;
    @FXML
    protected LineChart  AccelerationTimeChart;
     @FXML
    protected LineChart  positionxTimeChart;
      
    XYChart.Series series = new XYChart.Series <Number, Number>();
    XYChart.Series series2 = new XYChart.Series <Number, Number>();
    XYChart.Series series3 = new XYChart.Series <Number, Number>();
    XYChart.Series series4 = new XYChart.Series <Number, Number>(); 
    
    //check input
    private double lastFrameTime = 0.0;
    protected boolean isStopClicked = false;
    protected boolean isPlayClicked = false;
    protected boolean isPauseClicked = false;
    protected boolean isResetClicked = false;
    protected boolean isHelpClicked = false;
    protected boolean isExitClicked = false;
    protected boolean isAnimating = false;
       
    // used in time for animation and graphs
    long lastNow;
    long initialTime;   
    long resumeTime;
    long pausedTime;
    AnimationTimer anim ;
    
    boolean doItOnce = true;
    double difference = 0.0;                // difference in seconds
    double extraTime = 0.0;
    
    @FXML
    public void Jupiter(ActionEvent event)
    {
    GravitySlider.setValue(24.87);
    messageLabel.setText("Exactly Jupiter gravity = 24.87 ");
    }
    @FXML
    public void Earth(ActionEvent event)
    {
    GravitySlider.setValue(9.807);
    messageLabel.setText("Exactly Earth gravity = 9.807 ");
    }
    @FXML
    public void Mars(ActionEvent event)
    {
    GravitySlider.setValue(3.711);
     messageLabel.setText("Exactly Mars gravity = 3.711 ");
    }
    
    public void disableInputs()
    {
        GravitySlider.setDisable(true);
        VelocityInput.setDisable(true);
        AngleInput.setDisable(true);
        PosYinput.setDisable(true);
        Jupiter.setDisable(true);
        Earth.setDisable(true);
        Mars.setDisable(true);
    }
    
     public void enableInputs()
    {
         GravitySlider.setDisable(false);
        VelocityInput.setDisable(false);
        AngleInput.setDisable(false);
        PosYinput.setDisable(false);
        Jupiter.setDisable(false);
        Earth.setDisable(false);
        Mars.setDisable(false);
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
            disableInputs();
            resumeTime += System.nanoTime();
            isPlayClicked = true;
            
            //resets the charts 
            PositionTimeChart.getData().add(series);
            VelocityTimeChart.getData().add(series2);
            AccelerationTimeChart.getData().add(series3);
            positionxTimeChart.getData().add(series4);
            
            anim.start();
        }
    }
    
       @FXML
    public void Help(ActionEvent event)
    {       
      messageLabel.setText("Enter the values that are in the range inside of the boxes."+
              "\n You will be able to seeProjectile motion is a form of motion experienced by an object or particle(a projectile)"
              + " that is thrown near the Earth's surface and moves along a curved path under the action of gravity only "
              + "\n (in particular, the effects of air resistance are assumed to be negligible)."
              + " This curved path was shown by Galileo to be a parabola."
              + " The study of such motions is called ballistics, and such a trajectory is a ballistic trajectory. "
              + "\n The only force of significance that acts on the object is gravity, which acts downward, thus imparting to the object a downward acceleration."
              + "\n https://en.wikipedia.org/wiki/Projectile_motion ");
    }
    
    @FXML
    protected void handleExit(ActionEvent event) throws IOException 
    {
        anim.stop();
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
            pane.getChildren().setAll(pane2);
    }
    
     @FXML
    public void RESET(ActionEvent event)
    {
        resetButton.setDisable(true);
        pauseButton.setDisable(true);
        startButton.setDisable(false);
        enableInputs();
        c.setLayoutX(40);
        c.setLayoutY(390);
        
        pausedTime = 0;                // difference in seconds
        resumeTime = 0;
        
        messageLabel.setText("");
        
        series.getData().clear();       
        series2.getData().clear();
        series3.getData().clear();
        series4.getData().clear();

        AccelerationTimeChart.getData().clear();
        VelocityTimeChart.getData().clear();
        PositionTimeChart.getData().clear();
        positionxTimeChart.getData().clear();
    }
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
//        PositionTimeChart.getData().add(series);
//        VelocityTimeChart.getData().add(series2);
//        AccelerationTimeChart.getData().add(series3);
//        positionxTimeChart.getData().add(series4);
        
        backgroundSimulation.setImage(new Image(fileURL("./assets/images/ProjectileBackground.jpg")));
        VolcanoSimulation.setImage(new Image(fileURL("./assets/images/Projectile.gif")));
       
        
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
                //Time Calculation
                double currentTime = (now - initialTime-(resumeTime - pausedTime)) / 1000000000.0;
                lastFrameTime = currentTime;
                //System.out.println("now is " + now + " lastNow is " + lastNow  + " difference  is " + difference+ " current time is " + currentTime);
                
                //  position of ball
                double PosY = Double.parseDouble(PosYinput.getText());   
                c.setCenterY(-PosY); // sets initial position
                double circlePosY = c.getLayoutY();
                double circlePosX = c.getLayoutX();                                     // constant always at 10 due to radius of circle
                cPosition = new Vector2D(circlePosX, circlePosY);                       // right answer
                //System.out.println(circlePosX + " " + circlePosY + " position x / position y " );
                
                
                //  angle shot from horizontal
                double degree = Double.parseDouble(AngleInput.getText());               // user sets   
                double radian = Math.toRadians(-degree);                                 // right answer
                //System.out.println(degree + " " + radian + "  degree /  radiann");
                
                
                //  velocity of ball
                String velocity = VelocityInput.getText();  // user sets
                double velocityX = (Double.parseDouble(velocity))*(Math.cos(radian));   // right answer 
                double velocityY = (Double.parseDouble(velocity))*(Math.sin(radian));   // right answer
                cVelocity = new Vector2D(velocityX, velocityY);
                //System.out.println(Math.cos(radian) + "  " + Math.sin(radian)+"  cos(rad) / sin(rad)"); // 
                //System.out.println(velocityX + "  " + velocityY + "  " + velocity + "  velocityX / velocityY /velocity"); //

                // choose strenght of gravity
                acceleration = new Vector2D (0.0,GravitySlider.getValue()); 
                if(GravitySlider.getValue() > 3.5&& GravitySlider.getValue() < 4.5 )
                {
                    messageLabel.setText("Approximately mars gravitational pull");
                }
                if(GravitySlider.getValue() > 9.5 && GravitySlider.getValue() < 10.5 )
                {
                    messageLabel.setText("Approximately earths gravitational pull");
                }        
                if(GravitySlider.getValue() > 24.5 )
                {
                    messageLabel.setText("Approximately Jupiters gravitational pull");
                }               
                //System.out.println(acceleration.getY() +"   acceleration");
                
                // Based on Euler Integration
                // used for graphing 
                RealVelocity = new Vector2D(velocityX, velocityY);
                Vector2D frameAccelerationReal = acceleration.mul(currentTime);      //  ax = 0                      / ay = gravity * time
                RealVelocity = RealVelocity.add(frameAccelerationReal);              //  vx = Vox(cos())             / vy = Voy(sinx)+ (at)
                cPosition = (cPosition.add(RealVelocity.mul(currentTime)));         //  x = xo + Vox(cos(rad))(t)   / y = yo + voy(sin(rad))(t) + (ay)(t)(t)
                               
                // Update animation position
                c.setLayoutX(cPosition.getX());
                c.setLayoutY(cPosition.getY());
                
                // GRAPHS THE VELOCITY / POSITION / acceleration VS TIME
                AccelerationTimeChart.setCreateSymbols(false);
                VelocityTimeChart.setCreateSymbols(false);
                PositionTimeChart.setCreateSymbols(false);
                positionxTimeChart.setCreateSymbols(false);
                if (currentTime > 0 )
                {                    
                    XYChart.Data<Number, Number> data = new XYChart.Data <Number, Number> (currentTime, (-cPosition.getY()+390+PosY)); // -390+390+(20+posy)-20
                    series.getData().add(data);
                    
                    XYChart.Data<Number, Number> data2 = new XYChart.Data <Number, Number> (currentTime, -RealVelocity.getY());
                    series2.getData().add(data2);
                    
                    XYChart.Data<Number, Number> data3 = new XYChart.Data <Number, Number> (currentTime, -acceleration.getY());
                    series3.getData().add(data3);
                    
                    XYChart.Data<Number, Number> data4 = new XYChart.Data <Number, Number> ((cPosition.getX()-40), (-cPosition.getY()+390+PosY));
                    series4.getData().add(data4);
                }
                
                
                VolcanoSimulation.setFitHeight(PosY);
                VolcanoSimulation.setLayoutY(-PosY+400);
                if(PosY == 0){
                    VolcanoSimulation.setOpacity(0);
                    VolcanoSimulation.setFitHeight(1);
                    VolcanoSimulation.setLayoutY(1);
                }
                // hits the ground            
                double HitGround = 0;
                VolcanoSimulation.setOpacity(1);
                if(PosY >= 0){
                    
                    HitGround = topSimulation.getPrefHeight()+PosY-10;
                }
                if( cPosition.getY() > HitGround ) 
                {
                   //System.out.println("\t" + cPosition.getY() );
                    messageLabel.setText("Hit the ground ");
                    this.stop();
                    startButton.setDisable(true);
                    pauseButton.setDisable(true);
                }               
                             
            }
        };        
    }

      public boolean isAllEntered()
    {
        if (AngleInput.getText().isEmpty() || VelocityInput.getText().isEmpty() || PosYinput.getText().isEmpty())
        {
            messageLabel.setText("Missing values. Please verify that the velocity,angle or position in y has been entered");
            return false;
        }
        return true;
    }
    
    public boolean isInteger()
    {
        if (AngleInput.getText().matches("\\d+") && VelocityInput.getText().matches("\\d+") && PosYinput.getText().matches("\\d+"))      
        {
            return true;
        }
        else 
        {
            messageLabel.setText("Invalid values. Please verify that the velocity, angle and initial position in y are integers");
            return false;
        }
        
    }
    
     public boolean validateEntries ()
    {
        if ( isAllEntered() && isInteger())
        {
            if (Integer.parseInt(AngleInput.getText()) < 0 || Integer.parseInt(AngleInput.getText()) > 90 
                ||Integer.parseInt(VelocityInput.getText()) > 30 || Integer.parseInt(VelocityInput.getText()) < 0 
                ||Integer.parseInt(PosYinput.getText()) < 0 || Integer.parseInt(PosYinput.getText()) > 150)
            {
                messageLabel.setText("Invalid values. The angle or velocity is out of the range");
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