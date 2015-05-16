package com.scalainaction.mongo

import com.mongodb.BasicDBObject
import org.scalatest.{Matchers, FlatSpec}

class PongoClientSpec extends FlatSpec with Matchers {

  "PongoClient" should "allow basic database interaction" in {
    def client = new PongoClient()
    client.dropDb("testdb")

    def db = client.db("testdb")
    db.collectionNames shouldBe empty

    val col = db.readOnlyCollection("test")
    col.name shouldBe "test"


    val updatableCol = db.updatableCollection("test")

    val doc = new BasicDBObject()
    doc.put("name", "MongoDB")
    doc.put("type", "database")
    doc.put("count", 1.asInstanceOf[Number])

    val info = new BasicDBObject()
    info.put("x", 203.asInstanceOf[Number])
    info.put("y", 102.asInstanceOf[Number])
    doc.put("info", info)
    updatableCol += doc

    db.collectionNames should contain ("test")
    updatableCol.findOne shouldBe doc

    updatableCol -= doc
    updatableCol.findOne shouldBe null

    for (i <- 1 to 100) updatableCol += new BasicDBObject("i", i)

    val query = new BasicDBObject()
    query.put("i", 71.asInstanceOf[Number])

    val cursor = col.find(query)
    cursor.hasNext() shouldBe true
    cursor.next().get("i") shouldEqual 71

    val adminCol = db.administrableCollection("test")
    adminCol.drop
    db.collectionNames shouldNot contain ("test")
  }
}
