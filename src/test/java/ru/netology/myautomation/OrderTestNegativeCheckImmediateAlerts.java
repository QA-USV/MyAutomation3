package ru.netology.myautomation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTestNegativeCheckImmediateAlerts {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void quitDriver() {
        driver.quit();
        driver = null;
    }

    @Test
    void negative01NameFieldNotFilled() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(""); //The field is not filled in.
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).click(); //Positioning the cursor on the field
        String report = driver.findElement(By.xpath("(//*[contains(text(),'Поле')])[1]")).getText();
        assertEquals("Поле обязательно для заполнения", report.trim());
    }

    @Test
    void negative02PhoneFieldNotFilled() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит Джон");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(""); //The field is not filled in.
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        String report = driver.findElement(By.xpath("//*[contains(text(),'Поле')]")).getText();
        assertEquals("Поле обязательно для заполнения", report.trim());
    }

    @Test
    void negative03CheckboxNotClicked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит Джон");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[type='button']")).click();
        String report = driver.findElement(By.cssSelector("[role='presentation']")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", report.trim());
    }

    @Test
    void negative04NameFieldOneWordOnly() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит"); //One word in the field
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).click(); //Positioning the cursor on the field
        String report = driver.findElement(By.xpath("(//*[contains(text(),'Поле')])[1]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative05NameFieldHyphensOnly() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("--"); //Immediately when two hyphens are printed in a row
        String report = driver.findElement(By.xpath("(//*[contains(text(),'Поле')])[1]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative06NameFieldSpacesOnly() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("  "); //Immediately when two spaces are printed in a row
        String report = driver.findElement(By.xpath("(//*[contains(text(),'Поле')])[1]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative07NameFieldEnglishLang() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("S"); //Immediately when a nonRussian letter is printed
        String report = driver.findElement(By.xpath("//*[contains(text(),'Имя и Фамилия указаные неверно.')]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative08NameFieldNameAndFigures() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит 7"); //Immediately when a number is printed
        String report = driver.findElement(By.xpath("//*[contains(text(),'Имя и Фамилия указаные неверно.')]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative09NameFieldWithDoubleHyphen() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит--"); //Immediately when two spaces are printed in a row
        String report = driver.findElement(By.xpath("(//*[contains(text(),'Поле')])[1]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", report.trim());
    }

    @Test
    void negative10PhoneFieldNoPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит Джон");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("8"); //Should start fill in with "+".
        String report = driver.findElement(By.xpath("//*[contains(text(),'Телефон указан неверно.')]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", report.trim());
    }

    @Test
    void negative11PhoneFieldTenFigures() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смит Джон");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+798765432");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        String report = driver.findElement(By.xpath("//*[contains(text(),'Телефон указан неверно.')]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", report.trim());
    }
}