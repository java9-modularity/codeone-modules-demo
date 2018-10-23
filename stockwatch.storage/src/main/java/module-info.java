import codeone.stockwatch.api.StockwatchStorage;
import codeone.stockwatch.storage.inmem.InmemStorage;

module stockwatch.storage {
    requires stockwatch.api;

    provides StockwatchStorage with InmemStorage;
}