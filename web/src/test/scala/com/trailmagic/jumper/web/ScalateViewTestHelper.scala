package com.trailmagic.jumper.web

import org.fusesource.scalate.TemplateEngine
import java.io.File
import org.fusesource.scalate.layout.DefaultLayoutStrategy

class ScalateViewTestHelper {
  val moduleBase = System.getProperty("web.basedir")
  val templatesRoot = moduleBase + "/src/main/webapp/"
  lazy val engine = new TemplateEngine(Some(new File(templatesRoot)))
  engine.layoutStrategy = new DefaultLayoutStrategy(engine, "/WEB-INF/scalate/layouts/default.ssp")

  def layoutView(viewName: String, model: Map[String, Object] = Map()): String = {
    engine.layout("/WEB-INF/views/" + viewName + ".ssp", model)
  }
}