
import tester.*;    //import tester library

// This interface named "IFunc"  represents a function which takes an argument of type A and returns a result of type R
interface IFunc<A, R> {
    R apply(A arg);
}

// Interface named IListVisitor that represents a visitor pattern for working with lists of elements, such as Empty list and Non-empty List
// IListVisitor interface extends interface IFunc, which represents a function from an IList<T> to a result of type R
interface IListVisitor<T, R> extends IFunc<IList<T>, R> {
    R visitCons(Cons<T> cons);
    R visitMtList(MtList<T> mt);
}

// IList interface includes an abstract method named accept, which takes an instance of IListVisitor<T, R> as an argument.
// It accepts a visitor that operates on the list of elements of type T and returns a result of type R.
// Accepts and interacts with a visitor (visitor pattern design)
interface IList<T> {
    <R> R accept(IListVisitor<T, R> visitor);
}

// This class implements the IList<T> interface and representas a non-empty list
class Cons<T> implements IList<T> {
    T first; //first element
    IList<T> rest; //rest of list
    //constructor for non-empty list
    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
        /* TEMPLATE
        Fields:
        ... this.first ...   -- T
        ... this.rest ...    -- IList<T>
        Methods:
        ... this.accept(IListVisitor<T, R>) ...   -- R
        */
    }
    // In this accept method, the Cons class passes the visiting behavior to the visitor object's visitCons method, passing itself (this) as an argument.
    public <R> R accept(IListVisitor<T, R> visitor) {
        return visitor.visitCons(this);
    }
}

// This class implements the IList<T> interface and representas an empty list
class MtList<T> implements IList<T> {
    // In this accept method, the MtList class passes the visiting behavior to the visitor object's visitCons method, passing itself (this) as an argument.
    public <R> R accept(IListVisitor<T, R> visitor) {
        return visitor.visitMtList(this);
    }
}

// Represents a class course that has name of the course and list of prerequisites for that course
class Course {
    String name;    //name of the course
    IList<Course> prereqs;  //holds a list of prerequisites courses
    //constructor
    Course(String name, IList<Course> prereqs) {
        this.name = name;
        this.prereqs = prereqs;
    }
    /* TEMPLATE
    Fields:
    ... this.name ...       -- String
    ... this.prereqs ...    -- IList<Course>
    Methods:
    ... this.accept(IListVisitor<Course, R>) ...   -- R
    ... this.getDeepestPathLength() ...            -- int
    ... this.hasPrereq(String) ...                  -- boolean
    */
    // accept method -> allows the course to accept a visitor and accepts the prereqs list, passing the visitor to the list of prerequisite courses
    <R> R accept(IListVisitor<Course, R> visitor) {
        return this.prereqs.accept(visitor);
    }
    // method that calculates and returns the length of the deepest path in the course prerequisite history.
    // Calls the accpet method with a visitor to calculate the length.
    int getDeepestPathLength() {
        return this.accept(new DeepestPathLength());
    }
    // method that checks whether a course has a prerequisite with a specified name.
    // calls the accept method with the HasPrereq visitor to check for the exsistence of a speicified prereq course
    boolean hasPrereq(String prereqName) {
        return this.accept(new HasPrereq(prereqName));
    }
}

// Represents a visitor that calculates the deepest path length
class DeepestPathLength implements IListVisitor<Course, Integer> {
    /* TEMPLATE
        Methods:
        ... this.visitCons(Cons<Course>) ...  -- Integer
        ... this.visitMtList(MtList<Course>) ...  -- Integer
        ... this.apply(IList<Course>) ...         -- Integer
    */
    //This method is used to visit a non-empty list of courses to calculate the deepest path length.
    public Integer visitCons(Cons<Course> cons) {
        //This line calculates the depth of the current course by adding 1 to the depth of the first course in the Cons node and assuming that each course contributes 1 to the depth.
        int depth = 1 + cons.first.getDeepestPathLength(); // We assume each course contributes 1 to the depth
        //This line recursively calculates the depth of the remaining courses by calling the accept method on the rest of the courses and passing the same DeepestPathLength visitor to continue the depth calculation.
        int restDepth = cons.rest.accept(this);
        //This line returns the maximum depth between the current course and the depth of the rest of the courses (restDepth).
        // Makes sure that the visitor returns the deepest path length among all the courses in the list.
        return Math.max(depth, restDepth);
    }
    // This method is used to visit an empty list to calculate the deepest path length.
    // Since an empty list has no courses, its depth is 0, return 0.
    public Integer visitMtList(MtList<Course> mt) {
        return 0;
    }
    // This method is part of the IFunc interface and is used to apply the DeepestPathLength visitor to a list of courses.
    // It passes the visitation to the accept method of the provided list of courses and returns the result, which is the deepest path length.
    public Integer apply(IList<Course> arg) {
        return arg.accept(this);
    }
}

