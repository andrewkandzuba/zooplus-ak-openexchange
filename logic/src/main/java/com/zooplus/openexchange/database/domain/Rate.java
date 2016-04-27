package com.zooplus.openexchange.database.domain;

public class Rate {
    private Currency basic;
    private Currency alternative;
    private Double rate;

    public Rate() {
    }

    public Rate(Currency base, Currency alternative, Double rate) {
        this.basic = base;
        this.alternative = alternative;
        this.rate = rate;
    }

    public Currency getBasic() {
        return basic;
    }

    public void setBasic(Currency basic) {
        this.basic = basic;
    }

    public Currency getAlternative() {
        return alternative;
    }

    public void setAlternative(Currency alternative) {
        this.alternative = alternative;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
