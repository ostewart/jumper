package com.trailmagic.jumper.web

import org.junit.Assert._
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest


class NewUserViewTest {
  val viewHelper = new ScalateViewTestHelper

  @Test
  def rendersTitle() {
    val viewName = "new-user-form"

    val output = viewHelper.layoutView(viewName, Map("errors" -> Map(),
                                                     "contextPath" -> "/foo",
                                                     "request" -> new MockHttpServletRequest))

    assertTrue("Contains correct title", output.contains("<title>Jumper | Create Account</title>"))
  }
}