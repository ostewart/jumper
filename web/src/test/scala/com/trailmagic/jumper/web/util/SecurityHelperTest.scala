package com.trailmagic.jumper.web.util

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.junit.Test
import org.springframework.security.core.context.SecurityContextHolder
import org.junit.Assert._
import scala.collection.JavaConverters._


class SecurityHelperTest {
  val anonymousRoles = List[GrantedAuthority](new GrantedAuthorityImpl("ROLE_ANONYMOUS"))
  final val AnonymousAuthentication: AnonymousAuthenticationToken = new AnonymousAuthenticationToken("key", "principal", anonymousRoles.asJava)

  @Test
  def testStringPrincipalIsNoneAuthenticatedUser() {
    SecurityContextHolder.getContext.setAuthentication(AnonymousAuthentication)

    assertEquals(None, SecurityHelper.getAuthenticatedUser)
  }

  @Test
  def testAnoymousAuthenticationIsNotSignedIn() {
    SecurityContextHolder.getContext.setAuthentication(AnonymousAuthentication)

    assertFalse(SecurityHelper.isUserSignedIn)
  }
}