// Represents a Person with a name and a list of buddies
public class Person {
  Person() {
  }

  String username; // The username of the person
  ILoBuddy buddies; // A list of buddies associated with this person
  double dictionScore; // Diction score of the person
  double hearingScore; // Hearing score of the person

  //Purpose: "The constructor that takes in just the person’s name can set their diction and hearing scores to 0.0 as default values "
  Person(String username) {
    this.username = username;
    this.buddies = new MTLoBuddy();
    this.dictionScore = 0.0;
    this.hearingScore = 0.0;
  }

  // Purpose: "Add an additional constructor to the Person class that takes in the person’s diction score and hearing score.
  Person(String username, double dictionScore, double hearingScore) {
    this.username = username;
    this.buddies = new MTLoBuddy();
    this.dictionScore = dictionScore;
    this.hearingScore = hearingScore;
  }

    /* TEMPLATE:
   Fields:
   ... this.name ...           -- String
   ... this.buddies ...        -- ILoBuddy
   ... this.dictionScore ...   -- double
   ... this.hearingScore ...   -- double

   Constructors:
   ... this.Person(String) ...                  -- Person
   ... this.Person(String, double, double) ...  -- Person

   Methods:
   ... this.addBuddy(Person) ...               -- void
   ... this.hasDirectBuddy(Person) ...         -- boolean
   ... this.countCommonBuddies(Person) ...     -- int
   ... this.hasExtendedBuddy(Person) ...       -- boolean
   ... this.partyCount() ...                   -- int
   ... this.maxLikelihood(Person) ...          -- double
    */

  // Purpose: Change this person's buddy list so that it includes the given person
  void addBuddy(Person Buddy) {
    this.buddies = new ConsLoBuddy(Buddy, this.buddies);
  }

  // Purpose: Check if the person has a direct buddy, returns true if this Person has that as a direct buddy
  boolean hasDirectBuddy(Person that) {
    return this.buddies.contains(that);
  }

  // Purpose: Count the number of common buddies with another person, returns the number of people that are direct buddies of both this and that person
  int countCommonBuddies(Person that) {
    return this.buddies.countCommonBuddies(that.buddies);
  }

  // Purpose: Check if a person is an extended buddy, will the given person be invited to a party organized by this person?
  boolean hasExtendedBuddy(Person that) {
    return this.buddies.hasExtendedBuddy(that, new MTLoBuddy());
  }

  // Purpose: Count the number of attendees at a party, returns the number of people who will show up at the party given by this person
  //counts the number of people who will attend a party given by this person if all buddies and all buddies of all buddies (including the distant ones) come to the party. The party organizer attends their own party.
  int partyCount() {
    ILoBuddy checked = new ConsLoBuddy(this, new MTLoBuddy()); // Initialize with self
    return this.buddies.partyCount(checked) + 1;
  }

  // Purpose: Calculate the maximum likelihood of conveying a message to another person
  //takes in some other person that and returns the maximum likelihood that this person can correctly convey a message to that person.
  double maxLikelihood(Person that) {
    return this.buddies.maxLikelihood(that, this.dictionScore, new MTLoBuddy());
  }
}