package com.trailmagic.jumper.core.mongodb

import org.springframework.stereotype.Repository
import org.springframework.beans.factory.annotation.Autowired
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.MongoException.DuplicateKey
import com.trailmagic.jumper.core.{SavedUser, User, UserRepository}
import com.trailmagic.jumper.core.security.NonUniqueEmailException
import com.trailmagic.jumper.core.security.NonUniqueUsernameException

@Repository
class MongoUserRepository @Autowired()(db: MongoDB) extends UserRepository {
  private val users = db("users")
  users.ensureIndex(MongoDBObject("username" -> 1), "username_uniqueness", true)
  users.ensureIndex(MongoDBObject("email" -> 1), "email_uniqueness", true)

  def save(user: SavedUser): SavedUser = {
    try {
      users.update(MongoDBObject("_id" -> user.id), grater[User].asDBObject(user.user), false, false, WriteConcern.Safe)
    } catch {
      // this is shady since it could also be a username, but we won't let people change that and mongo doesn't seem to tell us, so...there it is
      case e: DuplicateKey => throw new NonUniqueEmailException(user.email + " is not unique")
    }
    user
  }

  def save(user: User): SavedUser = {
    val dbObject = grater[User].asDBObject(user)
    val newId = user.username.toLowerCase
    try {
      users.insert(MongoDBObject("_id" -> newId) ++ dbObject, WriteConcern.Safe)
    } catch {
      case e: DuplicateKey if e.getMessage.contains("_id") => throw new NonUniqueUsernameException(user.username + " is not unique")
      case e: DuplicateKey if e.getMessage.contains("email_uniqueness") => throw new NonUniqueEmailException(user.email + " is not unique")
    }

    SavedUser(newId, user)
  }

  def findById(id: String): Option[SavedUser] = {
    val lowerCaseId = id.toLowerCase
    val query = MongoDBObject("_id" -> lowerCaseId)
    users.findOne(query) match {
      case None => None
      case Some(obj: DBObject) => Some(SavedUser(lowerCaseId, grater[User].asObject(obj)))
    }
  }

  def dbObj2SavedUser(obj: DBObject): SavedUser = {
    SavedUser(obj("_id").toString, grater[User].asObject(obj))
  }

  def findByUsername(username: String) = {
    users.findOne(MongoDBObject("_id" -> username.toLowerCase)) match {
      case None => None
      case Some(obj: DBObject) => Some(dbObj2SavedUser(obj))
    }
  }

  def findUsers(usernames: List[String]): Map[String, SavedUser] = {
    users.find("_id" $in usernames.map(_.toLowerCase)).foldLeft(Map[String, SavedUser]())(
      (usernameUserMap, obj) => {
        val user = dbObj2SavedUser(obj)
        usernameUserMap + (user.username -> user)
      }
    )
  }

  def findWithFacebookIds(ids: Set[String]): Set[User] = {
    users.find("facebookId" $in ids).map(dbObj2SavedUser(_)).toSet
  }
}