package com.trailmagic.jumper.core.security


class NonUniqueUsernameException(message: String) extends RuntimeException(message)
class NonUniqueEmailException(message: String) extends RuntimeException(message)