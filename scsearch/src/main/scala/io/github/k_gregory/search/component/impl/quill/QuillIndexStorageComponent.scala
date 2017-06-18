package io.github.k_gregory.search.component.impl.quill

import java.util.Date

import io.github.k_gregory.search.component.decl.{IndexStorageComponent, QuillDbComponent}
import io.github.k_gregory.search.model.RssChannel
import io.github.k_gregory.search.model.db.{RssFeed, RssItem, RssItemTerms, Term}
import org.slf4j.LoggerFactory


trait QuillIndexStorageComponent extends IndexStorageComponent {
  this: QuillIndexStorageComponent with QuillDbComponent =>

  class H2IndexStorage extends IndexStorage {
    private val log = LoggerFactory.getLogger(this.getClass)

    import dbCtx._

    override def mergeChannel(originUrl: String, rssChannel: RssChannel): Traversable[Option[Long]] = {
      log.debug(s"Saving ${rssChannel.title} from $originUrl")

      val channelFeedInfo = RssFeed(
        None,
        originUrl,
        rssChannel.link,
        rssChannel.title,
        rssChannel.description,
        new Date()
      )

      val feed = mergeFeed(originUrl, channelFeedInfo)

      val itemsList = rssChannel
        .items
        .map(_.copy(feedId = feed.id))
        .toList

      log.debug("Saving channel items")
      mergeFeedItems(itemsList)
    }

    private def mergeFeed(originUrl: String, channelFeedInfo: RssFeed): RssFeed = {
      val foundFeed = dbCtx.run(findFeedByUrl(originUrl))

      foundFeed.headOption match {
        case Some(f) =>
          log.debug("Found feed in db")
          val newFeedInfo = channelFeedInfo.copy(id = f.id)
          if (newFeedInfo != f) {
            log.debug("Need to update feed")
            dbCtx.run(updateFeedQ(newFeedInfo))
          }
          newFeedInfo
        case None =>
          log.debug("Feed not found in DB, inserting")
          channelFeedInfo.copy(
            id = dbCtx.run(insertFeedQ(channelFeedInfo))
          )
      }
    }

    private def findFeedByUrl(url: String) = quote {
      query[RssFeed].filter(_.url == lift(url))
    }

    private def insertFeedQ(rssFeed: RssFeed) = quote {
      query[RssFeed].insert(lift(rssFeed)).returning(_.id)
    }

    private def updateFeedQ(rssFeed: RssFeed) = quote {
      val r = lift(rssFeed)
      query[RssFeed].filter(_.id == r.id).update(r)
    }

    private def mergeFeedItems(items: Traversable[RssItem]): Traversable[Option[Long]] = {
      items.map(i => {
        val quote1 = quote {
          query[RssItem]
            .filter(_.feedId == lift(i.feedId))
            .filter(_.description == lift(i.description) || lift(i.description.isEmpty))
            .filter(_.link == lift(i.link) || lift(i.link.isEmpty))
            .filter(_.title == lift(i.title) || lift(i.title.isEmpty))
            .nonEmpty
        }
        val present = dbCtx.run(quote1)
        if (present)
          None
        else dbCtx.run(quote {
          query[RssItem].insert(lift(i)).returning(_.id)
        })
      })
    }

    override def mergeTerms(values: Traversable[Term]): Unit = {
      values.foreach(v => {
        val present = dbCtx.run(quote {
          query[Term].filter(_.value == lift(v.value)).nonEmpty
        })
        if (present)
          dbCtx.run(quote {
            query[Term]
              .filter(_.value == lift(v.value))
              .update(t => t.documentFrequency -> (t.documentFrequency + lift(v.documentFrequency)))
          })
        else {
          dbCtx.run(quote {
            query[Term].insert(lift(v))
          })
        }
      })
    }

    override def saveRssItemTerms(values: List[RssItemTerms]): Int = {
      dbCtx.run(saveRssItemTermsQ(values)).size
    }


    /*
    override def mergeTerms(values: List[Term]): Unit = {

      TODO: Send bug report
      val valSet = values.toSet


      val presentValues = dbCtx.run(quote {
        query[Term].map(_.value).filter(s => liftQuery(valSet).map(_.value).contains(s))
      }).toSet
      }
    */

    private def saveRssItemTermsQ(values: List[RssItemTerms]) = quote {
      liftQuery(values).foreach(i =>
        query[RssItemTerms].insert(i)
      )
    }

    override def search(word: String): List[RssItem] = dbCtx.run(quote {
      for{
        rssItemTerms <- query[RssItemTerms] sortBy(-_.usages) if rssItemTerms.termValue == lift(word)
        rssItem <- query[RssItem] if rssItem.id.forall(_ == rssItemTerms.rssItemId)
      } yield rssItem
    })
  }

}
