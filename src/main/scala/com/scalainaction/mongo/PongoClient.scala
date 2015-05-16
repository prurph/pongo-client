package com.scalainaction.mongo

import com.mongodb.MongoClient

class PongoClient(val host: String, val port: Int) {
  require(host != null, "Host name must be provided")
  private val underlying = new MongoClient(host, port)

  def this() = this("127.0.0.1", 27017)

  def dropDb(name: String): Unit = underlying.dropDatabase(name)

  def createDb(name: String): Unit = DB(underlying.getDb(name))

  def db(name: String): Unit = DB(underlying.getDb(name))
}
