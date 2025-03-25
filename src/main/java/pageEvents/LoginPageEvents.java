package pageEvents;

import java.io.File;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.mail.*;
import javax.mail.search.SubjectTerm;

import java.util.List;
import java.util.Properties;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.reporters.Files;

import utils.Constants;
import pageObjects.LoginPageElements;
import utils.ElementFetch;

public class LoginPageEvents {
	ElementFetch ele=new ElementFetch();
	public WebDriver driver;
	

	public LoginPageEvents(WebDriver driver) {  // Constructor to set the driver
		this.driver = driver;
	}
	public void verifyIfBrowseByCategoryIsClickable()
	{
		ele.getwebelement("XPATH", LoginPageElements.BrowseByCategory).click();

	}
	
	public void searchForItem(String searchQuery) {
        WebElement searchBox = driver.findElement(By.xpath("(//input[@name='SearchTerm'])[1]"));
        searchBox.sendKeys(searchQuery);
        


		
	}
	
}





