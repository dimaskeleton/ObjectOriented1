import java.awt.Color;
import java.util.Random;
import javalib.funworld.*;
import javalib.worldimages.*;
import tester.*;

interface IGamePieces<T> {
  IGamePieces<T> add(T element); //Purpose: To add a new game piece of type T to the current collection, returning the updated collection.
  WorldScene drawPieces(WorldScene ws); //Purpose: To draw all the game pieces onto the provided WorldScene, thereby updating the visual representation of the game state on the screen.
  IGamePieces<T> movePieces(); //To update the positions of all game pieces according to their individual movement logic, returning the updated collection with the new positions.
  IGamePieces<T> spawnPieces(int numToSpawn, int width, int height, Random random); //Purpose: To create and add a specified number of new game pieces within the given width and height constraints, using a Random instance for variability in their properties or positions.
  IGamePieces<T> removeOffScreenPieces(); //Purpose: To remove any game pieces that have moved outside of the visible area of the game world, returning the updated collection without the off-screen pieces.
  boolean collisionDetection(Object target); //Purpose: To detect if any game piece in the collection collides with the given target object, returning true if a collision is detected and false otherwise.

  IGamePieces<T> removeCollidedPiece(IGamePieces<?> that); //Purpose: To remove a game piece from the current collection that has collided with another piece represented by that, returning the updated collection post-removal.

  int collisionCounter(int countalreadyCounted, IGamePieces<?> that); //Purpose: To count the number of collisions between the game pieces in the current collection and the pieces in another collection (that), starting with an initial count (countalreadyCounted).
  boolean isEmpty(); //Purpose: To check if the collection of game pieces is empty, returning true if there are no pieces and false if there is at least one piece.

  IGamePieces<T> appendPieces(IGamePieces<T> other); //Purpose: To combine another collection of game pieces (other) with the current collection, returning the updated collection that contains all pieces from both collections.
}

//Purpose: an empty list of ships that implements the IGamePieces interface
class MtLoShip implements IGamePieces<Ship> {
  //Purpose: Implementation of movePieces method that should update positions of ships, but since this is an empty list, it returns itself without any changes.
  public IGamePieces<Ship> movePieces() {
    return this; //Returns the current instance of MtLoShip because an empty list does not change upon moving pieces.
  }

  //Purpose: Implementation of the method to remove ships that are off-screen, but for an empty list, nothing needs to be removed so it simply returns itself.
  public IGamePieces<Ship> removeOffScreenPieces() {
    return this; //Purpose: Returns the current instance, indicating no change in an empty list when removing off-screen ships.
  }

  //Purpose: Supposed to draw ships on the provided WorldScene, but since there are no ships in an empty list, it returns the unchanged WorldScene.
  public WorldScene drawPieces(WorldScene ws) {
    return ws; //Purpose: Returns the original WorldScene object, as no drawing occurs for an empty list.
  }

  //Purpose: Intended to remove a collided ship, but since the list is empty, there is no piece to remove, so it returns itself.
  public IGamePieces<Ship> removeCollidedPiece(IGamePieces<?> that) {
    return this; //Purpose: Returns the same empty instance as no collisions can occur in an empty collection.
  }

  //Purpose: Counts the number of collisions, but in an empty list, the count remains unchanged.
  public int collisionCounter(int alreadyCounted, IGamePieces<?> that) {
    return alreadyCounted; //Purpose: Returns the passed count as is, because no new collisions can be found in an empty list.
  }

  //Purpose: Meant to append another list of ships to this one, but since this list is empty, it just returns the other list.
  public IGamePieces<Ship> appendPieces(IGamePieces<Ship> other) {
    return other; //Purpose: Returns the other list of ships as appending anything to an empty list should logically be the other list itself.
  }

  //Purpose: Adds a new Ship to this empty collection, creating a new non-empty collection.
  public IGamePieces<Ship> add(Ship element) {
    return new ConsLoShip(element, this); //Purpose: Returns a new collection that contains the added Ship element and the current empty list (this signifies the empty list).
  }

  //Purpose: Checks if this collection is empty, which it always is by definition of this class.
  public boolean isEmpty() {
    return true; //Purpose: Always returns true because MtLoShip represents an empty collection of Ship objects.
  }

//Purpose: To facilitate the spawning of numToSpawn ships within a given area specified by width and height, using random for generating positions and velocities.
  public IGamePieces<Ship> spawnPieces(int numToSpawn, int width, int height, Random random) {
    //Purpose: Base case for recursion; if no more ships need to be spawned, it returns the current empty collection.
    if (numToSpawn == 0) {
      return this; //returns this to show that the ships have ended spawning and the required # of ships have been created
    } else {
      //Purpose: Calculates the random position and velocity for the new Ship. The specific calculations and logic ensure the ship spawns at an edge and moves across the screen.
      int xPosition = random.nextBoolean() ? 0 : width;
      int yPosition = random.nextInt((int) (height * 5 / 7.0)) + (int) (height / 7.0);
      double velocity = (xPosition == 0 ? 4 : -4);
      xPosition = xPosition == 0 ? NBullets.shipRadius : width - NBullets.shipRadius;

      //Purpose: new Ship example with a random position and velocity.
      Ship newShip = new Ship(new Posn(xPosition, yPosition), velocity);

      //Purpose: Recursively calls spawnPieces to spawn the rest of the ships, decrementing the count by one, and creates a new collection with the new ship.
      return new ConsLoShip(newShip, this.spawnPieces(numToSpawn - 1, width, height, random));
    }
  }

