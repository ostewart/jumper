package com.trailmagic.jumper.web

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView

class GlobalModelInterceptor extends HandlerInterceptorAdapter {
  override def postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: AnyRef, modelAndView: ModelAndView) {
    val model = modelAndView.getModel
    if (model != null && !viewIsRedirect(modelAndView)) {
      model.put("request", request)
      model.put("contextPath", request.getContextPath)
    }
  }

  def viewIsRedirect(mav: ModelAndView): Boolean = {
    (mav.getViewName != null && mav.getViewName.startsWith("redirect:") || mav.getView != null && mav.getView.isInstanceOf[RedirectView])
  }
}