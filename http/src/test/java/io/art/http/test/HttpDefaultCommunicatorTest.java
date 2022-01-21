package io.art.http.test;

import io.art.http.*;
import io.art.http.meta.*;
import io.art.http.test.meta.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;

public class HttpDefaultCommunicatorTest {
    private final static String downloadFileName = "example.html";

    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaHttpTest(new MetaMetaTest(new MetaHttp()))),
                transport(),
                json(),
                http()
        );
    }

    @AfterAll
    public static void cleanup() {
        recursiveDelete(downloadFileName);
        shutdown();
    }

    @Test
    public void testHttpDefaultCommunicatorWithSlash() {
        Path downloaded = Paths.get(downloadFileName);
        Http.http()
                .get("https://example.com/")
                .execute()
                .download(downloaded);
        assertTrue(downloaded.toFile().exists() && readFile(downloaded).contains("Example Domain"));
    }

    @Test
    public void testHttpDefaultCommunicatorWithoutSlash() {
        Path downloaded = Paths.get(downloadFileName);
        Http.http()
                .get("https://example.com")
                .execute()
                .download(downloaded);
        assertTrue(downloaded.toFile().exists() && readFile(downloaded).contains("Example Domain"));
    }
}
