package com.trailmagic.jumper.core


trait UserRepository {
  def findByUsername(username: String): Option[SavedUser]
  def findById(id: String): Option[SavedUser]
  def findUsers(usernames: List[String]): Map[String,SavedUser]
  def findWithFacebookIds(ids: Set[String]): Set[User]
  def save(user: User): SavedUser
  def save(user: SavedUser): SavedUser
}