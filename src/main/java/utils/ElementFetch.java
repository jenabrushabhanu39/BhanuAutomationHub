package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import base.BaseTest;
import java.util.List;
public class ElementFetch {

	public WebElement getwebelement(String identifierType, String identifierValue)
	{
		switch(identifierType) {
		case "XPATH":
			return BaseTest.driver.findElement(By.xpath(identifierValue));
		case "CSS":
			return BaseTest.driver.findElement(By.cssSelector(identifierValue));
		case "ID":
			return BaseTest.driver.findElement(By.id(identifierValue));
		case "CLASSNAME":
			return BaseTest.driver.findElement(By.className(identifierValue));

		case "TAGNAME":
			return BaseTest.driver.findElement(By.tagName(identifierValue));

		default:
			return null;

		}
	}

	public List<WebElement> getwebelements(String identifierType, String identifierValue)
	{
		switch(identifierType) {
		case "XPATH":
			return BaseTest.driver.findElements(By.xpath(identifierValue));
		case "CSS":
			return BaseTest.driver.findElements(By.cssSelector(identifierValue));
		case "ID":
			return BaseTest.driver.findElements(By.id(identifierValue));
		case "CLASSNAME":
			return BaseTest.driver.findElements(By.className(identifierValue));

		case "TAGNAME":
			return BaseTest.driver.findElements(By.tagName(identifierValue));

		default:
			return null;

		}
	}

}
