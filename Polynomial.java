import tester.*;

// Represents a monomial: aix^i
class Monomial {
  int coefficient;  // x^i is multipled by (ai)
  int degree;       //  a non-negative integer that x is raised to (i)
  Monomial(int coefficient, int degree) {
    if(degree < 0) {
      throw new IllegalArgumentException("Degree must be non-negative.");
    }
    this.coefficient = coefficient;
    this.degree = degree;
  }

/* TEMPLATE
    Fields:
    ...this.coefficient... --int
    ...this.degree... --int
    Methods:
    ...sameMonomial(Monomial other)... --boolean
*/

  // Returns true if this monomial has the same coefficient and degree as the other
  boolean sameMonomial(Monomial other) {
    return this.coefficient == other.coefficient && this.degree == other.degree;
  }
}

// Represents a list of monomials
interface ILoMonomial {
  boolean containsDegree(int degree);
  boolean containsMonomial(Monomial m);
  boolean sameMonomialsList(ILoMonomial other);
  boolean containsAllOf(ILoMonomial other);
}

// Represents an empty list of monomials
class MtLoMonomial implements ILoMonomial {
  MtLoMonomial (){}
  public boolean containsDegree(int degree) {
    return false;
  }
  public boolean containsMonomial(Monomial m) {
    return false;
  }
  public boolean sameMonomialsList(ILoMonomial other) {
    return other instanceof MtLoMonomial;
  }
  public boolean containsAllOf(ILoMonomial other) {
    return other instanceof MtLoMonomial;
  }
}

// Represents a non-empty list of monomials
class ConsLoMonomial implements ILoMonomial {
  Monomial first;
  ILoMonomial rest;

  ConsLoMonomial(Monomial first, ILoMonomial rest) {
    if (rest.containsDegree(first.degree)) {
      throw new IllegalArgumentException("Cannot have two monomials with the same degree.");
    }
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE
      Fields:
      ...this.first... --Monomial
      ...this.rest... --ILoMonomial
      Methods:
      ...containsDegree(int degree)... --boolean
      ...containsMonomial(Monomial m)... --boolean
      ...sameMonomialsList(ILoMonomial other)... --boolean
      ...containsAllOf(ILoMonomial other)... --boolean
  */
  public boolean containsDegree(int degree) {
    if (this.first.degree == degree) {
      return true;
    }
    return this.rest.containsDegree(degree);
  }
  public boolean containsMonomial(Monomial m) {
    if (this.first.sameMonomial(m)) {
      return true;
    }
    return this.rest.containsMonomial(m);
  }
  public boolean sameMonomialsList(ILoMonomial other) {
    return this.containsAllOf(other) && other.containsAllOf(this);
  }
  public boolean containsAllOf(ILoMonomial other) {
    if (other instanceof MtLoMonomial) {
      return true;
    } else if (other instanceof ConsLoMonomial) {
      ConsLoMonomial otherCons = (ConsLoMonomial) other;
      return this.containsMonomial(otherCons.first) && this.containsAllOf(otherCons.rest);
    } else {
      return false;
    }
  }
}

// Represents a polynomial
class Polynomial {
  ILoMonomial monomials;

  Polynomial(ILoMonomial monomials) {
    this.monomials = monomials;
  }
/* TEMPLATE
    Fields:
    ...this.monomials... --ILoMonomial
    Methods:
    ...samePolynomial(Polynomial other)... --boolean
*/

  // Determines if two polynomials represent the same polynomial
  boolean samePolynomial(Polynomial other) {
    return this.monomials.sameMonomialsList(other.monomials);
  }
}

class ExamplesPolynomial {
  ExamplesPolynomial(){}
  Monomial mono1 = new Monomial(3, 2);
  Monomial mono2 = new Monomial(5, 0);
  Monomial mono3 = new Monomial(1, 1);
  Monomial mono4 = new Monomial(7, 3);
  Monomial mono5 = new Monomial(0, 4);
  Monomial mono6 = new Monomial(-5, 0);

