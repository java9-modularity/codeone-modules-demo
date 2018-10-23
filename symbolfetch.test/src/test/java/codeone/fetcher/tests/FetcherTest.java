package codeone.fetcher.tests;

import codeone.fetcher.test.FetcherLocator;
import codeone.symbolfetch.SymbolFetcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FetcherTest {
    @Test
    public void testLocate() {
        SymbolFetcher locate = new FetcherLocator().locate();
        assertNotNull(locate);
    }
}
