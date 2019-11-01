/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.stage.Stage;

/**
 *
 * @author cstuser
 */
public class FinalProjectYNA extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        Parent root= FXMLLoader.load(getClass().getResource("FXMLMainWindow.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.setHeight(820);
        stage.setWidth(1200);
        stage.setResizable(false);
        stage.show();
        stage.setTitle("Physics Interactive Project!");
     
        
    }
   

    
    
     public static void main(String[] args) 
     {
        launch(args);       
     }
  
    
        
    
}
