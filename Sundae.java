interface ISundae {
} // Represents a Sundae in an interface

class Scoop implements ISundae { // class Scoop implements ISundae interface
  String flavor; // String variable to the flavor of the scoop

  // constructor for Scoop class
  Scoop(String flavor) {
    this.flavor = flavor; // Initializes flavor variable with the given value
  }
}

class Topping implements ISundae { // class Topping implements ISundae interface
  ISundae inner; // variable to represent the inner layer of the sundae
  String name; // String variable to represent the topping

  // constructor for Topping class
  Topping(ISundae inner, String name) {
    this.inner = inner; // Initializes inner variable with the given layer
    this.name = name; // Initializes name variable with the given topping
  }
}

class ExamplesSundae {
  ExamplesSundae() {
  } // Empty constructor for ExamplesSundae

  ISundae yummyScoop = new Scoop("chocolate"); // creates a scoop of chocolate ice cream

  // adds rainbow sprinkles on top of chocolate scoop
  ISundae yummyTopping1 = new Topping(this.yummyScoop, "rainbow sprinkles");

  // adds caramel topping on top of rainbow sprinkles, on top of chocolate scoop
  ISundae yummyTopping2 = new Topping(this.yummyTopping1, "caramel");

  // adds whipped cream on top of caramel topping, on top of rainbow sprinkles, on top of chocolate scoop (finishes it off)
  // sundae topped with whipped cream, caramel, rainbow sprinkles, and a scoop of chocolate
  ISundae yummy = new Topping(this.yummyTopping2, "whipped cream");


  ISundae noThankYouScoop = new Scoop("vanilla"); // creates a scoop of vanilla ice cream

  // adds chocolate sprinkles on top of vanilla scoop
  ISundae noThankYouTopping1 = new Topping(this.noThankYouScoop, "chocolate sprinkles");

  // adds fudge on top of chocolate sprinkles, on top of vanilla scoop
  ISundae noThankYouTopping2 = new Topping(this.noThankYouTopping1, "fudge");

  // adds plum sauce on top of fudge, on top of chocolate sprinkles, on top of vanilla scoop (finishes it off)
  // sundae topped with plum sauce, fudge, chocolate sprinkles, and a scoop of vanilla
  ISundae noThankYou = new Topping(this.noThankYouTopping2, "plum sauce");
}