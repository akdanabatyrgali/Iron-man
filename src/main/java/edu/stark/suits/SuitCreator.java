package edu.stark.suits;

public abstract class SuitCreator {
    public final IronManSuit createSuit() { return buildSuit(); }
    protected abstract IronManSuit buildSuit();
}
