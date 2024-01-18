import tester.*;

// represent a campus tour
class CampusTour {
  int startTime; //minutes from midnight
  ITourLocation startingLocation; //starting location of tour

  CampusTour(int startTime, ITourLocation startingLocation) {
    this.startTime = startTime;
    this.startingLocation = startingLocation;
  }
/* TEMPLATE
    Fields:
    ...this.startTime... --int
    ...this.startingLocation... --ITourLocation
    Methods:
    ...sameTour(CampusTour other)... --boolean
*/

  // is this tour the same tour as the given one?
  boolean sameTour(CampusTour other) {
    return this.startTime == other.startTime &&
      this.startingLocation.isSameTourLocation(other.startingLocation);
  } //returns false
}

// a spot on the tour
interface ITourLocation {
  boolean isSameTourLocation(ITourLocation other);
}

abstract class ATourLocation implements ITourLocation {
  String speech; // the speech to give at this spot on the tour
  ATourLocation(String speech) {
    this.speech = speech;
  }
}

// the end of the tour
class TourEnd extends ATourLocation {
  ICampusLocation location;
  TourEnd(String speech, ICampusLocation location) {
    super(speech);
    this.location = location;
  }

  /* TEMPLATE
      Fields:
      ...this.location... --ICampusLocation
      Methods:
      ...isSameTourLocation(ITourLocation other)... --boolean
  */
  public boolean isSameTourLocation(ITourLocation other) {
    if (!(other instanceof TourEnd)) return false;
    TourEnd te = (TourEnd) other;
    return this.speech.equals(te.speech) &&
      this.location.isSameCampusLocation(te.location);
  }
}

//a mandatory spot on the tour with the next place to go
class Mandatory extends ATourLocation {
  ICampusLocation location;
  ITourLocation next;
  Mandatory(String speech, ICampusLocation location, ITourLocation next) {
    super(speech);
    this.location = location;
    this.next = next;
  }

  /* TEMPLATE
      Fields:
      ...this.location... --ICampusLocation
      ...this.next... --ITourLocation
      Methods:
      ...isSameTourLocation(ITourLocation other)... --boolean
  */
  public boolean isSameTourLocation(ITourLocation other) {
    if (!(other instanceof Mandatory)) return false;
    Mandatory m = (Mandatory) other;
    return this.speech.equals(m.speech) &&
      this.location.isSameCampusLocation(m.location) &&
      this.next.isSameTourLocation(m.next);
  }
}

// up to the tour guide where to go next
class BranchingTour extends ATourLocation {
  ITourLocation option1;
  ITourLocation option2;
  BranchingTour(String speech, ITourLocation option1, ITourLocation option2) {
    super(speech);
    this.option1 = option1;
    this.option2 = option2;
  }

  /* TEMPLATE
      Fields:
      ...this.option1... --ITourLocation
      ...this.option2... --ITourLocation
      Methods:
      ...isSameTourLocation(ITourLocation other)... --boolean
  */
  public boolean isSameTourLocation(ITourLocation other) {
    if (!(other instanceof BranchingTour)) return false;
    BranchingTour bt = (BranchingTour) other;
    return this.speech.equals(bt.speech) &&
      ((this.option1.isSameTourLocation(bt.option1) && this.option2.isSameTourLocation(bt.option2)) ||
        (this.option1.isSameTourLocation(bt.option2) && this.option2.isSameTourLocation(bt.option1)));
  }
}

// interface for campus location
interface ICampusLocation {
  boolean isSameCampusLocation(ICampusLocation other);
}

// building on campus
class Building implements ICampusLocation {
  String name;
  Address address;
  Building(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  /* TEMPLATE
      Fields:
      ...this.name... --String
      ...this.address... --Address
      Methods:
      ...isSameCampusLocation(ICampusLocation other)... --boolean
  */
  public boolean isSameCampusLocation(ICampusLocation other) {
    if (!(other instanceof Building)) return false;
    Building b = (Building) other;
    return this.name.equals(b.name) && this.address.equals(b.address);
  }
}

// represent an address
class Address {
  String street; // street for the address
  int number; // number of the address
  Address(String street, int number) {
    this.number = number;
    this.street = street;
  }

  /* TEMPLATE
      Fields:
      ...this.street... --String
      ...this.number... --int
      Methods:
      ...equals(Object o)... --boolean
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return number == address.number &&
      street.equals(address.street);
  }
}

// represent a quad on campus
class Quad implements ICampusLocation {
  String name;
  ILoCampusLocation surroundings;

  Quad(String name, ILoCampusLocation surroundings) {
    this.name = name;
    this.surroundings = surroundings;
  }

  /* TEMPLATE
      Fields:
      ...this.name... --String
      ...this.surroundings... --ILoCampusLocation
      Methods:
      ...isSameCampusLocation(ICampusLocation other)... --boolean
  */
  public boolean isSameCampusLocation(ICampusLocation other) {
    if (!(other instanceof Quad)) return false;
    Quad q = (Quad) other;
    return this.name.equals(q.name) &&
      this.surroundings.equals(q.surroundings);
  }
}

// interface of list of campus location
interface ILoCampusLocation {
  boolean equals(ILoCampusLocation other);
}

// empty list
class MtLoCampusLocation implements ILoCampusLocation {
  MtLoCampusLocation(){}

