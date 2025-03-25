package utils;
import org.testng.IRetryAnalyzer;

import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	private int retryCount = 0;
    private int maxRetryCount = 3; // max retries
	
@Override
public boolean retry(ITestResult result) {
    if (retryCount < maxRetryCount) {
        retryCount++;
        return true;
    }
    return false;
}
}
