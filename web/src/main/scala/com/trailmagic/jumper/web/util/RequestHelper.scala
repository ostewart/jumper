package com.trailmagic.jumper.web.util

import javax.servlet.http.HttpServletRequest
import java.net.URLEncoder

object RequestHelper {
  def currentUrlEscaped(request: HttpServletRequest): String = {
    val url = request.getRequestURL.toString
    val queryString = request.getQueryString

    val fullUrl = if (queryString == null) {
      url
    } else {
      url + "?" + queryString
    }

    URLEncoder.encode(fullUrl, "UTF-8")
  }

  def securityRedirectQueryString(request: HttpServletRequest): String = {
    request.getParameter("targetUrl") match {
      case null => ""
      case url: String => "?spring-security-redirect=" + URLEncoder.encode(url, "UTF-8")
    }
  }
}