package com.trailmagic.jumper.core


case class User(email: String, username: String, firstName: String, lastName: String,
                password: String, salt: String, friends: Set[Friend] = Set[Friend](),
                facebookId: Option[String] = None)

case class SavedUser(id: String, user: User)
  extends User(user.email, user.username, user.firstName, user.lastName, user.password, user.salt, user.friends, user.facebookId)

case class Friend(username: Option[String], facebookId: Option[String])