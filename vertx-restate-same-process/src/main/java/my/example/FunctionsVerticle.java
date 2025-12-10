package my.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This mimics the function registration in Microdose
 */
public class FunctionsVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(FunctionsVerticle.class);

    @Override
    public void start() {
        // These are just some example functions

        vertx.eventBus().<JsonObject>consumer(
                "update-address",
                message -> {
                    LOG.info("Got a request to update-address, echoing the response {}", message.body());
                    message.reply(message.body());
                }
        );

        vertx.eventBus().<JsonObject>consumer(
                "update-merchant",
                message -> {
                    LOG.info("Got a request to update-merchant, echoing the response {}", message.body());
                    message.reply(message.body());
                }
        );
    }
}