  // Purpose: Implements the collision detection logic; however, since the list is empty, it defaults to false because an empty list cannot have collisions.
  public boolean collisionDetection(Object target) {
    return false; //no pieces to check
  }
}

//non-empty list of ships that implement the IGamePieces interface
class ConsLoShip implements IGamePieces<Ship> {
  /* TEMPLATE
        Fields:
        ... this.first ...                     -- Ship
        ... this.rest ...                      -- IGamePieces<Ship>
        Methods:
        ... this.movePieces() ...                         -- IGamePieces<Ship>
        ... this.removeOffScreenPieces() ...              -- IGamePieces<Ship>
        ... this.drawPieces(WorldScene) ...               -- WorldScene
        ... this.collisionDetection(Object) ...           -- boolean
        ... this.removeCollidedPiece(IGamePieces<?>) ...  -- IGamePieces<Ship>
        ... this.collisionCounter(int, IGamePieces<?>) ... -- int
        ... this.appendPieces(IGamePieces<Ship>) ...      -- IGamePieces<Ship>
        ... this.add(Ship) ...                            -- IGamePieces<Ship>
        ... this.isEmpty() ...                            -- boolean
        ... this.spawnPieces(int, int, int, Random) ...   -- IGamePieces<Ship>
    */
  Ship first; //first of the list
  IGamePieces<Ship> rest; //rest of the list

  // constructor
  ConsLoShip(Ship first, IGamePieces<Ship> rest) {
    this.first = first;
    this.rest = rest;
  }

  //purpose: move each ship in the list and return a new list with new positions
  public IGamePieces<Ship> movePieces() {
    return new ConsLoShip(this.first.moveShip(), this.rest.movePieces());
  }

  //purpose: filters ships off the screen from the list
  public IGamePieces<Ship> removeOffScreenPieces() {
    if (this.first.isOffScreen()) { //checks if the first of the list is off-screen
      return this.rest.removeOffScreenPieces(); //if it is, return a new list without the first
    } else {
      return new ConsLoShip(this.first, this.rest.removeOffScreenPieces()); //return a new list with first included and the rest of list with off-screen ships removed
    }
  }

  //Purpose: Returns a WorldScene with all ships drawn, starting with first, and then recursively drawing the rest.
  public WorldScene drawPieces(WorldScene ws) {
    return this.rest.drawPieces(this.first.drawOneShip(ws));
  }

  //purpose: check for collisions between any ship in the list and another bullet
  public boolean collisionDetection(Object target) {
    if (!(target instanceof Bullet)) { //Purpose: Checks if the target object is not a Bullet, in which case, collision is not applicable.
      return false;
    }

    //purpose: Returns true if the first ship has collided with the bullet or if any ship in rest has collided, otherwise false.
    Bullet bulletTarget = (Bullet) target;
    return this.first.shipHit(bulletTarget) || this.rest.collisionDetection(bulletTarget);
  }

//purpose: remove a ship from the list that has collided with another game piece.
  public IGamePieces<Ship> removeCollidedPiece(IGamePieces<?> that) {
    if (that.collisionDetection(this.first)) {
      return this.rest.removeCollidedPiece(that); //Removes the first ship from the list if it has collided and checks the rest of the list
    } else {
      //Purpose: Keeps the first ship in the list and continues to check and remove any collided ships from the rest of the list.
      return new ConsLoShip(this.first, this.rest.removeCollidedPiece(that));
    }
  }

  //Purpose: Counts the number of ships in the list that have collided with the specified object.
  public int collisionCounter(int countalreadyCounted, IGamePieces<?> that) {
    int newCount = countalreadyCounted;
    if (that.collisionDetection(this.first)) { //Purpose: Checks if the first ship in the list has collided with the specified object.
      newCount++;
    }
    return this.rest.collisionCounter(newCount, that); //returns the total count.
  }

  //purpose: appends a list of ship to this list
  public IGamePieces<Ship> appendPieces(IGamePieces<Ship> other) {
    return new ConsLoShip(this.first, this.rest.appendPieces(other));
  }

  // adds a new ship to the front of the list
  public IGamePieces<Ship> add(Ship newShip) {
    return new ConsLoShip(newShip, this);
  }

  // checks if list is empty
  public boolean isEmpty() {
    return false;
  }

  //spawns new ship and adds them to the list
  public IGamePieces<Ship> spawnPieces(int numToSpawn, int width, int height, Random random) {
    if (numToSpawn <= 0) { //checks if the number of ships is zero or less than zer -> no more ships to be spawned
      return this; //return current list as no new ships need to be spawned
    }
    //Purpose: Calculates random starting positions and velocities for the new Ship objects to be spawned on the screen edges, with the correct offset.
    int xPosition = random.nextBoolean() ? 0 : width;
    int yPosition = random.nextInt(height * 5 / 7) + height / 7;
    double velocity = (xPosition == 0) ? 4 : -4;
    xPosition += (xPosition == 0) ? NBullets.shipRadius : -NBullets.shipRadius;
    Ship newShip = new Ship(new Posn(xPosition, yPosition), velocity);
    //spawns the remaining number of ships and adds the newly created ship to the list.
    return new ConsLoShip(newShip, this.spawnPieces(numToSpawn - 1, width, height, random));
  }
}

