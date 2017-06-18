package io.github.k_gregory.search.component.decl

import io.getquill.{H2JdbcContext, SnakeCase}

trait QuillDbComponent {
  val dbCtx: H2JdbcContext[SnakeCase]
}
