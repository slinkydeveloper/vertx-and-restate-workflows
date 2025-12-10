package com.example.restatestarter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.springboot.RestateService;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@RestateService
public class UpdateWorkflow {

    private final RestClient restClient;

    public UpdateWorkflow(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Handler
    public JsonNode run(Context ctx, JsonNode req) {
        // Call update-merchant function
        JsonNode updateMerchantResult = ctx.run(
                "update-merchant",
                JsonNode.class,
                () -> callFunction("update-merchant", req)
        );

        // Call update-address function
        JsonNode updateAddressResult = ctx.run(
                "update-address",
                JsonNode.class,
                () -> callFunction("update-address", req)
        );

        // Assemble result
        ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
        resultNode.set("updateAddressResult", updateAddressResult);
        resultNode.set("updateMerchantResult", updateMerchantResult);

        return resultNode;
    }

    private JsonNode callFunction(String function, JsonNode req) {
        // Build the payload expected by the Vert.x handler
        Map<String, Object> payload = new HashMap<>();
        payload.put("function", function);
        payload.put("input", req);

        // Call the CallFunctionHandler on the Vert.x side
        return restClient
                .post()
                .uri("http://localhost:8081")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(JsonNode.class);
    }

}


