package com.trailmagic.jumper.test

import org.junit.Test
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.{By, WebDriver}
import org.junit.Assert._


class JavascriptTest {
  private var driver: WebDriver = _

  @Test
  def runJavascriptTestHarness() {
    driver = new FirefoxDriver()
    driver.get(WebConstants.BaseUrl + "/javascript/test/test-runner.html")
    waitUntilTestsFinish()
    assertNoFailures()
    driver.quit()
  }

  private def assertNoFailures() {
    val runner = driver.findElement(By.className("runner"))
    assertTrue(runner.getAttribute("class").contains("passed"))
  }

  private def waitUntilTestsFinish() {
    val beginTime = System.currentTimeMillis
    while (System.currentTimeMillis < beginTime + 10 * 1000) {
      try {
        val finished = driver.findElement(By.className("finished-at"))
        if (finished != null && finished.isDisplayed) {
          return
        }
      }
      catch {
        case e: NoSuchElementException => {
        }
      }
    }
  }
}