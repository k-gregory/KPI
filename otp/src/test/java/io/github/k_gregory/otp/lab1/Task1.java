package io.github.k_gregory.otp.lab1;

import io.github.k_gregory.otp.Util;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.hamcrest.collection.IsArrayContainingInAnyOrder.arrayContainingInAnyOrder;
import static org.junit.Assert.assertThat;

public class Task1 {
    String patternSrc = "[0-9]+\\*E[0-9]+";

    @Test
    public void filterWords() throws IOException {
        Pattern pattern = Pattern.compile(patternSrc);
        Stream<String> words = stream(
                Util.readResource(getClass(), "/lab1/task1/words.txt").split("\\R+")
        ).filter(s -> pattern.matcher(s).matches());
        assertThat(words.toArray(), arrayContainingInAnyOrder(
                "45*E45",
                "45*E44"
        ));
    }
}

