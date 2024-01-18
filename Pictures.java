import tester.*;

// interface for pictures
interface IPicture {
  int getWidth(); // Variable to get the width of the picture

  int countShapes(); // variable to count the shapes in the picture

  int comboDepth(); // variable to get the depth of the pictures

  IPicture mirror(); // method to get the mirror of the picture

  String pictureRecipe(int depth); // variable to get the String description of the picture
}

// Represents a single shape
class Shape implements IPicture {
  String kind; // type of shape
  int size; // size of shape

  // Constructor
  Shape(String kind, int size) {
    this.kind = kind; // Initializes the kind variable of the shape
    this.size = size; // Initializes the size variable of the shape
  }

  public int getWidth() {
    return this.size; // Returns the width of the shape
  }

  public int countShapes() {
    return 1; // Returns 1 since a single shape counts as 1
  }

  public int comboDepth() {
    return 0; // Returns 0 because there aren't shape combinations
  }

  public IPicture mirror() {
    return this; // Shape stays unchanged when mirrored
  }

  public String pictureRecipe(int depth) {
    return this.kind; // Returns the kind of shape as the recipe
  }
}

// Represents a combination of shapes
class Combo implements IPicture {
  String name; // name of the combination
  IOperation operation; // operation that combines shapes

  // Constructor
  Combo(String name, IOperation operation) {
    this.name = name; // Initializes the name variable
    this.operation = operation; // Initializes the operation method
  }

  public int getWidth() {
    return this.operation.getWidth(); // Returns the operation to get the width
  }

  public int countShapes() {
    return this.operation.countShapes(); // Returns the operation to count the shapes
  }

  public int comboDepth() {
    return 1 + this.operation.comboDepth(); // Adds 1 to the depth of operation
  }

  public IPicture mirror() {
    return new Combo(this.name, this.operation.mirror()); // Creates a mirrored version of the combination
  }

  public String pictureRecipe(int depth) {
    if (depth == 0) {
      return this.name; // Returns the name if depth is zero
    } else {
      return this.operation.pictureRecipe(depth - 1); // Returns the operation for combinations greater than 0
    }
  }
}

// Interface for operation that is applied to the pictures
interface IOperation {
  int getWidth(); // Variable to get the width after the operation

  int countShapes(); // Variable to count the shapes after the operation

  int comboDepth(); // Variable to get the depth of combinations after the operation

  IOperation mirror(); // Method to get the mirrored version of the operation

  String pictureRecipe(int depth); // Variable to get a String representation of the operation
}

// Represents the scaling operation
class Scale implements IOperation {
  IPicture picture; // The picture that will be scaled

  // Constructor
  Scale(IPicture picture) {
    this.picture = picture; // Initializes the picture method
  }

  public int getWidth() {
    return 2 * this.picture.getWidth(); // Doubles the width of the picture
  }

  public int countShapes() {
    return this.picture.countShapes(); // countShapes stays the same after scaling
  }

  public int comboDepth() {
    return this.picture.comboDepth(); // comboDepth stays the same after scaling
  }

  public IOperation mirror() {
    return new Scale(this.picture.mirror()); // Creates a mirrored version of the scaled picture
  }

  public String pictureRecipe(int depth) {
    return "scale(" + this.picture.pictureRecipe(depth) + ")"; // Returns the recipe for the scaled picture
  }
}

// Represents the Beside operation
class Beside implements IOperation {
  IPicture left; // The left picture
  IPicture right; // The right picture

  // Constructor
  Beside(IPicture left, IPicture right) {
    this.left = left; // Initializes the left picture
    this.right = right; // Initializes the right picture
  }

  public int getWidth() {
    return this.left.getWidth() + this.right.getWidth(); // Sum the widths of the left and right pictures
  }

  public int countShapes() {
    return this.left.countShapes() + this.right.countShapes(); // Sum the shape counted from the left and right pictures
  }

  public int comboDepth() {
    return Math.max(this.left.comboDepth(), this.right.comboDepth()); // Take the maximum depth of the left and right pictures
  }

  public IOperation mirror() {
    return new Beside(this.right.mirror(), this.left.mirror()); // Creates a mirrored picture by swapping left and right
  }

  public String pictureRecipe(int depth) {
    return "beside(" + this.left.pictureRecipe(depth) + ", " + this.right.pictureRecipe(depth) + ")"; // Returns the recipe for the beside picture
  }
}

// Represents an overlay operation
class Overlay implements IOperation {
  IPicture top; // The top picture
  IPicture bottom; // The bottom picture

  // Constructor
  Overlay(IPicture top, IPicture bottom) {
    this.top = top; // Initialize the top picture
    this.bottom = bottom; // Initialize the bottom picture
  }

  public int getWidth() {
    return Math.max(this.top.getWidth(), this.bottom.getWidth()); // Returns the maximum width of the top and bottom pictures
  }

  public int countShapes() {
    return this.top.countShapes() + this.bottom.countShapes(); // Returns the sum of shape counts of the top and bottom pictures
  }

  public int comboDepth() {
    return Math.max(this.top.comboDepth(), this.bottom.comboDepth()); // Returns the maximum depth of the top and bottom pictures
  }

  public IOperation mirror() {
    return new Overlay(this.top.mirror(), this.bottom.mirror()); // Returns a mirrored version of the overlay picture
  }

