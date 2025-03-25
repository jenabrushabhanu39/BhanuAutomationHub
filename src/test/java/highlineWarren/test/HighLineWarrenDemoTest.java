package highlineWarren.test;

import java.io.IOException;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


import utils.Constants;

import base.BaseTest;
import jdk.internal.org.jline.utils.Log;
import pageEvents.HomePageEvents;
import pageEvents.LoginPageEvents;
import pageObjects.LoginPageElements;
import utils.ElementFetch;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


@Listeners(utils.SuiteListener.class)
public class HighLineWarrenDemoTest extends BaseTest {
	private ElementFetch ele = new ElementFetch();
	private HomePageEvents homePage;
	private LoginPageEvents loginPage;

	@BeforeMethod
	public void initializePageObjects() {
		homePage = new HomePageEvents(driver);  // Pass driver to HomePageEvents
		loginPage = new LoginPageEvents(driver);  // Pass driver to LoginPageEvents
	}

	@Test(priority=1)
	public void highLineWarrenTitleValidation() {
		String actualTitle=homePage.HomePageTitle();
		System.out.println("Actual Title: "+ actualTitle);
		Assert.assertEquals(actualTitle, Constants.TITLE, "Page title does not match!");

	}	
	@Test(priority=2)
	public void verifyHighLineWarrenLogo() {

		WebElement logo = driver.findElement(By.xpath("//*[@id='header']/div/div[2]/div/div[2]/a/img"));
		Assert.assertTrue(logo.isDisplayed(), "HighLineWarren Logo is NOT displayed");
		String expectedSrc = "https://www.shophighlinewarren.com/INTERSHOP/static/WFS/HIGHLINE-AFTERMARKET-Site/-/HIGHLINE-AFTERMARKET/en_US/warrenlogo_web.png";
		String actualSrc = logo.getAttribute("src");
		Assert.assertTrue(actualSrc.contains(expectedSrc), "Logo source is incorrect: " + actualSrc);
		int actualX = logo.getLocation().getX();
		int actualY = logo.getLocation().getY();
		System.out.println("📍 HighLineWarren Logo Coordinates:");
		System.out.println("X Position: " + actualX);
		System.out.println("Y Position: " + actualY);
		int expectedX = 79;
		int expectedY = 67;
		Assert.assertEquals(actualX, expectedX, "X-coordinate mismatch!");
		Assert.assertEquals(actualY, expectedY, "Y-coordinate mismatch!");
		System.out.println("HighLineWarren Logo position validation PASSED");

		String widthAttr = logo.getAttribute("width");
		String heightAttr = logo.getAttribute("height");
		if (widthAttr != null && heightAttr != null) {
			int expectedWidth = 325; // Replace with actual width
			int expectedHeight = 84;  // Replace with actual height
			int actualWidth = Integer.parseInt(widthAttr);
			int actualHeight = Integer.parseInt(heightAttr);

			System.out.println("HighLineWarren Logo Dimensions:");
			System.out.println("Actual Width: " + actualWidth);
			System.out.println("Actual Height: " + actualHeight);


			Assert.assertEquals(actualWidth, expectedWidth, "Logo width mismatch");
			Assert.assertEquals(actualHeight, expectedHeight, "Logo height mismatch");
			System.out.println("Logo dimensions are correct.");
		} else {
			System.out.println("Width/Height attributes not found, skipping dimension validation.");
		}

		System.out.println("HighLineWarren Logo validation PASSED");
	}


