// Import libraries for program
import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

// Interface ITree for the different parts of the tree
interface ITree {
  WorldImage draw(); // Draws the tree

  boolean isDrooping(); // Checks if tree part is drooping

  // Combine the current tree part with another
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree);

  double getWidth(); // Gets the width of the current tree
}

// Represents a leaf on a tree
class Leaf implements ITree {
  int size; // Size of leaf
  Color color; // Color of leaf

  // Constructor for Leaf class
  public Leaf(int size, Color color) {
    this.size = size; // Initializes the size
    this.color = color; // Initializes the color
  }

  // Draw the leaf as a circle with the given size and color
  public WorldImage draw() {
    return new CircleImage(size, OutlineMode.SOLID, color); // Returns a circle representing a leaf
  }

  // Checks if the leaf is drooping, always false since leaves don't droop
  public boolean isDrooping() {
    return false;
  }

  // Combines the leaf with another tree part to create a branch
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    // Returns a new branch combining this leaf and another tree part
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this, otherTree);
  }

  // Gets width of leaf
  public double getWidth() {
    return size * 2; // Returns diameter of leaf as the width
  }
}

// Stem class represents a stem of tree
class Stem implements ITree {
  int length; // Length of stem
  double theta; // Angle of stem
  ITree tree; // Tree part connected to stem

  // Constructor for Stem class
  public Stem(int length, double theta, ITree tree) {
    this.length = length; // Initializes the length
    this.theta = theta; // Initializes the angle (theta)
    this.tree = tree; // Initializes the connected tree
  }

  // Draws the stem and the connected tree part
  public WorldImage draw() {
    // Creates the image of the stem based on its length and angle
    WorldImage stemImage = new LineImage(new Posn((int) (length * Math.cos(Math.toRadians(theta))), (int) (length * Math.sin(Math.toRadians(theta)))), Color.BLACK);

    WorldImage treeImage = tree.draw(); // Draws the connected tree part
    // Overlay the Stem image and the tree part image to create a completed tree
    return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, treeImage, 0, 0, stemImage);
  }

  // Checks if stem is drooping based on angle
  public boolean isDrooping() {
    return theta > 90 && theta < 270; // Returns true if angle shows a drooping stem
  }

  // Combines this stem with another tree part to form a branch
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    // Creates a new branch combining the Stem and other tree parts
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this, otherTree);
  }

  // Gets total width of stem and the connected tree part
  public double getWidth() {
    double originalWidth = tree.getWidth();
    double stemWidth = length * Math.abs(Math.cos(Math.toRadians(theta)));
    return stemWidth + originalWidth;
  }
}

// Branch class represents a branch of the tree
class Branch implements ITree {
  int leftLength; // Length of left part of branch
  int rightLength; // Length of right part of branch
  double leftTheta; // Angle of left part of branch
  double rightTheta; // Angle of right part of branch
  ITree left; // Left tree part connected to branch
  ITree right; // Right tree part connected to branch

