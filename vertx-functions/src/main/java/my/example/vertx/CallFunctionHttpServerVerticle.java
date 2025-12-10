package my.example.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This starts the server exposing the {@link CallFunctionHandler}.
 * <p>
 * Microdose might have some way to register a Vert.x HTTP handler without setting up the http server itself,
 * but just providing it to some configuration/method in the AbstractMicrodoseVerticle.
 */
public class CallFunctionHttpServerVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(CallFunctionHttpServerVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        Handler<HttpServerRequest> httpHandler = new CallFunctionHandler(vertx);

        vertx.createHttpServer()
                .requestHandler(httpHandler)
                .listen(8081)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(ar.cause());
                    }
                })
                .onSuccess(server ->
                        LOG.info("CallFunction HTTP server started on port {}", server.actualPort()))
                .onFailure(e ->
                        LOG.error("CallFunction HTTP server start failed", e));
    }

}
