package com.rd;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class AddRecordTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/java/com/rd/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/webtables");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#addNewRecordButton")));
    }

    @Test
    public void testAddAndUpdateRecord() throws InterruptedException {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#addNewRecordButton")));
        addButton.click();

        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#firstName")));
        WebElement lastNameField = driver.findElement(By.cssSelector("#lastName"));
        WebElement emailField = driver.findElement(By.cssSelector("#userEmail"));
        WebElement ageField = driver.findElement(By.cssSelector("#age"));
        WebElement salaryField = driver.findElement(By.cssSelector("#salary"));
        WebElement departmentField = driver.findElement(By.cssSelector("#department"));
        WebElement submitButton = driver.findElement(By.cssSelector("#submit"));

        firstNameField.sendKeys("Sena");
        lastNameField.sendKeys("Efe");
        emailField.sendKeys("senaefe@gmail.com");
        ageField.sendKeys("28");
        salaryField.sendKeys("77000");
        departmentField.sendKeys("Software");
        submitButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#firstName")));

        // Reklam iframesini kaldırma
        removeIframe();

        List<WebElement> editButtons = driver.findElements(By.cssSelector("span[title='Edit']"));
        WebElement editButton = editButtons.get(editButtons.size() - 1);
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();

        WebElement firstNameEditField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#firstName")));
        firstNameEditField.clear();
        firstNameEditField.sendKeys("Aylin");
        submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#submit")));
        submitButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".rt-tbody .rt-tr-group")));

        boolean isRecordUpdated = false;
        List<WebElement> rows = driver.findElements(By.cssSelector(".rt-tbody .rt-tr-group"));
        for (WebElement row : rows) {
            WebElement firstNameCell = row.findElement(By.cssSelector("div[role='gridcell']:nth-of-type(1)"));
            WebElement lastNameCell = row.findElement(By.cssSelector("div[role='gridcell']:nth-of-type(2)"));
            WebElement salaryCell = row.findElement(By.cssSelector("div[role='gridcell']:nth-of-type(5)"));
            if (firstNameCell.getText().equals("Aylin") && lastNameCell.getText().equals("Efe") && salaryCell.getText().equals("77000")) {
                isRecordUpdated = true;
                break;
            }
        }

        assertTrue(isRecordUpdated, "Tablo güncellenmedi.");

        Thread.sleep(5000);
    }

    // Reklam iframe'ini Kaldıran JavaScript Kodu
    private void removeIframe() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelectorAll('iframe').forEach(iframe => iframe.remove());");
    }

    @AfterMethod
    public void cleanUp() {
        driver.quit();
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
