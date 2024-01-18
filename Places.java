import tester.*;

//Interface for features
//implemented by restaurant, venue, and shuttleBus
interface IFeature {

  // returns capacity of a venue
  // and returns 0 for everything else
  int getCapacity();

  // returns averageRating of a restaurant
  // and returns 0 for everything else
  double getRating();

  // returns the name of a restaurant
  // and returns null for everything else
  String getRestName();

  // returns the type of a restaurant
  // and returns null for everything else
  String getRestType();

  // returns the destination of the shuttleBus
  Place getBusDest();

  // returns the capacity of all venues at a bus's destination
  int getBusCapCount();

  // returns the number of restaurants at a bus's destination
  int getBusRestCount();

  // returns the number of restaurants in a list of features
  int restCount();

  // the sum of all restaurants' ratings at a bus's destination
  double getBusRatingCounter();
}


// interface for lists of features
// implemented by MtLoFeatures and ConsLoFeatures
interface ILoFeature {

  // returns the sum of all venues' capacities in a list of features
  // as well as those of any bus's destination in said list of features
  int totalCapacity();

  // returns the total number of restaurants in a list of features
  // as well as the total number of restaurants of any bus's destination in said list of features
  int totalRestaurants();

  // calculates the average rating of all restaurants in a list of features
  // and the restaurants of any bus's destination in said list of features
  double foodienessRating();

  // returns the names and types of all restaurants in a list of features
  String resturauntInfo();

  // returns the sum of the ratings of all restaurants in a place
  // and of the destinations of any buses in said place
  double totalRatingCounter();

  int getListType();

  String getBusRestInfoHelper();
}


// class for an empty list of features
class MtLoFeature implements ILoFeature {

  MtLoFeature() {
  }

  public int totalCapacity() {
    return 0;
  }

  public int totalRestaurants() {
    return 0;
  }

  public double foodienessRating() {
    return 0;
  }

  public String resturauntInfo() {
    return null;
  }

  public int getListType() {
    return 0;
  }

  public double totalRatingCounter() {
    return 0;
  }

  public String getBusRestInfoHelper() {
    return "";
  }


}


//class for a non-empty list of features
class ConsLoFeature implements ILoFeature {

  IFeature first;
  ILoFeature rest;

  ConsLoFeature(IFeature first, ILoFeature rest) {
    this.first = first;
    this.rest = rest;
  }


        /*  TEMPLATE
            FIELDS
        this.first  ->  IFeature
        this.rest  ->  ILoFeature
            METHODS
        this.destCapacity()  ->  int
        this.totalCapacity()  ->  int
        this.countRestaurants  ->  int
        this.totalRestaurants()  ->  int
        this.ratingCounter()  ->  double
        this.foodienessRating()  ->  double
        this.restaurantInfo()  ->  String
            METHODS OF FIELDS
        this.first.getCapacity()  ->  int
        this.first.getRating()  ->  double
        this.first.getRestName()  ->  String
        this.first.getRestType()  ->  String
        this.first.getBusDest()  ->  Place
        this.first.getBusCapCount()  ->  int
        this.first.getBusRestCount()  ->  int
        this.first.restCount()  ->  int
        this.first.getBusRatingCounter()  ->  double
         */


  public int totalCapacity() {
    return this.first.getCapacity()
      + this.first.getBusCapCount()
      + this.rest.totalCapacity();
  }

  public int totalRestaurants() {
    return this.first.restCount() + this.first.getBusRestCount() + this.rest.totalRestaurants();
  }

  public double totalRatingCounter() {
    return this.first.getRating()
      + this.first.getBusRatingCounter()
      + this.rest.totalRatingCounter();
  }

  public double foodienessRating() {
    return totalRatingCounter() / totalRestaurants();
  }


  public int getListType() {
    return 1;
  }

  public String getBusRestInfoHelper() {
    if (this.first.getRestName() != "") {
      return this.first.getRestName() +
        this.first.getRestType() +
        ", " +
        this.rest.getBusRestInfoHelper();
    } else if (this.first.getBusDest() != null) {
      return this.first.getBusDest().features.getBusRestInfoHelper();
    } else {
      return "";
    }
  }



