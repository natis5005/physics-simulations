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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

/**
 *
 * @author cstuser
 */
public class MainWindow implements Initializable {
    
    @FXML
    private  Pane pane1;
    
    @FXML 
    protected ImageView backgroundMain;
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    protected Label messageLabel;
    @FXML
    protected static MenuBar menuBar;
   
    @FXML
    protected static Menu mechanics;
    @FXML
    protected static Menu elecAndMagnetism;
    @FXML
    protected static Menu waves;
    @FXML
    protected static MenuItem projMotionItem;
    @FXML
    protected static MenuItem pendulumItem;
    @FXML
    protected static MenuItem lorentzItem;  
    @FXML
    protected static MenuItem faradayItem;
    @FXML
    protected static MenuItem photoEffectItem;
    @FXML
    protected static MenuItem dampedOsciItem;
    
    private double lastFrameTime = 0.0;
    private ArrayList<GameObject> circleList;
    private double mouseX=0.0, mouseY=0.0;
    private Blackhole leftBlackhole = null;
    private Blackhole rightBlackhole = null;
    private Crosshair crosshair = null;
    
    AnimationTimer mainAnimation;
    MediaPlayer musicPlayer;
   
    public void stopMainAnimation()
    {
        mainAnimation.stop();
        musicPlayer.stop();
    }
     @FXML
    protected void Exit(ActionEvent event)
    {
        System.exit(1);
    }
     
    @FXML
    protected void help(ActionEvent event)
    {
        messageLabel.setText("To proceed you must pick a section "
                + "\nof physics on the top of the screen. "
                + "\nHope you have fun!!");
    }
    
    @FXML
    protected void handleProjMotion(ActionEvent event) throws IOException 
    {
            stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLProjMotion.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    
    @FXML
    protected  void handlePendulum(ActionEvent event) throws IOException 
    {
            stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLPendulum.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    
    @FXML
    protected  void handleLorentz (ActionEvent event) throws IOException 
    {
            stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLLorentzforce.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    
    @FXML
    protected  void handleGenerator(ActionEvent event) throws IOException 
    {
            stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLGenerator.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    
    @FXML
    protected  void handlePhotoEffect(ActionEvent event) throws IOException 
    {
        stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLPhotoeffect.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    
    @FXML
    protected  void handleDampedOscillations(ActionEvent event) throws IOException 
    {
            stopMainAnimation();  
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLDampedOsc.fxml"));
            anchorPane.getChildren().setAll(pane);
    }
    @FXML
    public void onMouseMove(MouseEvent event)
    {
        mouseX = event.getX();
        mouseY = event.getY();
        
        crosshair.setPosition(mouseX, mouseY);
    }
    
    @FXML
    public void onMouseClick(MouseEvent event)
    {
        if (event.getButton() == MouseButton.PRIMARY)
        {
            int radius = 50;
            Vector2D mousePos = new Vector2D(mouseX, mouseY);
            Vector2D leftVelocity = mousePos.sub(leftBlackhole.getPosition());
            leftVelocity.setMagnitude(200);
            
            GameObject planet = new Planet( mousePos, 
                                            leftVelocity,
                                            radius);
            circleList.add(planet);
            addToPane(planet.getCircle());

            Vector2D rightVelocity = mousePos.sub(rightBlackhole.getPosition());
            rightVelocity.setMagnitude(200);

            planet = new Planet( rightBlackhole.getPosition(), 
                                 rightVelocity,
                                 radius);
            circleList.add(planet);
            addToPane(planet.getCircle());

            AssetManager.getNewPlanetSound().play();
        }
        
    }
    
    public void addToPane(Node node)
    {
        pane1.getChildren().add(node);
    }
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        AssetManager.preloadAllAssets();
        
        lastFrameTime = 0.0f;
        long initialTime = System.nanoTime();

        
        circleList = new ArrayList<GameObject>();
        
        musicPlayer = new MediaPlayer(AssetManager.getBackgroundMusic());
        musicPlayer.play();
        
     
        backgroundMain.setImage(new Image(fileURL("./assets/images/background.png")));
        
        leftBlackhole = new Blackhole(new Vector2D(200, 400), 200, 50);
        addToPane(leftBlackhole.getCircle());
        
        rightBlackhole = new Blackhole(new Vector2D(1000, 400), 200, -50);
        addToPane(rightBlackhole.getCircle());
        
        crosshair = new Crosshair(new Vector2D(mouseX, mouseY), 40);
        addToPane(crosshair.getCircle());
        
         mainAnimation = new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                // Time calculation                
                double currentTime = (now - initialTime) / 1000000000.0;
                double  frameDeltaTime = currentTime - lastFrameTime;
                lastFrameTime = currentTime;
                
                // Test for collisions
                for (int i=0; i<circleList.size(); ++i)
                {
                    GameObject c1 = circleList.get(i); 
                    for (int j=i+1; j<circleList.size(); ++j)
                    {
                        GameObject c2 = circleList.get(j);
                        double d = Vector2D.distance(c1.getPosition(), c2.getPosition());
                        
                        if (d < c1.getCircle().getRadius() + c2.getCircle().getRadius())
                        {
                            circleList.remove(c1);
                            circleList.remove(c2);
                            pane1.getChildren().remove(c1.getCircle());
                            pane1.getChildren().remove(c2.getCircle());
                            j = circleList.size();
                        }
                    }
                }
                
                
                // Update existing circles
                for (GameObject obj : circleList)
                {
                    obj.update(frameDeltaTime);
                }
                
                leftBlackhole.update(frameDeltaTime);
                rightBlackhole.update(frameDeltaTime);
            }
        };
         mainAnimation.start();
    }    
  
  
}