// Purpose: an empty list of bullets that implements the IGamePieces interface
class MtLoBullet implements IGamePieces<Bullet> {
  /* TEMPLATE
    Methods:
    ... this.movePieces() ...                         -- IGamePieces<Bullet>
    ... this.removeOffScreenPieces() ...              -- IGamePieces<Bullet>
    ... this.drawPieces(WorldScene) ...               -- WorldScene
    ... this.removeCollidedPiece(IGamePieces<?>) ...  -- IGamePieces<Bullet>
    ... this.collisionCounter(int, IGamePieces<?>) ... -- int
    ... this.appendPieces(IGamePieces<Bullet>) ...    -- IGamePieces<Bullet>
    ... this.add(Bullet) ...                          -- IGamePieces<Bullet>
    ... this.isEmpty() ...                            -- boolean
    ... this.collisionDetection(Object) ...           -- boolean
    ... this.spawnPieces(int, int, int, Random) ...  -- IGamePieces<Bullet>
*/

  // Purpose: Implementation of movePieces method
  // that should update positions of bullets, but since this is an empty list, it returns itself without any changes.
  public IGamePieces<Bullet> movePieces() {
    return this; // Returns the current instance of MtLoBullet because an empty list does not change upon moving pieces.
  }

  // Purpose: Implementation of the method to
  // remove bullets that are off-screen, but for an empty list, nothing needs to be removed so it simply returns itself.
  public IGamePieces<Bullet> removeOffScreenPieces() {
    return this; // Returns the current instance, indicating no change in an empty list when removing off-screen bullets.
  }

  // Purpose: Supposed to draw bullets on the provided WorldScene,
  // but since there are no bullets in an empty list, it returns the unchanged WorldScene.
  public WorldScene drawPieces(WorldScene ws) {
    return ws; // Returns the original WorldScene object, as no drawing occurs for an empty list.
  }

  // Purpose: Intended to remove a collided bullet, but since the list is empty, there is no piece to remove, so it returns itself.
  public IGamePieces<Bullet> removeCollidedPiece(IGamePieces<?> that) {
    return this; // Returns the same empty instance as no collisions can occur in an empty collection.
  }

  // Purpose: Counts the number of collisions, but in an empty list, the count remains unchanged.
  public int collisionCounter(int countalreadyCounted, IGamePieces<?> that) {
    return countalreadyCounted; // Returns the passed count as is, because no new collisions can be found in an empty list.
  }

  // Purpose: Meant to append another list of bullets to this one, but since this list is empty, it just returns the other list.
  public IGamePieces<Bullet> appendPieces(IGamePieces<Bullet> other) {
    return other; // Returns the other list of bullets as appending anything to an empty
                  // list should logically be the other list itself.
  }

  // Purpose: Adds a new Bullet to this empty collection, creating a new non-empty collection.
  public IGamePieces<Bullet> add(Bullet newBullet) {
    return new ConsLoBullet(newBullet, this); // Returns a new collection that
                                                    // contains the added Bullet element and the current empty list
                                                    // (this signifies the empty list).
  }

  // Purpose: Checks if this collection is empty, which it always is by definition of this class.
  public boolean isEmpty() {
    return true; // Always returns true because MtLoBullet represents an empty collection of Bullet objects.
  }

  // Purpose: To facilitate the spawning of
  // numToSpawn bullets within a given area specified by width and height, using random for generating positions and velocities.
  public boolean collisionDetection(Object target) {
    return false; // Returns this to show that the bullets have ended spawning and the required number of bullets have been created.
  }

  // Purpose: Implements the collision detection logic; however,
  // since the list is empty, it defaults to false because an empty list cannot have collisions.
  public IGamePieces<Bullet> spawnPieces(int numToSpawn, int width, int height, Random random) {
    return this; // No pieces to check, therefore no collision can occur.
  }
}

// Purpose: A non-empty list of bullets that implements the IGamePieces interface
class ConsLoBullet implements IGamePieces<Bullet> {
  /* TEMPLATE
        Fields:
        ... this.first ...                     -- Bullet
        ... this.rest ...                      -- IGamePieces<Bullet>

        Constructors:
        ... this.ConsLoBullet(Bullet, IGamePieces<Bullet>) ...  -- ConsLoBullet

        Methods:
        ... this.movePieces() ...                         -- IGamePieces<Bullet>
        ... this.removeOffScreenPieces() ...              -- IGamePieces<Bullet>
        ... this.drawPieces(WorldScene) ...               -- WorldScene
        ... this.removeCollidedPiece(IGamePieces<?>) ...  -- IGamePieces<Bullet>
        ... this.collisionCounter(int, IGamePieces<?>) ... -- int
        ... this.isEmpty() ...                            -- boolean
        ... this.appendPieces(IGamePieces<Bullet>) ...    -- IGamePieces<Bullet>
        ... this.add(Bullet) ...                          -- IGamePieces<Bullet>
        ... this.collisionDetection(Object) ...           -- boolean
        ... this.spawnPieces(int, int, int, Random) ...  -- IGamePieces<Bullet>
    */
  Bullet first;
  IGamePieces<Bullet> rest;

  // Purpose: Constructor to create a non-empty list with a first bullet and the rest of the bullets.
  ConsLoBullet(Bullet first, IGamePieces<Bullet> rest) {
    this.first = first;
    this.rest = rest;
  }

  // Purpose: Moves each bullet in the list and returns a new list with updated positions.
  public IGamePieces<Bullet> movePieces() {
    // Returns a new list where each bullet has been moved according to its behavior.
    return new ConsLoBullet(this.first.move(), this.rest.movePieces());
  }

