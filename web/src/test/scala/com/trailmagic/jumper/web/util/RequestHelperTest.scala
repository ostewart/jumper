package com.trailmagic.jumper.web.util

import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.junit.Assert._


class RequestHelperTest {
  @Test
  def testEncodesUrlWithoutQueryString() {
    val request = new MockHttpServletRequest("GET", "/places/nearby")
    request.setServerPort(8080)
    val url = RequestHelper.currentUrlEscaped(request)

    assertEquals("http%3A%2F%2Flocalhost%3A8080%2Fplaces%2Fnearby", url)
  }

  @Test
  def testEncodesUrlWithQueryString() {
    val request = new MockHttpServletRequest("GET", "/places/nearby?foo=bar&this=fünkeÇharß with spaces")
    request.setServerPort(8080)
    val url = RequestHelper.currentUrlEscaped(request)

    assertEquals("http%3A%2F%2Flocalhost%3A8080%2Fplaces%2Fnearby%3Ffoo%3Dbar%26this%3Df%C3%BCnke%C3%87har%C3%9F+with+spaces", url)
  }

  @Test
  def testEncodesLatitudeLongitudeUrl() {

    val request = new MockHttpServletRequest("GET", "/places/nearby?latitude=40.7355523&longitude=-73.9866242")
    request.setServerPort(8080)
    val url = RequestHelper.currentUrlEscaped(request)

    assertEquals("http%3A%2F%2Flocalhost%3A8080%2Fplaces%2Fnearby%3Flatitude%3D40.7355523%26longitude%3D-73.9866242", url)
  }

  @Test
  def testAddsEmptyQueryStringWhenNoTargetUrlParameter() {
    val request = new MockHttpServletRequest("GET", "/login")
    assertEquals("", RequestHelper.securityRedirectQueryString(request))
  }

  @Test
  def testAddsSpringSecurityRedirectParameterWhenTargetUrlParameterPresent() {
    val expectedUrl = "%2Fplaces%2Fnearby%3Ffoo%3Dbar%26qux%3Dbaz"
    val targetUrl = "/places/nearby?foo=bar&qux=baz"
    val request = new MockHttpServletRequest("GET", "/login?targetUrl=" + targetUrl)
    request.setParameter("targetUrl", targetUrl)
    assertEquals("?spring-security-redirect=" + expectedUrl, RequestHelper.securityRedirectQueryString(request))
  }
}