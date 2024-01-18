import tester.*;

interface LoRoadTripChunks {}

class MtLoRoadTripChunks implements LoRoadTripChunks {
  MtLoRoadTripChunks() {
  }
}

class ConsLoRoadTripChunks implements LoRoadTripChunks {
  RoadTripChunk first;
  LoRoadTripChunks rest;

  ConsLoRoadTripChunks(RoadTripChunk first, LoRoadTripChunks rest) {
    this.first = first;
    this.rest = rest;
  }
        /*
        TEMPLATE:
            Fields:
                this.first -- RoadTripChunk
                this.rest -- LoRoadTripChunks
            Methods:
            --
            Methods of Fields:
            --
         */
}

interface LoDirections {
  LoDirections finishChunkList(int interval, String driver, int traveled);

  LoDirections makeChunkList(int interval, String driver, int traveled, Direction lastDirRemainder);

  Direction getFirst();

  LoLoDirections makeLoChunkList(int interval, String driver1, String driver2, int traveled, Direction lastDirRemainder);

  Direction makeChunkListFoR(int interval, int traveled);

  LoDirections vRest(int interval, int traveled);
}

interface LoLoDirections {
  public LoRoadTripChunks splitUp(String driver1, String driver2);
}

// list containing a first LoDirections followed by a LoLoDirections
class ConsLoLoDirections implements LoLoDirections {
  LoDirections first;
  LoLoDirections rest;

  ConsLoLoDirections(LoDirections first, LoLoDirections rest) {
    this.first = first;
    this.rest = rest;
  }

  // pairs the correct driver with its corresponding LoDirections to form a RoadTripChunk
  // gets called recursively to create a LoRoadTripChunks for splitUpTrip
  public LoRoadTripChunks splitUp(String driver1, String driver2) {
    return new ConsLoRoadTripChunks(new RoadTripChunk(driver1, this.first), this.rest.splitUp(driver2, driver1));
  }
        /*
        TEMPLATE:
            Fields:
                this.first -- LoDirections
                this.rest -- LoLoDirections
            Methods:
                splitUp -- LoRoadTripChunks
            Methods of Fields:
                this.first.getFirst -- Direction
                this.first.checkDirection -- Direction
                this.first.makeChunkListFoRHelper -- Direction
                this.first.makeChunkListFoR -- Direction
                this.first.vRest -- LoDirections
                this.first.startChunkList -- Direction
                this.first.finishChunkListHelper -- LoDirections
                this.first.finishChunkList -- LoDirections
                this.first.checkRestHelper -- LoDirections
                this.first.checkRest -- LoDirections
                this.first.makeChunkList -- LoDirections
                this.first.makeLoChunkList -- LoLoDirections
                this.rest.splitUp -- LoRoadTripChunks
         */
}

class MtLoLoDirections implements LoLoDirections {
  public int getRest() {
    return 0;
  }

  @Override
  public LoRoadTripChunks splitUp(String driver1, String driver2) {
    return new MtLoRoadTripChunks();
  }
            /*
            TEMPLATE:
                Fields:
                --
                Methods:
                splitUp -- LoRoadTripChunks
                Methods of Fields:
                --
             */
}

class Direction {
  String description;
  int miles;

  Direction(String description, int miles) {
    this.description = description;
    this.miles = miles;
  }

  // returns the miles of a direction
  int getMiles() {
    return this.miles;
  }

  //returns the description of a direction
  String getDescription() {
    return this.description;
  }
            /*
            TEMPLATE:
                Fields:
                    this.description -- String
                    this.miles -- int
                Methods:
                    getMiles -- int
                    getDescription -- String
                Methods of Fields:
                --
             */
}

class MtLoDirections implements LoDirections {
  MtLoDirections() {
  }

  @Override
  public LoLoDirections makeLoChunkList
    (int interval, String driver1, String driver2, int traveled, Direction lastDirRemainder) {
    return new MtLoLoDirections();
  }

  @Override
  public Direction makeChunkListFoR(int interval, int traveled) {
    return null;
  }

  public LoDirections vRest(int interval, int traveled) {
    return new MtLoDirections();
  }

  public LoDirections finishChunkList(int interval, String driver, int traveled) {
    return new MtLoDirections();
  }

  public LoDirections makeChunkList(int interval, String driver, int traveled, Direction lastDirRemainder) {
    return new MtLoDirections();
  }