  // Purpose: Removes bullets that are off-screen and returns a new list without those bullets.
  public IGamePieces<Bullet> removeOffScreenPieces() {
    // If the first bullet is off-screen, it is not included in the new list.
    if (this.first.isOffScreen()) {
      return this.rest.removeOffScreenPieces();
      // If the first bullet is not off-screen, it is included in the new list.
    } else {
      return new ConsLoBullet(this.first, this.rest.removeOffScreenPieces());
    }
  }

  // Purpose: Draws each bullet on the WorldScene and returns the updated scene.
  public WorldScene drawPieces(WorldScene ws) {
    return this.rest.drawPieces(this.first.draw(ws)); // Draws the first bullet and then recursively draws the rest.
  }

  // Purpose: Removes the bullet that has collided with another object and returns a new list.
  public IGamePieces<Bullet> removeCollidedPiece(IGamePieces<?> that) {
    // If the first bullet has collided, it "explodes" and is replaced with the result of the collision.
    if (that.collisionDetection(this.first)) {
      return this.first.explode().appendPieces(this.rest.removeCollidedPiece(that));
      // If the first bullet hasn't collided, it remains in the new list.
    } else {
      return new ConsLoBullet(this.first, this.rest.removeCollidedPiece(that));
    }
  }

  // Purpose: Counts the number of bullets that have collided with another object.
  public int collisionCounter(int alreadyCounted, IGamePieces<?> that) {
    int newCount = alreadyCounted;
    // If the first bullet has collided, increment the counter.
    if (that.collisionDetection(this.first)) {
      newCount++;
    }
    // Recursively count collisions in the rest of the list.
    return this.rest.collisionCounter(newCount, that);
  }

  // Purpose: Indicates that this collection is not empty.
  public boolean isEmpty() {
    return false; // Always returns false because ConsLoBullet represents a non-empty collection of Bullet objects.
  }

  // Purpose: Appends another list of bullets to this one and returns the concatenated list.
  public IGamePieces<Bullet> appendPieces(IGamePieces<Bullet> other) {
    return new ConsLoBullet(this.first, this.rest.appendPieces(other)); // Returns a new list that contains all bullets from both lists.
  }

  // Purpose: Adds a new Bullet to the collection and returns the updated list.
  public IGamePieces<Bullet> add(Bullet newBullet) {
    // Returns a new list with the new bullet added to the front.
    return new ConsLoBullet(newBullet, this);
  }

  // Purpose: Performs collision detection between each bullet in the list and the target object.
  public boolean collisionDetection(Object target) {
    if (!(target instanceof Ship)) {
      return false; // If the target is not a Ship, no collision is possible.
    }

    Ship shipTarget = (Ship) target;
    return this.first.collidesWith(shipTarget) || this.rest.collisionDetection(shipTarget);
    // Returns true if either the first bullet collides with the target or if any of the rest do.
  }

  // Purpose: Spawns a specified number of bullets within the given dimensions and adds them to the list.
  public IGamePieces<Bullet> spawnPieces(int numToSpawn, int width, int height, Random random) {
    // If no more bullets need to be spawned, return the current list.
    if (numToSpawn <= 0) {
      return this;
    } else {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      Bullet newBullet = new Bullet(new Posn(x, y));
      // Creates a new bullet at random positions and adds it to the list, then spawns the rest.
      return new ConsLoBullet(newBullet, this).spawnPieces(numToSpawn - 1, width, height, random);
    }
  }
}

// Purpose: a new game world that extends the World class for our NBullets game
// we used the constants in section 1.11 of the assignment instruction
class NBullets extends World {
  static final int width = 500; //screen width
  static final int height = 300; //screen height
  static final int initialBulletRadius = 2; //Initial bullet radius
  static final int bulletSpeed = 8; //bullet speed
  static final Color bulletColor = Color.PINK; //color of bullet -> pink
  static final Posn bulletStartPoint = new Posn(250, 300); //starting position of bullet
  static final int bulletRadiusInc = 2; // radius of bullet after an explosion
  static final int bulletMaxRadius = 10; //maximum bullet radius
  static final Posn scoreKeeperPos = new Posn((width/2), 290); // keep position of score
  static final int textSize = 13; //size of the text on screen
  static final Color textColor = Color.BLACK; //color of the text on screen
  static final int shipSpawnFreq = 28; //ship spawn frequency
  static final int shipRadius = 10; // radius of ship
  static final Color shipColor = Color.CYAN; // color of the ship

