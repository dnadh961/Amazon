package com.kindle.util;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	public Properties dataProp;
	public String[] authorList;
	public WebDriverWait wait;

	public GenericKeywords() {
		prop = new Properties();
		dataProp = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/Config.properties");
			prop.load(fis);
			FileInputStream fis1 = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/Data.properties");
			dataProp.load(fis1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void openBrowser() {
		String browserType = prop.getProperty("browser_type");
		if (browserType.equalsIgnoreCase("mozilla")) {
			System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		} else if (browserType.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);

		} else if (browserType.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 15);
	}

	public void moveToMouse(WebElement element) {
		Actions act = new Actions(driver);
		act.moveToElement(element).build().perform();
	}

	public void navigate(String url) {
		url = prop.getProperty("amazon_url");
		driver.navigate().to(url);
	}

	public void click(String locatorKey) {
		WebElement e = getElement(locatorKey);
		e.click();
	}

	public void input(String locatorKey, String data) {
		WebElement e = getElement(locatorKey);
		data = dataProp.getProperty(data);
		e.sendKeys(data);
	}

	public void write(String locatorKey, String data) {
		WebElement e = getElement(locatorKey);
		e.sendKeys(data);
	}

	public WebElement getElement(String locatorKey) {
		WebElement e = null;
		System.out.println(prop.getProperty(locatorKey));
		try {
			if (locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(("Failure in Element Extraction - " + locatorKey));
			;
		}
		return e;
	}

	public List<WebElement> getElements(String locatorKey) {
		List<WebElement> list = null;
		System.out.println(prop.getProperty(locatorKey));
		try {
			if (locatorKey.endsWith("_id")) {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(prop.getProperty(locatorKey))));
				list = driver.findElements(By.id(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_xpath")) {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(prop.getProperty(locatorKey))));
				list = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
			}

			else if (locatorKey.endsWith("_name"))
				list = driver.findElements(By.name(prop.getProperty(locatorKey)));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(("Failure in Element Extraction - " + locatorKey));
			;
		}
		return list;
	}

	public boolean isElementPresent(String xpath) {
		try {
			driver.findElement(By.xpath(xpath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void waitforElement(String xpathExpression) throws InterruptedException {
		WebDriverWait wWait = new WebDriverWait(driver, 100);
		wWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression)));
		Thread.sleep(2000);
	}

	public void waitforElementAbsence(String xpathExpression) throws InterruptedException {
		WebDriverWait wWait = new WebDriverWait(driver, 100);
		wWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpathExpression)));
		Thread.sleep(2000);
	}
	
	public void terminate(){
		driver.close();
		driver.quit();
	}

	public int getTotWindowHandles() {
		return driver.getWindowHandles().size();
	}

	public WebDriver getDriver() {
		return driver;
	}

}
