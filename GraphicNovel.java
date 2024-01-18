class GraphicNovel {
  String title; // String variable for the title of the graphic novel
  String author; // String variable for the author of the graphic novel
  String artist; // String variable for the artist of the graphic novel
  int year; // int variable for the year the graphic novel was published
  double cost; // double variable for the cost of the graphic novel
  boolean monochrome; // boolean variable if the graphic novel is monochrome

  // Constructor for the GraphicNovel class
  GraphicNovel(String title, String author, String artist, int year, double cost, boolean monochrome) {
    this.title = title; // initializes the title variable with the given value
    this.author = author; // initializes the author variable with the given value
    this.artist = artist; // initializes the artist variable with the given value
    this.year = year; // initializes the year variable with the given value
    this.cost = cost; // initializes the cost variable with the given value
    this.monochrome = monochrome; // initializes the monochrome variable with the given value
  }
}

class ExampleGraphicNovel {
  ExampleGraphicNovel() {
  } // Empty constructor for ExampleGraphicNovel

  // Creates a graphic novel with the details of Maus
  GraphicNovel maus = new GraphicNovel("Maus", "Spiegelman", "Spiegelman", 1980, 17.60, true);

  // Creates a graphic novel with the details of Logicomix
  GraphicNovel logicomix = new GraphicNovel("Logicomix", "Doxiadis", "Papadatos", 2009, 21.00, false);

  // Creates a graphic novel with the details of Pashmina
  GraphicNovel Pashmina = new GraphicNovel("Pashmina", "Chanani", "Chanani", 2017, 11.29, false);
}