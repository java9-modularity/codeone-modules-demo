package codeone.stockwatch.api;

import java.util.List;

public interface StockwatchStorage {
    void store(StockWatch stockWatch);
    List<StockWatch> list();
}
