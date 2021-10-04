package com.mpakbaz.accountManager.infrastructure.ExchangeService;

import com.mpakbaz.accountManager.infrastructure.HttpClient;
import com.mpakbaz.accountManager.infrastructure.ExchangeService.entities.ExchangeRateResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Service
public class ExchangeServiceProvider {

    @Autowired
    private HttpClient httpClient;

    public double getUSDEURRate() {
        ResponseSpec responseSpec = this.httpClient.getRequest(
                "http://api.currencylayer.com/live?access_key=97b9b65771c8c78ee1f886caba6ddea3&currencies=EUR");
        ExchangeRateResponse responseBody = responseSpec.bodyToMono(ExchangeRateResponse.class).block();
        return responseBody.quotes.USDEUR;
    }

    public double getEURUSDRate() {
        double usdToEur = this.getUSDEURRate();
        return 1 / usdToEur;
    }
}
