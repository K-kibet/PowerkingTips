package com.kibet.powerking;

public class Tip {
    public String getHomeTeam() {
        return homeTeam;
    }
    public String getAwayTeam() {
        return awayTeam;
    }
    public String getPrediction() {
        return prediction;
    }
    public String getOdds() {
        return odds;
    }
    public String getStatus() {
        return status;
    }
    public String getWon() {
        return won;
    }
    public String getId() {
        return id;
    }
    private final String homeTeam;
    private final String awayTeam;
    private final String prediction;
    private final String odds;
    private final String status;
    private final String won;
    private final String id;

    public Tip(String homeTeam, String awayTeam, String prediction, String odds, String status, String won, String id) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.prediction = prediction;
        this.odds = odds;
        this.status = status;
        this.won = won;
        this.id = id;
    }
}