  public String resturauntInfo() {
    if (this.first.getRestName() != "") {
      return this.first.getRestName() +
        this.first.getRestType() +
        this.rest.resturauntInfo();
    } else if (this.first.getBusDest() != null) {
      return getBusRestInfoHelper() + this.rest.resturauntInfo();
    } else {
      if (this.rest.getListType() == 1) {
        return ", " + this.rest.resturauntInfo();
      } else {
        return "" + this.rest.resturauntInfo();
      }
    }
  }

}


// class represents a place
//includes a name and list features(restaurant, venue, shuttle bus)
class Place {
  String name; // Name of the place
  ILoFeature features;

  Place(String name, ILoFeature features) {
    this.name = name;
    this.features = features;
  }
}

//class represents a restaurant
//includes the name, type of food served, and the average rating
//which is represented as an integer that equates to a star rating of 1-5
class Restaurant implements IFeature {
  String name;
  String type;
  Double averageRating;

  Restaurant(String name, String type, Double averageRating) {
    this.name = name;
    this.type = type;
    this.averageRating = averageRating;
  }

  public int getCapacity() {
    return 0;
  }

  public double getRating() {
    return this.averageRating;
  }

  public String getRestName() {
    return this.name;
  }

  public String getRestType() {
    return " (" + this.type + ")";
  }

  public Place getBusDest() {
    return null;
  }

  public int getBusCapCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalCapacity();
    }

  }

  public int getBusRestCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRestaurants();
    }
  }

  public int restCount() {
    return 1;
  }

  public double getBusRatingCounter() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRatingCounter();
    }
  }
}


//class represents a venue
//includes the name, type of venue, and maximum capacity
class Venue implements IFeature {
  String name;
  String type;
  int capacity;

  Venue(String name, String type, int capacity) {
    this.name = name;
    this.type = type;
    this.capacity = capacity;
  }

  public int getCapacity() {
    return this.capacity;
  }

  public double getRating() {
    return 0;
  }

  public String getRestName() {
    return "";
  }

  public String getRestType() {
    return "";
  }

  public Place getBusDest() {
    return null;
  }

  public int getBusCapCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalCapacity();
    }
  }

  public int getBusRestCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRestaurants();
    }
  }

  public int restCount() {
    return 0;
  }

  public double getBusRatingCounter() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRatingCounter();
    }
  }
}

// class that represents a shuttle bus
// includes the name and destination of the bus
class ShuttleBus implements IFeature {
  String name;
  Place destination;

  ShuttleBus(String name, Place destination) {
    this.name = name;
    this.destination = destination;
  }

  public int getCapacity() {
    return 0;
  }

  public double getRating() {
    return 0;
  }

  public String getRestName() {
    return "";
  }

  public String getRestType() {
    return "";
  }

  public Place getBusDest() {
    return this.destination;
  }

  public int getBusCapCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalCapacity();
    }

  }

  public int getBusRestCount() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRestaurants();
    }
  }

  public int restCount() {
    return 0;
  }

  public double getBusRatingCounter() {
    if (this.getBusDest() == null) {
      return 0;
    } else {
      return this.getBusDest().features.totalRatingCounter();
    }
  }
}

class ExamplesPlaces {
  Restaurant japocalypse = new Restaurant("Japocalypse", "sushi", 4.1);
  Restaurant starbucks = new Restaurant("Starbucks", "coffee", 4.1);
  Restaurant ottosPizza = new Restaurant("Otto's Pizza", "pizza", 4.2);
  Restaurant dailyCatch = new Restaurant("The Daily Catch", "Sicilian", 4.4);
  Restaurant borderCafe = new Restaurant("Border Cafe", "Tex-Mex", 4.5);

  Venue tdGarden = new Venue("TD Garden", "stadium", 19580);
  Venue bostonCommon = new Venue("Boston Common", "public", 150000);
  Venue harvardStadium = new Venue("Harvard Stadium", "football", 30323);