  public String pictureRecipe(int depth) {
    return "overlay(" + this.top.pictureRecipe(depth) + ", " + this.bottom.pictureRecipe(depth) + ")"; // Returns the recipe for the Overlay on pictures
  }
}


// ExamplesPictures holds the examples and test methods
class ExamplesPicture {
  IPicture circle = new Shape("circle", 20); // A circle with width of 20
  IPicture square = new Shape("square", 30); // A square with the width of 30
  IPicture bigCircle = new Combo("big circle", new Scale(circle)); // A scaled circle
  IPicture squareOnCircle = new Combo("square on circle", new Overlay(square, circle)); // A square overlaying a circle

  // Two combinations of square on big circle and side by side
  IPicture doubledSquareOnCircle = new Combo("doubled square on circle", new Beside(new Combo("", new Overlay(square, bigCircle)), new Combo("", new Overlay(square, bigCircle))));


  // Tests the getWidth method
  boolean testGetWidthCircle(Tester t) {
    return t.checkExpect(this.circle.getWidth(), 20); // Expects width to be 20
  }

  boolean testGetWidthSquare(Tester t) {
    return t.checkExpect(this.square.getWidth(), 30); // Expects width to be 30
  }

  boolean testGetWidthBigCircle(Tester t) {
    return t.checkExpect(this.bigCircle.getWidth(), 40); // Expects width to be 40 after being scaled
  }

  boolean testGetWidthSquareOnCircle(Tester t) {
    return t.checkExpect(this.squareOnCircle.getWidth(), 30); // Expects width to be 30 since square is bigger
  }

  boolean testGetWidthDoubledSquareOnCircle(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.getWidth(), 80); // Expects width to be 80 since combinations are side by side
  }


  // Tests the countShapes method
  boolean testCountShapesCircle(Tester t) {
    return t.checkExpect(this.circle.countShapes(), 1); // Expects 1 shape
  }

  boolean testCountShapesSquare(Tester t) {
    return t.checkExpect(this.square.countShapes(), 1); // Expects 1 shape
  }

  boolean testCountShapesBigCircle(Tester t) {
    return t.checkExpect(this.bigCircle.countShapes(), 1); // Expects 1 shape since scale doesn't affect count
  }

  boolean testCountShapesSquareOnCircle(Tester t) {
    return t.checkExpect(this.squareOnCircle.countShapes(), 2); // Expects 2 shapes since it's a square and circle
  }

  boolean testCountShapesDoubledSquareOnCircle(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.countShapes(), 4); // Expects 4 shapes (2 squares + 2 circles)
  }


  // Tests the comboDepth method
  boolean testComboDepthCircle(Tester t) {
    return t.checkExpect(this.circle.comboDepth(), 0); // Expects depth to be 0 since it's a single shape
  }

  boolean testComboDepthSquare(Tester t) {
    return t.checkExpect(this.square.comboDepth(), 0); // Expects depth to be 0 since it's a single shape
  }

  boolean testComboDepthBigCircle(Tester t) {
    return t.checkExpect(this.bigCircle.comboDepth(), 1); // Expects depth to be 1 since it's 1 combination
  }

  boolean testComboDepthSquareOnCircle(Tester t) {
    return t.checkExpect(this.squareOnCircle.comboDepth(), 1); // Expects depth to be 1 since it's 1 combination
  }

  boolean testComboDepthDoubledSquareOnCircle(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.comboDepth(), 3); // Expects depth to be 3 since there's 3 combinations
  }


  // Tests for the mirrored method
  boolean testMirrorCircle(Tester t) {
    return t.checkExpect(this.circle.mirror(), this.circle); // Expects mirrored circle to remain unchanged
  }

  boolean testMirrorSquare(Tester t) {
    return t.checkExpect(this.square.mirror(), this.square); // Expects mirrored square to remain unchanged
  }

  boolean testMirrorBigCircle(Tester t) {
    return t.checkExpect(this.bigCircle.mirror(), new Combo("big circle", new Scale(this.circle.mirror()))); // Expects mirrored big circle to be a scaled up mirrored circle
  }

  // Tests for the recipe method
  boolean testPictureRecipeCircle(Tester t) {
    return t.checkExpect(this.circle.pictureRecipe(0), "circle"); // Expects the recipe to be "circle" with depth 0
  }

  boolean testPictureRecipeSquare(Tester t) {
    return t.checkExpect(this.square.pictureRecipe(0), "square"); // Expects the recipe to be "square" with depth 0
  }

  boolean testPictureRecipeBigCircle(Tester t) {
    return t.checkExpect(this.bigCircle.pictureRecipe(1), "scale(circle)"); // Expects the recipe to be "scale(circle)" with depth 1
  }

  boolean testPictureRecipeSquareOnCircle(Tester t) {
    return t.checkExpect(this.squareOnCircle.pictureRecipe(1), "overlay(square, circle)"); // Expects the recipe to be "overlay(square, circle)" with depth 1
  }

  // Expects the recipe to be "beside(overlay(square, big circle), overlay(square, big circle))" with depth 2
  boolean testPictureRecipeDoubledSquareOnCircle(Tester t) {
    return t.checkExpect(this.doubledSquareOnCircle.pictureRecipe(2), "beside(overlay(square, big circle), overlay(square, big circle))");
  }
}
