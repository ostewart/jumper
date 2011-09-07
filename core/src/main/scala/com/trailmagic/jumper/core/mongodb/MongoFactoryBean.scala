package com.trailmagic.jumper.core.mongodb

import org.springframework.beans.factory.FactoryBean
import com.mongodb.casbah.{MongoConnection, MongoDB}


class MongoFactoryBean(dbName: String) extends FactoryBean[MongoDB] {
  def isSingleton = true

  def getObjectType = classOf[MongoDB]

  def getObject = {
    MongoConnection()(dbName)
  }
}