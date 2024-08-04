package shared.constants;


import static shared.constants.Constants.FRAME_DIMENSION;

public class SmileyConstants {

    public static final double a = 20; // Maximum acceleration (pixels/s^2)
    public static final double screenHeight = FRAME_DIMENSION.getHeight(); // Example screen height in pixels
    public static final double yCollision = screenHeight; // Collision point at the bottom of the screen
    public static final double t1 = Math.sqrt(2 * yCollision / a); // Time to reach the collision point
    public static final double vMax = a * t1; // Maximum velocity
    public static final int FPS = 60; // Frames per second
    public static final double dt = 1.0 / FPS; // Time per frame in seconds
    public static final double restitution = 1; // Coefficient of restitution (bounciness)
    public static final double bounceHeightFactor = 0.3; // Factor of the height to bounce back up
    public static final double BULLET_SPEED = 3; // Factor of the height to bounce back up
    public static final double SQUEEZE_DURATION = 5; // Factor of the height to bounce back up
    public static double MAX_SPEED;

}
