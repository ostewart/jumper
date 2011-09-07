package com.trailmagic.jumper.test

import com.trailmagic.jumper.core.User
import com.mongodb.casbah.MongoConnection
import com.trailmagic.jumper.core.mongodb.MongoUserRepository


object DataHelper {
  val TestUser = User("testy@example.com", "tester", "Testy", "McTesterton", "4879b790020a04377613fd860a4e754add628f6bfdd5c1e675a28c286d379ebd", "cc11ce5ba1cd05411aa492b1a2f6bd78")

  def prepareTestData() {
    val con = MongoConnection()
    val db = con("test_jumper")

    db("users").drop()
    db("places").drop()

    val repository = new MongoUserRepository(db)
    repository.save(TestUser)

    con.close()
  }
}