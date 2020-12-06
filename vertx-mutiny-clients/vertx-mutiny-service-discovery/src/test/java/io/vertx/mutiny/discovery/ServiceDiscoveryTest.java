package io.vertx.mutiny.discovery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.servicediscovery.ServiceDiscovery;
import io.vertx.mutiny.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

public class ServiceDiscoveryTest {

    private Vertx vertx;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
    }

    @After
    public void tearDown() {
        vertx.closeAndAwait();
    }

    @Test
    public void test() {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("service-announce")
                        .setName("my-name"));

        Record record = new Record()
                .setType("eventbus-service-proxy")
                .setLocation(new JsonObject().put("endpoint", "the-service-address"))
                .setName("my-service")
                .setMetadata(new JsonObject().put("some-label", "some-value"));

        discovery.publishAndAwait(record);

        record = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");
        discovery.publishAndAwait(record);

        discovery.close();
    }

}
