import codeone.symbolfetch.SymbolFetcher;

module symbolfetcher {
    requires java.net.http;
    exports codeone.symbolfetch;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    provides SymbolFetcher with codeone.symbolfetch.alphavantage.AlphavantageFetcher;

    opens codeone.symbolfetch.alphavantage to com.fasterxml.jackson.databind;
}