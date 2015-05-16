package com.scalainaction.mongo

import com.mongodb.{DBCollection => MongoDBCollection, DBCursor, DBObject}

trait ReadOnly {

  val underlying: MongoDBCollection

  def name: String = underlying getName
  def fullName: String = underlying getFullName

  def find(query: Query): DBCursor = {
    def applyOptions(cursor: DBCursor, option: QueryOption): DBCursor = {
      option match {
        case Skip(skip, next) => applyOptions(cursor.skip(skip), next)
        case Sort(sorting, next) => applyOptions(cursor.sort(sorting), next)
        case Limit(limit, next) => applyOptions(cursor.limit(limit), next)
        case NoOption => cursor
      }
    }
    applyOptions(find(query.q), query.option)
  }
  def find(doc: DBObject): DBCursor = underlying find doc
  def findOne: DBObject = underlying findOne
  def findOne(doc: DBObject) = underlying findOne doc
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
