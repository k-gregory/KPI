package io.github.k_gregory.otp.rss_reader.entity;

import javax.persistence.*;

/**
 * Created by grego on 13.05.2017.
 */

@Entity
public class OldDocument {
    private Integer id;
    private String content;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
