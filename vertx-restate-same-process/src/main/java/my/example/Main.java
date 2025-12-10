package my.example;

import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // The code below is only deploying verticles.
        // It might not be necessary when using the Microdose framework
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FunctionsVerticle())
                .onSuccess(id -> LOG.info("Verticle deployed {}", id))
                .onFailure(e -> LOG.error("Cannot start verticle", e));
        vertx.deployVerticle(new RestateSdkVerticle(List.of(
                        // Add the restate services you want to deploy here
                        new UpdateWorkflow(vertx)
                )))
                .onSuccess(id -> LOG.info("Verticle deployed {}", id))
                .onFailure(e -> LOG.error("Cannot start verticle", e));
    }

}
