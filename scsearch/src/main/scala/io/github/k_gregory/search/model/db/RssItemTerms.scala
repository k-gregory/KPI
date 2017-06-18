package io.github.k_gregory.search.model.db

/**
  * Created by gregory on 5/27/17.
  */
case class RssItemTerms(
                         rssItemId: Long,
                         termValue: String,
                         usages: Int
                       )
