package finalprojectyna;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Planet extends GameObject {
    public Planet(Vector2D position, Vector2D velocity, double radius)
    {
        super(position, velocity, new Vector2D(0.0, 9.8), radius);
        

        circle.setFill(AssetManager.getRandomPlanet());
    }
    public void update(double dt)
    {
        super.update(dt);
        
        //Make it Bounce down!
        Pane p = (Pane)circle.getParent();
        
        if (position.getY() > p.getHeight() - circle.getRadius())
        {
            double absVelocityY = Math.abs(velocity.getY());
            absVelocityY *= 1.0;
            velocity.setY(-absVelocityY);
        }
        
        //make it bounce up
        if (position.getY() < 50)
        {
            
            double vel = Math.abs(velocity.getY());
            velocity.setY(vel);
        }
        
        //make it bounce right
        if (position.getX() > p.getWidth() - circle.getRadius())
        {
            double absVelocityY = Math.abs(velocity.getX());
            absVelocityY *= 1.0;
            velocity.setX(-absVelocityY);
        }
        
        //make it bounce left
        if (position.getX() < 50)
        {
            double absVelocityY = Math.abs(velocity.getX());
            absVelocityY *= 1.0;
            velocity.setX(absVelocityY);
        }
    }

}
