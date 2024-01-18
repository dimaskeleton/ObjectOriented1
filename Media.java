import tester.Tester;

interface IMedia {
  boolean isReallyOld();

  boolean isCaptionAvailable(String language);

  String format();
}

abstract class AMedia implements IMedia {
  String title; // Title of the media
  ILoString captionOptions; // List of available captions for the media

  // Constructor for AMedia class
  AMedia(String title, ILoString captionOptions) {
    this.title = title; // Initializes the title
    this.captionOptions = captionOptions; // Initializes the caption
  }

  // Method to check if captions are available in a given language
  public boolean isCaptionAvailable(String language) {
    // Returns true if the caption in the given language is available, false otherwise
    return this.captionOptions.contains(language);
  }
}

class Movie extends AMedia {
  int year;

  Movie(String title, int year, ILoString captionOptions) {
    super(title, captionOptions);
    this.year = year;
  }

  // Method to check if a Movie is really old
  public boolean isReallyOld() {
    return this.year < 1930; // Returns true if year is released before 1930, false otherwise
  }

  // Method to format to properly display a piece of media
  public String format() {
    // Returns format as: "Title (year)"
    return this.title + " (" + this.year + ")";
  }
}

class TVEpisode extends AMedia {
  String showName;
  int seasonNumber;
  int episodeOfSeason;

  TVEpisode(String title, String showName, int seasonNumber, int episodeOfSeason,
            ILoString captionOptions) {
    super(title, captionOptions);
    this.showName = showName;
    this.seasonNumber = seasonNumber;
    this.episodeOfSeason = episodeOfSeason;
  }

  // Method to check if a TVEpisode is really-old
  public boolean isReallyOld() {
    return false; // Returns false since TV is new
  }

  // Method to format to properly display a piece of media
  public String format() {
    // Returns format as: "Show Name Season.Episode - Episode Title"
    return this.showName + " " + this.seasonNumber + "." + this.episodeOfSeason + " - " + this.title;
  }
}

class YTVideo extends AMedia {
  String channelName;

  YTVideo(String title, String channelName, ILoString captionOptions) {
    super(title, captionOptions);
    this.channelName = channelName;
  }

  // Method to check if a TVEpisode is really old
  public boolean isReallyOld() {
    return false; // Returns false since YTVideo is new
  }

  // Method to format to properly display a piece of media
  public String format() {
    // Returns format as: "Title by Channel Name"
    return this.title + " by " + this.channelName;
  }
}

interface ILoString {
  boolean contains(String str);
}

class MtLoString implements ILoString {
  public boolean contains(String str) {
    return false;
  }
}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean contains(String str) {
    return this.first.equals(str) || this.rest.contains(str);
  }
}

// Examples Class to test the methods above
class ExamplesMedia {
  ILoString Languages = new ConsLoString("English", new ConsLoString("Spanish", new ConsLoString("Russian", new MtLoString())));

  ILoString english = new ConsLoString("English", new MtLoString());

  IMedia movie1 = new Movie("Eurotrip", 2004, Languages);
  IMedia movie2 = new Movie("Steamboat Willie", 1928, english);
  IMedia movie3 = new Movie("Interstellar", 2014, english);
  IMedia movie4 = new Movie("Cheburashka", 2023, Languages);
  IMedia movie5 = new Movie("Wizards of Oz", 1937, english);

  IMedia tvEpisode1 = new TVEpisode("Pilot", "Breaking Bad", 1, 1, Languages);
  IMedia tvEpisode2 = new TVEpisode("Fly", "Breaking Bad", 3, 10, english);
  IMedia tvEpisode3 = new TVEpisode("We Gotta Go Now", "The Boys", 2, 5, english);
  IMedia tvEpisode4 = new TVEpisode("Felina", "Breaking Bad", 5, 16, english);
  IMedia tvEpisode5 = new TVEpisode("Point and Shoot", "Better Call Saul", 6, 8, english);

  IMedia ytVideo1 = new YTVideo("Object-oriented Programming in 7 minutes", "Programming with Mosh", Languages);
  IMedia ytVideo2 = new YTVideo("Gordon Versus RAW & Uncooked Food | Hell's Kitchen", "Hell’s Kitchen", english);
  IMedia ytVideo3 = new YTVideo("iPhone 15 Transparent Back Glass Mod", "Phone Repair Guru", english);
  IMedia ytVideo4 = new YTVideo("Ancient Life as Old as the Universe", "Kurzgesagt – In a Nutshell", english);
  IMedia ytVideo5 = new YTVideo("What Was The First Black Hole?", "History of the Universe", english);

