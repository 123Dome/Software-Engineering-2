package org.hbrs.se2.project.startupx.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import org.hbrs.se2.project.startupx.entities.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
public class RegistrationTest {
	private static WebDriver driver;

	@BeforeEach
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // wait 2s to let vaadin load, before exiting on error
	}

	@AfterEach
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testRegistration() {
		/*
		 * Ablauf:
		 * 1. open webpage
		 * 2. go to registration
		 * 3. fill registration values
		 * 4. register
		 * 5. login
		 * 6. if successful great
		 * 7. go to profile
		 * 8. go to profile edit
		 * 9. delete profile
		 * 10. confirm delete
		*/
		driver.get("http://localhost:8080");

		// need to wait 2s for page to load, so page title is taken correctly (the driver's implicit wait doesn't do anything here)
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ignored) {}

		assertEquals("Login", driver.getTitle());

		// Startseite: Registrierungs Button drücken
		driver.findElement(By.id("registerStudentButton")).click();

		// Test data
		var benutzername = "quack";
		var vorname = "Donald";
		var nachname = "Duck";
		var email = "donald@duck.com";
		var geburtsdatum = LocalDate.of(2025, 3, 6);
		var matrikelnr = "11111";
		var studiengang = "Informatik";
		var skills = "Photoshop";
		var passwort = "Ente123!";
		var passwortBestaetigen = "Ente123!";

		// Registrierungsseite: Daten eingeben
		//var benutzerNameTextField = driver.findElement(By.id("BenutzernameTextField"));
		var benutzerNameTextField = driver.findElement(By.id("input-vaadin-text-field-38"));
		benutzerNameTextField.sendKeys(benutzername);

		var emailTextField = driver.findElement(By.id("input-vaadin-text-field-39"));
		emailTextField.sendKeys(email);

		var vornameTextField = driver.findElement(By.id("input-vaadin-text-field-40"));
		vornameTextField.sendKeys(vorname);

		var nachnameTextField = driver.findElement(By.id("input-vaadin-text-field-41"));
		nachnameTextField.sendKeys(nachname);

		var geburtsdatumDatePicker = driver.findElement(By.id("input-vaadin-date-picker-42"));
		geburtsdatumDatePicker.sendKeys(DateTimeFormatter.ofPattern("d/M/yyyy").format(geburtsdatum));
		geburtsdatumDatePicker.sendKeys(Keys.ENTER); // close pop-up Date Picker

		var matrikelnrTextField = driver.findElement(By.id("input-vaadin-integer-field-43"));
		matrikelnrTextField.sendKeys(matrikelnr);

		var studiengangComboBox = driver.findElement(By.id("input-vaadin-combo-box-45"));
		studiengangComboBox.sendKeys(studiengang);
		studiengangComboBox.sendKeys(Keys.ENTER);
		studiengangComboBox.sendKeys(Keys.TAB); // close pop-up Combo Box

		// multi select combo box needs click -> input text -> enter -> tab to successfully set the value
		var skillsMultiSelectComboBox = driver.findElement(By.id("input-vaadin-multi-select-combo-box-47"));
		skillsMultiSelectComboBox.click();
		skillsMultiSelectComboBox.sendKeys(skills);
		skillsMultiSelectComboBox.sendKeys(Keys.ENTER);
		skillsMultiSelectComboBox.sendKeys(Keys.TAB); // close pop-up Multi Select Combo Box

		var passwortTextField = driver.findElement(By.id("input-vaadin-password-field-48"));
		passwortTextField.sendKeys(passwort);

		var passwortBestaetigenTextField = driver.findElement(By.id("input-vaadin-password-field-49"));
		passwortBestaetigenTextField.sendKeys(passwortBestaetigen);

		// Test if all values are set correctly
		assertEquals(benutzername, benutzerNameTextField.getAttribute("value")); // input text is stored in attribute value
		assertEquals(email, emailTextField.getAttribute("value"));
		assertEquals(vorname, vornameTextField.getAttribute("value"));
		assertEquals(nachname, nachnameTextField.getAttribute("value"));
		assertEquals(DateTimeFormatter.ofPattern("d/M/yyyy").format(geburtsdatum), geburtsdatumDatePicker.getAttribute("value"));
		assertEquals(matrikelnr, matrikelnrTextField.getAttribute("value"));
		assertEquals(studiengang, studiengangComboBox.getAttribute("value"));
		assertEquals(skills, skillsMultiSelectComboBox.getAttribute("placeholder")); // placeholder holds comma separated list of selected skills, but we have only 1 item so thats fine
		assertEquals(passwort, passwortTextField.getAttribute("value"));
		assertEquals(passwortBestaetigen, passwortBestaetigenTextField.getAttribute("value"));

		// registrieren button drücken
		var registrierenButton = driver.findElement(By.id("RegistrierenButton"));
		registrierenButton.click();

		// wenn alles gut lief sollte jetzt wieder auf Login Page sein
		// need to wait 2s for page to load, so page title is taken correctly (the driver's implicit wait doesn't do anything here)
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ignored) {}
		assertEquals("Login", driver.getTitle());
	}


}
