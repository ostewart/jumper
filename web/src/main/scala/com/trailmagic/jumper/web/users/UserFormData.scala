package com.trailmagic.jumper.web.users

import reflect.BeanProperty
import javax.validation.constraints.Size
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import com.trailmagic.jumper.core.User


class UserFormData {
  val EmailPattern = ".+@.+\\..+"

  @BeanProperty @NotNull @Size(min=1, message="Please fill in all fields") var username: String = ""
  @BeanProperty @NotNull @Size(min=1, message = "Please fill in all fields") var firstName: String = ""
  @BeanProperty @NotNull @Size(min=1, message = "Please fill in all fields") var lastName: String = ""
  @BeanProperty @NotNull @Pattern(regexp = ".+@.+\\..+", message = "Please enter a valid email address") var email: String = ""
  @BeanProperty @NotNull @Size(min=8, message = "Password must be at least 8 characters") var password: String = ""

  def toUser: User = {
    User(email, username, firstName, lastName, password, "")
  }
}
