package edu.stark.suits;

/** Factory Method: subclasses decide which concrete suit to instantiate. */
public abstract class SuitCreator {
    public final IronManSuit createSuit() { return buildSuit(); }
    protected abstract IronManSuit buildSuit();
}
