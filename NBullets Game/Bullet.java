import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;

// Bullet class that takes a radius, posn coordinates, velocity, color, and number of explosions
public class Bullet {
  int radius; // Purpose: integer to store the radius of the bullet.
  Posn coords; // Purpose: stores the current coordinates of the bullet.
  Posn velocity; // Purpose: stores the velocity of the bullet in x and y directions.
  Color color = NBullets.bulletColor; // Purpose: bullet's color with a set color from NBullets.
  int numExplosions; // Purpose: integer to count how many times the bullet has exploded.

  //Constructor
  public Bullet(int radius, Posn coords, Posn velocity, int numExplosions) {
    this.radius = radius; // Purpose: Set the bullet's radius
    this.coords = coords; // Purpose: Set the bullet's coordinates
    this.velocity = velocity; // Purpose: Set the bullet's velocity
    this.numExplosions = numExplosions; // Purpose: Set the initial number of explosions the bullet has undergone
  }

  // Purpose: constructor for creating a Bullet  with specific attributes
  public Bullet(Posn coords) {
    // Calls the main constructor with a specified radius,
    // specified coordinates, a velocity pointing upwards, and zero explosions.
    this(NBullets.initialBulletRadius, coords, new Posn(0, -NBullets.bulletSpeed), 0);
  }
    /* TEMPLATE
    Fields:
    ... this.radius ...          -- int
    ... this.coords ...          -- Posn
    ... this.velocity ...        -- Posn
    ... this.color ...           -- Color
    ... this.numExplosions ...   -- int
    Constructors:
    ... this.Bullet(int, Posn, Posn, int) ...  -- Bullet
    ... this.Bullet(Posn) ...                  -- Bullet
    Methods:
    ... this.isOffScreen() ...                 -- boolean
    ... this.draw(WorldScene) ...              -- WorldScene
    ... this.move() ...                        -- Bullet
    ... this.collidesWith(Ship) ...            -- boolean
    ... this.explode() ...                     -- IGamePieces<Bullet>
    ... this.explodeHelper(int) ...            -- IGamePieces<Bullet>
*/

  // Purpose: Method to determine if the bullet is off-screen.
  public boolean isOffScreen() {
    // Purpose: Return true if the bullet's coordinates are outside the boundaries of the screen.
    return this.coords.x < 0 || this.coords.x > NBullets.width ||
      this.coords.y < 0 || this.coords.y > NBullets.height;
  }

  // Purpose: Method to draw the bullet onto the world scene.
  public WorldScene draw(WorldScene ws) {
    // Purpose: Place an image of the bullet onto the provided WorldScene at the bullet's coordinates.
    return ws.placeImageXY(new CircleImage(this.radius, OutlineMode.SOLID, this.color),
      this.coords.x, this.coords.y);
  }

  // Purpose: Method to create a new Bullet representing the bullet's new position after moving.
  public Bullet move() {
    // Purpose: Return a new Bullet object with updated coordinates based on the current velocity.
    return new Bullet(this.radius,
      new Posn(this.coords.x + this.velocity.x, this.coords.y + this.velocity.y),
      this.velocity, this.numExplosions);
  }

  // Purpose: Method to determine if this bullet has collided with a ship.
  public boolean collidesWith(Ship ship) {
    // Purpose: Return true if the given ship is hit by this bullet.
    return ship.shipHit(this);
  }

  // Purpose: Method to show the explosion of the bullet and create resulting bullet fragments.
  public IGamePieces<Bullet> explode() {
    // Purpose: Our Helper method call to handle the explosion logic, increasing the explosion counter.
    return this.explodeHelper(this.numExplosions + 1);
  }

  // Purpose: Helper method that generates new bullets in an explosive pattern.
  private IGamePieces<Bullet> explodeHelper(int modExplosions) {
    // Purpose: Check base case, if no more explosions are to be made, returns an empty list of bullets.
    if (modExplosions <= 0) {
      return new MtLoBullet();
    }

    // Purpose: Calculates the angle for the new bullet direction based on the number of explosions.
    int degrees = modExplosions * (360 / (this.numExplosions + 1));

    // Purpose: Increment the radius for the next bullet, at a max value.
    int nextRadius = this.radius < NBullets.bulletMaxRadius ?
      this.radius + NBullets.bulletRadiusInc : this.radius;

    // Purpose: new Bullet with the incremented radius and adjusted velocity according to the calculated angle.
    Bullet newBullet = new Bullet(nextRadius, this.coords,
      new Posn((int) (NBullets.bulletSpeed * Math.cos(Math.toRadians(degrees))),
        (int) (NBullets.bulletSpeed * Math.sin(Math.toRadians(degrees)))),
      modExplosions);

    // Purpose: a list of new bullets resulting from the explosion, decreasing the explosion mod
    return new ConsLoBullet(newBullet, this.explodeHelper(modExplosions - 1));
  }
}