package com.test.kmlparser;

public interface Visitable {
    public void accept(Visitor visitor);
}