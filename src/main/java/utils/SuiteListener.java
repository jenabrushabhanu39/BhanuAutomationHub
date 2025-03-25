package utils;
import java.io.File;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IRetryAnalyzer;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IAnnotationTransformer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;

import base.BaseTest;
import org.testng.ITestListener;

@Listeners(SuiteListener.class)
public class SuiteListener implements ITestListener, IAnnotationTransformer{
	
@Override
	public void onTestFailure(ITestResult result) {
	// Construct the screenshot filename with date and time
		String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
		String methodName = result.getMethod().getMethodName();
		String filename = methodName + ".png";
		
		
		//Create the screenshot directory if it doesn't exist
		String screenshotDirPath = System.getProperty("user.dir") + File.separator + "screenshot" + File.separator 
	            + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
	    File screenshotDir = new File(screenshotDirPath);
	    if (!screenshotDir.exists()) {
	        screenshotDir.mkdirs();
	    }
	    
	 // Take screenshot
	    File screenshotFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);

	    try {
	        // Save screenshot with date-wise file name
	        FileUtils.copyFile(screenshotFile, new File(screenshotDir + File.separator + filename));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);   
		
//@Override
//public void transform(ITestAnnotation annotation, Class<?> testClass, Constructor<?> testConstructor, Method testMethod) {
//    annotation.setRetryAnalyzer(RetryAnalyzer.class);
}		
		  }
	
	
	