  ILoMonomial empty = new MtLoMonomial();
  ILoMonomial monoList1 = new ConsLoMonomial(mono1, new ConsLoMonomial(mono2, empty));
  ILoMonomial monoList2 = new ConsLoMonomial(mono2, new ConsLoMonomial(mono1, empty));
  ILoMonomial monoList3 = new ConsLoMonomial(mono3, new ConsLoMonomial(mono4, empty));
  ILoMonomial monoList4 = new ConsLoMonomial(mono3, empty);
  ILoMonomial monoList5 = new ConsLoMonomial(mono5, new ConsLoMonomial(mono3, empty));
  ILoMonomial monoList6 = new ConsLoMonomial(mono6, new ConsLoMonomial(mono3, empty));

  Polynomial poly1 = new Polynomial(monoList1);
  Polynomial poly2 = new Polynomial(monoList2);
  Polynomial poly3 = new Polynomial(monoList3);
  Polynomial poly4 = new Polynomial(monoList4);
  Polynomial poly5 = new Polynomial(monoList5);
  Polynomial poly6 = new Polynomial(monoList6);

  boolean testSamePolynomial1(Tester t) {
    return t.checkExpect(poly1.samePolynomial(poly2), true);
  }
  boolean testSamePolynomial2(Tester t) {
    return t.checkExpect(poly1.samePolynomial(poly3), false);
  }
  boolean testSamePolynomial3(Tester t) {
    return t.checkExpect(poly3.samePolynomial(poly4), false);
  }
  boolean testSamePolynomial4(Tester t) {
    return t.checkExpect(poly4.samePolynomial(poly5), false);
  }
  boolean testSamePolynomial5(Tester t) {
    return t.checkExpect(poly1.samePolynomial(poly6), false);
  }
  boolean testSamePolynomial6(Tester t) {
    return t.checkExpect(poly4.samePolynomial(poly6), false);
  }
  boolean testSamePolynomial7(Tester t) {
    return t.checkExpect(poly5.samePolynomial(poly6), false);
  }
  boolean testSamePolynomial8(Tester t) {
    return t.checkExpect(poly3.samePolynomial(poly5), false);
  }

  //examples for the methods
  ILoMonomial monomials1 = new ConsLoMonomial(new Monomial(3, 2), new ConsLoMonomial(new Monomial(5, 0), new MtLoMonomial()));
  ILoMonomial monomials2 = new ConsLoMonomial(new Monomial(5, 0), new ConsLoMonomial(new Monomial(3, 2), new MtLoMonomial()));
  ILoMonomial monomials3 = new ConsLoMonomial(new Monomial(1, 1), new MtLoMonomial());

  // tests for testContainsDegree method
  boolean testContainsDegree1(Tester t) {
    return t.checkExpect(monomials1.containsDegree(2), true);
  }
  boolean testContainsDegree2(Tester t) {
    return t.checkExpect(monomials2.containsDegree(0), true);
  }
  // tests for testContainsMonomial
  boolean testContainsMonomial1(Tester t) {
    return t.checkExpect(monomials2.containsMonomial(new Monomial(3, 2)), true);
  }
  boolean testContainsMonomial2(Tester t) {
    return t.checkExpect(monomials2.containsMonomial(new Monomial(7, 3)), false);
  }
  // tests for testSameMonomialsList method
  boolean testSameMonomialsList1(Tester t) {
    return t.checkExpect(monomials1.sameMonomialsList(monomials2), true);
  }
  boolean testSameMonomialsList2(Tester t) {
    return t.checkExpect(monomials1.sameMonomialsList(monomials3), false);
  }
  // tests for testContainsAllOf method
  boolean testContainsAllOf1(Tester t) {
    return t.checkExpect(monomials1.containsAllOf(monomials2), true);
  }
  boolean testContainsAllOf2(Tester t) {
    return t.checkExpect(monomials1.containsAllOf(monomials3), false);
  }
}