  @Override
  public Direction getFirst() {
    return null;
  }
            /*
            TEMPLATE:
                Fields:
                --
                Methods:
                    getFirst -- Direction
                    makeChunkListFoR -- Direction
                    vRest -- LoDirections
                    finishChunkList -- LoDirections
                    makeChunkList -- LoDirections
                    makeLoChunkList -- LoLoDirections
                Methods of Fields:
                --
             */
}

// LoDirections that contains a first direction and a following LoDirections
class ConsLoDirections implements LoDirections {
  Direction first;
  LoDirections rest;

  ConsLoDirections(Direction first, LoDirections rest) {
    this.first = first;
    this.rest = rest;
  }

  public Direction getFirst() {
    return this.first;
  }

  // Returns a direction
  // the direction will be added as the next item in a LoDirections
  public Direction checkDirection(int interval, String driver, int traveled) {
    if (this.first.getMiles() + traveled <= interval) {
      return this.first;
    } else {
      return new Direction
        ("Switch with " + driver, interval - traveled);
    }
  }

  // Determines what lastDirRemainder will contain in the recursive call
  // of MakeLoChunkList in MakeLoChunkList
  public Direction makeChunkListFoRHelper(int interval, String driver, int traveled, Direction lastDirRemainder) {
    if (this.first.description == "Switch with " + driver) {
      return new Direction(lastDirRemainder.getDescription(), lastDirRemainder.getMiles() - interval);
    } else {
      return makeChunkListFoR(interval, traveled);
    }
  }

  // Determines what lastDirRemainder will contain in the recursive call
  // of MakeLoChunkList in MakeLoChunkList
  public Direction makeChunkListFoR(int interval, int traveled) {
    if (this.first.getMiles() + traveled < interval) {
      return this.rest.makeChunkListFoR(interval, traveled + this.first.getMiles());
    } else if (this.first.getMiles() + traveled == interval) {
      return null;
    } else {
      return new Direction(this.first.getDescription(), (this.first.getMiles() - (interval - traveled)));
    }
  }

  // determines the remainder of the original list that still needs to be traversed
  // after MakeChunkList is called in MakeLoChunkList
  public LoDirections vRest(int interval, int traveled) {
    if (this.first.getMiles() + traveled < interval) {
      return this.rest.vRest(interval, traveled + this.first.getMiles());
    } else if (this.first.getMiles() + traveled > interval) {
      return new ConsLoDirections(new Direction(this.first.getDescription(), this.first.getMiles() - (interval - traveled)), this.rest);
    } else {
      return this.rest;
    }
  }

  // returns the first in a ConsLoDirections for MakeChunkList
  // assists in determining what direction is returned
  public Direction startChunkList(int interval, String driver, int traveled, Direction lastDirRemainder) {
    if (traveled > interval) {
      if (lastDirRemainder.getMiles() <= interval) {
        return lastDirRemainder;
      } else {
        return new Direction
          ("Switch with " + driver, interval - traveled);
      }
    } else {
      return checkDirection(interval, driver, traveled);
    }
  }

  // assists in calling the correct method to construct the rest of a LoDirections for makeChunkList
  // assists in determining what direction will be added to the list and whether to continue or cap the list
  public LoDirections finishChunkListHelper(int interval, String driver, int traveled) {
    if (checkDirection(interval, driver, traveled) != first) {
      return new MtLoDirections();
    } else {
      return this.rest.finishChunkList
        (interval, driver, traveled +
          this.checkDirection(interval, driver, traveled).getMiles());
    }
  }

  // assists in calling the correct method to construct the rest of a LoDirections for makeChunkList
  // determines what direction will be added to the list and whether to continue or cap the list
  public LoDirections finishChunkList(int interval, String driver, int traveled) {
    if (traveled >= interval) {
      return new ConsLoDirections
        (new Direction("Switch with " + driver, interval - traveled),
          new MtLoDirections());
    } else {
      return new ConsLoDirections
        (checkDirection(interval, driver, traveled), finishChunkListHelper(interval, driver, traveled));
    }
  }

  // assists in calling the correct method to construct the rest of a LoDirections for makeChunkList
  public LoDirections checkRestHelper(int interval, String driver, int traveled, Direction lastDirRemainder) {
    if (this.startChunkList(interval, driver, traveled, lastDirRemainder) != this.first &&
      this.startChunkList(interval, driver, traveled, lastDirRemainder) != lastDirRemainder) {
      return new MtLoDirections();
    } else if (this.finishChunkList
      (interval, driver, startChunkList(interval, driver, traveled, lastDirRemainder).getMiles()).getFirst()
      == this.startChunkList(interval, driver, traveled, lastDirRemainder)) {
      return this.rest.finishChunkList
        (interval, driver, startChunkList(interval, driver, traveled, lastDirRemainder).getMiles());
    } else {
      return checkRest
        (interval, driver,
          startChunkList(interval, driver, traveled, lastDirRemainder).getMiles(),
          lastDirRemainder);
    }
  }

