// Represents an empty list of Person's buddies that implements the ILoBuddy interface
class MTLoBuddy implements ILoBuddy {
  // Constructor for MTLoBuddy, representing an empty list of buddies
  MTLoBuddy(){}

  // Checks if this empty list contains a specific person
  public boolean contains(Person that) {
    return false; // An empty list does not contain any person
  }

  // Checks if a specific person is an extended buddy in this empty list
  public boolean hasExtendedBuddy(Person that, ILoBuddy checked) {
    return false; // An empty list has no extended buddies
  }

  // Counts the total number of unique buddies in the network starting from this empty list
  public int partyCount(ILoBuddy checked) {
    return 0; // An empty list has a count of 0
  }

  // Calculates the maximum likelihood of conveying a message in this empty list
  public double maxLikelihood(Person that, double likelihood, ILoBuddy checked) {
    return 0; // In an empty list, there is no likelihood of conveying a message
  }

  // Counts the number of common buddies between this empty list and another list
  public int countCommonBuddies(ILoBuddy thatBuddies) {
    return 0; // An empty list has no common buddies with any other list
  }
}