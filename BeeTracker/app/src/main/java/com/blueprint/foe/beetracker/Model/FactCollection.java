package com.blueprint.foe.beetracker.Model;

import java.util.ArrayList;

/**
 * This class wraps an ArrayList of facts.
 * The primary purpose of this class is to leverage Gson when storing/loading.
 * Gson expects a class as input, hence this wrapper.
 *
 * In the future we may want to leverage this class to help with appending new facts.
 */

public class FactCollection {

    private ArrayList<Fact> facts;

    public FactCollection() {
        this.facts = new ArrayList<>();
    }

    public void addFact(Fact fact) {
        this.facts.add(fact);
    }

    public ArrayList<Fact> getFacts () {
        return this.facts;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof FactCollection)) {
            return false;
        }
        if (other == this) return true;

        FactCollection that = (FactCollection) other;

        return this.facts.equals(that.facts);
    }
}
