package libraries;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
//import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.log4testng.Logger;


public class CommonLib {


	public static int intSmallDelay = 2
	public static int intMediumDelay = 5
	public static int intLargeDelay = 10
	public static int intXLDelay = 20

	/*
	 Method Name: locatorValue
	 Description: Provides Element identifier option using Locator identifier and locator value
	 Input: locatorType (eg - id, name, xpath) ; value = value of locator (eg - "//a[@id='FirstName']")
	 Output: By which can be consumed by Find Element/Find Elements method 
	 Developed By: Rahul Vaidya
	 */
	static By locatorValue(String locatorType, String value) {
		By by;
		switch (locatorType) {
		case "id":
			by = By.id(value);
			break;
		case "name":
			by = By.name(value);
			break;
		case "xpath":
			by = By.xpath(value);
			break;
		case "css":
			by = By.cssSelector(value);
			break;
		case "linkText":
			by = By.linkText(value);
			break;
		case "partialLinkText":
			by = By.partialLinkText(value);
			break;
		default:
			by = null;
			break;
		}
		return by;
	}

	/**
	 * @return Created Date: Dec 27, 2017 Method Name: getLogger Created By: Rahul
	 */
	public static Logger getLogger(String strConfig) {
		Logger logger = null; 
		switch (strConfig) {
		case "Config1":
			logger = Clipboard.logger;
			break;
		case "Config2":
			logger = Test2.logger;
			break;
		case "Config3":
			logger = Test3.logger;
			break;
		case "Config4":
			logger = Test4.logger;
			break;
		case "Config5":
			logger = Test5.logger;
			break;
		default:
			logger = Test1.logger;
			break;
		}
		return logger;
	}