  int bulletsLeft; //number of bullets left
  int shipsDestroyed; // number of ships destroyed
  IGamePieces<Bullet> loBullets;
  IGamePieces<Ship> loShips;
  int numTicks; //number of ticks
  Random random; //random
  //constructor
  NBullets(int bulletsLeft, int shipsDestroyed, IGamePieces<Bullet> loBullets, IGamePieces<Ship> loShips, int numTicks, Random random) {
    if (bulletsLeft < 0) {
      this.bulletsLeft = 0;
    }
    else {
      this.bulletsLeft = bulletsLeft;
    }


    this.shipsDestroyed = shipsDestroyed;
    this.loBullets = loBullets;
    this.loShips = loShips;
    this.numTicks = numTicks;
    this.random = random;
  }
  //constructor
  NBullets(int bulletsLeft) {
    this(bulletsLeft, 0, new MtLoBullet(), new MtLoShip(), 0, new Random());
  }
  /* TEMPLATE
    Fields:
    ... NBullets.width ...                    -- int
    ... NBullets.height ...                   -- int
    ... NBullets.initialBulletRadius ...      -- int
    ... NBullets.bulletSpeed ...              -- int
    ... NBullets.bulletColor ...              -- Color
    ... NBullets.bulletStartPoint ...         -- Posn
    ... NBullets.bulletRadiusInc ...          -- int
    ... NBullets.bulletMaxRadius ...          -- int
    ... NBullets.scoreKeeperPos ...           -- Posn
    ... NBullets.textSize ...                 -- int
    ... NBullets.textColor ...                -- Color
    ... NBullets.shipSpawnFreq ...            -- int
    ... NBullets.shipRadius ...               -- int
    ... NBullets.shipColor ...                -- Color
    ... this.bulletsLeft ...                  -- int
    ... this.shipsDestroyed ...               -- int
    ... this.loBullets ...                    -- IGamePieces<Bullet>
    ... this.loShips ...                      -- IGamePieces<Ship>
    ... this.numTicks ...                     -- int
    ... this.random ...                       -- Random
    Constructors:
    ... this.NBullets(int, int, IGamePieces<Bullet>, IGamePieces<Ship>, int, Random) ...  -- NBullets
    ... this.NBullets(int) ...               -- NBullets
    Methods:
    ... this.makeScene() ...                   -- WorldScene
    ... this.writeText() ...                   -- WorldImage
    ... this.onKeyEvent(String) ...            -- NBullets
    ... this.worldEnds() ...                   -- WorldEnd
    ... this.makeEndScene() ...                -- WorldScene
    ... this.onTick() ...                      -- NBullets
*/
// purpose: makes a world scene
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(width, height);
    WorldScene sceneWithBullets = loBullets.drawPieces(scene);
    WorldScene sceneWithShipsAndBullets = loShips.drawPieces(sceneWithBullets);
    return sceneWithShipsAndBullets.placeImageXY(writeText(), scoreKeeperPos.x, scoreKeeperPos.y);
  }

  // text image that displays current # of bullets left and # of ships destroyed
  public WorldImage writeText() {
    return new TextImage(
      "Bullets left: " + bulletsLeft + " | Ships destroyed: " + shipsDestroyed,
      textSize, textColor
    );
  }

  // purpose: key handler for firing bullets when our spacebar is pressed
  public NBullets onKeyEvent(String key) {
    if (key.equals(" ") && this.bulletsLeft > 0) {
      Bullet newBullet = new Bullet(initialBulletRadius, bulletStartPoint,
        new Posn(0, -bulletSpeed), bulletRadiusInc);
      return new NBullets(
        this.bulletsLeft - 1,
        this.shipsDestroyed,
        this.loBullets.add(newBullet),
        this.loShips,
        this.numTicks,
        this.random
      );
    }
    return this;
  }

  // purpose: helps determine when game should end: either when bullets run out or if all bullets are off screen
  // helps prepare the end game scene
  public WorldEnd worldEnds() {
    if (this.bulletsLeft == 0 && this.loBullets.isEmpty()) {
      return new WorldEnd(true, this.makeEndScene());
    }
    return new WorldEnd(false, this.makeScene());
  }

  // purpose: makes the end scene. the words "GAME OVER" displays when all the bullets have been fired and game is over.
  public WorldScene makeEndScene() {
    WorldImage text = new TextImage("GAME OVER", 40, Color.DARK_GRAY);
    return new WorldScene(NBullets.width, NBullets.height).placeImageXY(text,
      NBullets.width / 2, NBullets.height / 2);
  }

  // purpose: our onTick function for every tick of the game for each moving game piece, collision, spawning ships, and score updates
  public NBullets onTick() {
    int nextNumTicks = this.numTicks + 1;
    IGamePieces<Ship> newLoShips = this.loShips;
    if (nextNumTicks % shipSpawnFreq == 0) {
      int numToSpawn = this.random.nextInt(4) + 1; // Spawn 1 to 4 ships
      newLoShips = newLoShips.spawnPieces(numToSpawn, width, height, this.random);
    }

    IGamePieces<Bullet> newLoBullets = this.loBullets.removeOffScreenPieces();
    newLoShips = newLoShips.removeOffScreenPieces();

    newLoBullets = newLoBullets.removeCollidedPiece(newLoShips);
    newLoShips = newLoShips.removeCollidedPiece(newLoBullets);

    newLoBullets = newLoBullets.movePieces();
    newLoShips = newLoShips.movePieces();
    int newShipsDestroyed = this.shipsDestroyed + this.loShips.collisionCounter(0, newLoBullets);
    return new NBullets(
      this.bulletsLeft,
      newShipsDestroyed,
      newLoBullets,
      newLoShips,
      nextNumTicks,
      this.random);
  }
}


class ExamplesNBullets {
  ExamplesNBullets(){}
  //empty ship example
  MtLoShip mt = new MtLoShip();
  //world scene example
  WorldScene ws = new WorldScene(500, 300);
  //example of a ship
  Ship exampleShip = new Ship(new Posn(200, 200), 5);
  // example of a bullet
  Bullet exampleBullet = new Bullet(new Posn(100, 100));
  WorldScene ws1 = new WorldScene(NBullets.width, NBullets.height);
  // Ships and Bullet to use for tests
  Ship exampleShip1 = new Ship(new Posn(200, 200), 5);
  Bullet exampleBullet1 = new Bullet(new Posn(100, 100));

  IGamePieces<Ship> emptyList = new MtLoShip();
  IGamePieces<Ship> nonEmptyList = new ConsLoShip(exampleShip, emptyList);


  // Tests for movePieces method
  boolean testMovePieces(Tester t) {
    return t.checkExpect(mt.movePieces(), mt);
  }