  // assists in calling the correct method to construct the rest of a LoDirections for makeChunkList
  public LoDirections checkRest(int interval, String driver, int traveled, Direction lastDirectionRemainder) {
    if (lastDirectionRemainder == null) {
      return this.rest.finishChunkList(interval, driver, traveled);
    } else {
      return finishChunkList(interval, driver, traveled);
    }
  }

  // takes a ConsLoDirections and returns a smaller list that has a total mileage equal to interval
  public LoDirections makeChunkList(int interval, String driver, int traveled, Direction lastDirRemainder) {
    return new ConsLoDirections
      (startChunkList(interval, driver, traveled, lastDirRemainder),
        checkRestHelper(interval, driver, traveled, lastDirRemainder));
  }

  // takes a ConsLoDirections and breaks it up into several lists based on number of miles (interval)
  public LoLoDirections makeLoChunkList(int interval, String driver1, String driver2, int traveled, Direction lastDirRemainder) {
    return new ConsLoLoDirections(makeChunkList
      (interval, driver2, traveled, lastDirRemainder),
      vRest(interval, 0).makeLoChunkList(interval, driver2, driver1, traveled,
        makeChunkListFoRHelper(interval, driver2, 0, lastDirRemainder)));
  }
            /*
            TEMPLATE:
                Fields:
                    this.first -- Direction
                    this.rest -- LoDirections
                Methods:
                    getFirst -- Direction
                    checkDirection -- Direction
                    makeChunkListFoRHelper -- Direction
                    makeChunkListFoR -- Direction
                    vRest -- LoDirections
                    startChunkList -- Direction
                    finishChunkListHelper -- LoDirections
                    finishChunkList -- LoDirections
                    checkRestHelper -- LoDirections
                    checkRest -- LoDirections
                    makeChunkList -- LoDirections
                    makeLoChunkList -- LoLoDirections
                Methods of Fields:
                    this.first.getDescription -- String
                    this.first.getMiles -- int
             */
}

class RoadTrip {
  String driver1;
  String driver2;
  LoDirections directions;

  RoadTrip(String driver1, String driver2, LoDirections directions) {
    this.driver1 = driver1;
    this.driver2 = driver2;
    this.directions = directions;
  }

  // takes in a road trip and returns a list of chunks broken up by number of total miles (interval)
  LoRoadTripChunks splitUpTrip(int interval) {
    return this.directions.makeLoChunkList
      (interval, driver1, driver2, 0, null).splitUp(driver1, driver2);
  }
            /*
            TEMPLATE:
                Fields:
                    driver1 -- String
                    driver2 -- String
                    directions -- LoDirections
                Methods:
                    splitUpTrip -- LoRoadTripChunks
                Methods of Fields:
                    this.directions.getFirst -- Direction
                    this.directions.checkDirection -- Direction
                    this.directions.makeChunkListFoRHelper -- Direction
                    this.directions.makeChunkListFoR -- Direction
                    this.directions.vRest -- LoDirections
                    this.directions.startChunkList -- Direction
                    this.directions.finishChunkListHelper -- LoDirections
                    this.directions.finishChunkList -- LoDirections
                    this.directions.checkRestHelper -- LoDirections
                    this.directions.checkRest -- LoDirections
                    this.directions.makeChunkList -- LoDirections
                    this.directions.makeLoChunkList -- LoLoDirections
             */
}

// consists of a driver and a list of directions
class RoadTripChunk {
  String driver;
  LoDirections directions;

  RoadTripChunk(String driver, LoDirections directions) {
    this.driver = driver;
    this.directions = directions;
  }
            /*
            TEMPLATE:
                Fields:
                    driver -- String
                    directions -- LoDirections
                Methods:
                --
                Methods of Fields:
                    this.directions.getFirst -- Direction
                    this.directions.checkDirection -- Direction
                    this.directions.makeChunkListFoRHelper -- Direction
                    this.directions.makeChunkListFoR -- Direction
                    this.directions.vRest -- LoDirections
                    this.directions.startChunkList -- Direction
                    this.directions.finishChunkListHelper -- LoDirections
                    this.directions.finishChunkList -- LoDirections
                    this.directions.checkRestHelper -- LoDirections
                    this.directions.checkRest -- LoDirections
                    this.directions.makeChunkList -- LoDirections
                    this.directions.makeLoChunkList -- LoLoDirections
             */
}

