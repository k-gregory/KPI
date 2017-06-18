package io.github.k_gregory.otp.rss_reader.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by grego on 13.05.2017.
 */

@Entity
public class Feed {
    private String srcUrl;
    private String link;
    private String description;


    @Id
    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String url) {
        this.srcUrl = url;
    }
}
