package edu.stark.suits;

public class MarkIIICreator extends SuitCreator {
    @Override protected IronManSuit buildSuit() { return new MarkIII(); }
}
