package com.blueprint.foe.beetracker.Model;

/**
 * This class represents everything about a fact or call-to-action. These facts are displayed on
 * the LearnActivity.
 */
public class Fact {
    private static final String TAG = Fact.class.toString();

    public enum Category {
        General, Water
    }

    String mTitle;
    String mDescription;
    Category mCategory;
    int mId;
    boolean mCompleted;

    public Fact(String title, String description, int id) {
        this.mTitle = title;
        this.mDescription = description;
        this.mId = id;
        this.mCategory = Category.General;
        this.mCompleted = false;
    }

    public Fact(String title, String description, int id, Category category) {
        this(title, description, id);
        this.mCategory = category;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public int getId() {
        return this.mId;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public Category getCategory() { return this.mCategory; }

    public boolean isCompleted() { return this.mCompleted;}

    public void setCompleted() {
        this.mCompleted = true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Fact)) {
            return false;
        }
        if (other == this) return true;

        Fact that = (Fact) other;

        return this.mTitle.equals(that.mTitle)
                && this.mDescription.equals(that.mDescription)
                && this.mId == that.mId
                && this.mCategory.equals(that.mCategory)
                && this.mCompleted == that.mCompleted;
    }
}