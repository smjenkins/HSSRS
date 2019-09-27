package edu.umuc.models;

/**
 * Model to hold a variety of general properties.
 */
public class GeneralProperties {

    private int startingYear;

    public int getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    @Override
    public String toString() {
        return "GeneralProperties{" +
                "startingYear=" + startingYear +
                '}';
    }
}
