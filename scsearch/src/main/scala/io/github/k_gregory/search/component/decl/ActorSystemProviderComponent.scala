package io.github.k_gregory.search.component.decl

import akka.actor.ActorSystem

trait ActorSystemProviderComponent {
  val actorSystem: ActorSystem
}
