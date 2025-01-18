package ru.netology;

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

public class CallbackTest {

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
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestV1() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input[name='name']")).sendKeys("Смирнов Роман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input[name='phone']")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        // Вообще достаточно селектора по типу, так как кнопка на форме одна и каких-то специфических особенностей у неё нет
        driver.findElement(By.cssSelector(
                "form[action='/'][autocomplete='on'][method='post'] div.form-field button[role='button'][type='button']"
        )).click();
        driver.findElement(By.cssSelector("[data-test-id='order-success'] span.icon_name_ok")); // Ищем иконку с галочкой
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

}
