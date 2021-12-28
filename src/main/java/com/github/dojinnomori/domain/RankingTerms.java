package com.github.dojinnomori.domain;

public enum RankingTerms {

    DAILY("daily"),

    WEEKLY("weekly"),

    MONTHLY("monthly"),

    ALL("all");

    private final String term;

    RankingTerms(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
