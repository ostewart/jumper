package com.trailmagic.jumper.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletResponse

@Controller
class ProtectedController {
  @RequestMapping(Array("/protected"))
  def showProtectedResource(response: HttpServletResponse) {
    response.getWriter.println("Top Secret!!")
  }
}