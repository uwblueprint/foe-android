package com.blueprint.foe.beetracker.Model;

import java.util.ArrayList;

/**
 * Created by johnsington on 2017-07-07.
 */
public class FactCollection {

    ArrayList<Fact> facts;
    static int id;

    public FactCollection() {
        this.facts = new ArrayList<>();
        this.id = 0;
    }

    public void addFact(String title, String description) {
        Fact newFact = new Fact(title, description, this.id);
        this.facts.add(newFact);
    }

    public ArrayList<Fact> getFacts () {
        return this.facts;
    }

    public class Fact {
        String title;
        String description;
        int id;

        public Fact(String title, String description, int id) {
            this.title = title;
            this.description = description;
            this.id = id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
