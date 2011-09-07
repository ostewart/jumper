package com.trailmagic.jumper.core.signup

import org.junit.Assert._
import com.mongodb.casbah.MongoConnection
import org.junit.{Before, Test}
import org.joda.time.DateTime

class MongoSignupRepositoryTest {
  val con = MongoConnection()
  val TestDbName = "test_jumper"
  val db = con(TestDbName)
  val repository = new MongoSignupRepository(db)
  val signups = db("signups")
  val FullSignupInfo = SignupInfo(Some("Testy"), Some("McTesterton"), Some("United States"),
                                Some("10001"), Some(SignupSource.Friend), Some("Sounds awesome"))
  val FullSignup = SignupRequest("foo@example.com", new DateTime(), Some(FullSignupInfo))

  @Before
  def setUp() {
    signups.drop()
  }

  @Test
  def testCanSaveAndRetrieveSingleSignupWithoutExtraInfo() {
    val signup = SignupRequest("foo@example.com", new DateTime, None)

    repository.save(signup)
    
    val results = repository.findAll()

    assertEquals(1, signups.find().size)
    assertEquals(1, results.size)
    assertEquals(signup, results(0))
  }
  
  @Test
  def testCanSaveAndRetrieveSingleSignupWithExtraInfo() {
    repository.save(FullSignup)

    val results = repository.findAll()

    assertEquals(1, results.size)
    assertEquals(FullSignup, results(0))
    assertEquals(FullSignupInfo, results(0).extraInfo.get)
  }

  @Test
  def testCanSaveAndRetrieveSingleSignupWithPartialExtraInfo() {
    val signupInfo = FullSignupInfo.copy(lastName = None, country = None, postalCode = None, comments = None, source = None)
    val signup = FullSignup.copy(extraInfo = Some(signupInfo))

    repository.save(signup)

    val results = repository.findAll()
    assertEquals(1, results.size)
    val foundInfo: SignupInfo = results(0).extraInfo.get
    assertEquals(signupInfo, foundInfo)
    assertEquals(None, foundInfo.country)
    assertEquals(FullSignupInfo.firstName, foundInfo.firstName)
  }

  @Test
  def testCanSaveAndRetrieveMultipleSignup() {
    val secondSignup = SignupRequest("foo@bar.com", new DateTime, None)

    repository.save(FullSignup)
    repository.save(secondSignup)

    val results = repository.findAll()

    assertEquals(2, results.size)
    results.foreach(_ match {
      case `FullSignup` =>
      case `secondSignup` =>
      case _ => fail("unexpected signup")
    })
  }
}