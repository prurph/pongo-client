package com.scalainaction.mongo

import com.mongodb.{DB => MongoDB}
import scala.collection.JavaConverters._

class DB private(val underlying: MongoDB) {
  private def collection(name: String) = underlying.getCollection(name)

  def readOnlyCollection(name: String) = new DBCollection(collection(name))
  def administrableCollection(name: String) = new DBCollection(collection(name)) with Administrable
  def updatableCollection(name: String) = new DBCollection(collection(name)) with Updatable

  def collectionNames = underlying.getCollectionNames.asScala
}

object DB {
  def apply(underlying: MongoDB) = new DB(underlying)
}
