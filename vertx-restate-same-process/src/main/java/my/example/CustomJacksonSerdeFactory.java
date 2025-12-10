package my.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.restate.serde.jackson.JacksonSerdeFactory;
import io.vertx.core.json.jackson.VertxModule;

/**
 * This overrides the Restate default's {@link JacksonSerdeFactory} to support Vert.x JSON types.
 */
class CustomJacksonSerdeFactory extends JacksonSerdeFactory {
  public CustomJacksonSerdeFactory() {
    super(new ObjectMapper()
            // Add to Jackson ObjectMapper the Vert.x module, to serialize/deserialize Vert.x JSON types
            .registerModule(new VertxModule()));
  }
}