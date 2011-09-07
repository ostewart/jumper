package com.trailmagic.jumper.core.signup

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.commons.conversions.scala._

@Repository
class MongoSignupRepository @Autowired()(db: MongoDB) extends SignupRepository {
  RegisterJodaTimeConversionHelpers()
  private val signups = db("signups")

  def save(signup: SignupRequest) {
    signups.save(grater[SignupRequest].asDBObject(signup))
  }

  def findAll(): List[SignupRequest] = {
    signups.find().map(grater[SignupRequest].asObject(_)).toList
  }
}