	@Test(priority=3)
	public void verifyHighLineWarrenUrl()
	{
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "https://www.shophighlinewarren.com/", "URL is incorrect!");
		System.out.println("HighLineWarren URL is Up and Running: " + currentUrl);

	}


	@Test(priority=4)
	public void verifyBrowserByCategoryIsClickable() throws InterruptedException
	{
		loginPage.verifyIfBrowseByCategoryIsClickable();
		boolean status = driver.findElement(By.xpath("//*[@id='globalnav']/div/div/ul/li[2]/a[1]")).isDisplayed();
		Assert.assertTrue(status, "Browse By Category is NOT clickable");
		System.out.println("Browse By Category is Clickable");

	}

	@Test(priority=5)
	public void verifyAllCategoryAreScrollableAndCountAllCategories() throws InterruptedException {
		loginPage.verifyIfBrowseByCategoryIsClickable();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		int expectedCategories = 9;// Get the total number of categories

		for (int i = 1; i <= expectedCategories; i++) {
			loginPage.verifyIfBrowseByCategoryIsClickable();
			WebElement category = driver.findElement(By.xpath("(//li[@class='main-navigation-level1-item'])[" + i + "]"));
			js.executeScript("arguments[0].scrollIntoView(true);", category);
			category.click();
			Thread.sleep(2000);
		}
	}    
	@Test(priority=6)
	public void verifyFirstCategoryIsClickableAndFunctional() throws InterruptedException, TimeoutException {
		loginPage.verifyIfBrowseByCategoryIsClickable();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
		System.out.println("Clicking on the First Category");


		List<WebElement> categoryElements = driver.findElements(By.xpath("//li[@class='main-navigation-level1-item']"));
		if (categoryElements.isEmpty()) {
			System.out.println("No categories found, exiting...");
			return;
		}

		WebElement firstCategory = categoryElements.get(0);

		js.executeScript("arguments[0].scrollIntoView(true);", firstCategory);
		wait.until(ExpectedConditions.elementToBeClickable(firstCategory));
		firstCategory.click();
		System.out.println("Clicked on the First Category");        
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class, 'main-navigation-level2-item')]")));

		js.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);
		js.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);


		List<WebElement> subcategoryElements = driver.findElements(By.xpath("//li[contains(@class, 'main-navigation-level2-item')]"));
		System.out.println("First Category has " + subcategoryElements.size() + " subcategories");

		for (WebElement subcategory : subcategoryElements) {
			String subcategoryText = subcategory.getAttribute("innerText").trim();
			System.out.println(" Subcategory: " + (subcategoryText.isEmpty() ? "Empty subcategory" : subcategoryText));
		}
	}

	@Test(priority=7)
	public void verifySecondCategory() throws InterruptedException, TimeoutException {
		loginPage.verifyIfBrowseByCategoryIsClickable();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

		System.out.println("Clicking on the Second Category");


		List<WebElement> categoryElements = driver.findElements(By.xpath("//li[@class='main-navigation-level1-item']"));

		if (categoryElements.isEmpty()) {
			System.out.println("No categories found, exiting...");
			return;
		}

		WebElement secondCategory = categoryElements.get(1);
		js.executeScript("arguments[0].scrollIntoView(true);", secondCategory);
		wait.until(ExpectedConditions.elementToBeClickable(secondCategory));
		secondCategory.click();

		System.out.println("Clicked on the First Category");


		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class, 'main-navigation-level2-item')]")));
		js.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);
		js.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);


		List<WebElement> subcategoryElements = driver.findElements(By.xpath("//li[contains(@class, 'main-navigation-level2-item')]"));
		System.out.println("Second Category has " + subcategoryElements.size() + " subcategories");

		for (WebElement subcategory : subcategoryElements) {
			String subcategoryText = subcategory.getAttribute("innerText").trim();
			System.out.println(" Subcategory: " + (subcategoryText.isEmpty() ? "Empty subcategory" : subcategoryText));
		}


	}
	@Test(priority=8)
	public void verifySecondCategoryFunctionalityWithBrands() throws InterruptedException, TimeoutException {
		loginPage.verifyIfBrowseByCategoryIsClickable();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

		System.out.println("Clicking on the Second Category");


		List<WebElement> categoryElements = driver.findElements(By.xpath("//li[@class='main-navigation-level1-item']"));

		if (categoryElements.isEmpty()) {
			System.out.println("No categories found, exiting...");
			return;
		}

		WebElement secondCategory = categoryElements.get(1);
		js.executeScript("arguments[0].scrollIntoView(true);", secondCategory);
		wait.until(ExpectedConditions.elementToBeClickable(secondCategory));
		secondCategory.click();

		System.out.println("Clicked on the First Category");


		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class, 'main-navigation-level2-item')]")));
		js.executeScript("window.scrollBy(0,500);");
		Thread.sleep(1000);
		js.executeScript("window.scrollBy(0,500);");



		Thread.sleep(3000); 
		List<WebElement> brandElements = driver.findElements(By.xpath("//*[@id='filter-accordion']/div[2]"));

		if (brandElements.isEmpty()) {
			System.out.println("No brands found for 'engine oil'. Waiting...");
			Thread.sleep(5000);
			brandElements = driver.findElements(By.xpath("//*[@id='filter-accordion']/div[2]"));
		}

		if (brandElements.isEmpty()) {
			System.out.println("No brands found even after waiting.");
		} else {
			System.out.println("Found " + brandElements.size() + " brands:");
			for (WebElement brand : brandElements) {
				String brandText = brand.getText().trim();
				System.out.println("Brand: " + (brandText.isEmpty() ? " Empty brand name" : brandText));
			}
		}
	}

	@Test(priority=9)
	public void verifySearchFunctionalityIsClickable() {
		loginPage.searchForItem("ENGINE DEGREASER");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement searchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//ul[@class='search-suggest-results'])[1]")));
		Assert.assertTrue(searchResults.isDisplayed(), "Search results are NOT displayed");
		WebElement searchButton = driver.findElement(By.xpath("(//button[@class='btn-search btn btn-primary'])[1]"));
		searchButton.click();
		System.out.println("Search functionality is working correctly for 'ENGINE DEGREASER'");    

	}

	@Test(priority=10)
	public void verifySearchFunctionalityWithBrands() throws InterruptedException, TimeoutException {
		loginPage.searchForItem("Engine oil");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement searchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//ul[@class='search-suggest-results'])[1]")));
		Assert.assertTrue(searchResults.isDisplayed(), "❌ Search results are NOT displayed");
		WebElement searchButton = driver.findElement(By.xpath("(//button[@class='btn-search btn btn-primary'])[1]"));
		searchButton.click();
		System.out.println("Search functionality is working correctly for 'engine oil'");
		Thread.sleep(3000); 
		By searchResultsContainer = By.xpath("//div[contains(@class, 'search-container header-search-container')]");
		wait.until(ExpectedConditions.presenceOfElementLocated(searchResultsContainer));
		List<WebElement> brandElements = driver.findElements(By.xpath("//*[@id='filter-accordion']/div[1]"));

		if (brandElements.isEmpty()) {
			System.out.println("No brands found for 'engine oil'. Waiting...");
			Thread.sleep(5000);
			brandElements = driver.findElements(By.xpath("//*[@id='filter-accordion']/div[1]"));
		}

		if (brandElements.isEmpty()) {
			System.out.println("No brands found even after waiting.");
		} else {
			System.out.println("Found " + brandElements.size() + " brands:");
			for (WebElement brand : brandElements) {
				String brandText = brand.getText().trim();
				System.out.println("Brand: " + (brandText.isEmpty() ? " Empty brand name" : brandText));
			}
		}
	}
}