import tester.*;

class ExamplesBuddies {
  ExamplesBuddies() {
  }

/* EXAMPLES OF CIRCLE OF BUDDIES
Ann's buddies are Bob and Cole
Bob's buddies are Ann and Ed and Hank
Cole's buddy is Dan
Dan's buddy is Cole
Ed's buddy is Fay
Fay's buddies are Ed and Gabi
Gabi's buddies are Ed and Fay
Hank does not have any buddies
Jan's buddies are Kim and Len
Kim's buddies are Jan and Len
Len's buddies are Jan and Kim
*/

  Person ann, bob, cole, dan, ed, fay, gabi, hank, jan, kim, len;

  void initBuddies() { //as per the directions should be called initBuddies and should take in no arguments
    // Examples of Persons
    ann = new Person("Ann");
    bob = new Person("Bob");
    cole = new Person("Cole");
    dan = new Person("Dan");
    ed = new Person("Ed");
    fay = new Person("Fay");
    gabi = new Person("Gabi");
    hank = new Person("Hank");
    jan = new Person("Jan");
    kim = new Person("Kim");
    len = new Person("Len");

    // buddy relationships
    ann.addBuddy(bob);
    ann.addBuddy(cole);
    bob.addBuddy(ann);
    bob.addBuddy(ed);
    bob.addBuddy(hank);
    cole.addBuddy(dan);
    dan.addBuddy(cole);
    ed.addBuddy(fay);
    fay.addBuddy(ed);
    fay.addBuddy(gabi);
    gabi.addBuddy(ed);
    gabi.addBuddy(fay);
    jan.addBuddy(kim);
    jan.addBuddy(len);
    kim.addBuddy(jan);
    kim.addBuddy(len);
    len.addBuddy(jan);
    len.addBuddy(kim);
  }

  // Test for hasDirectBuddy
  boolean testHasDirectBuddy(Tester t) {
    initBuddies();
    return t.checkExpect(ann.hasDirectBuddy(bob), true) && // Ann has direct buddy Bob
      t.checkExpect(bob.hasDirectBuddy(hank), true) && // Bob has direct buddy Hank
      t.checkExpect(cole.hasDirectBuddy(dan), true) && // Cole has direct buddy Dan
      t.checkExpect(hank.hasDirectBuddy(ann), false); // Hank doesn't have direct buddy Ann
  }

  // Test for countCommonBuddies
  boolean testCountCommonBuddies(Tester t) {
    initBuddies();
    return t.checkExpect(ann.countCommonBuddies(bob), 1) && // Common buddies between Ann and Bob
      t.checkExpect(jan.countCommonBuddies(len), 2); // Common buddies between Jan and Len
  }

  // Test for hasExtendedBuddy
  boolean testHasExtendedBuddy(Tester t) {
    initBuddies();
    return t.checkExpect(ann.hasExtendedBuddy(dan), true) && // Ann has extended buddy Dan
      t.checkExpect(bob.hasExtendedBuddy(gabi), true) && // Bob has extended buddy Gabi
      t.checkExpect(jan.hasExtendedBuddy(hank), false); // Jan doesn't have extended buddy Hank
  }

  // Test for partyCount
  boolean testPartyCount(Tester t) {
    initBuddies();
    return t.checkExpect(ann.partyCount(), 4) && // Ann, Bob, Cole, Dan
      t.checkExpect(jan.partyCount(), 3); // Jan, Kim, Len
  }

  // Test for maxLikelihood
  boolean testMaxLikelihood(Tester t) {
    Person a = new Person("A", 0.95, 0.8);
    Person b = new Person("B", 0.85, 0.99);
    Person c = new Person("C", 0.95, 0.9);
    Person d = new Person("D", 1.0, 0.95);

    a.addBuddy(b);
    a.addBuddy(c);
    b.addBuddy(d);
    c.addBuddy(d);

    double result = a.maxLikelihood(d);
    double roundedResult = Math.round(result * 1000) / 1000.0;
    double roundedExpected = 0.772; // Expected value rounded to three decimal places

    return t.checkExpect(roundedResult, roundedExpected); // max likelihood from A to D through buddies B and C
  }
}