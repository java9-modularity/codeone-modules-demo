package codeone.fetcher.test;

import codeone.symbolfetch.SymbolFetcher;

import java.util.ServiceLoader;

public class FetcherLocator {
    public SymbolFetcher locate() {
        return ServiceLoader.load(SymbolFetcher.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fetcher service not found"));
    }
}
