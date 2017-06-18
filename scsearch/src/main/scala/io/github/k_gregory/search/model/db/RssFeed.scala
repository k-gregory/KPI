package io.github.k_gregory.search.model.db

import java.util.Date

case class RssFeed(
                    id: Option[Long],
                    url: String,
                    link: String,
                    title: String,
                    description: String,
                    lastUpdate: Date
                  )
