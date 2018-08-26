package com.test.kmlparser.algo;

import com.test.kmlparser.algo.Observer;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
