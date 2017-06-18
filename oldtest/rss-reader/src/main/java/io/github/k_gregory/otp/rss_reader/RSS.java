package io.github.k_gregory.otp.rss_reader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

class Item{
    @XmlElement String title;
    @XmlElement String link;
    @XmlElement String description;
}


class Channel{
    @XmlElement String title;
    @XmlElement String link;
    @XmlElement String description;
    @XmlElement(name = "item") List<Item> items;
}

@XmlRootElement(name = "rss")
class RSS{
    @XmlAttribute String version;
    @XmlElement Channel channel;
}