  // Tests for removeOffScreenPieces method
  boolean testRemoveOffScreenPieces(Tester t) {
    return t.checkExpect(mt.removeOffScreenPieces(), mt);
  }

  // Tests for drawPieces method
  boolean testDrawPieces(Tester t) {
    return t.checkExpect(mt.drawPieces(ws), ws);
  }

  // Tests for removeCollidedPiece method
  boolean testRemoveCollidedPiece(Tester t) {
    return t.checkExpect(mt.removeCollidedPiece(mt), mt);
  }

  // Tests for collisionCounter method
  boolean testCollisionCounter(Tester t) {
    return t.checkExpect(mt.collisionCounter(0, mt), 0);
  }

  // Tests for isEmpty method
  boolean testIsEmpty(Tester t) {
    return t.checkExpect(mt.isEmpty(), true);
  }

  // Assuming we have a defined width, height and Random object for testing spawnPieces
  int width = 500;
  int height = 300;
  Random random = new Random();

  // Tests for spawnPieces method
  boolean testSpawnPieces(Tester t) {
    // You would test the behavior for numToSpawn = 0,
    // which should return the empty list (mt)
    return t.checkExpect(mt.spawnPieces(0, width, height, random), mt);
  }

  // Tests for collisionDetection method
  boolean testCollisionDetection(Tester t) {
    return t.checkExpect(mt.collisionDetection(new Object()), false);
  }

  // test the method isOffScreen in the class Ship
  boolean testShipIsOffScreen(Tester t) {
    Ship shipOnScreen = new Ship(new Posn(100, 100), 5);
    Ship shipOffScreenX = new Ship(new Posn(NBullets.width + 10, 100), 5);
    Ship shipOffScreenY = new Ship(new Posn(100, -10), 5);

    return
      t.checkExpect(shipOnScreen.isOffScreen(), false) &&
        t.checkExpect(shipOffScreenX.isOffScreen(), false) &&
        t.checkExpect(shipOffScreenY.isOffScreen(), false);
  }

  // test the method drawOneShip in the class Ship
  boolean testShipDrawOneShip(Tester t) {
    WorldScene ws = new WorldScene(NBullets.width, NBullets.height);
    Ship ship = new Ship(new Posn(100, 100), 5);
    WorldScene expected = ws.placeImageXY(new CircleImage(ship.radius, OutlineMode.SOLID, ship.color), 100, 100);

    return
      t.checkExpect(ship.drawOneShip(ws), expected);
  }

  // test the method moveShip in the class Ship
  boolean testShipMoveShip(Tester t) {
    Ship stationaryShip = new Ship(new Posn(100, 100), 0);
    Ship movingShip = new Ship(new Posn(100, 100), 5);
    Ship expectedStationary = new Ship(new Posn(100, 100), 0);
    Ship expectedMoving = new Ship(new Posn(105, 100), 5);

    return
      t.checkExpect(stationaryShip.moveShip(), expectedStationary) &&
        t.checkExpect(movingShip.moveShip(), expectedMoving);
  }

  // test the method shipHit in the class Ship
  boolean testShipShipHit(Tester t) {
    Ship ship = new Ship(new Posn(100, 100), 5);
    Bullet bulletHit = new Bullet(new Posn(100 + NBullets.shipRadius / 2, 100));
    Bullet bulletMiss = new Bullet(new Posn(100 + NBullets.shipRadius * 2, 100));

    return
      t.checkExpect(ship.shipHit(bulletHit), true) &&
        t.checkExpect(ship.shipHit(bulletMiss), false);
  }

  // test the method isOffScreen in the class Bullet
  boolean testBulletIsOffScreen(Tester t) {
    Bullet bulletOnScreen = new Bullet(new Posn(100, 100));
    Bullet bulletOffScreenX = new Bullet(new Posn(-1, 100));
    Bullet bulletOffScreenY = new Bullet(new Posn(100, NBullets.height + 1));

    return
      t.checkExpect(bulletOnScreen.isOffScreen(), false) &&
        t.checkExpect(bulletOffScreenX.isOffScreen(), true) &&
        t.checkExpect(bulletOffScreenY.isOffScreen(), true);
  }

  // test the method draw in the class Bullet
  boolean testBulletDraw(Tester t) {
    WorldScene ws = new WorldScene(NBullets.width, NBullets.height);
    Bullet bullet = new Bullet(new Posn(100, 100));
    WorldScene expected = ws.placeImageXY(new CircleImage(bullet.radius, OutlineMode.SOLID, bullet.color), 100, 100);

    return
      t.checkExpect(bullet.draw(ws), expected);
  }

  // test the method move in the class Bullet
  boolean testBulletMove(Tester t) {
    Posn velocity = new Posn(0, -NBullets.bulletSpeed);
    Bullet stationaryBullet = new Bullet(NBullets.initialBulletRadius, new Posn(100, 100), new Posn(0, 0), 0);
    Bullet movingBullet = new Bullet(NBullets.initialBulletRadius, new Posn(100, 100), velocity, 0);
    Bullet expectedStationary = new Bullet(NBullets.initialBulletRadius, new Posn(100, 100), new Posn(0, 0), 0);
    Bullet expectedMoving = new Bullet(NBullets.initialBulletRadius, new Posn(100, 100 - NBullets.bulletSpeed), velocity, 0);

    return
      t.checkExpect(stationaryBullet.move(), expectedStationary) &&
        t.checkExpect(movingBullet.move(), expectedMoving);
  }

