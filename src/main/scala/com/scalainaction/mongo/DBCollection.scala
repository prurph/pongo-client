package com.scalainaction.mongo

import com.mongodb.{DBCollection => MongoDBCollection}
import com.mongodb.DBObject

trait ReadOnly {

  val underlying: MongoDBCollection

  def name: String = underlying getName
  def fullName: String = underlying getFullName
  def find(doc: DBObject): DBObject = underlying findOne doc
  def findOne: DBObject = underlying findOne
  def getCount(doc: DBObject): Long = underlying getCount doc
}

trait Administrable extends ReadOnly {

  def drop(): Unit = underlying drop()
  def dropIndexes(): Unit = underlying dropIndexes()
}

trait Updatable extends ReadOnly {

  def -=(doc: DBObject): Unit = underlying remove doc
  def +=(doc: DBObject): Unit = underlying save doc
}

class DBCollection(override val underlying: MongoDBCollection) extends ReadOnly {

  private def collection(name: String): MongoDBCollection =
    underlying.getCollection(name)

  def readOnlyCollection(name: String): DBCollection =
    new DBCollection(collection(name))
  def administrableCollection(name: String): DBCollection =
    new DBCollection(collection(name)) with Administrable
  def updatableCollection(name: String): DBCollection =
    new DBCollection(collection(name)) with Updatable
}
