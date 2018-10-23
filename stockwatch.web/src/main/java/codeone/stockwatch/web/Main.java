package codeone.stockwatch.web;

import codeone.stockwatch.api.StockWatch;
import codeone.stockwatch.api.StockwatchStorage;
import codeone.stockwatch.cron.PriceCheckCronService;
import codeone.symbolfetch.StockPrice;
import codeone.symbolfetch.SymbolFetcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class Main {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private StockwatchStorage stockwatchStorage;
    private SymbolFetcher symbolFetcher;

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        this.stockwatchStorage = locateService(StockwatchStorage.class);
        this.symbolFetcher = locateService(SymbolFetcher.class);

        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.post("/watches")
                .handler(BodyHandler.create())
                .handler(this::postHandler);

        router.get("/watches").handler(ctx -> ctx.response().end(Json.encode(stockwatchStorage.list())));

        PriceCheckCronService.getInstance().startCron();

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void postHandler(RoutingContext ctx) {
        JsonObject json = ctx.getBodyAsJson();

        symbolFetcher.fetch(json.getString("symbol"))
                .whenComplete((result, error) -> {

                    if (error != null) {
                        LOGGER.error("Error fetch price for {}", json.getString("symbol"));
                        ctx.response().setStatusCode(500).end(error.getMessage());
                    } else {
                        if (result.isEmpty()) {
                            LOGGER.error("Empty result from fetcher");
                            ctx.response().setStatusCode(500).end("Empty response");
                        } else {
                            StockPrice stockPrice = result.get(0);

                            var stockWatch = new StockWatch(
                                    json.getString("symbol"),
                                    json.getDouble("changePercentage"),
                                    stockPrice.getPrice()
                            );

                            LOGGER.info("Storing stockwatch {}", stockWatch);

                            stockwatchStorage.store(stockWatch);
                            ctx.response().end(Json.encode(stockWatch));
                        }
                    }
                });
    }

    private static <T> T locateService(Class<T> serviceType) {
        return ServiceLoader.load(serviceType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Service of type %s not found", serviceType.getName())));
    }
}