  public boolean equals(ILoCampusLocation other) {
    return other instanceof MtLoCampusLocation;
  }
}

// non-empty list
class ConsLoCampusLocation implements ILoCampusLocation {
  ICampusLocation first;
  ILoCampusLocation rest;

  ConsLoCampusLocation(ICampusLocation first, ILoCampusLocation rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE
      Fields:
      ...this.first... --ICampusLocation
      ...this.rest... --ILoCampusLocation
      Methods:
      ...equals(ILoCampusLocation other)... --boolean
  */
  public boolean equals(ILoCampusLocation other) {
    if (!(other instanceof ConsLoCampusLocation)) return false;
    ConsLoCampusLocation cl = (ConsLoCampusLocation) other;
    return this.first.isSameCampusLocation(cl.first) &&
      this.rest.equals(cl.rest);
  }
}

class ExamplesCampus {
  ExamplesCampus() {}

  // examples
  Address wardplace = new Address("wardplace", 123);
  Address setondrive = new Address("setondrive", 456);
  Building mcnultey = new Building("mcnultey", wardplace);
  Building corrigan = new Building("corrigan", setondrive);

  ILoCampusLocation locList1 = new ConsLoCampusLocation(mcnultey, new MtLoCampusLocation());
  ILoCampusLocation locList2 = new ConsLoCampusLocation(corrigan, new MtLoCampusLocation());

  Quad quad1 = new Quad("Quad1", locList1);
  Quad quad2 = new Quad("Quad2", locList2);

  Mandatory mQuad1 = new Mandatory("Speech for Quad1", quad1, new TourEnd("End", mcnultey));
  Mandatory mQuad2 = new Mandatory("Speech for Quad2", quad2, new TourEnd("End", corrigan));
  Mandatory mBuild1 = new Mandatory("Speech for Building1", mcnultey, new TourEnd("End", mcnultey));
  Mandatory mBuild2 = new Mandatory("Speech for Building2", corrigan, new TourEnd("End", corrigan));

  BranchingTour branchingTour1 = new BranchingTour("Speech1", mQuad1, mBuild1);
  BranchingTour branchingTour2 = new BranchingTour("Speech1", mBuild1, mQuad1);
  BranchingTour branchingTour3 = new BranchingTour("Speech1", mQuad2, mBuild2);
  BranchingTour branchingTour4 = new BranchingTour("Different Speech", mBuild1, mQuad1);

  CampusTour ct1 = new CampusTour(900, branchingTour1);
  CampusTour ct2 = new CampusTour(900, branchingTour2);
  CampusTour ct3 = new CampusTour(900, branchingTour3);

  // tests for same tour
  // Test cases for sameTour method in CampusTour class
  boolean testSameTour1(Tester t) {
    return t.checkExpect(ct1.sameTour(ct2), true);
  }

  boolean testSameTour2(Tester t) {
    return t.checkExpect(ct1.sameTour(ct3), false);
  }

  // Tests for isSameTourLocation method in ITourLocation interface
  boolean testIsSameTourLocation1(Tester t) {
    return t.checkExpect(mQuad1.isSameTourLocation(mBuild2), false);
  }

  boolean testIsSameTourLocation2(Tester t) {
    return t.checkExpect(mQuad1.isSameTourLocation(mQuad1), true);
  }

  // Tests for isSameTourLocation method in TourEnd class
  boolean testIsSameTourLocation3(Tester t) {
    return t.checkExpect(mQuad1.isSameTourLocation(new TourEnd("End", mcnultey)), false);
  }

  boolean testIsSameTourLocation4(Tester t) {
    return t.checkExpect(mQuad1.isSameTourLocation(new TourEnd("End", corrigan)), false);
  }

  // Tests for isSameTourLocation method in Mandatory class
  boolean testIsSameTourLocation5(Tester t) {
    return t.checkExpect(mBuild1.isSameTourLocation(mQuad1), false);
  }

  boolean testIsSameTourLocation6(Tester t) {
    return t.checkExpect(mBuild1.isSameTourLocation(mBuild1), true);
  }

  // Tests for isSameTourLocation method in BranchingTour class
  boolean testIsSameTourLocation7(Tester t) {
    return t.checkExpect(branchingTour1.isSameTourLocation(branchingTour3), false);
  }

  boolean testIsSameTourLocation8(Tester t) {
    return t.checkExpect(branchingTour1.isSameTourLocation(branchingTour2), true);
  }

  boolean testIsSameTourLocation9(Tester t) {
    return t.checkExpect(branchingTour1.isSameTourLocation(branchingTour4), false);
  }
}