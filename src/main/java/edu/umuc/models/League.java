package edu.umuc.models;

import java.util.List;

/**
 * Stores information about a league
 */
public class League {
    /**
     * The leagueId to be used like a primary key
     */
    private String leagueId;
    
    /**
     * The display name of the league
     */
    private String name;
    
    /**
     * The weight of the league used in the calculation of Rank Points for a school
     */
    private float weight;
    
    /**
     * List of all schools in the league
     */
    private List<String> schools;

    public League() {
        
    }
    
    public League(String leagueId, String leagueName, float leagueWeight) {
        this.leagueId = leagueId;
        this.name = leagueName;
        this.weight = leagueWeight;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String leagueName) {
        this.name = leagueName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float leagueWeight) {
        this.weight = leagueWeight;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public boolean containsSchool(String schoolName) {
        return schools.stream().anyMatch(school -> school.equals(schoolName));
    }

    @Override
    public String toString() {
        return "League{" + "leagueId=" + leagueId + ", name=" + name + ", weight=" + weight + ", schools=" + schools + '}';
    }
}
