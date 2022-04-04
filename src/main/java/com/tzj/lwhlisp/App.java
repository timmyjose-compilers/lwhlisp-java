package com.tzj.lwhlisp;

public class App {
  public static void main(String[] args) {
    System.out.println("Welcome to lwh lisp");

    var atom = Util.makeInt(100);
    System.out.println(atom);

    atom = Util.makeSym("foo");
    System.out.println(atom);

    atom =
        Util.cons(
            Util.makeInt(1),
            Util.cons(
                Util.makeInt(2),
                Util.cons(
                    Util.makeInt(3),
                    Util.cons(Util.makeInt(4), Util.cons(Util.makeInt(5), Util.nil)))));
    System.out.println(atom);

    atom = Util.cons(Util.makeInt(1), Util.cons(Util.makeInt(2), Util.makeInt(3)));
    System.out.println(atom);

    atom = Util.cons(Util.makeSym("foo"), Util.nil);
    System.out.println(atom);
  }
}

class Util {
  public static final Atom nil = new AtomNil();

  public static Atom makeInt(int n) {
    return new AtomInteger(n);
  }

  public static Atom makeSym(String s) {
    return new AtomSymbol(s);
  }

  public static Atom cons(Atom car, Atom cdr) {
    return new AtomPair(car, cdr);
  }
}

abstract sealed interface Atom permits AtomNil, AtomInteger, AtomSymbol, AtomPair {
  default Object value() {
    throw new UnsupportedOperationException();
  }

  default boolean nilp() {
    return false;
  }
}

final class AtomNil implements Atom {
  @Override
  public boolean nilp() {
    return true;
  }

  @Override
  public String toString() {
    return "NIL";
  }
}

record AtomSymbol(String s) implements Atom {
  @Override
  public Object value() {
    return s;
  }

  @Override
  public String toString() {
    return s;
  }
}

record AtomInteger(int n) implements Atom {
  @Override
  public Object value() {
    return n;
  }

  @Override
  public String toString() {
    return String.valueOf(n);
  }
}

record AtomPair(Atom car, Atom cdr) implements Atom {
  @Override
  public Object value() {
    return this;
  }

  @Override
  public String toString() {
    var sb = new StringBuffer("(");
    sb.append(car().toString());
    var atom = cdr();

    while (!(atom instanceof AtomNil)) {
      if (atom instanceof AtomPair carPair) {
        sb.append(" ");
        sb.append(carPair.car().toString());
        atom = carPair.cdr();

        if (atom instanceof AtomPair cdrPair) {
          sb.append(" ");
          sb.append(cdrPair.car().toString());
          atom = cdrPair.cdr();
        } else {
          sb.append(" . ").append(atom.toString());
          break;
        }
      }
    }

    sb.append(")");

    return sb.toString();
  }
}
