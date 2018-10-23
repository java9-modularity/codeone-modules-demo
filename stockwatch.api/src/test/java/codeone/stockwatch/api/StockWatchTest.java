package codeone.stockwatch.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StockWatchTest {
    @ParameterizedTest
    @MethodSource("stockWatches")
    void parameterizedTest(WatchResult watchResult) {
        assertEquals(watchResult.stockWatch.shouldTrigger(watchResult.price), watchResult.result);
    }

    static Stream<WatchResult> stockWatches() {
        return Stream.of(
                new WatchResult(new StockWatch("NFLX", 10, 400), 441, true),
                new WatchResult(new StockWatch("NFLX", 10, 400), 409, false),
                new WatchResult(new StockWatch("NFLX", -10, 400), 360, true),
                new WatchResult(new StockWatch("NFLX", -10, 400), 390, false)
        );
    }


    static class WatchResult {
        final StockWatch stockWatch;
        final double price;

        final boolean result;

        public WatchResult(StockWatch stockWatch, double price, boolean result) {
            this.stockWatch = stockWatch;
            this.price = price;
            this.result = result;
        }
    }

}