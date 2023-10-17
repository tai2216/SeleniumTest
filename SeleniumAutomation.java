package com.selenium;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aspectj.util.FileUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumAutomation {

	private static final String URL = "";
	private static final String CHROME_DRIVER_DESTINATION = "";
	private static final String IMG_DESTINATION = "";
	private static final String ACCOUNT = "";
	private static final String PASSWORD = "";
	public static void main(String[] args){
		System.setProperty("webdriver.chrome.driver",CHROME_DRIVER_DESTINATION);
		WebDriver driver =null;
		String findRptName= "xx0022";
		File testReportFolder = new File(IMG_DESTINATION);
		if(!testReportFolder.exists()) {
			if(testReportFolder.mkdir()) {
				System.out.println("資料夾已成功創建: "+testReportFolder.getAbsolutePath());
			}else {
				System.out.println("無法創建資料夾");
			}
		}else {
			System.out.println("資料夾已存在");
		}
		try {
			driver = (WebDriver)new ChromeDriver();
			driver.get(URL);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			WebElement username = waitUntilxPath(driver,10,"ssdsds");
			username.sendKeys(ACCOUNT);
			WebElement password = waitUntilxPath(driver,10,"ppasw");
			password.sendKeys(PASSWORD);
			WebElement loginButton = waitUntilxPath(driver,10,"lloggiibuon");
			loginButton.click();
			waitUntilxPath(driver,10,"side-menu").click();
			waitUntilxPath(driver,10,"alink").click();
			driver.switchTo().frame(0);
			WebElement rptCategory = waitUntilxPath(driver,10,"selectElement");
			System.out.println("找到rpt category");
			Select select = new Select(rptCategory);
			System.out.println("找到select element");
			List<WebElement> options = select.getOptions();
			for(WebElement option:options) {
				System.out.println("選項值: "+option.getText());
			}
			select.selectByValue("2");
			WebElement pageCount = waitUntilxPath(driver,10,"selectOption");
			Select pageCountSelect = new Select(pageCount);
			waitUntilxPath(driver,10,"searchbutton").click();
			loopTable(driver,findRptName);
			
			waitUntilxPath(driver,10,"logoutButton").click();
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			System.out.println("完成測試");
		}catch(Exception e) {
			System.out.println("失敗");
			e.printStackTrace();
		}finally {
			if(driver!=null)
				driver.quit();
		}
		
		
		
	}
	
	
	public static WebElement waitUntilxPath(WebDriver driver, int waitSeconds, String xPath) {
		WebDriverWait wait = new WebDriverWait(driver,waitSeconds);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
		return element;
	}
	
	public static WebElement waitUntilId(WebDriver driver, int waitSeconds, String id) {
		WebDriverWait wait = new WebDriverWait(driver,waitSeconds);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		return element;
	}
	
	public static AtomicBoolean findRptId(WebDriver driver,WebElement table, String findRptName){
		AtomicBoolean found = new AtomicBoolean(false);
		table.findElements(By.tagName("tr")).forEach(
			(element)->{
				System.out.println("為什麼只能在這裡print "+ element.getText());
				element.findElements(By.tagName("td")).forEach(
					(td)->{
						System.out.println("抓到的title "+td.getAttribute("title"));
						if(findRptName.equals(td.getAttribute("title"))) {
							found.set(true);
							td.click();
							TakesScreenshot shot = (TakesScreenshot)driver;
							File screenshotAs = shot.getScreenshotAs(OutputType.FILE);
							try {
								FileUtil.copyFile(screenshotAs, new File(IMG_DESTINATION+"\\測試截圖"+findRptName+"_"+new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())));
								System.out.println("已截圖");
								waitUntilxPath(driver, 10, "/html/body/div[2]").click();
								driver.switchTo().alert().accept();
								System.out.println("已點擊重製報表產生");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return;
						}
					}	
				);
				if(found.get()) return;	
			}	
		);
		return found;
	}
	
	public static void loopTable(WebDriver driver, String findRptName)throws IOException {
		AtomicBoolean found =new AtomicBoolean(false);
		int count = 0;
		WebElement table = waitUntilxPath(driver,10,"/html/body/div[2]");
		while(findRptId(driver,table,findRptName).get()!=true & count <100) {
			waitUntilxPath(driver,10,"sfxcv").click();
			System.out.println("點擊下一頁");
			table =waitUntilId(driver,10,"jqgrid_table");
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			count++;
		}
	}
	
//SeleniumAutomation的括號
}
