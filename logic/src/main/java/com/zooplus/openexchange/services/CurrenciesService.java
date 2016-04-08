package com.zooplus.openexchange.services;

import com.zooplus.openexchange.database.domain.Currency;
import java.util.List;

public interface CurrenciesService {
    List<Currency> getCurrencies();
}
