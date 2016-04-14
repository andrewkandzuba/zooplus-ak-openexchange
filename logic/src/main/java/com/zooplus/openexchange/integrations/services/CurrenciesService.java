package com.zooplus.openexchange.integrations.services;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CurrenciesService {
    List<Currency> getCurrencies();
    List<Rate> getRates(Date date, Optional<Currency> basic);
}
