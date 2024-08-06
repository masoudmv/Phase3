package shared.Model;

public class Match {

    private double survivalTime;
    private int gainedXP;

    public Match(double survivalTime, int gainedXP) {
        this.survivalTime = survivalTime;
        this.gainedXP = gainedXP;
    }

    public Match() {
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
