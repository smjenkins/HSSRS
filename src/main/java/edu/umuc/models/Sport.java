package edu.umuc.models;

import java.util.Objects;

/**
 * Represents a Sport and stores the information needed to build the url to scrape data
 * example: fall is the season and football is the path in https://www.washingtonpost.com/allmetsports/2018-fall/paint-branch/football/
 */
public class Sport implements Comparable<Sport>{

    /**
     * The name of the sport to be displayed
     */
    private String name;
    
    /** 
     * The season for the sport to be used in the path to scrape the data
     */
    private String season;
    
    /**
     * The path for the sport to be used in the url creation
     */
    private String path;

    public Sport(String name, String season, String path) {
        this.name = name;
        this.season = season;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(Sport other) {
        return name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sport other = (Sport) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.season, other.season)) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }
    
    
}