class ExamplesRoadTrip {
  Direction d11 = new Direction
    ("Make a left at Albuquerque", 13);
  Direction d12 = new Direction
    ("Make a right at the fork", 2);
  Direction d13 = new Direction
    ("Make a left at the fork", 3);
  Direction d14 = new Direction
    ("Take the overpass", 45);
  Direction d15 = new Direction
    ("Destination on left", 12);

  ConsLoDirections lod11 = new ConsLoDirections
    (d11, new ConsLoDirections
      (d12, new ConsLoDirections
        (d13, new ConsLoDirections
          (d14, new ConsLoDirections
            (d15, new MtLoDirections())))));

  ConsLoDirections lod12 = new ConsLoDirections
    (d14, new ConsLoDirections
      (d15, new MtLoDirections()));

  ConsLoDirections lod13 = new ConsLoDirections
    (d11, new ConsLoDirections
      (d12, new ConsLoDirections
        (new Direction("Switch with Hazel", 0),
          new MtLoDirections())));

  ConsLoLoDirections lolod11 = new ConsLoLoDirections(new ConsLoDirections
    (d11, new ConsLoDirections
      (d12, new ConsLoDirections
        (new Direction("Switch with Henry", 0),
          new MtLoDirections()))), new ConsLoLoDirections(new ConsLoDirections
    (d13, new ConsLoDirections
      (new Direction("Switch with Hazel", 12),
        new MtLoDirections())), new ConsLoLoDirections(new ConsLoDirections
    (new Direction("Switch with Henry", 15),
      new MtLoDirections()), new ConsLoLoDirections(new ConsLoDirections
    (new Direction("Switch with Hazel", 15),
      new MtLoDirections()), new ConsLoLoDirections(new ConsLoDirections
    (new Direction("Take the overpass", 3), new ConsLoDirections
      (d15, new MtLoDirections())), new MtLoLoDirections())))));

  RoadTripChunk chunk11 = new RoadTripChunk
    ("Hazel", new ConsLoDirections
      (d11, new ConsLoDirections
        (d12, new ConsLoDirections
          (new Direction("Switch with Henry", 0),
            new MtLoDirections()))));

  RoadTripChunk chunk12 = new RoadTripChunk
    ("Henry", new ConsLoDirections
      (d13, new ConsLoDirections
        (new Direction("Switch with Hazel", 12),
          new MtLoDirections())));

  RoadTripChunk chunk13 = new RoadTripChunk
    ("Hazel", new ConsLoDirections
      (new Direction("Switch with Henry", 15),
        new MtLoDirections()));

  RoadTripChunk chunk14 = new RoadTripChunk
    ("Henry", new ConsLoDirections
      (new Direction("Switch with Hazel", 15),
        new MtLoDirections()));

  RoadTripChunk chunk15 = new RoadTripChunk
    ("Hazel", new ConsLoDirections
      (new Direction("Take the overpass", 3), new ConsLoDirections
        (d15, new MtLoDirections())));

  LoRoadTripChunks loChunks1 = new ConsLoRoadTripChunks
    (chunk11, new ConsLoRoadTripChunks
      (chunk12, new ConsLoRoadTripChunks
        (chunk13, new ConsLoRoadTripChunks
          (chunk14, new ConsLoRoadTripChunks
            (chunk15, new MtLoRoadTripChunks())))));

  RoadTrip trip1 = new RoadTrip("Hazel", "Henry", lod11);

  boolean testCheckDirection1(Tester t) {
    return t.checkExpect(lod11.checkDirection(15, "Hazel", 0), lod11.first);
  }

  boolean testCheckDirection2(Tester t) {
    return t.checkExpect(lod12.checkDirection(15, "Hazel", 0),
      new Direction("Switch with Hazel", 15));
  }

  boolean testStartChunkList(Tester t) {
    return t.checkExpect(lod11.startChunkList(15, "Hazel", 0, null), lod11.first);
  }

  boolean testerFinishChunkList(Tester t) {
    return t.checkExpect(lod11.finishChunkList(15, "Hazel", 0), lod13);
  }

  boolean testerMakeChunkList(Tester t) {
    return t.checkExpect(lod11.makeChunkList(15, "Hazel", 0, null), lod13);
  }

  boolean testerMakeLoChunkList(Tester t) {
    return t.checkExpect(lod11.makeLoChunkList(15, "Hazel", "Henry", 0, null), lolod11);
  }

  boolean testerSplitUpTrip(Tester t) {
    return t.checkExpect(trip1.splitUpTrip(15), loChunks1);
  }
}