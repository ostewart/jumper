package com.trailmagic.jumper.web

import org.springframework.web.servlet.ModelAndView
import util.SecurityHelper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import com.trailmagic.jumper.core.signup.SignupRequest
import org.springframework.beans.factory.annotation.Autowired
import com.trailmagic.jumper.core.signup.SignupRepository
import com.trailmagic.jumper.core.TimeSource

@Controller
class HomePageController @Autowired()(signupRepository: SignupRepository, timeSource: TimeSource){
  @RequestMapping(Array("/"))
  def showIndex() = {
    new ModelAndView("index", "currentUser", SecurityHelper.getAuthenticatedUser)
  }
  def plausibleEmail(email: String): Boolean = {
    email != null && email.matches(".+@.+\\..+")
  }

  @RequestMapping(Array("/signup"))
  def signup(@RequestParam("emailAddress") email: String): ModelAndView = {
    if (plausibleEmail(email)) {
      signupRepository.save(SignupRequest(email, timeSource.now, None))
      new ModelAndView("redirect:/signup-thankyou")
    }
    else new ModelAndView("redirect:/", "failure", "true")
  }

  @RequestMapping(Array("/signup-thankyou"))
  def signupThanks(): String = {
    "signup-thankyou"
  }
}