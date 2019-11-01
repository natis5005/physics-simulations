/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectyna;


public interface Constants 
{
    //Projectile Motion Constants
    final Vector2D EGRAVITY = new Vector2D (0.0,9.8);
    //Pendulum Constants
    
    //Lorentz force constants
    
    //Generator Constants
    final float MAX_MAGNETIC_FIELD = 6.9f;
    final float RESISTENCE = 10f; 
    
    //Photoelectric effect Constants
    final double ELECTRON_MASS = (9.109 * Math.pow(10, -31));                   // Kg
    final double SPEED_OF_LIGHT = (3* Math.pow(10, 8));                         // m/s
    final double PLANCKS_CONSTANT = (6.626 * Math.pow(10, -34));                // m*Kg/s
    final double ELECTRON_CHARGE = (1.602*Math.pow(10, -19));                   // C
    final double PLATE_SEPARATION = 0.01;                                        //m
    final int QUANTUM_ELEVATION_PT_CU = 2;
    final int QUANTUM_ELEVATION_NA_CA_ZN = 3;
    
    final double SODIUM_WORK_FUNCTION = (2.36 * ELECTRON_CHARGE);                 // J
    final double CALCIUM_WORK_FUNCTION = (2.87 * ELECTRON_CHARGE);
    final double ZINC_WORK_FUNCTION = (3.63 * ELECTRON_CHARGE);
    final double COPPER_WORK_FUNCTION = (4.53 * ELECTRON_CHARGE);
    final double PLATINUM_WORK_FUNCTION = (5.64 * ELECTRON_CHARGE);
    
    final double SODIUM_PEAK_LAMBDA = (196*Math.pow(10, -9));                  //nm
    final double CALCIUM_PEAK_LAMBDA = (180*Math.pow(10, -9));
    final double ZINC_PEAK_LAMBDA = (149*Math.pow(10, -9));
    final double COPPER_PEAK_LAMBDA = (141*Math.pow(10, -9));
    final double PLATINUM_PEAK_LAMBDA = (120*Math.pow(10, -9));
    
    final Vector2D SODIUM_QUANT_EFECT1 = new Vector2D (-20817963.354727,10.947557 );
    final Vector2D SODIUM_QUANT_EFECT2 = new Vector2D (258.326919,-18494162.128308);
    final Vector2D CALCIUM_QUANT_EFECT1 = new Vector2D (-25732765.087531,10.9963);
    final Vector2D CALCIUM_QUANT_EFECT2 = new Vector2D (472.07,-23869113.55);
    final Vector2D ZINC_QUANT_EFECT1 = new Vector2D (-31933616.617868,10.9367);
    final Vector2D ZINC_QUANT_EFECT2 = new Vector2D (12291.93,-48670283.35);
    final Vector2D COPPER_QUANT_EFECT1 = new Vector2D (-39936690.6730712,10.9448);
    final Vector2D COPPER_QUANT_EFECT2 = new Vector2D (32700.42,-58840382.21);
    final Vector2D PLATINUM_QUANT_EFECT1 = new Vector2D (-49564618.08823,10.926);
    final Vector2D PLATINUM_QUANT_EFECT2 = new Vector2D (64850.04,-76599019.40);
    
    
    
    
    
    //Damped Oscillations Constants
}
