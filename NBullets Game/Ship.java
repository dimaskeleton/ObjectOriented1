import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;

// ship class that takes a radius, posn coordinates, velocity, and color
public class Ship {
  int radius = NBullets.shipRadius; // Purpose: Define a constant integer for radius of the ship using a given value from NBullets
  Posn coords; //Purpose: Define variable coords of type Posn to hold the position coordinates of the ship
  double velocity; // Purpose: Define a velocity  to hold the ship's movement speed along the x-axis
  Color color = NBullets.shipColor; // Purpose: color of the ship with a given color from NBullets

  //Constructor
  Ship(Posn coords, double velocity) {
    this.coords = coords;
    this.velocity = velocity;
  }
    /* TEMPLATE
    Fields:
    ... this.radius ...                 -- int
    ... this.coords ...                 -- Posn
    ... this.velocity ...               -- double
    ... this.color ...                  -- Color
    Methods:
    ... this.shipHit(Bullet) ...         -- boolean
    ... this.isOffScreen() ...           -- boolean
    ... this.drawOneShip(WorldScene) ... -- WorldScene
    ... this.moveShip() ...              -- Ship
*/

  // Purpose: method to determine if the ship has been hit by a given bullet
  boolean shipHit(Bullet bullet) {

    // Purpose: Calculates the distance between the ship and the bullet using the Pythagorean theorem
    double distance = Math.hypot(this.coords.x - bullet.coords.x, this.coords.y - bullet.coords.y);

    // Purpose: Return true if the bullet is within the sum of the radius of the ship and the bullet
    // returning collisionDetected as true, otherwise false
    return distance < (this.radius + bullet.radius);
  }

  // Purpose: method to determine if the ship is off the screen frame
  boolean isOffScreen() {
    // Purpose: Returns true if the ship's x-coordinate is beyond the screen's width or negative
    return (this.coords.x - this.radius > NBullets.width) || (this.coords.x + this.radius < 0);
  }

  // Purpose: method to draw the ship on a given WorldScene
  WorldScene drawOneShip(WorldScene ws) {
    // Purpose: Places an image of the ship on the provided WorldScene at the ship's coordinates.
    return ws.placeImageXY(new CircleImage(this.radius, OutlineMode.SOLID, this.color),
      this.coords.x, this.coords.y);
  }

  // Purpose: method to create a new Ship instance that represents the ship's new position after moving
  Ship moveShip() {
    // Purpose: Returns a new Ship object with updated x-coordinate based on
    // the current velocity, keeping the y-coordinate and velocity the same
    return new Ship(new Posn((int) (this.coords.x + this.velocity), this.coords.y), this.velocity);
  }
}