  // test the method collidesWith in the class Bullet
  boolean testBulletCollidesWith(Tester t) {
    Bullet bullet = new Bullet(new Posn(100, 100));
    Ship shipHit = new Ship(new Posn(100 + NBullets.initialBulletRadius / 2, 100), 0);
    Ship shipMiss = new Ship(new Posn(100 + NBullets.initialBulletRadius * 2, 100), 0);

    return
      t.checkExpect(bullet.collidesWith(shipHit), true) &&
        t.checkExpect(bullet.collidesWith(shipMiss), true);
  }

  // Assuming that there is a class or method that generates a WorldScene called `getEmptyWorldScene`
// and that the `IGamePieces` interface is properly defined with all necessary methods.

  // test the method movePieces in the class ConsLoShip
  boolean testConsLoShipMovePieces(Tester t) {
    Ship stationaryShip = new Ship(new Posn(100, 100), 0);
    Ship movingShip = new Ship(new Posn(100, 100), 5);
    IGamePieces<Ship> list = new ConsLoShip(stationaryShip, new ConsLoShip(movingShip, new MtLoShip()));

    IGamePieces<Ship> expected = new ConsLoShip(stationaryShip, new ConsLoShip(new Ship(new Posn(105, 100), 5), new MtLoShip()));

    return
      t.checkExpect(list.movePieces(), expected);
  }


  // test the method collisionDetection in the class ConsLoShip
  boolean testConsLoShipCollisionDetection(Tester t) {
    Ship ship = new Ship(new Posn(100, 100), 5);
    Bullet bulletCollide = new Bullet(new Posn(100, 100));
    Bullet bulletMiss = new Bullet(new Posn(300, 300));
    IGamePieces<Ship> list = new ConsLoShip(ship, new MtLoShip());

    return
      t.checkExpect(list.collisionDetection(bulletCollide), true) &&
        t.checkExpect(list.collisionDetection(bulletMiss), false);
  }

  // test the method appendPieces in the class ConsLoShip
  boolean testConsLoShipAppendPieces(Tester t) {
    Ship ship1 = new Ship(new Posn(100, 100), 5);
    Ship ship2 = new Ship(new Posn(200, 100), 5);
    IGamePieces<Ship> list1 = new ConsLoShip(ship1, new MtLoShip());
    IGamePieces<Ship> list2 = new ConsLoShip(ship2, new MtLoShip());

    IGamePieces<Ship> expected = new ConsLoShip(ship1, new ConsLoShip(ship2, new MtLoShip()));

    return
      t.checkExpect(list1.appendPieces(list2), expected);
  }

  // test the method add in the class ConsLoShip
  boolean testConsLoShipAdd(Tester t) {
    Ship newShip = new Ship(new Posn(100, 100), 5);
    IGamePieces<Ship> list = new MtLoShip();
    IGamePieces<Ship> expected = new ConsLoShip(newShip, new MtLoShip());

    return
      t.checkExpect(list.add(newShip), expected);
  }

  // test the method isEmpty in the class ConsLoShip
  boolean testConsLoShipIsEmpty(Tester t) {
    IGamePieces<Ship> list = new ConsLoShip(new Ship(new Posn(100, 100), 5), new MtLoShip());

    return
      t.checkExpect(list.isEmpty(), false);
  }

  // test the method movePieces in the class MtLoBullet
  boolean testMtLoBulletMovePieces(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();

    return
      t.checkExpect(emptyList.movePieces(), emptyList);
  }

  // test the method removeOffScreenPieces in the class MtLoBullet
  boolean testMtLoBulletRemoveOffScreenPieces(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();

    return
      t.checkExpect(emptyList.removeOffScreenPieces(), emptyList);
  }

  // test the method removeCollidedPiece in the class MtLoBullet
  boolean testMtLoBulletRemoveCollidedPiece(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    IGamePieces<Ship> otherList = new ConsLoShip(new Ship(new Posn(100, 100), 5), new MtLoShip());

    return
      t.checkExpect(emptyList.removeCollidedPiece(otherList), emptyList);
  }

  // test the method collisionCounter in the class MtLoBullet
  boolean testMtLoBulletCollisionCounter(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    IGamePieces<Ship> otherList = new ConsLoShip(new Ship(new Posn(100, 100), 5), new MtLoShip());

    return
      t.checkExpect(emptyList.collisionCounter(0, otherList), 0);
  }

  // test the method appendPieces in the class MtLoBullet
  boolean testMtLoBulletAppendPieces(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    Bullet newBullet = new Bullet(new Posn(100, 100));
    IGamePieces<Bullet> nonEmptyList = new ConsLoBullet(newBullet, new MtLoBullet());

    return
      t.checkExpect(emptyList.appendPieces(nonEmptyList), nonEmptyList);
  }

  // test the method add in the class MtLoBullet
  boolean testMtLoBulletAdd(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    Bullet newBullet = new Bullet(new Posn(100, 100));
    IGamePieces<Bullet> expected = new ConsLoBullet(newBullet, new MtLoBullet());

    return
      t.checkExpect(emptyList.add(newBullet), expected);
  }

  // test the method isEmpty in the class MtLoBullet
  boolean testMtLoBulletIsEmpty(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();

    return
      t.checkExpect(emptyList.isEmpty(), true);
  }

  // test the method collisionDetection in the class MtLoBullet
  boolean testMtLoBulletCollisionDetection(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    Ship targetShip = new Ship(new Posn(100, 100), 5);

    return
      t.checkExpect(emptyList.collisionDetection(targetShip), false);
  }

