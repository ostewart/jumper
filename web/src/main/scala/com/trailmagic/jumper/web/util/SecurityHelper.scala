package com.trailmagic.jumper.web.util

import org.springframework.security.access.AccessDeniedException
import com.trailmagic.jumper.core.SavedUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import com.trailmagic.jumper.core.security.JumperUserDetails


object SecurityHelper {
  def setAuthenticatedUser(userOption: Option[SavedUser]) {
    val authentication = userOption match {
      case Some(user) =>
        val userDetails = new JumperUserDetails(user)
        val authentication = new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities)
        authentication
      case None =>
        null
    }
    SecurityContextHolder.getContext.setAuthentication(authentication)
  }

  def getAuthenticatedUser: Option[SavedUser] = {
    SecurityContextHolder.getContext.getAuthentication match {
      case null => None
      case authentication: Authentication => authentication.getPrincipal match {
        case userDetails: JumperUserDetails => Some(userDetails.user)
        case null => None
        case principal: String => None
      }
    }
  }

  def getMandatoryAuthenticatedUser: SavedUser = {
    SecurityHelper.getAuthenticatedUser match {
      case None => throw new AccessDeniedException("No authenticated user")
      case Some(user) => user
    }
  }

  def isUserSignedIn: Boolean = {
    getAuthenticatedUser match {
      case Some(user) => true
      case None => false
    }
  }
}