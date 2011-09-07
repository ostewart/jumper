package com.trailmagic.jumper.core.security

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetailsService}
import com.trailmagic.jumper.core.{SavedUser, User, UserRepository}

@Service
class UserService @Autowired() (saltGenerator: SaltGenerator, passwordEncoder: PasswordEncoder, userRepository: UserRepository) extends UserDetailsService {
  def loadUserByUsername(username: String) = {
    userRepository.findByUsername(username) match {
      case Some(user) => new JumperUserDetails(user)
      case None => throw new UsernameNotFoundException("No such user: " + username)
    }
  }
  /**
   * Encodes the passed in user's (plaintext) password with a generated salt, and saves the resulting user information to the UserRepository
   */
  def createUser(user: User): SavedUser = {
    val salt = saltGenerator.nextSalt
    val augmentedUser = user.copy(password = passwordEncoder.encodePassword(user.password, salt), salt = salt)
    userRepository.findByUsername(user.username) match {
      case Some(existingUser) => throw new NonUniqueUsernameException("User with username " + existingUser.username + " already exists")
      case None => userRepository.save(augmentedUser)
    }
  }
}