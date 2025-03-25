package pageEvents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pageObjects.HomePageElements;
import utils.ElementFetch;

public class HomePageEvents {
	ElementFetch ele=new ElementFetch();
public WebDriver driver;
	
	public HomePageEvents(WebDriver driver) {  // Constructor to set the driver
		this.driver = driver;
	}
	public String HomePageTitle() {
		return driver.getTitle();

	}
	
}