  Place northEnd = new Place("North End",
    new ConsLoFeature(tdGarden,
      new ConsLoFeature(dailyCatch,
        new MtLoFeature())));

  Place harvard = new Place("Harvard",
    new ConsLoFeature(borderCafe,
      new ConsLoFeature(harvardStadium,
        new MtLoFeature())));

  Place southStation = new Place("South Station",
    new ConsLoFeature(new ShuttleBus("Little Italy Express", northEnd),
      new ConsLoFeature(ottosPizza,
        new ConsLoFeature(new ShuttleBus("Crimson Cruiser", harvard),
          new ConsLoFeature(bostonCommon,
            new MtLoFeature())))));

  Place cambridgeSide = new Place("CambridgeSide Galleria",
    new ConsLoFeature(japocalypse,
      new ConsLoFeature(starbucks,
        new ConsLoFeature(new ShuttleBus("bridge shuttle", southStation),
          new MtLoFeature()))));

  // CHECK EXPECTS--------------------------------------------

  boolean testCambridgeSideGalleria(Tester t) {
    return
      t.checkExpect(this.cambridgeSide.name, "CambridgeSide Galleria") &&
        t.checkExpect(((Restaurant) ((ConsLoFeature) this.cambridgeSide.features).first).name, "Japocalypse") &&
        t.checkExpect(((Restaurant) ((ConsLoFeature) this.cambridgeSide.features).first).averageRating, 4.1);
  }

  // Test the properties of South Station
  boolean testSouthStation(Tester t) {
    return
      t.checkExpect(this.southStation.name, "South Station") &&
        t.checkExpect(((ShuttleBus) ((ConsLoFeature) this.southStation.features).first).name, "Little Italy Express") &&
        t.checkExpect(((ShuttleBus) ((ConsLoFeature) this.southStation.features).first).destination, this.northEnd);
  }

  // Test the properties of North End
  boolean testNorthEnd(Tester t) {
    return
      t.checkExpect(this.northEnd.name, "North End") &&
        t.checkExpect(((Venue) ((ConsLoFeature) this.northEnd.features).first).name, "TD Garden") &&
        t.checkExpect(((Venue) ((ConsLoFeature) this.northEnd.features).first).capacity, 19580);
  }

  // Test the properties of Harvard
  boolean testHarvard(Tester t) {
    return
      t.checkExpect(this.harvard.name, "Harvard") &&
        t.checkExpect(((Restaurant) ((ConsLoFeature) this.harvard.features).first).name, "Border Cafe") &&
        t.checkExpect(((Restaurant) ((ConsLoFeature) this.harvard.features).first).averageRating, 4.5);
  }
  boolean testTotalCapacity(Tester t) {
    return
      t.checkExpect(this.cambridgeSide.features.totalCapacity(), 199903) &&
        t.checkExpect(this.southStation.features.totalCapacity(), 199903) &&
        t.checkExpect(this.northEnd.features.totalCapacity(), 19580) &&
        t.checkExpect(this.harvard.features.totalCapacity(), 30323);
  }
  boolean testFoodienessRating(Tester t) {
    return
      t.checkExpect(this.cambridgeSide.features.foodienessRating(), (4.1 + 4.1 + 4.2) / 3) &&
        t.checkExpect(this.southStation.features.foodienessRating(), (4.2 + 4.4 + 4.5) / 3) &&
        t.checkExpect(this.northEnd.features.foodienessRating(), 4.4) &&
        t.checkExpect(this.harvard.features.foodienessRating(), 4.5);
  }
  boolean testResturauntInfo(Tester t) {
    return
      t.checkExpect(this.cambridgeSide.features.resturauntInfo(), "Japocalypse (sushi), Starbucks (coffee), Otto's Pizza (pizza)") &&
        t.checkExpect(this.southStation.features.resturauntInfo(), "Otto's Pizza (pizza), The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
        t.checkExpect(this.northEnd.features.resturauntInfo(), "The Daily Catch (Sicilian)") &&
        t.checkExpect(this.harvard.features.resturauntInfo(), "Border Cafe (Tex-Mex)");
  }
}