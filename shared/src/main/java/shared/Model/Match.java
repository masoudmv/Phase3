package shared.Model;

public class Match {
    private String username;
    private double survivalTime;
    private int gainedXP;

    public Match(String username, double survivalTime, int gainedXP) {
        this.username = username;
        this.survivalTime = survivalTime;
        this.gainedXP = gainedXP;
    }

    public Match() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(double survivalTime) {
        this.survivalTime = survivalTime;
    }

    public int getGainedXP() {
        return gainedXP;
    }

    public void setGainedXP(int gainedXP) {
        this.gainedXP = gainedXP;
    }
}
