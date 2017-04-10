package io.github.k_gregory.otp.lab1;

import io.github.k_gregory.otp.Util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordSearch {
    private String patternSrc = "[0-9]+\\*E[0-9]+";

    public static void main(String... args) throws IOException {
        new WordSearch().findWords();
    }

    private void findWords() throws IOException {
        String text = Util.readResource(getClass(), "/lab1/task1/text.txt");
        Pattern pattern = Pattern.compile(patternSrc);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int preCtx = Math.max(matcher.start() - 5, 0);
            int postCtx = Math.min(matcher.end() + 5, text.length());
            String s = text.substring(preCtx, matcher.start()) +
                    "<" + text.substring(matcher.start(), matcher.end()) + ">"
                    + text.substring(matcher.end(), postCtx);
            System.out.println(s);
        }
    }
}
