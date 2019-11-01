package finalprojectyna;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;

public class AssetManager {
    
    static private ImagePattern generatorBackground = null;    
    
//Mainwindow Assets
    static private Background backgroundImage = null;
    static private ArrayList<ImagePattern> planets = new ArrayList<>();
    static private ImagePattern blackHoleImage = null;
    static private ImagePattern crossHairImage = null;
    
    static private Media backgroundMusic = null;
    static private AudioClip newPlanetSound = null;
    static private AudioClip shootingSound = null;
    
    

    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    static public void preloadAllAssets()
    {
       
        generatorBackground = new ImagePattern(new Image(fileURL("./assets/images/generatorBackground.jpg"))); 
        

        // Preload all images Main Window
        Image background = new Image(fileURL("./assets/images/background.png"));
        BackgroundSize backgroundSize = new BackgroundSize(1200, 800, false, false, true, true);
        
        backgroundImage = new Background(
                            new BackgroundImage(background, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundPosition.DEFAULT,
                                                backgroundSize));
        //Background mainBackground = new Background(backgroundImage);
        
        
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/newton.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/prism.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/atom.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/circuit.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/photoEffectImage.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/newton1.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/energy.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/physicsCollage.jpg"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/physicsCollage1.jpg"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/einstein.jpg"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/keepCalm.jpg"))));
        
        blackHoleImage = new ImagePattern(new Image(fileURL("./assets/images/blackhole.png")));
        crossHairImage = new ImagePattern(new Image(fileURL("./assets/images/crosshair.png")));

        // Preload all music tracks
        backgroundMusic = new Media(fileURL("./assets/music/backgroundMusic.mp3"));

        // Preload all sound effects
        newPlanetSound = new AudioClip(fileURL("./assets/soundfx/newPlanet.wav"));
        shootingSound =  new AudioClip(fileURL("./assets/soundfx/shooting.wav"));
    }
    
    static public Background getBackgroundImage()
    {
        return backgroundImage;        
    }
    
    static public ImagePattern getRandomPlanet()
    {
        Random rng = new Random();
        int i = rng.nextInt(planets.size());
        return planets.get(i);
    }

    static public ImagePattern getBlackHoleImage()
    {
        return blackHoleImage;
    }
    
    static public ImagePattern getGeneratorBackground()
    {
        return generatorBackground;
    }
    
    static public ImagePattern getCrossHairImage()
    {
        return crossHairImage;
    }
    
    static public Media getBackgroundMusic()
    {
        return backgroundMusic;
    }
    
    static public AudioClip getNewPlanetSound()
    {
        return newPlanetSound;
    }
}
