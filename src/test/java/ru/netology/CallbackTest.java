package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    void shouldSubmitFormSuccessfully() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смирнов Роман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        driver.findElement(By.cssSelector("[data-test-id='order-success'] span.icon_name_ok")); // Ищем иконку с галочкой
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Smirnov Roman",     // Латиница
            "12345",             // Цифры
            "Smirnov-Roman",     // Латиница с дефисом
            "Иван_Иванов",       // Русский с символами
            "Иван!Иванов",       // Русский с недопустимыми символами
            "Смирнов Roman"      // Кириллица с латиницей
    })
    void shouldShowValidationErrorForInvalidName(String invalidName) {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(invalidName);
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаны неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",     // Пустое поле
            " "     // Символ пробела
    })
    void shouldShowErrorForEmptyNameField(String invalidName) {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(invalidName);
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id='name'] .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "89270000000",      // Без знака +
            "070123456789",     // 12 символов (требуется 11 цифр и знак +)
            "+792700000000",    // Слишком длинный номер
            "+7927000000",      // Слишком короткий номер
            "*79270000000",     // Символ *
            "+7цифраЦифра",     // Кириллические символы
            "+7oneTwoFive"      // Латинские символы
    })
    void shouldShowValidationErrorForInvalidPhone(String invalidPhone) {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смирнов Роман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(invalidPhone);
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",     // Пустое поле
            " "     // Символ пробела
    })
    void shouldShowErrorForEmptyPhoneField(String invalidPhone) {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смирнов Роман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(invalidPhone);
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id='phone'] .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void shouldShowAgreementValidationError() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Смирнов Роман");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("button")).click();
        driver.findElement(By.cssSelector(".input_invalid[data-test-id='agreement']"));
    }

}
