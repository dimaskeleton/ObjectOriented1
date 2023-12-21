// represents an interface for list of Person's buddies

// Interface for a list of buddies
interface ILoBuddy {
  //method to determine if the current list contains a specific person
  boolean contains(Person that);

  //method to check whether a given person (buddy) has an extended buddy within a specified list of buddies
  boolean hasExtendedBuddy(Person that, ILoBuddy checked);

  //method that returns the number of people who will show up at the party given by this person
  int partyCount(ILoBuddy checked);

  double maxLikelihood(Person that, double likelihood, ILoBuddy checked);

  //method to return the number of people that are direct buddies of both this and
  int countCommonBuddies(ILoBuddy thatBuddies);
}