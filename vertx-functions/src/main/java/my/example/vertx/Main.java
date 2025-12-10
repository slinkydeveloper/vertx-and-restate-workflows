package my.example.vertx;

import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // The code below is only deploying verticles.
        // It might not be necessary when using the Microdose framework
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FunctionsVerticle())
                .onSuccess(id -> LOG.info("Verticle deployed {}", id))
                .onFailure(e -> LOG.error("Cannot start verticle", e));
        vertx.deployVerticle(new CallFunctionHttpServerVerticle())
                .onSuccess(id -> LOG.info("Verticle deployed {}", id))
                .onFailure(e -> LOG.error("Cannot start verticle", e));
    }

}
