package org.wildfly.swarm.it.jaas.form;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.wildfly.swarm.it.AbstractIntegrationTest;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JAASApplicationIT extends AbstractIntegrationTest {

    @Drone
    WebDriver browser;

    @After
    public void tearDown() throws Exception {
        browser.manage().deleteAllCookies();
    }

    @Test
    public void testLoginOK() {
        browser.get("http://localhost:8080/");
        assertThat(browser.getPageSource()).contains("<h1>Login</h1>");

        browser.findElement(By.name("j_username")).sendKeys("Penny");
        browser.findElement(By.name("j_password")).sendKeys("password");
        browser.findElement(By.name("submit")).click();

        assertThat(browser.getPageSource()).contains("<h1>Employees</h1>");
    }

    @Test
    public void testLoginError() {
        browser.get("http://localhost:8080/");

        browser.findElement(By.name("j_username")).sendKeys("invalid");
        browser.findElement(By.name("j_password")).sendKeys("invalid");
        browser.findElement(By.name("submit")).click();

        assertThat(browser.getPageSource()).contains("<h1>Login Error</h1>");
    }

}
