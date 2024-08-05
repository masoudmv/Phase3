package shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.awt.Polygon;
import java.io.IOException;

public class PolygonSerializer extends JsonSerializer<Polygon> {

    @Override
    public void serialize(Polygon polygon, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("xpoints");
        for (int x : polygon.xpoints) {
            gen.writeNumber(x);
        }
        gen.writeEndArray();
        gen.writeArrayFieldStart("ypoints");
        for (int y : polygon.ypoints) {
            gen.writeNumber(y);
        }
        gen.writeEndArray();
        gen.writeNumberField("npoints", polygon.npoints);
        gen.writeEndObject();
    }
}