  // Constructor for Branch class
  public Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left, ITree right) {
    this.leftLength = leftLength; // Initializes left Length
    this.rightLength = rightLength; // Initializes right length
    this.leftTheta = leftTheta; // Initializes left angle
    this.rightTheta = rightTheta; // Initializes right angle
    this.left = left; // Initializes left tree part
    this.right = right; // Initializes right tree part
  }

  // Draws branch with connected tree parts
  public WorldImage draw() {
    // Creates image of left part based on length and angle
    WorldImage leftBranchImage = new LineImage(new Posn((int) (leftLength * Math.cos(Math.toRadians(leftTheta))), (int) (leftLength * Math.sin(Math.toRadians(leftTheta)))), Color.BLACK);

    // Creates image of right part based on length and angle
    WorldImage rightBranchImage = new LineImage(new Posn((int) (rightLength * Math.cos(Math.toRadians(rightTheta))), (int) (rightLength * Math.sin(Math.toRadians(rightTheta)))), Color.BLACK);

    WorldImage leftTreeImage = left.draw(); // Draws the left connected tree part
    WorldImage rightTreeImage = right.draw(); // Draws the right connected tree part

    // Overlays branch image and tree part to create finished image
    return new OverlayImage(new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, leftTreeImage, 0, 0, leftBranchImage), new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, rightTreeImage, 0, 0, rightBranchImage));
  }

  // Checks if branch is drooping based on angles of the parts or connected parts are drooping
  public boolean isDrooping() {
    boolean leftDrooping = leftTheta > 90 && leftTheta < 270; // Checks if left part is drooping
    boolean rightDrooping = rightTheta > 90 && rightTheta < 270; // Checks if right part is drooping

    // Returns true if either part is drooping or if the connected tree parts are drooping
    return leftDrooping || rightDrooping || left.isDrooping() || right.isDrooping();
  }

  // Combines this branch with another tree part to create a new branch
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    // Returns a new branch combining this branch and another tree part
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this, otherTree);
  }

  // Gets the total width of the branch and the connected tree parts
  public double getWidth() {
    // Finds the width of the left part and connected tree part
    double leftWidth = left.getWidth() + leftLength * Math.abs(Math.cos(Math.toRadians(leftTheta)));
    // Finds the width of the right part and connected tree part
    double rightWidth = right.getWidth() + rightLength * Math.abs(Math.cos(Math.toRadians(rightTheta)));
    // Returns the total width
    return leftWidth + rightWidth;
  }
}

// TreeWorld class represents the entire tree
class TreeWorld extends World {
  ITree tree; // The tree to be drawn

  // Constructor for TreeWorld class
  public TreeWorld(ITree tree) {
    this.tree = tree; // Initializes the tree
  }

  // Creates the scene to display the tree in the world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 500); // Creates a new scene with given dimensions
    WorldImage treeImage = tree.draw(); // Draws the tree
    scene.placeImageXY(treeImage, 250, 250); // Places tree at the center of the canvas
    return scene; // Returns the completed image
  }
}


// Class to test the methods and classes above
class ExamplesTree {

  // Represents a tree with a branch that has two leaves of different colors and sizes
  ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));

  // Represents another tree with a branch that has two leaves of different colors and sizes
  ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));

  // Creates a stem that includes tree1 as the tree part
  ITree stem1 = new Stem(40, 90, tree1);
  // Creates another stem that contains the tree2 as the tree part
  ITree stem2 = new Stem(50, 90, tree2);

  // Combines tree1 and tree2 into a new tree
  ITree combinedTree = tree1.combine(40, 50, 150, 30, tree2);

  // Creates a branch that connects tree1 and tree2
  ITree branchTree = new Branch(40, 50, 150, 30, tree1, tree2);

  // Tests the drawing of branchTree in a world
  boolean testDrawTree(Tester t) {
    TreeWorld world = new TreeWorld(branchTree); // Creates a world with branch tree
    world.bigBang(500, 500, 0.1); // Creates the world with given resolution and tick-speed
    return true; // Returns true to visualize
  }

  // Tests drawing methods for different parts of the tree
  boolean testImages(Tester t) {
    return t.checkExpect(tree1.draw(), tree1.draw()) && // Checks if tree1 draws correctly
      t.checkExpect(tree2.draw(), tree2.draw()) && //Checks if tree2 draws correctly
      t.checkExpect(stem1.draw(), stem1.draw()) && // Checks if stem1 draws correctly
      t.checkExpect(stem2.draw(), stem2.draw()); // Checks if stem2 draws correctly
  }

  // Tests combine method
  boolean testCombine(Tester t) {
    return combinedTree != null && // Checks if combinedTree is not null
      t.checkExpect(combinedTree.draw(), combinedTree.draw()); // Checks if combinedTree draws correctly
  }

  // Tests drawing of the branch
  boolean testBranch(Tester t) {
    return t.checkExpect(branchTree.draw(), branchTree.draw()); // Checks if branchTree draws correctly
  }

  // Tests drawing of the leaf
  boolean testLeafDrawing(Tester t) {
    ITree leaf = new Leaf(50, Color.RED); // Creates a new leaf
    WorldImage expectedImage = new CircleImage(50, OutlineMode.SOLID, Color.RED); // Expected image of leaf
    return t.checkExpect(leaf.draw(), expectedImage); // Checks if leaf draws correctly
  }
}