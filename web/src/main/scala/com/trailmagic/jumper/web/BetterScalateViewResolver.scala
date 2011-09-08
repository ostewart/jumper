package com.trailmagic.jumper.web

import org.springframework.web.servlet.view.{AbstractUrlBasedView, AbstractTemplateViewResolver}
import org.fusesource.scalate.spring.view._

class BetterScalateViewResolver extends AbstractTemplateViewResolver {
  setViewClass(requiredViewClass())

  override def requiredViewClass(): java.lang.Class[_] = classOf[org.fusesource.scalate.spring.view.ScalateView]

  override def buildView(viewName: String): AbstractUrlBasedView = {
    var view: AbstractScalateView = null

    if (viewName.startsWith("nolayout:")) {
      val urlView = new ScalateUrlView with DefaultScalateRenderStrategy
      urlView.setUrl(getPrefix + viewName.substring("nolayout:".length()) + getSuffix)
      view = urlView
    } else {
      val urlView = new ScalateUrlView with LayoutScalateRenderStrategy
      urlView.setUrl(getPrefix + viewName + getSuffix)
      view = urlView
    }

    val contentType = getContentType
    if (contentType != null) {
      view.setContentType(contentType)
    }

    view.asInstanceOf[AbstractUrlBasedView]
  }
}
