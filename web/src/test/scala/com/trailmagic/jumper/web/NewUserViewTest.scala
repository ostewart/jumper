package com.trailmagic.jumper.web

import org.junit.Assert._
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.jsoup.Jsoup


class NewUserViewTest {
  val viewHelper = new ScalateViewTestHelper

  @Test
  def rendersTitle() {
    val viewName = "new-user-form"

    val output = viewHelper.layoutView(viewName, Map("errors" -> Map(),
                                                     "contextPath" -> "/foo",
                                                     "request" -> new MockHttpServletRequest))
    val doc = Jsoup.parse(output)
    assertEquals("Jumper | Create Account", doc.select("title").text)
    assertEquals("Create an Account", doc.select("h1").text)
  }
}