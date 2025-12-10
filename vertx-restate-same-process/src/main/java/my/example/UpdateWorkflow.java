package my.example;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.CustomSerdeFactory;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@Service
@CustomSerdeFactory(CustomJacksonSerdeFactory.class)
public class UpdateWorkflow {

    private final Vertx vertx;

    public UpdateWorkflow(Vertx vertx) {
        this.vertx = vertx;
    }

    @Handler
    public JsonObject run(Context ctx, JsonObject req) {
        // Call update-merchant function
        JsonObject updateMerchantResult = ctx.run(
                "update-merchant",
                JsonObject.class,
                () -> callFunction("update-merchant", req)
        );

        // Call update-address function
        JsonObject updateAddressResult = ctx.run(
                "update-address",
                JsonObject.class,
                () -> callFunction("update-address", req)
        );

        // Assemble result
        JsonObject resultJson = new JsonObject();
        resultJson.put("updateAddressResult", updateAddressResult);
        resultJson.put("updateMerchantResult", updateMerchantResult);

        return resultJson;
    }

    private JsonObject callFunction(String function, JsonObject req) throws Exception {
        // Call the functions exposed on the Vert.x event bus
        var vertxFuture = vertx.eventBus()
                .<JsonObject>request(function, req)
                .toCompletionStage()
                .toCompletableFuture();
        return vertxFuture.get().body();
    }

}


