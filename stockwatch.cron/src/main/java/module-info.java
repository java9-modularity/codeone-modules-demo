import codeone.stockwatch.api.StockwatchStorage;
import codeone.symbolfetch.SymbolFetcher;

module stockwatch.cron {
    requires stockwatch.api;
    requires symbolfetcher;
    requires slf4j.api;

    exports codeone.stockwatch.cron;

    uses StockwatchStorage;
    uses SymbolFetcher;
}