package io.github.k_gregory.otp.rss_reader;

import io.github.k_gregory.otp.rss_reader.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableScheduling
public class Application {
    @Bean
    public CommandLineRunner runner(RSSFetcher rss){
        return args -> {
            rss.fetch();
        };
    }

    /*
    @Bean
    public CommandLineRunner runner(DocumentRepository repo){
        return args -> {
            AsyncRestTemplate restTemplate = new AsyncRestTemplate();
            restTemplate
                    .getForEntity("https://www.theguardian.com/world/rss", RSS.class)
                    .addCallback(res->{
                        List<Item> items = res.getBody().channel.items;
                        for(Item item : items){
                            OldDocument document = new OldDocument();
                            document.setContent(item.description);
                            document = repo.save(document);
                            System.out.println(document.getContent());
                        }
                    }, Throwable::printStackTrace);
        };
    }
    */

    public static void main(String... args){
        SpringApplication.run(Application.class,args);
    }
}
