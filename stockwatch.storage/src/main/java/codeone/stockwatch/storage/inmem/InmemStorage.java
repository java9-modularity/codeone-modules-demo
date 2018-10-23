package codeone.stockwatch.storage.inmem;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockwatchStorage;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InmemStorage implements StockwatchStorage {
    private final static StockwatchStorage instance = new InmemStorage();
    private final List<StockWatch> storage = new CopyOnWriteArrayList<>();

    @Override
    public void store(StockWatch stockWatch) {
        storage.add(stockWatch);
    }

    @Override
    public List<StockWatch> list() {
        return Collections.unmodifiableList(storage);
    }

    public static StockwatchStorage provider() {
        return instance;
    }
}
