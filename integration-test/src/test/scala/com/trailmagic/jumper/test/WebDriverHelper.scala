package com.trailmagic.jumper.test

import org.openqa.selenium.{By, WebDriver}
import org.junit.Assert._

class WebDriverHelper(driver: WebDriver) {
  def fillAndSubmitLoginForm() {
    driver.findElement(By.id("username")).sendKeys("tester");
    val passwordElement = driver.findElement(By.id("password"));
    passwordElement.sendKeys("password");
    passwordElement.submit();
  }

  def login() {
    driver.get(WebConstants.BaseUrl + "/");

    assertEquals(WebConstants.SecureBaseUrl + "/login", driver.getCurrentUrl)

    fillAndSubmitLoginForm()
  }
}
