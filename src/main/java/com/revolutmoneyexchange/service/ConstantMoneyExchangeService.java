package com.revolutmoneyexchange.service;

import com.revolutmoneyexchange.model.Currency;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is implementation of the <code>MoneyExchangeService</code> which uses hardcoded exchange rates.
 * It has been implemented for simplicity. If later on we will need to have an integration with 3rd party service
 * it will be possible by make another implementation
 */
public class ConstantMoneyExchangeService implements MoneyExchangeService {
    private final static Map<Pair<Currency, Currency>, BigDecimal> conversionRates =
            Collections.unmodifiableMap(new HashMap<Pair<Currency, Currency>, BigDecimal>() {{
                put(new Pair<>(Currency.INR, Currency.EUR), BigDecimal.valueOf(0.013));
                put(new Pair<>(Currency.INR, Currency.USD), BigDecimal.valueOf(0.015));
                put(new Pair<>(Currency.INR, Currency.INR), BigDecimal.valueOf(1D));
                put(new Pair<>(Currency.USD, Currency.EUR), BigDecimal.valueOf(0.86));
                put(new Pair<>(Currency.USD, Currency.USD), BigDecimal.valueOf(1D));
                put(new Pair<>(Currency.USD, Currency.INR), BigDecimal.valueOf(68.12));
                put(new Pair<>(Currency.EUR, Currency.EUR), BigDecimal.valueOf(1D));
                put(new Pair<>(Currency.EUR, Currency.USD), BigDecimal.valueOf(1.17));
                put(new Pair<>(Currency.EUR, Currency.INR), BigDecimal.valueOf(79.43));
            }});

    @Override
    public BigDecimal exchange(BigDecimal amount, Currency amountCurrency, Currency targetCurrency) {
        return amount.multiply(conversionRates.get(new Pair<>(amountCurrency, targetCurrency)));
    }
}
