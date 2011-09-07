package com.trailmagic.jumper.core


class InMemoryUserRepository extends UserRepository {
  private var users = Map[String, SavedUser]()

  def save(user: SavedUser): SavedUser = {
    users += (user.id.toLowerCase -> user)
    user
  }

  def save(user: User): SavedUser = {
    save(SavedUser(user.username.toLowerCase, user))
  }

  def findById(id: String): Option[SavedUser] = {
    users.get(id.toLowerCase)
  }

  def findByUsername(username: String) = {
    users.values.find((user) => user.id == username.toLowerCase)
  }

  def findUsers(usernames: List[String]): Map[String, SavedUser] = {
    users.values.foldLeft(Map[String, SavedUser]())(
      (usernameUserMap, user) =>
        if (usernames.map(_.toLowerCase).contains(user.id)) usernameUserMap + (user.username -> user)
        else usernameUserMap
    )
  }

  def findWithFacebookIds(ids: Set[String]): Set[User] = {
    users.values.filter((u) => {u.facebookId.isDefined && ids.contains(u.facebookId.get)}).toSet
  }
}
