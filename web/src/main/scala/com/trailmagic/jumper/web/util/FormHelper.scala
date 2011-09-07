package com.trailmagic.jumper.web.util

import org.springframework.validation.BindingResult
import scala.collection.JavaConverters._

object FormHelper {
  def getAllErrors(bindingResult: BindingResult): Set[String] = {
    bindingResult.getAllErrors.asScala.map(_.getDefaultMessage).toSet
  }
}