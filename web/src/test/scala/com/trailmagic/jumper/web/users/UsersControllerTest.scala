package com.trailmagic.jumper.web.users

import com.trailmagic.jumper.web.ResourceNotFoundException
import com.trailmagic.jumper.core.security.UserService
import org.junit.Assert._
import org.junit.{Before, Test}
import org.mockito.{MockitoAnnotations, Mock}
import com.trailmagic.jumper.core.{InMemoryUserRepository, User, UserRepository}


class UsersControllerTest {
  var controller: UsersController = _
  var userRepository: UserRepository = _
  @Mock var userService: UserService = _
  val TestUser = User("testy@example.com", "tester", "Testy", "McTesterton", "password", "salt")

  @Before
  def setUp() {
    MockitoAnnotations.initMocks(this)
    userRepository = new InMemoryUserRepository
    controller = new UsersController(userRepository, userService)
  }

  @Test
  def testUserProfilePage() {
    val savedUser = userRepository.save(TestUser)

    val mav = controller.displayUserProfile(TestUser.username)

    assertEquals(savedUser, mav.getModel.get("user"))
  }

  @Test(expected = classOf[ResourceNotFoundException])
  def testUserProfilePageThrowsExceptionWhenUserNotFound() {
    controller.displayUserProfile("no user")
  }
  }
