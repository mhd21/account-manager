package com.mpakbaz.accountManager.infrastructure;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Service
public class HttpClient {

    public WebClient client() {
        return WebClient.create();
    }

    public ResponseSpec getRequest(String url) {
        return this.client().get().uri(url).accept(MediaType.APPLICATION_JSON).retrieve();
    }

}
