package rpa;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Adjustment_Objects {

	WebDriver driver;
//	private WebDriverWait wait10, wait20;
	private WebDriverWait wait;
	public Adjustment_Objects(WebDriver driver) {
		super();
		this.driver = driver;
		wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	
	
	@FindBy(id = "doctorID")
	public WebElement usernameField;
	
	@FindBy(id = "passwordField")
	public WebElement passwordField;
	
	@FindBy(id = "Login")
	public WebElement loginBtn;
	
	@FindBy(id = "nextStep")
	public WebElement nextBtn;
	
	@FindBy(id = "jellybean-panelLink4")
	public WebElement menuBtn;
	
	@FindBy(xpath = "//a[@class='cursor' and text()='Billing']")
	public WebElement billingBtn;
	
	@FindBy(xpath = "//span[text()='Claims']")
	public WebElement claimBtn;
	
	@FindBy(id = "claimStatusCodeId")
	public WebElement claimStatusDropDown;
	
	@FindBy(id = "claimLookupSel4")
	public WebElement claimSortDropDown;
	
	@FindBy(xpath = "//div[@class='col-sm-5 nopadding blocked pull-right']/input")
	public WebElement claimField;
	
	@FindBy(id = "btnclaimlookup")
	public WebElement lookupBtn;
	
	@FindBy(id = "claimLoadingImg")
	public WebElement loadingBtn;
	
	
	@FindBy(xpath = "//table[@id='billingClaimTbl15']/descendant::td[text()='Balance']/following-sibling::td/span")
	public WebElement balanceField;
	
	@FindBy(id = "billingClaimBtn35")
	public WebElement adjustmentBtn;
	
	@FindBy(id = "billingClaimBtn36")
	public WebElement cancelBtn;
	
	
	@FindBy(id = "claimAdjustmentsBtn9")
	public WebElement addBtn;
	
	@FindBy(id = "claimAdjustmentsBtn11")
	public WebElement threeDots;
	
	@FindBy(xpath = "//td[text()='No Authorization']")
	public WebElement noAuthorization;
	
	@FindBy(id = "FinancialAdjustmentCodeBtn5")
	public WebElement CodeOkBtn;
	
	@FindBy(id = "cptAmt")
	public WebElement amountField;
	

	@FindBy(id = "claimAdjustmentsBtn13")
	public WebElement AmountAddBtn;
	
	@FindBy(id = "claimAdjustmentsBtn14")
	public WebElement postCPTBtn;
	
	@FindBy(xpath = "//div/span[@ng-bind='ClaimData.InvId']")
	public WebElement ClaimVerif;
	
	@FindBy(id = "btnOKPostCPT")
	public WebElement OkBtn2;
	
	@FindBy(xpath = "//table[@id='billingClaimTbl15']/descendant::td[text()='Payments/Adj']/following-sibling::td/span")
	public WebElement adjustmentBalance;

	@FindBy(id = "claimAdjustmentsBtn16")
	public WebElement OkBtn3;
	
	@FindBy(xpath = "//button[contains(@id,'claimScreenOkBtn')]")
	public WebElement OkBtn4;
	
	@FindBy(xpath = "//button[@id='claimAdjustmentsBtn20' and text()='Yes']")
	public WebElement yesBtn;
	
	@FindBy(xpath = "//button[@id='claimAdjustmentsBtn23' and text()='Yes']")
	public WebElement yesBtn2;
	
	
	
	
	
	public WebElement getbalanceFromApp(String date, String cpt) {
		return driver.findElement(By.xpath("//tr/td[1][contains(text(),'"+date+"')]/following-sibling::td[2][contains(text(),'"+cpt+"')]/following-sibling::td[2]"));
	}
	
	public WebElement writeOffbalanceInApp(String date, String cpt) {
		return driver.findElement(By.xpath("//tr/td[1][contains(text(),'"+date+"')]/following-sibling::td[2][contains(text(),'"+cpt+"')]/following-sibling::td[3]/input"));
	}
	
	public void waitFunc(WebElement webEle) {
		wait.until(ExpectedConditions.elementToBeClickable(webEle));
	}
	
	public void waitFuncInvisibility(WebElement webEle) {
		wait.until(ExpectedConditions.invisibilityOf(webEle));
	}
	
}
