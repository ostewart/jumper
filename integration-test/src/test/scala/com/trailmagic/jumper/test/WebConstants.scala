package com.trailmagic.jumper.test

import java.net.URLEncoder


object WebConstants {
  final val BaseUrl = System.getProperty("testing.baseUrl", "http://localhost:8080")
  final val SecureBaseUrl = System.getProperty("testing.secureBaseUrl", "https://localhost:8443")
  final val NewUserUrl = SecureBaseUrl + "/users/new"
  final val ProtectedUrl = SecureBaseUrl + "/protected"
  final val HomeUrl = SecureBaseUrl + "/"
  final val SignUpThankYouUrl = WebConstants.SecureBaseUrl + "/signup-thankyou"
  final val LoginUrl = SecureBaseUrl + "/login"

  def UserProfileUrl(username: String): String = {
    SecureBaseUrl + "/users/" + URLEncoder.encode(username, "UTF-8")
  }
}
