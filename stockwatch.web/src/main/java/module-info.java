import codeone.stockwatch.api.StockwatchStorage;
import codeone.symbolfetch.SymbolFetcher;

module stockwatch.web {
    uses StockwatchStorage;
    uses SymbolFetcher;

    requires vertx.core;
    requires vertx.web;
    requires stockwatch.api;
    requires symbolfetcher;
    requires stockwatch.cron;

    requires slf4j.api;
}