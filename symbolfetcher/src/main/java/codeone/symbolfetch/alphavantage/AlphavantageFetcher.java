package codeone.symbolfetch.alphavantage;

import codeone.symbolfetch.StockPrice;
import codeone.symbolfetch.SymbolFetcher;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AlphavantageFetcher implements SymbolFetcher {
    @Override
    public CompletableFuture<List<StockPrice>> fetch(String... symbols) {

        if(symbols.length == 0) {
            return CompletableFuture.completedFuture(List.of());
        }


        String symbolsString = String.join(",", symbols);
        System.out.println(symbolsString);

        String url = String.format("https://www.alphavantage.co/query?function=BATCH_QUOTES_US&symbols=%s&apikey=GHKKM5CDKE525WQE", symbolsString);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::readStockFromJson);

    }

    private List<StockPrice> readStockFromJson(String body) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            try {
                BatchQuote batchQuote = mapper.readValue(body, BatchQuote.class);
                return batchQuote.quotes.stream().map(q -> new StockPrice(q.symbol, q.price)).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        static class BatchQuote {
            @JsonProperty("Stock Batch Quotes")
            List<Quote> quotes;
        }

        static class Quote {
            @JsonProperty("1. symbol")
            String symbol;

            @JsonProperty("5. price")
            double price;
        }
}
