package rpa;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import objects.ExcelOperations;
import objects.SeleniumUtils;
import objects.Utility;
import utilities.ExcelReader;
import rpa.Adjustment_Objects;



public class ECWAdjustments {
	Logger logger = LogManager.getLogger(ECWAdjustments.class);

	String projDirPath, status, claimNo, patientName ,DOB ,serviceDate ,Balance, CPT, date;
	String[] cptArray;
	SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");
	// output format: yyyy-MM-dd
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	//System.out.println(formatter.format(parser.parse(data.get("DOB"))));
	public static ExcelReader excel; public static String sheetName = "Sheet1",cpt;
	int rowNum = 1;

	WebDriver driver;
	
	//JavascriptExecutor js;
	SeleniumUtils sel;
	Utility utility;
	
	ExcelOperations excelFile;
	Adjustment_Objects adjObj;
	static String excelFileName;

@BeforeTest
public void preRec() throws InterruptedException, SAXException, IOException, ParserConfigurationException {
	
	sel = new SeleniumUtils(projDirPath);

	driver = sel.getDriver();

	//js = (JavascriptExecutor) driver;
	adjObj= new Adjustment_Objects(driver);
	utility = new Utility();
	
	System.out.println(Integer.valueOf("13"));
	String[] params = new String[]{"url", "username", "password", "excelName"};
	HashMap<String, String> configs = utility.getConfig("config.xml", params);

	String url = configs.get("url"), 
			username = configs.get("username"), 
			password = configs.get("password");

	excelFileName = configs.get("excelName");
	System.out.println(excelFileName);

	driver.get(url);
	logger.info("Open url: " + url);

	adjObj.usernameField.sendKeys(username);
	logger.info("Enter username: " + username);
	sel.pauseClick(adjObj.nextBtn, 10);

	

	adjObj.nextBtn.click();
	logger.info("Click next button");

	sel.pauseClick(adjObj.loginBtn, 10);

	adjObj.passwordField.sendKeys(password);
	logger.info("Enter password");

	Thread.sleep(2000);

	adjObj.loginBtn.click();
	logger.info("Click login button");

		
	adjObj.waitFunc(adjObj.menuBtn);
	adjObj.menuBtn.click();
	logger.info("Clicked on Menu Button");
	
	adjObj.waitFunc(adjObj.billingBtn);
	adjObj.billingBtn.click();
	logger.info("Clicked on Billing Button");
	
	adjObj.waitFunc(adjObj.claimBtn);
	adjObj.claimBtn.click();
	logger.info("Clicked on Claim Button");
	
	adjObj.waitFunc(adjObj.claimStatusDropDown);
	Select claimStatus = new Select(adjObj.claimStatusDropDown);
	claimStatus.selectByValue("AllClaims");
	logger.info("Selected All Claims from the Dropdown");
	
	adjObj.waitFunc(adjObj.claimSortDropDown);
	Select claimSort = new Select(adjObj.claimSortDropDown);
	claimSort.selectByValue("Service Date");
	logger.info("Selected Service Date from the Dropdown");
	
	
}

@Test(dataProvider= "getData") 
public void AdjustmentCase(Hashtable<String,String> data) throws InterruptedException, ParseException {
	
	 status = data.get("Status");
	 claimNo = data.get("Claim No");
	 patientName = data.get("Patient Name");
	 DOB = data.get("Patient DOB");
	 serviceDate = data.get("Service Date");
	 Balance = data.get("Balance");
	 CPT = data.get("CPT Code");
	
	 cptArray= CPT.split(", ");
	 logger.info("Comma separated");
	 cptArray= CPT.split(" ; ");
	 logger.info(" ; separated");
	 
	 System.out.println("Balance is "+ Balance);
	 for(int i=0; i < cptArray.length; i++) {
			 cpt = cptArray[i];
			System.out.println(cptArray[i]);
			}
	
	rowNum++;
	
	if(status.isBlank() || status.isEmpty()) {
	

	
		adjObj.waitFunc(adjObj.claimField);
		Thread.sleep(2000);
		adjObj.claimField.clear();
		int claimNumber = 0;
		try {
			Double claimNum = Double.parseDouble(claimNo);
			 claimNumber = claimNum.intValue();
			 Integer.toString(claimNumber);
		}catch(Exception e) {
			
		}
		System.out.println(Integer.toString(claimNumber));
		adjObj.claimField.sendKeys(Integer.toString(claimNumber));
		logger.info("Entered Claim No. in Claims Field" +Integer.toString(claimNumber));
		
		adjObj.waitFunc(adjObj.lookupBtn);
		adjObj.lookupBtn.click();;
		logger.info("Clicked on LookUp Button");
		
		adjObj.waitFuncInvisibility(adjObj.loadingBtn);
		Thread.sleep(3000);		
		adjObj.waitFunc(adjObj.balanceField);
		Thread.sleep(3000);
		System.out.println(adjObj.balanceField.getText()+" is balance in app");
		double balance = Double.parseDouble(adjObj.balanceField.getText());
		logger.info("Balance is "+ balance);
		if(balance <= 0) {
			excel.setCellData(sheetName, "Status", rowNum, "Balance less than 0");
			adjObj.cancelBtn.click();
			//Assert.fail("Balance less than 0");
			throw new SkipException("Skipping this exception");
		}
		else {
			logger.info("Balance greater than 0");
			adjObj.waitFunc(adjObj.adjustmentBtn);
			adjObj.adjustmentBtn.click();
			logger.info("Clicked on Adjustment Button");
			
			adjObj.waitFunc(adjObj.addBtn);
			adjObj.addBtn.click();
			logger.info("Clicked on Add Button");
			
			adjObj.waitFunc(adjObj.threeDots);
			adjObj.threeDots.click();
			logger.info("Clicked on Three Dots Button");
			
			adjObj.waitFunc(adjObj.noAuthorization);
			adjObj.noAuthorization.click();
			logger.info("Clicked on No Authorization Button");
			
			adjObj.waitFunc(adjObj.CodeOkBtn);
			adjObj.CodeOkBtn.click();
			logger.info("Clicked on OK Button");
			
			adjObj.waitFunc(adjObj.amountField);
			adjObj.amountField.sendKeys(Balance);
			logger.info("Balance Entered");
			
			adjObj.waitFunc(adjObj.AmountAddBtn);
			adjObj.AmountAddBtn.click();
			logger.info("Clicked on Click Button");
			
			adjObj.waitFunc(adjObj.postCPTBtn);
			Thread.sleep(1000);
			adjObj.postCPTBtn.click();
			logger.info("Clicked on post CPT Btn Button");
			
			
			
			adjObj.waitFunc(adjObj.ClaimVerif);
			System.out.println(formatter.format(parser.parse(serviceDate)) +" is date");
		 date=	formatter.format(parser.parse(serviceDate));
			for(int j=0; j < cptArray.length; j++) {
				  cpt = cptArray[j].trim().replace(".0", "");
				System.out.println(cpt);
				
				String writtenBalance = adjObj.getbalanceFromApp(date, cpt).getText();
				System.out.println("Balance from App is: "+ writtenBalance);
				Thread.sleep(3000);
				adjObj.writeOffbalanceInApp(date, cpt).clear();
				adjObj.writeOffbalanceInApp(date, cpt).sendKeys(writtenBalance);
				System.out.println("Balance written in App: "+ writtenBalance);
				
			}
			adjObj.OkBtn2.click();
			System.out.println("OK Clicked");
			
			try {
				adjObj.waitFunc(adjObj.yesBtn);
				Thread.sleep(1000);
				adjObj.yesBtn.click();
				logger.info("Yes button clicked");
			}catch(Exception e) {
				
			}
			Thread.sleep(2000);
			adjObj.OkBtn3.click();
			System.out.println("Another OK Clicked");
			try {
				adjObj.waitFunc(adjObj.yesBtn2);
				Thread.sleep(1000);
				adjObj.yesBtn2.click();
				logger.info("Another Yes button clicked");
			}catch(Exception e) {
			}
			
			Thread.sleep(2000);
			System.out.println(adjObj.adjustmentBalance.getText() + "----"+ Balance);
			if(adjObj.adjustmentBalance.getText().equals(Balance)) {
				excel.setCellData(sheetName, "Status", rowNum, "Pass");
				adjObj.OkBtn4.click();
				System.out.println("Another OK4 Clicked");
			}
			else {
				excel.setCellData(sheetName, "Status", rowNum, "Adjustment and Balance mismatch");
				adjObj.OkBtn4.click();
				System.out.println("Another OK4 Clicked");
				throw new SkipException("Skipping this exception");
				//Assert.fail("Balance and Adjustment do not match");
				
			}
			
		}
		
	}
}

@AfterMethod()
public void afterMethod(ITestResult result) throws IOException {

	if(!result.isSuccess()) {
		// Test Failed
		String error = result.getThrowable().getLocalizedMessage();
		logger.info(error);
		//result.getThrowable().printStackTrace();
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File ss = ts.getScreenshotAs(OutputType.FILE);
			String ssPath = "./Screenshots/" + result.getName() + " - " + rowNum + ".png";
			FileUtils.copyFile(ss, new File(ssPath));
		} catch (Exception e) {
			System.out.println("Error taking screenshot");
		}

	}
	else {
		logger.info("Test completed successfully");
	}}
@DataProvider
public static Object[][] getData(){
	
	
	if(excel == null){
		
		
		excel = new ExcelReader(System.getProperty("user.dir")+"\\"+excelFileName);
		
		
	}
	
	
	int rows = excel.getRowCount(sheetName);
	int cols = excel.getColumnCount(sheetName);
	
	Object[][] data = new Object[rows-1][1];
	
	Hashtable<String,String> table = null;
	
	for(int rowNum=2; rowNum<=rows; rowNum++){
		
		table = new Hashtable<String,String>();
		
		for(int colNum=0; colNum<cols; colNum++){
			
	//	data[rowNum-2][colNum]=	excel.getCellData(sheetName, colNum, rowNum);
	
		table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));	
		data[rowNum-2][0]=table;	
			
		}
}
	
	return data;
	
}}
