package com.trailmagic.jumper.web

import org.junit.Assert._
import org.junit.Test
import org.springframework.web.servlet.view.RedirectView
import org.springframework.mock.web.{MockHttpServletResponse, MockHttpServletRequest}
import org.springframework.web.servlet.ModelAndView

class GlobalModelInterceptorTest {
  val interceptor = new GlobalModelInterceptor

  def handleWithMavForModel(interceptor: GlobalModelInterceptor, modelAndView: ModelAndView) = {
    interceptor.postHandle(new MockHttpServletRequest, new MockHttpServletResponse, null, modelAndView)
    modelAndView.getModel
  }

  @Test
  def leavesModelEmptyForRedirect() {
    assertTrue(handleWithMavForModel(interceptor, new ModelAndView("redirect:")).isEmpty)
    assertTrue(handleWithMavForModel(interceptor, new ModelAndView(new RedirectView)).isEmpty)
  }

  @Test
  def addsStandardModelAttributes() {
    val request = new MockHttpServletRequest
    val contextPath = "/foo/bar/blah"
    request.setContextPath(contextPath)
    val mav = new ModelAndView("foo-view")

    interceptor.postHandle(request, new MockHttpServletResponse, null, mav)

    assertEquals(request, mav.getModel.get("request"))
    assertEquals(contextPath, mav.getModel.get("contextPath"))
  }
}