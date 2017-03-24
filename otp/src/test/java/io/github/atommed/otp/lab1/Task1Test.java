package io.github.atommed.otp.lab1;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Task1Test{
    @Test public void javaKnowlege(){
	assertTrue(Integer.valueOf(100) == Integer.valueOf(100));
	assertTrue(Integer.valueOf(200) != Integer.valueOf(200));
    }

    @Test
    public void task1() throws IOException {
        InputStream input = getClass().getResourceAsStream("/lab1/task1.txt");
        String str = IOUtils.toString(input, StandardCharsets.UTF_8);
        assertEquals(str.split("\n")[0],"this");
    }
}

