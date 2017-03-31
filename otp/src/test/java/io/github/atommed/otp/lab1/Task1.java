package io.github.atommed.otp.lab1;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import static org.hamcrest.collection.IsArrayContainingInAnyOrder.*;

public class Task1 {
    private String readResource(String name) throws IOException {
        InputStream input = getClass().getResourceAsStream(name);
        String s = IOUtils.toString(input, StandardCharsets.UTF_8);
        input.close();
        return s;
    }

    @Test
    public void filterWords() throws IOException {
        Pattern pattern = Pattern.compile("[0-9]*\\*E[0-9]*");
        Stream<String> words = stream(
                readResource("/lab1/task1/words.txt").split("\\R+")
        ).filter(s -> pattern.matcher(s).matches());
        assertThat(words.toArray(), arrayContainingInAnyOrder(
                "45*E45",
                "45*E44"
        ));
    }

    @Test
    public void findWords() throws IOException {
        String text = readResource("/lab1/task1/text.txt");
    }
}

