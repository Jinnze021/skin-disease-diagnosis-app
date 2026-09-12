package com.project.demo.easydl;

public final class DiagnosisResult {

    private final String name;
    private final double score;

    public DiagnosisResult(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
}