  boolean testIsReallyOld(Tester t) {
    return t.checkExpect(movie1.isReallyOld(), false)
      && t.checkExpect(movie2.isReallyOld(), true)
      && t.checkExpect(movie3.isReallyOld(), false)
      && t.checkExpect(movie4.isReallyOld(), false)
      && t.checkExpect(movie5.isReallyOld(), false)
      && t.checkExpect(tvEpisode1.isReallyOld(), false)
      && t.checkExpect(tvEpisode2.isReallyOld(), false)
      && t.checkExpect(tvEpisode3.isReallyOld(), false)
      && t.checkExpect(tvEpisode4.isReallyOld(), false)
      && t.checkExpect(tvEpisode5.isReallyOld(), false)
      && t.checkExpect(ytVideo1.isReallyOld(), false)
      && t.checkExpect(ytVideo2.isReallyOld(), false)
      && t.checkExpect(ytVideo3.isReallyOld(), false)
      && t.checkExpect(ytVideo4.isReallyOld(), false)
      && t.checkExpect(ytVideo5.isReallyOld(), false);
  }

  boolean testIsCaptionAvailable(Tester t) {
    return t.checkExpect(movie1.isCaptionAvailable("English"), true)
      && t.checkExpect(movie2.isCaptionAvailable("Spanish"), false)
      && t.checkExpect(movie3.isCaptionAvailable("English"), true)
      && t.checkExpect(movie4.isCaptionAvailable("Russian"), true)
      && t.checkExpect(movie5.isCaptionAvailable("English"), true)
      && t.checkExpect(tvEpisode1.isCaptionAvailable("Spanish"), true)
      && t.checkExpect(tvEpisode2.isCaptionAvailable("French"), false)
      && t.checkExpect(tvEpisode3.isCaptionAvailable("Spanish"), false)
      && t.checkExpect(tvEpisode4.isCaptionAvailable("English"), true)
      && t.checkExpect(tvEpisode5.isCaptionAvailable("English"), true)
      && t.checkExpect(ytVideo1.isCaptionAvailable("German"), false)
      && t.checkExpect(ytVideo2.isCaptionAvailable("English"), true)
      && t.checkExpect(ytVideo3.isCaptionAvailable("Chinese"), false)
      && t.checkExpect(ytVideo4.isCaptionAvailable("Indian"), false)
      && t.checkExpect(ytVideo5.isCaptionAvailable("English"), true);
  }

  boolean testFormat(Tester t) {
    return t.checkExpect(movie1.format(), "Eurotrip (2004)")
      && t.checkExpect(movie2.format(), "Steamboat Willie (1928)")
      && t.checkExpect(movie3.format(), "Interstellar (2014)")
      && t.checkExpect(movie4.format(), "Cheburashka (2023)")
      && t.checkExpect(movie5.format(), "Wizards of Oz (1937)")
      && t.checkExpect(tvEpisode1.format(), "Breaking Bad 1.1 - Pilot")
      && t.checkExpect(tvEpisode2.format(), "Breaking Bad 3.10 - Fly")
      && t.checkExpect(tvEpisode3.format(), "The Boys 2.5 - We Gotta Go Now")
      && t.checkExpect(tvEpisode4.format(), "Breaking Bad 5.16 - Felina")
      && t.checkExpect(tvEpisode5.format(), "Better Call Saul 6.8 - Point and Shoot")
      && t.checkExpect(ytVideo1.format(), "Object-oriented Programming in 7 minutes by Programming with Mosh")
      && t.checkExpect(ytVideo2.format(), "Gordon Versus RAW & Uncooked Food | Hell's Kitchen by Hell’s Kitchen")
      && t.checkExpect(ytVideo3.format(), "iPhone 15 Transparent Back Glass Mod by Phone Repair Guru")
      && t.checkExpect(ytVideo4.format(), "Ancient Life as Old as the Universe by Kurzgesagt – In a Nutshell")
      && t.checkExpect(ytVideo5.format(), "What Was The First Black Hole? by History of the Universe");
  }
}