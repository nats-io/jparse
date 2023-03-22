package io.nats.jparse.examples;

import io.nats.jparse.Json;
import io.nats.jparse.node.Node;
import io.nats.jparse.node.ObjectNode;
import io.nats.jparse.source.Sources;

import java.io.File;

public class CloudEventMain {

    public static void main(String... args) {

        final File file = new File("./src/test/resources/cloudevents/glossaryEvent.json");

        final ObjectNode objectNode = Json.toRootNode(Sources.fileSource(file)).asObject();

        if (objectNode.getNode("subject").equalsContent("glossaryFeed")) {
            final String id = objectNode.getString("id");
            final String type = objectNode.getString("type");
            final String data = Json.serializeToString(objectNode.getNode("data"));
            System.out.printf("%s %s %s \n%s\n", "glossaryFeed", id, type, data);
        }

    }

}
