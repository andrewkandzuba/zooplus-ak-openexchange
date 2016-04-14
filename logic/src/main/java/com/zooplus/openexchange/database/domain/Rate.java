package com.zooplus.openexchange.database.domain;

import java.math.BigDecimal;

public class Rate {
    private Currency basic;
    private Currency alternative;
    private BigDecimal rate;

    public Rate() {
    }

    public Rate(Currency base, Currency alternative, BigDecimal rate) {
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
