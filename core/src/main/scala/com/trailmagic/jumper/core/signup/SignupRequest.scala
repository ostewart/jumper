package com.trailmagic.jumper.core.signup

import org.joda.time.DateTime

case class SignupRequest(email: String, date: DateTime, extraInfo: Option[SignupInfo])

case class SignupInfo(firstName: Option[String],
                      lastName: Option[String],
                      country: Option[String],
                      postalCode: Option[String],
                      source: Option[SignupSource.Value],
                      comments: Option[String])

