package com.scalainaction.mongo

import com.mongodb._

class PongoClient(val host: String, val port: Int) {
  require(host != null, "Host name must be provided")
  private val underlying: Mongo = new Mongo(host, port)

  def this() = this("127.0.0.1", 27017)

  def dropDb(name: String): Unit = underlying.dropDatabase(name)

  def createDb(name: String): DB = DB(underlying.getDB(name))

  def db(name: String): DB = DB(underlying.getDB(name))
}
