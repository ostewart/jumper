package com.trailmagic.jumper.core.security

import org.springframework.stereotype.Service
import java.security.SecureRandom
import org.apache.commons.codec.binary.Hex


@Service
class SaltGenerator {
  val rng = new SecureRandom()
  rng.setSeed("fantastically random".getBytes)

  def nextSalt: String = {
    val randomBytes = Array.ofDim[Byte](16)
    rng.nextBytes(randomBytes)
    Hex.encodeHexString(randomBytes)
  }
}