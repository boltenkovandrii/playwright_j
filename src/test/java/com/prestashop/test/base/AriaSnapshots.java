package com.prestashop.test.base;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class AriaSnapshots {

    private AriaSnapshots() {
    }

    public static String load(String name, String locale) {
        String path = "/snapshots/prestashop/" + name + "_" + locale + ".yml";
        try (InputStream stream = AriaSnapshots.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("ARIA snapshot resource is not available: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load ARIA snapshot resource: " + path, exception);
        }
    }
}
