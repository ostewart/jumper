package com.trailmagic.jumper.web

import org.joda.time.DateTime
import org.mockito.Mockito
import com.trailmagic.jumper.core.signup.SignupRepository
import com.trailmagic.jumper.core.TimeSource
import org.junit.Test
import org.junit.Assert._
import com.trailmagic.jumper.core.signup.SignupRequest


class HomePageControllerTest {
  val TheTime = new DateTime
  val repository = Mockito.mock(classOf[SignupRepository])
  val controller = new HomePageController(repository, new TimeSource {
    override def now = TheTime
  })

  @Test
  def testPlausibleEmail() {
    assertFalse(controller.plausibleEmail("no"))
    assertFalse(controller.plausibleEmail("a.b.c.com"))
    assertTrue(controller.plausibleEmail("a@b.c.com"))
    assertTrue(controller.plausibleEmail("a.b@c.com"))
    assertTrue(controller.plausibleEmail("a.b@c.gov"))
    assertTrue(controller.plausibleEmail("a.b@c.org"))
    assertTrue(controller.plausibleEmail("a.b@c.us"))
    assertTrue(controller.plausibleEmail("b@c.us"))
  }

  @Test
  def testValidEmailSignupSavesToRepository() {
    controller.signup("foo@bar.com")

    Mockito.verify(repository).save(SignupRequest("foo@bar.com", TheTime, None))
  }
}