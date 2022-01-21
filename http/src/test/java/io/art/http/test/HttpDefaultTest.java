package io.art.http.test;

import io.art.http.*;
import io.art.http.meta.*;
import io.art.http.test.meta.*;
import io.art.http.test.registry.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;

public class HttpDefaultTest {
    private static final Path testFile = Paths.get("test.txt");

    @BeforeAll
    public static void setup() {
        writeFile(testFile, "test");
        initialize(
                meta(() -> new MetaHttpTest(new MetaMetaTest(new MetaHttp()))),
                transport(),
                json(),
                http(http -> http.server(server -> server.file("/file", testFile).configure(serverConfigurator -> serverConfigurator.port(1234))))
        );
    }

    @AfterAll
    public static void cleanup() {
        recursiveDelete(testFile.toFile());
        shutdown();
    }

    @Test
    public void testHttpDefaultCommunicator() {
        Path downloaded = Paths.get("downloaded.txt");
        String url = HTTP_SCHEME + SCHEME_DELIMITER + LOCALHOST_IP_ADDRESS + COLON + 1234;
        Http.http()
                .get(url + "/file")
                .execute()
                .download(downloaded);
        assertTrue(downloaded.toFile().exists());
        assertEquals(readFile(downloaded), "test");
    }
}
