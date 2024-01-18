interface IResource {
} // Defines interface IResource

class Denial implements IResource { // class Denial implements IResource interface
  String subject; // String variable for the subject of denial
  int believability; // int variable for the believability of denial

  // constructor for the Denial class
  Denial(String subject, int believability) {
    this.subject = subject; // initializes the subject variable with the given value
    this.believability = believability; // initializes the believability variable with the given value
  }
}

class Bribe implements IResource { // class Bribe implements IResource interface
  String target; // String value for the target of the Bribe
  int value; // int value for the value of the Bribe

  // constructor for the Bribe class
  Bribe(String target, int value) {
    this.target = target; // initializes the target variable with the given target
    this.value = value; // initializes the value variable with the given value
  }
}

class Apology implements IResource { // class Apology implements IResource interface
  String excuse; // String variable for the excuse of the Apology
  boolean reusable; // boolean variable to check if an apology is reusable

  // constructor for the Apology class
  Apology(String excuse, boolean reusable) {
    this.excuse = excuse; // initializes the excuse variable with the given value
    this.reusable = reusable; // initializes the reusable variable with the given value
  }
}


interface IAction {
} // Defines interface IAction

class Purchase implements IAction { // class Purchase implements IAction interface
  int cost; // int variable for the cost of Purchase
  IResource resource; // variable for IResource for the resource purchased

  // constructor for the Purchase class
  Purchase(int cost, IResource resource) {
    this.cost = cost; // initializes the cost variable with the given cost
    this.resource = resource; // initializes the resource variable with the given IResource
  }
}

class Swap implements IAction { // class Swap implements IAction interface
  IResource consumedResource; // variable from IResource for the resource consumed
  IResource receivedResource; // variable from IResource for the resource received

  // constructor for the Swap class
  Swap(IResource consumedResource, IResource receivedResource) {
    this.consumedResource = consumedResource; // initializes the consumedResource variable with the given value
    this.receivedResource = receivedResource; // initializes the receivedResource variable with the given value
  }
}

class ExamplesGame {
  ExamplesGame() {
  } // empty constructor for ExamplesGame
  // Tests using the IResource interface on what's given

  // creates a Denial resource with subject knowledge as subject with believability of 51
  IResource iDidntKnow = new Denial("knowledge", 51);

  // creates a Bribe resource with target innocent witness with value of 49
  IResource witness = new Bribe("innocent witness", 49);

  // creates an Apology resource with excuse I won't do it again with reusable false
  IResource iWontDoItAgain = new Apology("I won't do it again", false);

  // Additional tests as requested by the problem

  // creates a Denial resource with subject I forgot the assignment with believability of 30
  IResource forgotAssignment = new Denial("I forgot the assignment", 30);

  // creates a Bribe resource with target sibling with value of 80
  IResource siblingBribe = new Bribe("sibling", 80);

  // creates an Apology resource with excuse I forgot to submit with reusable true
  IResource forgotSubmission = new Apology("I forgot to submit", true);

  // Actions using the resources based on given tests------------

  // creates a Purchase action to buy the witness with cost 40 (buys witness silence)
  IAction purchase1 = new Purchase(40, this.witness);

  // creates a Swap action to swap iDidntKnow for iWontDoItAgain
  IAction swap1 = new Swap(this.iDidntKnow, this.iWontDoItAgain);

  // Additional tests as requested by the problem

  // creates a Purchase action to buy the forgotAssignment with cost 50 (buys more time)
  IAction purchase2 = new Purchase(50, this.forgotAssignment);

  // creates a Swap action to swap forgotSubmission for siblingBribe
  IAction swap2 = new Swap(this.forgotSubmission, this.siblingBribe);
}