package com.trailmagic.jumper.core.signup

trait SignupRepository {
  def save(signup: SignupRequest)
  def findAll(): List[SignupRequest]
}