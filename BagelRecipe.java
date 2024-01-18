import tester.*;

// A BagelRecipe is the amount of flour, water, yeast, salt, and malt in the recipe
// to represent a BagelRecipe:
public class BagelRecipe {
  // type double to represent the weight of the ingredients as ounces
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;

  // main constructor
  // takes in all the fields and enforces all above constraints to ensure a perfect bagel recipe
  public BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    if (Math.abs(flour - water) > 0.001) {
      throw new IllegalArgumentException("Flour and water weight must be equal.");
    }
    if (Math.abs(yeast - malt) > 0.001) {
      throw new IllegalArgumentException("Yeast and malt weight must be equal.");
    }
    if (Math.abs(flour * 0.05 - (salt + yeast)) > 0.001) {
      throw new IllegalArgumentException("The weight of the salt + yeast should be 1/20th the weight of the flour.");
    }

    this.flour = flour;
    this.water = water;
    this.yeast = yeast;
    this.salt = salt;
    this.malt = malt;
  }

  //  constructor that only requires the weights of flour and yeast
  public BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, flour * 0.05 - yeast, yeast);
  }

  //  constructor that takes in the flour, yeast, and salt as volumes
  public BagelRecipe(double flourVolume, double yeastVolume, double saltVolume) {
    this(flourVolume * 4.25, flourVolume * 4.25, yeastVolume * 5 / 48, saltVolume * 10 / 48, yeastVolume * 5 / 48);
  }

    /* TEMPLATE
   Fields:
   ...this.flour...       -- double
   ...this.water...       -- double
   ...this.yeast...       -- double
   ...this.salt...        -- double
   ...this.malt...        -- double

   Methods:
   ...this.sameRecipe(BagelRecipe other)... -- boolean
    */


  // Method to check if two recipes are the same
  // returns true if the same ingredients have the same weights to within 0.001 ounces.
  public boolean sameRecipe(BagelRecipe other) {
    return Math.abs(this.flour - other.flour) < 0.001
      && Math.abs(this.water - other.water) < 0.001
      && Math.abs(this.yeast - other.yeast) < 0.001
      && Math.abs(this.salt - other.salt) < 0.001
      && Math.abs(this.malt - other.malt) < 0.001;
  }
}

class ExamplesBagelRecipe {
  ExamplesBagelRecipe(){}

  // examples of bagel recipes
  BagelRecipe recipe1 = new BagelRecipe(21, 21, 1, 0.05, 1);
  BagelRecipe recipe2 = new BagelRecipe(21, 1);
  BagelRecipe recipe3 = new BagelRecipe(25, 1.25);
  BagelRecipe recipe4 = new BagelRecipe(5, 0.5);

  // tests for method sameRecipe (which returns true if the same ingredients have the same weights to within 0.001 ounces)
  boolean testSameRecipe1(Tester t) {
    return t.checkExpect(this.recipe1.sameRecipe(this.recipe2), true);
  }

  boolean testSameRecipe2(Tester t) {
    return t.checkExpect(this.recipe1.sameRecipe(this.recipe3), false);
  }

  boolean testSameRecipe3(Tester t) {
    return t.checkExpect(this.recipe1.sameRecipe(this.recipe1), true);
  }

  boolean testSameRecipe4(Tester t) {
    return t.checkExpect(this.recipe3.sameRecipe(this.recipe4), false);
  }

  boolean testSameRecipe5(Tester t) {
    BagelRecipe tempRecipe = new BagelRecipe(21, 21, 1, 0.05, 1);
    return t.checkExpect(this.recipe1.sameRecipe(tempRecipe), true);
  }
}