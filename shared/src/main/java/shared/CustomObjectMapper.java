package shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.awt.*;

public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Polygon.class, new PolygonSerializer());
        module.addDeserializer(Polygon.class, new PolygonDeserializer());
        this.registerModule(module);
    }
}
