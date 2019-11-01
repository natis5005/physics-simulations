package finalprojectyna;

public class Crosshair extends GameObject{
    public Crosshair(Vector2D position, double radius)
    {
        super(position, new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0), radius);

        circle.setFill(AssetManager.getCrossHairImage());
    }
    
    public void update(double dt)
    {
        // Make it always on top!
        getCircle().toFront();
    }
}
