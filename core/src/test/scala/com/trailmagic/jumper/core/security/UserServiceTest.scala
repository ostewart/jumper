package com.trailmagic.jumper.core.security

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.junit.Assert._
import com.trailmagic.jumper.core.{InMemoryUserRepository, UserRepository, SavedUser, User}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.{Ignore, Before, Test}
import org.springframework.security.authentication.encoding.{PasswordEncoder, ShaPasswordEncoder}
import org.mockito.{MockitoAnnotations, Mock}

class UserServiceTest {
  var repository: UserRepository = _
  val TestUser = SavedUser("tester", User("testy@example.com", "tesTer", "Testy", "McTesterton", "password", "salt"))
  var service: UserService = _
  @Mock var saltGenerator: SaltGenerator = _
  @Mock var passwordEncoder: PasswordEncoder = _

  @Before
  def setUp() {
    MockitoAnnotations.initMocks(this)
    repository = new InMemoryUserRepository
    service = new UserService(saltGenerator, passwordEncoder, repository)
  }
  @Test
  def testFindsUserByUsername() {
    repository.save(TestUser)

    val user = service.loadUserByUsername(TestUser.username)
    assertTrue(user.isInstanceOf[JumperUserDetails])
    assertEquals(TestUser, user.asInstanceOf[JumperUserDetails].user)
  }

  @Test
  def testFindsUserByUsernameWithDifferentCase() {
    repository.save(TestUser)

    val user = service.loadUserByUsername(TestUser.username.toUpperCase)
    assertTrue(user.isInstanceOf[JumperUserDetails])
    assertEquals(TestUser, user.asInstanceOf[JumperUserDetails].user)
  }

  @Test(expected = classOf[UsernameNotFoundException])
  def testThrowsExceptionWhenNoUserFound() {
    service.loadUserByUsername("foo")
  }


  @Test(expected = classOf[NonUniqueUsernameException])
  def testThrowsExceptionForAlreadyExistingUser() {
    val testUser = User("testy@example.com", "username", "first", "last", "password", "")
    service.createUser(testUser)
    service.createUser(testUser)
  }

  @Test
  def testAddsSaltAndEncodesPasswordBeforeSavingUser() {
    val testUser = User("testy@example.com", "username", "first", "last", "password", "random junk")
    val salt = "sea salt"
    val encodedPassword = "encoded password"

    when(saltGenerator.nextSalt).thenReturn(salt)
    when(passwordEncoder.encodePassword(anyString(), any())).thenReturn(encodedPassword)

    val newUser = service.createUser(testUser)

    verify(passwordEncoder).encodePassword(testUser.password, salt)
    assertEquals(encodedPassword, newUser.password)
    assertNotNull(newUser.id)
  }

  @Test
  @Ignore
  def convenienceMethodToManuallyGenerateAPassword() {
    service = new UserService(new SaltGenerator, new ShaPasswordEncoder(256), repository)
    assertEquals("no", service.createUser(User("testy@example.com", "tester", "Testy", "McTesterton", "password", "shouldn't matter")))
  }
}