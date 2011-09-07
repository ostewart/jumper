package com.trailmagic.jumper.core.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import com.trailmagic.jumper.core.SavedUser
import scala.collection.JavaConverters._

class JumperUserDetails(val user: SavedUser) extends UserDetails {
  def isEnabled = true

  def isCredentialsNonExpired = true

  def isAccountNonLocked = true

  def isAccountNonExpired = true

  def getUsername = user.username

  def getPassword = user.password

  def getSalt = user.salt

  def getAuthorities = List[GrantedAuthority](new GrantedAuthorityImpl("ROLE_USER")).asJavaCollection

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[JumperUserDetails]) {
      return false
    }
    val other = obj.asInstanceOf[JumperUserDetails]
    user.equals(other.user)
  }

  override def toString: String = {
    "JumperUserDetails(" + user.toString + ")"
  }
}