	/*
	 * Method Name: Description Input: Output: Developed By:
	 */
	public void waitForLoad(RemoteWebDriver driver, String strConfig) {
		getLogger(strConfig).info("Inside waitForLoad");
		long istartTime = System.currentTimeMillis();
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 90);
		try {
			wait.until(pageLoadCondition);
			long intEndTime = System.currentTimeMillis();
			long totalTime = intEndTime - istartTime;
			getLogger(strConfig).info("Waiting for page load " + (totalTime / 1000) + " Seconds");
		} catch (JavascriptException e) {
			TestReporter.Info(driver, strConfig, "JavaScript Exception",
					"Received JavaScript Exception on page" + driver.getTitle());
			getLogger(strConfig).info(e.getMessage());
		} catch (StaleElementReferenceException e) {
			TestReporter.Info(driver, strConfig, "StaleElementReference Exception",
					"Received StaleElementReference Exception on page" + driver.getTitle());
			getLogger(strConfig).info(e.getMessage());
		}
	}

	/*
	 * Method Name: Description Input: Output: Developed By: Rahul
	 */
	public Boolean ElementExists(RemoteWebDriver driver, String strConfig, String strObjName) {
		boolean present;
		String[] arrObjDetails = ObjectRepository.GetObjectlocators(strObjName);
		By byObjDetails = ObjectRepository.GetObject(arrObjDetails);
		try {
			waitForLoad(driver, strConfig);
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			driver.findElement(byObjDetails);
			present = true;
		} catch (NoSuchElementException e) {
			present = false;
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return present;
	}

	/*
	 * Method Name: WaitForElement Description: Waits for some time until object is
	 * visible on screen Input: Header, or XPath Output: void Developed By: Rahul
	 */
	public static void WaitForElement(RemoteWebDriver driver, String strConfig, String Header) {
		String[] arrObjectDetails = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			if (Header.contains("//")) {
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Header)));
			} else {
				arrObjectDetails = ObjectRepository.GetObjectlocators(Header);
				CommonLib.getLogger(strConfig).info("Waiting for element: " + Header);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(arrObjectDetails[1])));
			}
		} catch (NoSuchElementException e) {
			TestReporter.Error(driver, strConfig, "Exception Thrown",
					"NoSuchElementException on- WaitForElement: " + Header);
		} catch (StaleElementReferenceException e) {
			TestReporter.Error(driver, strConfig, "Exception Thrown",
					"StaleElementReferenceException on- WaitForElement: " + Header);
		} catch (JavascriptException e) {
			TestReporter.Error(driver, strConfig, "Exception Thrown",
					"JavascriptException on- WaitForElement: " + Header);
		} catch (NullPointerException e) {
			TestReporter.Error(driver, strConfig, "Exception Thrown",
					"NullPointerException on- WaitForElement: " + Header);
		} catch (IllegalArgumentException e) {
			TestReporter.Error(driver, strConfig, "Exception Thrown",
					"IllegalArgumentException on- WaitForElement: " + Header);
		}
	}

	/*
	 * Method Name: Description Input: Output: Developed By:
	 */
	public void enter_URL(RemoteWebDriver driver, String strConfig, String URL) {
		// Requirements
		Logger logger;
		switch (strConfig) {
		case "Config1":
			logger = Test1.logger;
			break;
		case "Config2":
			logger = Test2.logger;
			break;
		case "Config3":
			logger = Test3.logger;
			break;
		case "Config4":
			logger = Test4.logger;
			break;
		case "Config5":
			logger = Test5.logger;
			break;
		default:
			logger = Test1.logger;
			break;
		}
		// Execution
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		try {
			logger.info("Inside enter_URL");
//			driver.navigate().to(URL);
			driver.get(URL);
		} catch (NoSuchSessionException e) {
			TestReporter.Error(driver, strConfig, "Driver Error",
					"Session ID is null. Using WebDriver after calling quit()?");
			logger.error(e.getMessage());
		}
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}

	public static RemoteWebDriver getRemoteDriver(String Browser) throws MalformedURLException {
		DesiredCapabilities capabilities = getCapabilities(Browser);
		RemoteWebDriver rdriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		rdriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		rdriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		rdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		rdriver.manage().window().maximize();
		WebElement html = rdriver.findElement(By.tagName("html"));
		html.sendKeys(Keys.chord(Keys.CONTROL, "0"));
		return rdriver;
	}

	/**
	 * @param browser
	 * @return Created Date: Dec 27, 2017 Method Name: getCapabilities Created By:
	 *         Rahul
	 */
	private static DesiredCapabilities getCapabilities(String browser) {
		switch (browser) {
		case "IE":
			DesiredCapabilities IEcapabilities = DesiredCapabilities.htmlUnit();
			IEcapabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			IEcapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			IEcapabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
			IEcapabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, Constants.strUTOMURL);
			IEcapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
					false);
			System.setProperty("webdriver.ie.driver",
					ProjectConstants.EXTERNALLIBRARIES + "/IEDriverServer.exe");
			return IEcapabilities;
		case "Chrome":
			DesiredCapabilities CRCapabilities = DesiredCapabilities.htmlUnit();
			System.setProperty("webdriver.chrome.driver",
					ProjectConstants.EXTERNALLIBRARIES + "/chromedriver.exe");
			return CRCapabilities;
		case "Firefox":
			System.setProperty("webdriver.gecko.driver", ProjectConstants.EXTERNALLIBRARIES + "//geckodriver.exe");
			DesiredCapabilities FFCapabilities = DesiredCapabilities.htmlUnit();
			return FFCapabilities;
		default:
			return null;
		}
	}

	/*
	 * Method Name: Description Input: Output: Developed By:
	 */
	public static String GetCurrentDate() {
		String CurrDate;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		CurrDate = dateFormat.format(date);
		return CurrDate;
	}

	/*
	 * Method Name: Description Input: Output: Developed By:
	 */
	public static String GetCurrentTime() {
		String CurrTime;
		DateFormat dateFormat = new SimpleDateFormat("h:mm:ss a");
		Date date = new Date();
		CurrTime = dateFormat.format(date);
		return CurrTime;
	}

	public void PerformOperation(RemoteWebDriver driver, String strConfig, String Header, String Value) {
		By by = null;
		// CommonLib.getLogger().info(Arr_ObjectDetails[0] + " " +
		// Arr_ObjectDetails[1]);
		String[] SplitHeader = Header.split("_");
		String ObjectType = SplitHeader[0];

		String[] Arr_ObjectDetails;
		// CommonLib.getLogger().info("Object Type is: " + ObjectType);
		switch (ObjectType.trim()) {
		case "LN":
			new Actions(driver).moveToElement(repository.GetObject(driver, strConfig, Header));
			boolean blnReturnVal = common.JSClick(driver, strConfig, Header);
			if (blnReturnVal) {
				CommonLib.getLogger(strConfig).info(Header + " Clicked Successfully");
			}
			waitForLoad(driver, strConfig);
			break;
		case "BTN":
			new Actions(driver).moveToElement(repository.GetObject(driver, strConfig, Header));
			boolean blnReturnVal1 = common.JSClick(driver, strConfig, Header);
			if (blnReturnVal1) {
				CommonLib.getLogger(strConfig).info(Header + " Clicked Successfully");
			}
			waitForLoad(driver, strConfig);
			break;
		case "TXT":
			Arr_ObjectDetails = ObjectRepository.GetObjectlocators(Header);
			by = ObjectRepository.GetObject(Arr_ObjectDetails);
			waitForLoad(driver, strConfig);
			WebElement objTextBox = driver.findElement(by);
			new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(objTextBox));
			if (Value.equalsIgnoreCase("PressEnter")) {
				objTextBox.click();
				objTextBox.sendKeys(Keys.RETURN);
			} else {
				objTextBox.click();
				objTextBox.sendKeys(Value);
			}
			break;
		case "CBO":
			common.select_comboboxOption(driver, strConfig, Header, Value);
			break;
		case "CHK":
			Arr_ObjectDetails = ObjectRepository.GetObjectlocators(Header);
			by = ObjectRepository.GetObject(Arr_ObjectDetails);
			common.waitForLoad(driver, strConfig);
			WebElement objCheckBox = driver.findElement(by);
			new Actions(driver).moveToElement(objCheckBox).perform();
			Boolean strStatus = objCheckBox.isSelected();
			if (strStatus == true && Value.equalsIgnoreCase("uncheck")) {
				common.JSClick(driver, by);
			} else if (strStatus == false && Value.equalsIgnoreCase("check")) {
				common.JSClick(driver, by);
			} else if (strStatus == true && Value.equalsIgnoreCase("check")) {
				// Do nothing
			} else if (Value.equalsIgnoreCase("click")) {
				common.JSClick(driver, by);
			}
			waitForLoad(driver, strConfig);
			break;
		case "RDO":
			Header = Header + "_" + Value;
			Arr_ObjectDetails = ObjectRepository.GetObjectlocators(Header);
			by = ObjectRepository.GetObject(Arr_ObjectDetails);
			waitForLoad(driver, strConfig);
			JSClick(driver, by);
			break;
		}
	}
}