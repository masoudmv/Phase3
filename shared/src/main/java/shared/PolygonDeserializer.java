package shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.awt.Polygon;
import java.io.IOException;

public class PolygonDeserializer extends StdDeserializer<Polygon> {

    public PolygonDeserializer() {
        this(null);
    }

    public PolygonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Polygon deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode xpointsNode = node.get("xpoints");
        JsonNode ypointsNode = node.get("ypoints");
        int npoints = (int) node.get("npoints").numberValue();

        int[] xpoints = new int[xpointsNode.size()];
        int[] ypoints = new int[ypointsNode.size()];

        for (int i = 0; i < xpointsNode.size(); i++) {
            xpoints[i] = xpointsNode.get(i).intValue();
        }

        for (int i = 0; i < ypointsNode.size(); i++) {
            ypoints[i] = ypointsNode.get(i).intValue();
        }

        return new Polygon(xpoints, ypoints, npoints);
    }
}
