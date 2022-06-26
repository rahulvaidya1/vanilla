/*
Name: Object Repository
Type: Public Class
Descrription: This class stores the locators for all objects required for processing.
Author: Rahul Vaidya
Created Date: 25/6/2022
Last Modified Date: 26/6/2022
*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

public class ObjectRepository {
	public static By GetObject(String[] locatordetails) {
		By locators;
		String locatorType = locatordetails[0];
		String value = locatordetails[1];
		locators = CommonLib.locatorValue(locatorType, value);
		return locators;
	}

	public WebElement GetObject(RemoteWebDriver driver, String strConfig, String header) {
		String[] arrObjectLocators = GetObjectlocators(header);
		By byGetObject = GetObject(arrObjectLocators);
		common.waitForLoad(driver, strConfig);
		WebElement objElement = driver.findElement(byGetObject);
		return objElement;
	}

	public static String[] GetObjectlocators(String header) {
		switch(header) {
		//Amazon Home Page
		case "LN_HomePage_Hamburger":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//a[@id='nav-hamburger-menu']";
			break;
		//Amazon Menu Navigation
		case "LN_MainMenu_TVAppliancesElectronics":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//a/div[contains(., 'TV, Appliances, Electronics')]";
			break;
		case "LN_SubMenu_Televisions":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//a[contains(.,'Televisions')]";
			break;
		//Filter Select Brand
		case "CHK_Filter_BrandSamsung":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//a[contains(@href,'Samsung')]//input[@type='checkbox']";
			break;
		//Sort By Product List
		case "CBO_ResultsPage_SortBy":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//select[contains(@id, 'sort')]";
			break;
		//2nd Element in the Search Result list; Modify div counter to access link of any search result objects
		case "LN_SearchResult_SecondItem":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//div[contains(@class, 'result-list')]/div[3]//h2/a";
			break;
		//About this item Label
		case "lbl_ProductPage_AboutThisItemHeader":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//div[contains(@id, 'feature-bullets')]/h1";
			break;	
		case "lst_ProductPage_AboutThisItemValue":
			locatordetails[0]: "xpath";
			locatordetails[1]: "//div[contains(@id, 'feature-bullets')]/ul/li";
			break;
		//If Element not defined
		default:
			Reporter.log("Object not defined for - " + header);
		}
		return locatordetails;
	}
}


