package codeone.stockwatch.cron.impl;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockwatchStorage;
import codeone.stockwatch.cron.PriceCheckCronService;
import codeone.symbolfetch.SymbolFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TimerCronService implements PriceCheckCronService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TimerCronService.class);

    @Override
    public void startCron() {
        var fetcher = locateService(SymbolFetcher.class);
        var storage = locateService(StockwatchStorage.class);

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("Checking prices...");

                Map<String, StockWatch> stocks = storage.list().stream().collect(Collectors.toMap(StockWatch::getSymbol, watch -> watch));
                String[] symbols = stocks.keySet().toArray(new String[0]);
                stocks.keySet().toArray(symbols);

                fetcher.fetch(symbols)
                        .whenComplete((result, error) -> {
                            if(error != null) {
                                LOGGER.error("Error fetching prices", error);
                                return;
                            }
                            System.out.println(result);
                            result.stream()
                                    .filter(price -> stocks.get(price.getSymbol()).shouldTrigger(price.getPrice()))
                                    .forEach(triggered -> LOGGER.info("Triggered for {} with a price of {}", triggered.getSymbol(), triggered.getPrice()));
                        });

            }
        }, 5000, 3000);
    }

    private static <T> T locateService(Class<T> serviceType) {
        return ServiceLoader.load(serviceType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Service of type %s not found", serviceType.getName())));
    }
}
