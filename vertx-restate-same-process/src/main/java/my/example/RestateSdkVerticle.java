package my.example;

import dev.restate.sdk.endpoint.Endpoint;
import dev.restate.sdk.http.vertx.HttpEndpointRequestHandler;
import dev.restate.sdk.http.vertx.RestateHttpServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RestateSdkVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(RestateSdkVerticle.class);

    private final List<Object> restateServices;

    public RestateSdkVerticle(List<Object> restateServices) {
        this.restateServices = restateServices;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        // Create the Endpoint object used by the Restate SDK
        Endpoint.Builder endpointBuilder = Endpoint.builder();
        for (Object service : restateServices) {
            endpointBuilder.bind(service);
        }

        // Instantiate the HttpEndpointRequestHandler.
        // This is a regular Vert.x HTTP handler serving the Restate SDK.
        Handler<HttpServerRequest> httpHandler = HttpEndpointRequestHandler.fromEndpoint(
              endpointBuilder.build()
        );

        // Start the HTTP Server serving the Restate SDK
        vertx.createHttpServer()
                .requestHandler(httpHandler)
                .listen(9080)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(ar.cause());
                    }
                })
                .onSuccess(server ->
                        LOG.info("RestateSdkVerticle HTTP server started on port {}", server.actualPort()))
                .onFailure(e ->
                        LOG.error("RestateSdkVerticle HTTP server start failed", e));
    }
}
