package io.github.k_gregory.search.model

import io.github.k_gregory.search.model.db.RssItem

case class RssChannel(
                       title: String,
                       link: String,
                       description: String,
                       items: Seq[RssItem]
                     )
