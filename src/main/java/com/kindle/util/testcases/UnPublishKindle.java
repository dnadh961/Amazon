package com.kindle.util.testcases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.kindle.util.GenericKeywords;
import com.kindle.util.WaitHandler;

public class UnPublishKindle {
	
	public static void main(String[] args) {
		GenericKeywords app = null;
		try{
			app=new GenericKeywords();
			app.openBrowser();
			app.navigate("amazon_url");
			app.click("signin_button_id");
			app.input("email_field_id", "username");
			app.input("password_field_id", "password");
			app.click("signinwith_credentials_button_id");
			List<WebElement> list_bookstatus=app.getElements("kindlebook_status_xpath");
			List<WebElement> list_humbergers=app.getElements("humberger_icon_xpath");
			for(int n=1;n<=list_bookstatus.size();n++){
				WebElement x=list_bookstatus.get(n);
				System.out.println("iteration:  "+n);
				
				System.out.println(x.findElement(By.xpath("//div[@class='element-popover-text']/span")).getText());
				if(x.findElement(By.xpath("//div[@class='element-popover-text']/span")).getText().contains("LIVE")){
					WaitHandler.sleep(2);
					System.out.println("Location  "+n);
					Actions act=new Actions(app.getDriver());
					act.moveToElement(list_humbergers.get(n)).build().perform();
					WaitHandler.waitForElementClickable(app.getDriver(), By.xpath("//a[contains(text(),'Unpublish eBook')]"));
					app.getDriver().findElement(By.xpath("//a[contains(text(),'Unpublish eBook')]")).click();
					WaitHandler.sleep(3);	
					app.click("unpublish_button_xpath");;
				}
			}
		}finally {
			app.terminate();
		}
	}
	

}
