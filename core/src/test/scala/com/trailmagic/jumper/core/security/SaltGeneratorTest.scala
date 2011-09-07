package com.trailmagic.jumper.core.security

import org.junit.Test
import org.junit.Assert._


class SaltGeneratorTest {
  val sg = new SaltGenerator
  @Test
  def testGenerates32CharacterString() {
    assertEquals(32, sg.nextSalt.length)
    assertTrue(sg.nextSalt.isInstanceOf[String])
  }
  
  @Test
  def testSubsequentCallsAreUnique() {
    assertFalse(sg.nextSalt == sg.nextSalt)
    assertFalse(sg.nextSalt == sg.nextSalt)
  }
}