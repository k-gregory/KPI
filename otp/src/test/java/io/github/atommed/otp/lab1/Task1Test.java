package io.github.atommed.otp.lab1;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class Task1Test{
    @Test public void javaKnowlege(){
	assertTrue(Integer.valueOf(100) == Integer.valueOf(100));
	assertTrue(Integer.valueOf(200) != Integer.valueOf(200));
    }

    @Test
    public void task1() {
        InputStream input = getClass().getResourceAsStream("/lab1/task1.txt");
    }
}

