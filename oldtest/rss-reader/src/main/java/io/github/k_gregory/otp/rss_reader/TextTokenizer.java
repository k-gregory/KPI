package io.github.k_gregory.otp.rss_reader;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextTokenizer {
    private static final String data = "\"hi\", there!can't <p>Trump’s, decision to fire James Comey stunned Washington, upset the bureau, and brought Russia back into focus. How much more can Republicans take?</p><p>Ill met by moonlight, a dozen reporters and cameramen peered into the darkness. <a href=\"https://www.theguardian.com/us-news/2017/may/10/theres-nothing-there-white-house-staff-deny-comey-sacking-is-linked-to-russia\">Where was Sean Spicer?</a> The press secretary had given a TV interview at 9pm then disappeared behind an awning, apparently conferring with colleagues. Journalists waited on the drive. The White House glowed behind them. “This is so weird,” one said. “It’s like hunting a dog and then killing it.”</p><p>A couple of minutes later Spicer emerged on a path running along a fence and hedgerow. He was caught in a blinding light and asked the cameramen to turn it off. “Relax, enjoy the night, have a glass of wine,” he said jocularly. Spicer then spent 12 minutes trying to explain why Donald Trump had taken the most explosive decision of his young presidency: axing the director of the FBI.</p> <a href=\"https://www.theguardian.com/us-news/2017/may/13/donald-trump-james-comey-fbi-explosive-week\">Continue reading...</a>";

    private static String extractTextFromHtml(String input) {
        final StringBuilder sb = new StringBuilder();
        new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof TextNode)
                    sb.append(((TextNode) node).text()).append(' ');
            }

            @Override
            public void tail(Node node, int depth) {

            }
        }).traverse(Jsoup.parse(input));
        return sb.toString();
    }

    public static void main(String... args) {
        final EnglishStemmer stemmer = new EnglishStemmer();
        String toLowerCase = extractTextFromHtml(data)
                .toLowerCase();
        String text = toLowerCase
                .replaceAll("['’][\\w\\d]([^\\w\\d])","$1");

        Pattern pattern = Pattern.compile("[^\\w\\d]+",Pattern.UNICODE_CHARACTER_CLASS);

        List<Map.Entry<String, Long>> collect = Stream
                // .of(text.split("[ :,.?“”]+|\\R+", 0))
                //.of(text.split("[\\p{P}\\p{Space}]+|\\R+", 0))
                .of(pattern.split(text, 0))
                .map(w -> {
                    stemmer.setCurrent(w);
                    stemmer.stem();
                    return stemmer.getCurrent();//.replaceAll("\\p{P}+", "");
                })
                //.map(System.out::println)
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue).reversed())
                .collect(Collectors.toList());
        System.out.println(collect);

        System.out.print("YEEAH!");
    }
}
