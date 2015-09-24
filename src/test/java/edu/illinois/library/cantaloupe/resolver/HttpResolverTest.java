package edu.illinois.library.cantaloupe.resolver;

import edu.illinois.library.cantaloupe.Application;
import edu.illinois.library.cantaloupe.image.SourceFormat;
import junit.framework.TestCase;
import org.apache.commons.configuration.BaseConfiguration;

import java.io.IOException;

public class HttpResolverTest extends TestCase {

    HttpResolver instance;

    public void setUp() throws IOException {
        BaseConfiguration config = new BaseConfiguration();
        config.setProperty("HttpResolver.url_prefix", "http://localhost/");
        Application.setConfiguration(config);

        instance = new HttpResolver();
    }

    public void testGetInputStream() {
        try {
            assertNull(instance.getInputStream("bogus"));
            fail("Expected exception");
        } catch (IOException e) {
            // pass
        }
        // TODO: test against a real server
    }

    public void testGetSourceFormat() {
        assertEquals(SourceFormat.JPG, instance.getSourceFormat("image.jpg"));
        assertEquals(SourceFormat.UNKNOWN, instance.getSourceFormat("image.bogus"));
        assertEquals(SourceFormat.UNKNOWN, instance.getSourceFormat("image"));
    }

    public void testGetUrl() {
        BaseConfiguration config = (BaseConfiguration) Application.getConfiguration();
        // with prefix
        config.setProperty("HttpResolver.url_prefix",
                "http://example.org/prefix/");
        assertEquals("http://example.org/prefix/id",
                instance.getUrl("id").toString());
        // with suffix
        config.setProperty("HttpResolver.url_suffix", "/suffix");
        assertEquals("http://example.org/prefix/id/suffix",
                instance.getUrl("id").toString());
        // without prefix or suffix
        config.setProperty("HttpResolver.url_prefix", "");
        config.setProperty("HttpResolver.url_suffix", "");
        assertEquals("http://example.org/images/image.jpg",
                instance.getUrl("http://example.org/images/image.jpg").toString());
        // with path separator
        config.setProperty("HttpResolver.url_prefix", "http://example.org/");
        config.setProperty("HttpResolver.url_suffix", "");
        String separator = "CATS";
        config.setProperty("HttpResolver.path_separator", separator);
        assertEquals("http://example.org/1/2/3",
                instance.getUrl("1" + separator + "2" + separator + "3").toString());
    }

}
