package finalprojectyna;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;

public class GameObject {
    protected Circle circle;
    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D acceleration;
    
    public GameObject(Vector2D position, Vector2D velocity, Vector2D acceleration, double radius)
    {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration; 
        
        circle = new Circle(0.0, 0.0, radius);
        circle.setLayoutX(position.getX());
        circle.setLayoutY(position.getY());
    }
    
    public Vector2D getPosition()
    {
        return position;
    }
    
    public void setPosition(double x, double y)
    {
        position.setX(x);
        position.setY(y);
        circle.setLayoutX(position.getX());
        circle.setLayoutY(position.getY());        
    }
    
    public Circle getCircle()
    {
        return circle;
    }
    
    public void changeSound(AudioClip audio)
    {
        audio.play();
    }
    
    public void update(double dt)
    {
        // Euler Integration
        // Update velocity
        Vector2D frameAcceleration = acceleration.mul(dt);
        velocity = velocity.add(frameAcceleration);

        // Update position
        position = position.add(velocity.mul(dt));
        circle.setLayoutX(position.getX());
        circle.setLayoutY(position.getY());
        
   
        
    }
    
    public void updatePos(double dt)
    {
        // Euler Integration
        // Update velocity
        Vector2D frameAcceleration = acceleration.mul(dt);
        velocity = velocity.add(frameAcceleration);

        // Update position
        position = position.add(velocity.mul(dt));
        circle.setLayoutX(position.getX());
        circle.setLayoutY(position.getY());
        
        // Make it bounce!
        Pane p = (Pane)circle.getParent();
        if (position.getY() > p.getHeight() - circle.getRadius())
        {
            double absVelocityY = Math.abs(velocity.getY());
            absVelocityY *= 0.8;
            velocity.setY(-absVelocityY);
        }
    }
}
