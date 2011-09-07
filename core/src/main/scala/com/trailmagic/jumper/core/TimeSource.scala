package com.trailmagic.jumper.core

import org.springframework.stereotype.Service
import org.joda.time.DateTime


@Service
class TimeSource {
  def now = {
    new DateTime
  }
}