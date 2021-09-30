package com.mpakbaz.accountManager.infrastructure.ExchangeService.entities;

import com.mpakbaz.accountManager.constants.Currencies;

public class ExchangeRateResponse {

    public class Quotes {
        public double USDEUR = 0;
    }

    public Boolean success = false;

    public String source = Currencies.USD;

    public Quotes quotes;

}
