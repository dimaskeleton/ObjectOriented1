// Represents a non-empty list of Person's buddies
class ConsLoBuddy implements ILoBuddy {
  ConsLoBuddy(){}
  Person first; // The first buddy in the list
  ILoBuddy rest; // The rest of the buddies in the list

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }
    /* TEMPLATE
   Fields:
   ... this.first ...       -- Person
   ... this.rest ...        -- ILoBuddy
   Methods:
   ... this.contains(Person) ...               -- boolean
   ... this.hasExtendedBuddy(Person) ...       -- boolean
   ... this.partyCount() ...                   -- int
   ... this.maxLikelihood(Person) ...          -- double
   ... this.countCommonBuddies(Person) ...     -- int
    */

  // Checks if this list contains a specific person
  public boolean contains(Person that) {
    // Returns true if the first buddy is the person or if the rest of the list contains the person
    return this.first.equals(that) || this.rest.contains(that);
  }

  // Checks if a specific person is an extended buddy
  public boolean hasExtendedBuddy(Person that, ILoBuddy checked) {
    if (this.first.equals(that)) {
      // Returns true if the first buddy is the person we're looking for
      return true;
    }
    else if (!checked.contains(this.first)) {
      // If the first buddy is not already checked, check both in the rest of this list and in the first buddy's buddies
      return this.rest.hasExtendedBuddy(that, new ConsLoBuddy(this.first, checked)) ||
        this.first.buddies.hasExtendedBuddy(that, new ConsLoBuddy(this.first, checked));
    }
    else {
      // Continue the search in the rest of the list if the first buddy is already checked
      return this.rest.hasExtendedBuddy(that, checked);
    }
  }

  // Counts the total number of unique buddies in the network
  public int partyCount(ILoBuddy checked) {
    // If the first buddy is not already counted, count them and continue counting in the rest of the list
    if (!checked.contains(this.first)) {
      ILoBuddy newChecked = new ConsLoBuddy(this.first, checked);
      return 1 + this.first.buddies.partyCount(newChecked) + this.rest.partyCount(checked);
    }
    // Continue the count in the rest of the list if the first buddy is already counted
    else {
      return this.rest.partyCount(checked);
    }
  }

  // Calculates the maximum likelihood of getting a message
  public double maxLikelihood(Person that, double likelihood, ILoBuddy checked) {
    // Completes the likelihood calculation if the first buddy is the target
    if (this.first.equals(that)) {
      return likelihood * this.first.hearingScore;
    }
    else if (!checked.contains(this.first)) {
      // Update likelihood for the next step and continue exploring the path
      double nextStepLikelihood = likelihood * this.first.dictionScore * this.first.hearingScore;

      // Continues exploring the path from this first buddy and also tries other paths
      return Math.max(this.rest.maxLikelihood(that, likelihood, new ConsLoBuddy(this.first, checked)),
        this.first.buddies.maxLikelihood(that, nextStepLikelihood, new ConsLoBuddy(this.first, checked))
      );
    }
    else {
      // Continue with other paths if the first buddy is already checked
      return this.rest.maxLikelihood(that, likelihood, checked);
    }
  }

  // Counts the number of common buddies between this list and another list
  public int countCommonBuddies(ILoBuddy thatBuddies) {
    if (thatBuddies.contains(this.first)) {
      // If the first buddy is also in the other list, count them and continue counting in the rest of the list
      return 1 + this.rest.countCommonBuddies(thatBuddies);
    }
    else {
      // Continue the count in the rest of the list if the first buddy is not in the other list
      return this.rest.countCommonBuddies(thatBuddies);
    }
  }
}