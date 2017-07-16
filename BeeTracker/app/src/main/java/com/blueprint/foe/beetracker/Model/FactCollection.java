package com.blueprint.foe.beetracker.Model;

import java.util.ArrayList;

/**
 * Created by johnsington on 2017-07-07.
 */

public class FactCollection {

    public enum Category {
        GENERAL, GARDEN, WATER
    }

    ArrayList<Fact> facts;
    static int id;

    public FactCollection() {
        this.facts = new ArrayList<>();
        this.id = 0;
    }

    public void addFact(String title, String description) {
        Fact newFact = new Fact(title, description, this.id);
        this.facts.add(newFact);
        this.id++;
    }

    public void addFact(String title, String description, Category category) {
        Fact newFact = new Fact(title, description, this.id, category);
        this.facts.add(newFact);
        this.id++;
    }

    public ArrayList<Fact> getFacts () {
        return this.facts;
    }

    public class Fact {

        String title;
        String description;
        Category category;
        int id;


        public Fact(String title, String description, int id) {
            this.title = title;
            this.description = description;
            this.id = id;
            this.category = Category.GENERAL;
        }

        public Fact(String title, String description, int id, Category category) {
            this(title, description, id);
            this.category = category;
        }

        public String getTitle() {
            return this.title;
        }

        public int getId() {
            return this.id;
        }

        public String getDescription() {
            return this.description;
        }

        public Category getCategory() { return this.category; }
    }
}