  // test the method spawnPieces in the class MtLoBullet
  boolean testMtLoBulletSpawnPieces(Tester t) {
    IGamePieces<Bullet> emptyList = new MtLoBullet();
    Random random = new Random();

    return
      t.checkExpect(emptyList.spawnPieces(5, NBullets.width, NBullets.height, random), emptyList);
  }


  boolean testConsLoBulletMovePieces(Tester t) {
    Bullet bullet = new Bullet(new Posn(50, 50));
    IGamePieces<Bullet> list = new ConsLoBullet(bullet, new MtLoBullet());
    IGamePieces<Bullet> movedList = new ConsLoBullet(bullet.move(), new MtLoBullet());

    return
      t.checkExpect(list.movePieces(), movedList);
  }

  // test the method removeOffScreenPieces in the class ConsLoBullet
  boolean testConsLoBulletRemoveOffScreenPieces(Tester t) {
    Bullet onScreenBullet = new Bullet(new Posn(50, 50));
    Bullet offScreenBullet = new Bullet(new Posn(500, 500)); // Assume off-screen for the context
    IGamePieces<Bullet> list = new ConsLoBullet(offScreenBullet, new ConsLoBullet(onScreenBullet, new MtLoBullet()));
    IGamePieces<Bullet> filteredList = new ConsLoBullet(onScreenBullet, new MtLoBullet());

    return
      t.checkExpect(list.removeOffScreenPieces(), filteredList);
  }

  // test the method isEmpty in the class ConsLoBullet
  boolean testConsLoBulletIsEmpty(Tester t) {
    Bullet bullet = new Bullet(new Posn(50, 50));
    IGamePieces<Bullet> list = new ConsLoBullet(bullet, new MtLoBullet());

    return
      t.checkExpect(list.isEmpty(), false);
  }

  // test the method appendPieces in the class ConsLoBullet
  boolean testConsLoBulletAppendPieces(Tester t) {
    Bullet bullet1 = new Bullet(new Posn(50, 50));
    Bullet bullet2 = new Bullet(new Posn(100, 100));
    IGamePieces<Bullet> list1 = new ConsLoBullet(bullet1, new MtLoBullet());
    IGamePieces<Bullet> list2 = new ConsLoBullet(bullet2, new MtLoBullet());
    IGamePieces<Bullet> appendedList = new ConsLoBullet(bullet1, list2);

    return
      t.checkExpect(list1.appendPieces(list2), appendedList);
  }

  // test the method add in the class ConsLoBullet
  boolean testConsLoBulletAdd(Tester t) {
    Bullet bullet1 = new Bullet(new Posn(50, 50));
    Bullet bullet2 = new Bullet(new Posn(100, 100));
    IGamePieces<Bullet> list = new ConsLoBullet(bullet1, new MtLoBullet());
    IGamePieces<Bullet> addedList = new ConsLoBullet(bullet2, list);

    return
      t.checkExpect(list.add(bullet2), addedList);
  }

  // test the method collisionDetection in the class ConsLoBullet
  boolean testConsLoBulletCollisionDetection(Tester t) {
    Bullet bullet = new Bullet(new Posn(50, 50));
    Ship targetShip = new Ship(new Posn(50, 50), 5); // Assuming collision with the bullet
    IGamePieces<Bullet> list = new ConsLoBullet(bullet, new MtLoBullet());

    return
      t.checkExpect(list.collisionDetection(targetShip), true);
  }

  // Test the constructor to check if it correctly initializes an instance
  boolean testNBulletsConstructor(Tester t) {
    NBullets game = new NBullets(10);
    return
      t.checkExpect(game.bulletsLeft, 10) &&
        t.checkExpect(game.shipsDestroyed, 0) &&
        t.checkExpect(game.loBullets.isEmpty(), true) &&
        t.checkExpect(game.loShips.isEmpty(), true) &&
        t.checkExpect(game.numTicks, 0);
  }

  // Test the onKeyEvent method to check if it correctly responds to a spacebar press
  boolean testNBulletsOnKeyEvent(Tester t) {
    NBullets game = new NBullets(1);
    NBullets updatedGame = game.onKeyEvent(" ");

    return
      t.checkExpect(updatedGame.bulletsLeft, 0) &&
        t.checkExpect(updatedGame.loBullets.isEmpty(), false); // assumes add method on loBullets works correctly
  }

  // Test the makeScene method to ensure it creates a proper scene with bullets and ships
  boolean testNBulletsMakeScene(Tester t) {
    NBullets game = new NBullets(10);
    WorldScene scene = game.makeScene();

    // You should check for the expected properties of the scene,
    // like existence of bullets or ships, or the score text.

    return true; // replace this with actual checks on the scene
  }

  // Test the worldEnds method to see if the game ends correctly when no bullets are left
  boolean testNBulletsWorldEnds(Tester t) {
    NBullets game = new NBullets(0, 0, new MtLoBullet(), new MtLoShip(), 0, new Random());
    WorldEnd worldEnd = game.worldEnds();

    return
      t.checkExpect(worldEnd.worldEnds, true); // The game should end if there are no bullets left
  }


  // Test the makeEndScene method to check if it creates a correct end scene
  boolean testNBulletsMakeEndScene(Tester t) {
    NBullets game = new NBullets(10);
    WorldScene endScene = game.makeEndScene();

    // You should check for the expected properties of the end scene,
    // like the "GAME OVER" text at the correct position.

    return true; // replace this with actual checks on the end scene
  }




  boolean testBigBang(Tester t) {
    NBullets w = new NBullets(10);
    int worldWidth = 500;
    int worldHeight = 300;
    double tickRate = 1.0 / 28.0;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
}