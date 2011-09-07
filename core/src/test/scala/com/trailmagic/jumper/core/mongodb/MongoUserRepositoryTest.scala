package com.trailmagic.jumper.core.mongodb

import org.junit.{Test, Before}
import org.junit.Assert._
import com.trailmagic.jumper.core._
import com.trailmagic.jumper.core.security._
import com.mongodb.casbah.MongoConnection

class MongoUserRepositoryTest {
  val con = MongoConnection()
  val TestDbName = "test_jumper"

  val db = con(TestDbName)
  val users = db("users")
  var repository: MongoUserRepository = _

  val TestUser = User("testy@example.com", "Tester", "Testy", "McTesterton", "4879b790020a04377613fd860a4e754add628f6bfdd5c1e675a28c286d379ebd", "cc11ce5ba1cd05411aa492b1a2f6bd78")
  val AnotherUser = User("another@example.com", "anoTher", "Another", "User", "password", "salt")

  @Before
  def setUp() {
    users.drop()
    repository = new MongoUserRepository(db)
  }

  @Test
  def testSaveAndFindUserById() {
    val savedUser = repository.save(TestUser)
    val foundUser = repository.findById(savedUser.id).get

    assertEquals(savedUser, foundUser)
    assertEquals(TestUser, foundUser.user)
    assertEquals(TestUser, savedUser.user)
  }

  @Test
  def testFindByUsername() {
    val savedUser = repository.save(TestUser)
    repository.save(AnotherUser)

    val foundUser = repository.findByUsername(TestUser.username).get

    assertEquals(savedUser, foundUser)
    assertEquals(TestUser, foundUser.user)
    assertEquals(TestUser.username, foundUser.username)
  }

  @Test
  def testFindUsersFindsAllSpecifiedExistingUsers() {
    val savedUsers = List(TestUser, AnotherUser).map(repository.save)

    val foundUsers = repository.findUsers(List(TestUser.username, AnotherUser.username))

    assertEquals(savedUsers(0), foundUsers(TestUser.username))
    assertEquals(savedUsers(1), foundUsers(AnotherUser.username))
    assertEquals(2, foundUsers.size)
  }

  @Test
  def testFindUsersFindsAllUsersIrrespectiveOfCase() {
    val savedUsers = List(TestUser, AnotherUser).map(repository.save)

    val foundUsers = repository.findUsers(List("TesTeR", "aNoTHer"))

    assertEquals(savedUsers(0), foundUsers(TestUser.username))
    assertEquals(savedUsers(1), foundUsers(AnotherUser.username))
    assertEquals(2, foundUsers.size)
  }

  @Test
  def testFindUsersIgnoresUnknownUsernames() {
    val foundUsers = repository.findUsers(List("junk"))

    assertEquals(Map(), foundUsers)
  }

  @Test(expected = classOf[NonUniqueUsernameException])
  def testCannotInsertUserWithDuplicateUsername() {
    repository.save(AnotherUser)
    repository.save(AnotherUser.copy(email = "notanother@email.com"))
  }

  @Test(expected = classOf[NonUniqueUsernameException])
  def testDifferentCaseUsernameCountsAsDuplicateId() {
    repository.save(AnotherUser)
    repository.save(AnotherUser.copy(username = AnotherUser.username.toUpperCase, email = "notanother@email.com"))
  }

  @Test(expected = classOf[NonUniqueEmailException])
  def testCannotInsertUserWithDuplicateEmail() {
    repository.save(AnotherUser)
    repository.save(AnotherUser.copy(username = "notanother"))
  }

  @Test(expected = classOf[NonUniqueEmailException])
  def testCannotChangeUserEmailToDuplicate() {
    repository.save(TestUser)
    val savedUser: SavedUser = repository.save(AnotherUser)
    repository.save(SavedUser(savedUser.id, savedUser.user.copy(email = TestUser.email)))
  }

  @Test
  def testUserIdIsLowercaseUsername() {
    val savedUser = repository.save(TestUser.copy(username = "TesTeR"))

    assertEquals("tester", savedUser.id)
    assertEquals("TesTeR", savedUser.username)
    assertEquals(savedUser, repository.findById(savedUser.id).get)
    assertEquals("TesTeR", repository.findByUsername(savedUser.username).get.username)
  }

  @Test
  def testFindByIdAlwaysUsesLowercase() {
    val savedUser = repository.save(TestUser.copy(username = "TesTeR"))
    assertEquals("tester", savedUser.id)
    assertEquals(savedUser, repository.findById(savedUser.username).get)
  }

  @Test
  def testFindByUsernameAlwaysUsesLowercase() {
    val savedUser = repository.save(TestUser.copy(username = "TesTeR"))
    assertEquals("tester", savedUser.id)
    assertEquals(savedUser, repository.findByUsername("tEStEr").get)
  }

  @Test
  def testCanUpdateSavedUser() {
    val savedUser = repository.save(TestUser.copy(username = "TesTeR"))
    val updatedUser = repository.save(SavedUser(savedUser.id, savedUser.user.copy(lastName = "Testleton")))

    assertEquals("Testleton", updatedUser.lastName)
    assertEquals("Testleton", repository.findById(savedUser.id).get.lastName)
  }

  @Test
  def testCanSaveAndRetrieveUserWithFriends() {
    val facebookFriend = Friend(None, Some("12345"))
    val localFriend = Friend(Some("another"), None)
    val facebookAndLocalFriend = Friend(Some("third"), Some("4567"))
    val user = TestUser.copy(friends = Set(facebookFriend, localFriend, facebookAndLocalFriend))
    val savedUser = repository.save(user)

    val foundUser = repository.findById(savedUser.id).get

    assertTrue(foundUser.friends.contains(facebookFriend))
    assertTrue(foundUser.friends.contains(localFriend))
    assertTrue(foundUser.friends.contains(facebookAndLocalFriend))

  }

  @Test
  def testCanAddFacebookIdToExistingUser() {
    val savedUser = repository.save(TestUser)
    assertEquals(None, repository.findById(savedUser.id).get.facebookId)

    val updatedUser = repository.save(SavedUser(savedUser.id, savedUser.user.copy(facebookId = Some("1234"))))

    assertEquals(Some("1234"), updatedUser.facebookId)
    assertEquals(Some("1234"), repository.findById(savedUser.id).get.facebookId)
  }

  @Test
  def testCanFindByFacebookId() {
    val savedUser = repository.save(TestUser.copy(facebookId = Some("1234")))
    val results = repository.findWithFacebookIds(Set("1234"))
    assertEquals(Set(savedUser), results)
  }

  @Test
  def testCanFindMultipleByFacebookId() {
    val ids = Set("1234", "2345", "4567", "7878")
    val savedUsers = ids.map((id) => repository.save(TestUser.copy(username = id, facebookId = Some(id), email = id + "@foo.com")))

    assertEquals(savedUsers.toSet, repository.findWithFacebookIds(ids))
  }

  @Test
  def testFindByFacebookWithNoIdsYieldsEmptySet() {
    assertEquals(Set(), repository.findWithFacebookIds(Set()))
  }
}