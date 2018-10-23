package codeone.symbolfetch;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SymbolFetcher {
    CompletableFuture<List<StockPrice>> fetch(String... symbols);
}
