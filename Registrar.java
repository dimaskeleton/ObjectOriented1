import java.util.ArrayList;
import java.util.List;
import tester.*;
// A course has a name (a String), and instructor named prof, and a list of students named students
class Course {
  String name; //name of the course
  Instructor prof; //an instructor available to teach the course, named prof
  List<Student> students; //list of students in the course
  //Constructor
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new ArrayList<>();
    prof.assignCourse(this);
  }
  /* TEMPLATE:
 Fields:
 ... this.name ...        -- String
 ... this.prof ...        -- Instructor
 ... this.students ...    -- List<Student>
 Methods:
 ... this.addStudent(Student) ...  -- void
  */
  //This method lets us add a Student to a list of students enrolled in a particular course
  public void addStudent(Student student) { //takes in a Student as a parameter
    students.add(student); //adds the student to the list of students
  }
}
//An instructor has a name and list of courses he or she teaches. Are initially constructed without taking any courses.
class Instructor {
  String name; //name of the instructor
  List<Course> courses; //list of courses he/she teaches
  //constructor
  Instructor(String name) {
    this.name = name;
    this.courses = new ArrayList<>();
  }
  /* TEMPLATE:
 Fields:
 ... this.name ...        -- String
 ... this.courses ...     -- List<Course>
 Methods:
 ... this.assignCourse(Course) ...  -- void
 ... this.dejavu(Student) ...       -- boolean
  */
  // This method associates a course with a specific instructor
  public void assignCourse(Course course) { //takes a Course
    courses.add(course); //adds the given course to the list of courses that the instructor is assigned to teach
  }
  //Methods that determines whether the given student is in any of the same classes as this Student
  public boolean dejavu(Student c) {
    int count = 0; //variable to keep track how many times the given student is found among the students in the instructors courses
    for (Course course : courses) { //for loop that goes through all the courses with the instructor
      if (course.students.contains(c)) {
        count++;
      }
    }
    return count > 1; //returns true if count is greater than 1 -> indicates student is enrolled in more than 1 of the instructors courses
  }
}
//A Student has a name, an id number (int), and a list of Courses they are taking
class Student {
  String name; //name of student
  int id; //ID number of student
  List<Course> courses; //list of courses student is taking
  //constructor
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new ArrayList<>();
  }

  /* TEMPLATE:
 Fields:
 ... this.name ...        -- String
 ... this.id ...          -- int
 ... this.courses ...     -- List<Course>
 Methods:
 ... this.enroll(Course) ...    -- void
 ... this.classmates(Student) ...  -- boolean
  */
  // Method that enrolls a student in the given course
  public void enroll(Course c) {
    courses.add(c); //adds the course to the courses list with the students
    c.addStudent(this); //add the current student (this) to the list of students enrolled in the course (roster)
  }
  //Method that determines whether the given Student is in more than one of this Instructor's Courses
  public boolean classmates(Student c) {
    for (Course course : courses) {
      if (c.courses.contains(course)) { //checks if the courses list of the given student c contains the current course
        return true; //if true for any course, the method immediately returns true -> the two students share at least one course in common
      }
    }
    return false; //If none of the courses in the current student's list are also in the given student's list of courses -> the method returns false
  }
}
class ExamplesRegistrar {
  ExamplesRegistrar() {
  }

  Instructor instructor1;
  Instructor instructor2;

  Course course1;
  Course course2;
  Course course3;
  Course course4;

  Student student1;
  Student student2;
  Student student3;
  Student student4;
  Student student5;

  // Initializes and initRegistrars the state before each test
  void initRegistrar() {
    instructor1 = new Instructor("Professor Bob");
    instructor2 = new Instructor("TA Jason");

    course1 = new Course("Programming Languages", instructor1);
    course2 = new Course("Objects 2", instructor1);
    course3 = new Course("Chemistry", instructor2);
    course4 = new Course("Biology", instructor2);

    student1 = new Student("Ashley", 101);
    student2 = new Student("Kyle", 102);
    student3 = new Student("Gabby", 103);
    student4 = new Student("Collin", 104);
    student5 = new Student("Chris", 105);
  }

  // Test cases for classmates method
  boolean testClassmates1(Tester t) {
    initRegistrar();
    student1.enroll(course1);
    student2.enroll(course1);
    return t.checkExpect(student1.classmates(student2), true);
  }

  boolean testClassmates2(Tester t) {
    initRegistrar();
    student1.enroll(course1);
    student3.enroll(course3);
    return t.checkExpect(student1.classmates(student3), false);
  }

  boolean testClassmates3(Tester t) {
    initRegistrar();
    student2.enroll(course2);
    student4.enroll(course3);
    return t.checkExpect(student2.classmates(student4), false);
  }

  boolean testClassmates4(Tester t) {
    initRegistrar();
    student3.enroll(course3);
    student4.enroll(course3);
    return t.checkExpect(student3.classmates(student4), true);
  }

  boolean testClassmates5(Tester t) {
    initRegistrar();
    student5.enroll(course4);
    student1.enroll(course1);
    return t.checkExpect(student5.classmates(student1), false);
  }

  boolean testClassmates6(Tester t) {
    initRegistrar();
    student1.enroll(course1);
    student2.enroll(course1);
    student1.enroll(course2);
    student2.enroll(course2);
    return t.checkExpect(student1.classmates(student2), true);
  }

  boolean testClassmates7(Tester t) {
    initRegistrar();
    student3.enroll(course3);
    return t.checkExpect(student3.classmates(student4), false);
  }

  boolean testClassmates8(Tester t) {
    initRegistrar();
    return t.checkExpect(student4.classmates(student5), false);
  }

  // Test cases for dejavu method
  boolean testDejavu1(Tester t) {
    initRegistrar();
    student2.enroll(course1);
    student2.enroll(course2);
    return t.checkExpect(instructor1.dejavu(student2), true);
  }

  boolean testDejavu2(Tester t) {
    initRegistrar();
    student3.enroll(course3);
    return t.checkExpect(instructor1.dejavu(student3), false);
  }

  boolean testDejavu3(Tester t) {
    initRegistrar();
    student4.enroll(course3);
    return t.checkExpect(instructor2.dejavu(student4), false);
  }

  boolean testDejavu4(Tester t) {
    initRegistrar();
    student5.enroll(course4);
    return t.checkExpect(instructor2.dejavu(student5), false);
  }

  boolean testDejavu5(Tester t) {
    initRegistrar();
    student1.enroll(course1);
    return t.checkExpect(instructor1.dejavu(student1), false);
  }

  boolean testDejavu6(Tester t) {
    initRegistrar();
    student3.enroll(course1);
    student3.enroll(course3);
    return t.checkExpect(instructor1.dejavu(student3), false);
  }

  boolean testDejavu7(Tester t) {
    initRegistrar();
    student4.enroll(course2);
    return t.checkExpect(instructor1.dejavu(student4), false);
  }

  boolean testDejavu8(Tester t) {
    initRegistrar();
    student5.enroll(course2);
    student5.enroll(course3);
    return t.checkExpect(instructor1.dejavu(student5), false);
  }
}