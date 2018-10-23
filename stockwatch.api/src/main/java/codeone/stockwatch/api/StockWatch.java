package codeone.stockwatch.api;

import java.util.Objects;

public class StockWatch {
    private final String symbol;
    private final double changePercentage;
    private final double basePrice;

    public StockWatch(String symbol, double changePercentage, double basePrice) {
        this.symbol = symbol;
        this.changePercentage = changePercentage;
        this.basePrice = basePrice;
    }

    public boolean shouldTrigger(double currentPrice) {
        double percentage =  currentPrice / basePrice * 100;
        if(changePercentage < 0) {
            return percentage <= (100 + changePercentage);
        } else {
            return percentage >= (100 + changePercentage);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockWatch that = (StockWatch) o;
        return Double.compare(that.changePercentage, changePercentage) == 0 &&
                Double.compare(that.basePrice, basePrice) == 0 &&
                Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, changePercentage, basePrice);
    }

    @Override
    public String toString() {
        return "StockWatch{" +
                "symbol='" + symbol + '\'' +
                ", changePercentage=" + changePercentage +
                ", basePrice=" + basePrice +
                '}';
    }
}
