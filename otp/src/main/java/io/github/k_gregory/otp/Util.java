package io.github.k_gregory.otp;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {
    public static <T> String readResource(Class<? extends T> cls, String name) throws IOException {
        InputStream input = cls.getResourceAsStream(name);
        String s = IOUtils.toString(input, StandardCharsets.UTF_8);
        input.close();
        return s;
    }
}
