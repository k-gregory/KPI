package io.github.k_gregory.search.model.db

import java.util.Date

case class RssItem(
                    id: Option[Long],
                    title: Option[String],
                    link: Option[String],
                    description: Option[String],
                    tdLength: Option[Double],
                    pubTime: Option[Date],
                    feedId: Option[Long]
                  )
