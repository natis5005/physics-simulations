package finalprojectyna;

public class Blackhole extends GameObject{
    protected double rotationSpeed;
    
    public Blackhole(Vector2D position, double radius, double rotationSpeed)
    {
        super(position, new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0), radius);
        this.rotationSpeed = rotationSpeed; 

        circle.setFill(AssetManager.getBlackHoleImage());
    }
    
    
    public void update(double dt)
    {
        super.update(dt);
        circle.setRotate(circle.getRotate() + rotationSpeed*dt);
    }
}
