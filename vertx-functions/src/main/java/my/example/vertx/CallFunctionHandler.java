package my.example.vertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

/**
 * This HTTP Handler implements the logic to invoke functions using HTTP.
 * It can be mounted on any Vert.x HTTP Server.
 */
public class CallFunctionHandler implements Handler<HttpServerRequest> {

    private final Vertx vertx;

    public CallFunctionHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(HttpServerRequest request) {
        request.body()
                .compose(bodyBuffer -> {
                    // The json sent to this http request contains function name, and input.
                    var inputJson = new JsonObject(bodyBuffer);
                    var functionName = Objects.requireNonNull(inputJson.getString("function"), "'function' field should not be null");
                    var functionInput = Objects.requireNonNull(inputJson.getJsonObject("input"), "'input' field should not be null");

                    // Now let's call the vertx function using the event bus
                    return vertx.eventBus()
                            .<JsonObject>request(functionName, functionInput)
                            .map(Message::body);
                })
                .onSuccess(functionOutput -> {
                    var response = request.response();

                    // Send the success response
                    response.setStatusCode(200);
                    response.putHeader(CONTENT_TYPE, "application/json");
                    response.end(functionOutput.toBuffer());
                })
                .onFailure(t -> {
                    var response = request.response();

                    // Send the failure response
                    response.setStatusCode(500);
                    response.putHeader(CONTENT_TYPE, "text/plain");
                    response.end(t.toString());
                });

    }
}
