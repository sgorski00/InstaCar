package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.InstaCar.config.exceptions.ApiRequestException;
import pl.studia.InstaCar.model.dto.RateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class WebClientService {

    private final WebClient webClient;

    @Autowired
    public WebClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * This method returns last rate for given currency from NBP API.
     * @param currency 3 letter currency code
     * @return rate for given currency
     * @throws ApiRequestException when API request code is not 2xx
     */
    private Mono<RateResponse> getNbpResponse(String currency) {
        return webClient.get()
                .uri("/{currency}?format=json", currency)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response -> response
                                .bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new ApiRequestException("API error: " + error)))
                        )
                .bodyToMono(RateResponse.class);
    }

    /**
     * This method retrieves rates for multiple currencies.
     * @param currencies 3-letter currency codes
     * @return List<RateResponse> a list of rate responses
     */
    public List<RateResponse> getRates(Iterable<String> currencies) {
        return Flux.fromIterable(currencies)
                .flatMap(this::getNbpResponse)
                .collectList()
                .block();
    }
}
