package com.abby.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchImage{

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		//Launch the browser
		System.setProperty("webdriver.chrome.driver",".\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-gpu","window-size=1024,768","--no-sandbox");
		RemoteWebDriver driver = new ChromeDriver(options);
		
		//Open website and search with local image
		driver.get("http://www.baidu.com");
		driver.findElementByClassName("soutu-btn").click();
		new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.className("upload-pic")));
		String imgPath = "D:\\abby\\eclipse-workspace\\seleniumTest\\upload\\cat.jpg";
		driver.findElement(By.className("upload-pic")).sendKeys((imgPath));
		new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("graph-same-list-item")));
        
		//Get the VISIT_RESULT value from myconfig.properties file and click on the related result
		Properties properties = getConfig();
		int visitResult = Integer.parseInt(properties.getProperty("VISIT_RESULT"));
		List<WebElement> resultList = driver.findElementsByClassName("graph-same-list-item");
		resultList.get(visitResult-1).click();
		Thread.sleep(2000);
		
		//Take screenshot for the last page
		String handle = driver.getWindowHandle();
        for (String handles:driver.getWindowHandles()) {
        	if (handles.equals(handle)) {
        		continue;
        	}
            driver.switchTo().window(handles);
            takeScreen(driver, "searchResult");
        }
		driver.quit();
	}
	
	public static void takeScreen(RemoteWebDriver driver,String filename) {
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
        try {
            //copy the generated screenshot to the given directory
        	FileUtils.copyFile(src, new File(".\\screenshots\\"+filename+".png"));
        }
         
        catch (IOException e)
         {
        	System.out.println(e.getMessage());
        }
	}
	
	public static Properties getConfig() throws FileNotFoundException {
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream(".\\myconfig.properties");
		try {
			properties.load(inputStream);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

}
