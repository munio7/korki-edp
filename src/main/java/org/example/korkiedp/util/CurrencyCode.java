package org.example.korkiedp.model;

public enum CurrencyCode {
    PLN,
    EUR,
    USD,
    GBP,
    CZK;
    @Override
    public String toString() {
        return name();
    }

}

