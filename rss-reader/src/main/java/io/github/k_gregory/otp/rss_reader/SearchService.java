package io.github.k_gregory.otp.rss_reader;

import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Document {
    @Override
    public String toString() {
        return "Document{" +
                "text='" + text + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    private final String text;


    public Document(String text) {
        this.text = text;
    }

    public Map<String, Long> calculateTermCount() {
        Pattern nonWord = Pattern.compile("[^\\w\\d]+", Pattern.UNICODE_CHARACTER_CLASS);
        Pattern nonWordStart = Pattern.compile("^[^\\w\\d]+", Pattern.UNICODE_CHARACTER_CLASS);
        String clearedStartText = nonWordStart
                .matcher(text)
                .replaceFirst("");

        Map<String, Long> collect = Stream.of(nonWord.split(clearedStartText.toLowerCase()))
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        return collect;
    }
}

class TermDocumentInfo implements Comparable<TermDocumentInfo> {
    private final Document document;
    private final long termFrequency;

    TermDocumentInfo(Document document, long termFrequency) {
        this.document = document;
        this.termFrequency = termFrequency;
    }

    @Override
    public String toString() {
        return "TermDocumentInfo{" +
                "document=" + document +
                ", termFrequency=" + termFrequency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermDocumentInfo that = (TermDocumentInfo) o;

        if (termFrequency != that.termFrequency) return false;
        return document.equals(that.document);
    }

    @Override
    public int hashCode() {
        int result = document.hashCode();
        result = 31 * result + (int) (termFrequency ^ (termFrequency >>> 32));
        return result;
    }

    @Override
    public int compareTo(TermDocumentInfo o) {
        int tc = Long.compare(termFrequency, o.termFrequency);
        return tc == 0 ? document.getText().compareTo(o.document.getText()) : tc;
        /*
        int docCompare = document.getText().compareTo(o.document.getText());
        return docCompare == 0 ? Long.compare(termFrequency, o.termFrequency) : docCompare;
        */
    }
}

public class SearchService {
    private Map<String, Long> termFrequency = new HashMap<>();
    private SortedSetMultimap<String, TermDocumentInfo> termDocumentIndex = TreeMultimap.create();
   // private Map<String, SortedSet<TermDocumentInfo>> ttt = new HashMap<>();

    public static void main(String... args) throws IOException {

        SearchService search = new SearchService();
        String s = StreamUtils
                .copyToString(
                        SearchService.class.getClassLoader().getResourceAsStream("pap.txt")
                        , StandardCharsets.UTF_8
                );

        for(int i = 0; i < 5; i++)
            search.indexDocument(new Document(s+i));
        search = new SearchService();

        long l = System.currentTimeMillis();
        search.indexDocument(new Document(s));
        System.out.println(System.currentTimeMillis() - l);
        /*
        System.out.println((System.currentTimeMillis() - l));
        search.indexDocument(new Document("hi there fuck"));
        search.indexDocument(new Document("fuck you"));
        search.indexDocument(new Document("fuck you, you ASShole"));
        search.indexDocument(new Document("asshole in sight, lol"));
        search.search("you");
        System.out.println(search.termFrequency);
        System.out.println(search.termDocumentIndex);
        */

    }

    private double cosineSimilarity(Document d1, Document q){
        Map<String, Long> d1tf = d1.calculateTermCount();
        Map<String, Long> qtf = q.calculateTermCount();
        qtf
                .entrySet()
                .stream()
                .map(e->e.getValue()/(double)termFrequency.get(e.getKey()))
                .map(c->c*c);
        return 0;
                //.collect(Collectors.su)
    }

    public void search(String query){
        Document q = new Document(query);
        Map<String, Long> queryTermCount = q.calculateTermCount();
        queryTermCount
                .keySet()
                .stream()
                .flatMap(k -> termDocumentIndex.get(k).stream())
                .sorted((o1, o2) -> {
                    return 0;
                })
                .forEach(System.out::println);
    }

    public void indexDocument(Document document) {
        Map<String, Long> documentWordCount = document.calculateTermCount();
        for (Map.Entry<String, Long> termInfo : documentWordCount.entrySet()) {
            termFrequency.put(
                    termInfo.getKey(),
                    termFrequency.getOrDefault(termInfo.getKey(), 0L) + 1
            );
            /*SortedSet<TermDocumentInfo> termDocumentInfos = ttt.get(termInfo.getKey());
            if(termDocumentInfos == null){
                termDocumentInfos = new TreeSet<>();
            }
            termDocumentInfos.add(new TermDocumentInfo(document, termInfo.getValue()));
            ttt.put(termInfo.getKey(), termDocumentInfos);
            */
            //long l = System.nanoTime();
            termDocumentIndex.put(
                    termInfo.getKey(),
                    new TermDocumentInfo(document, termInfo.getValue())
            );
            //System.out.println(System.nanoTime()- l);
        }
    }
}