//HasPrereq class implements the IListVisitor interface to check if any course in a list of courses has a prerequisite with a given name
class HasPrereq implements IListVisitor<Course, Boolean> {
    String prereqName; //the name of the prereq course that is being checked for
    // constructor
    HasPrereq(String prereqName) {
        this.prereqName = prereqName;
    }
/* TEMPLATE
Fields:
... this.prereqName ...    -- String
Methods:
... this.visitCons(Cons<Course>) ...  -- Boolean
... this.visitMtList(MtList<Course>) ...  -- Boolean
... this.apply(IList<Course>) ...         -- Boolean
*/

    // Method to visit a non-empty list of cources to check is any course in the list has a prereq with a specified name
    // It checks if the first course has the same name as the specified prerequisiteor if the first course has a prerequisite with the specified name.
    public Boolean visitCons(Cons<Course> cons) {
        // If not found in the first course, it recursively checks the rest of the courses by calling "accept(this)".
        //It returns true if any of these conditions are met => a course with the specified prerequisite name was found.
        return cons.first.name.equals(prereqName) ||
          cons.first.hasPrereq(prereqName) ||
          cons.rest.accept(this);
    }
    //This method is used to visit an empty list to check if any course in the list has a prerequisite with the specified name.
    // Since an empty list contains no courses, it always returns false to indicate that no course with the specified prerequisite name was found.
    public Boolean visitMtList(MtList<Course> mt) {
        return false;
    }
    //This method is part of the IFunc interface and is used to apply the HasPrereq visitor to a list of courses.
    // It controls the visitation to the accept method of the provided list of courses and returns the result, which is a Boolean value indicating whether any course in the list has the specified prerequisite.
    public Boolean apply(IList<Course> arg) {
        return arg.accept(this);
    }
}

// Examples class
class ExamplesCourse {
    ExamplesCourse() {}

    // examples of courses a person could take
    Course calc1 = new Course("Calculus 1", new MtList<>());
    Course calc2 = new Course("Calculus 2", new MtList<>());
    Course calc3 = new Course("Calculus 3", new Cons<>(calc1, new MtList<>()));
    Course calc4 = new Course("Multivariable calc", new Cons<>(calc2, new MtList<>()));
    Course linearAlgebra = new Course("Linear Algebra", new MtList<>());
    Course diffEq = new Course("Differential Equations", new Cons<>(linearAlgebra, new MtList<>()));
    Course physics1 = new Course("Physics 1", new Cons<>(calc1, new MtList<>()));
    Course physics2 = new Course("Physics 2", new Cons<>(calc3, new Cons<>(physics1, new MtList<>())));
    Course algorithms = new Course("Algorithms", new Cons<>(calc2, new Cons<>(diffEq, new MtList<>())));
    Course discreteMath = new Course("Discrete Math", new Cons<>(calc1, new MtList<>()));

    // Test for the getDeepestPathLength() method
    //Purpose: calculates the deepest path length for each course
    boolean testGetDeepestPathLength(Tester t) {
        return t.checkExpect(calc1.getDeepestPathLength(), 0)
          && t.checkExpect(calc2.getDeepestPathLength(), 0)
          && t.checkExpect(calc3.getDeepestPathLength(), 1)
          && t.checkExpect(calc4.getDeepestPathLength(), 1)
          && t.checkExpect(physics1.getDeepestPathLength(), 1)
          && t.checkExpect(physics2.getDeepestPathLength(), 2) // Physics 2 -> Physics 1 -> Calc 1
          && t.checkExpect(diffEq.getDeepestPathLength(), 1)
          && t.checkExpect(algorithms.getDeepestPathLength(), 2)
          && t.checkExpect(discreteMath.getDeepestPathLength(), 1);
    }

    // Test for hasPrereq(String prereqName) method
    // Purpose: Determine if a course has a specific prerequisite by comparing it with the provided prerequisite name
    boolean testHasPrereq(Tester t) {
        return t.checkExpect(calc1.hasPrereq("Calculus 1"), false)
          && t.checkExpect(calc2.hasPrereq("Calculus 2"), false)
          && t.checkExpect(calc3.hasPrereq("Calculus 1"), true) // Calc 3 has Calc 1 as a prereq
          && t.checkExpect(calc4.hasPrereq("Calculus 2"), true) // Multivariable Calc has Calc 2 as a prereq
          && t.checkExpect(calc4.hasPrereq("Calculus 1"), false) // Multivariable Calc has Calc 1 as a prereq
          && t.checkExpect(calc1.hasPrereq("Multivariable Calc"), false)
          && t.checkExpect(physics2.hasPrereq("Calculus 1"), true) // Physics 2 has Physics 1 which has Calc 1
          && t.checkExpect(physics2.hasPrereq("Calculus 3"), true) // Physics 2 directly has Calc 3
          && t.checkExpect(algorithms.hasPrereq("Linear Algebra"), true) // Algorithms has Diff Eq which has Linear Algebra
          && t.checkExpect(algorithms.hasPrereq("Physics 1"), false) // Algorithms does not have Physics 1
          && t.checkExpect(physics2.hasPrereq("Differential Equations"), false)
          && t.checkExpect(discreteMath.hasPrereq("Calculus 1"), true)
          && t.checkExpect(discreteMath.hasPrereq("Calculus 2"), false);
    }
}

