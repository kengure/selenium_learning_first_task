package ru.ibs.appline.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Task1Test {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        // Для Мас и Linux
        // System.setProperty("webdriver.chrome.driver",
        // "src/test/resources/webdriver/chromedriver");
        // Для Windows
        System.setProperty("webdriver.chrome.driver", "src/test/resources/webdriver/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certificate-errors");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 100);

        String baseUrl = "http://training.appline.ru/user/login";
        driver.get(baseUrl);
    }

    @Test
    public void firstTask() {

        // Ввести имя пользователя и пароль
        String fieldXPath = "//input[@name='%s']";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "_username"))), "Taraskina Valeriya");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "_password"))), "testing");

        // кликнуть по кнопке "Войти"
        String submitButtonXPath = "//button[contains(text(),'Войти')]";
        clickOnButton(submitButtonXPath);

        // Проверить, что страница загрузилась
        String pageTitlQuickRunXPath = "//h1[text() = 'Панель быстрого запуска']";
        waitUntilElementToBeVisible(By.xpath(pageTitlQuickRunXPath));

        // Кликнуть на пункт меню Расходы
        String expensesDropDownButtonXPath = "//ul[@class='nav nav-multilevel main-menu']/li[@class='dropdown'][a/span[text() = 'Расходы']]";
        clickOnButton(expensesDropDownButtonXPath);

        // Кликнуть на подпункт меню Командировки
        String tripsButtonXPath = "//li[@data-route='crm_business_trip_index']";
        clickOnButton(tripsButtonXPath);

        // Проверить, что загрузилась страница Все командировки
        String pageTitleAllBusinessTripsXPath = "//h1[normalize-space(text()) = 'Все Командировки']";
        waitUntilElementToBeVisible(By.xpath(pageTitleAllBusinessTripsXPath));

        // Нажать на Создать командировку
        String createBusinessTripButtonXPath = "//div[*[@title='Создать командировку']]";
        clickOnButton(createBusinessTripButtonXPath);

        // Проверить наличие на странице заголовка "Создать командировку"
        String pageTitleCreateBusinessTripXPath = "//h1[normalize-space(text()) = 'Создать командировку']";
        waitUntilElementToBeVisible(By.xpath(pageTitleCreateBusinessTripXPath));

        // Заполнить поле Подразделение
        String businessUnitXPath = "//select[contains(@id, 'crm_business_trip_businessUnit')]";
        WebElement selectBisunessUnit = driver.findElement(By.xpath(businessUnitXPath));
        Select select = new Select(selectBisunessUnit);
        select.selectByVisibleText("Отдел внутренней разработки");

        // нажать "Открыть список"
        String openListButtonXPath = "//*[text()='Открыть список']";
        clickOnButton(openListButtonXPath);

        // Заполнить поле Укажите организацию
        String companyDropDownXPath = "//div[@class='select2-container select2 input-widget']";
        clickOnButton(companyDropDownXPath);

        String openedCompanyListXPath = "//ul[@class='select2-results'][li]";
        waitUntilElementToBeVisible(By.xpath(openedCompanyListXPath));

        String elementFromOpenedCompanyListXPath = "//ul[@class='select2-results']/li[4]";
        clickOnButton(elementFromOpenedCompanyListXPath);
        waitUntilElementToBeNotVisible(By.xpath(openedCompanyListXPath));

        // Проверить, что поле Принимающая организация заполнено
        waitUntilElementToBeNotVisible(By.xpath(openedCompanyListXPath));
        String hostOrganizationXpath = "//input[@name='crm_business_trip[company]']";
        WebElement hostOrganization = driver.findElement(By.xpath(hostOrganizationXpath));
        String value = hostOrganization.getAttribute("value");

        if (value != null && !value.isEmpty()) {
            System.out.println("Input не пустой");
        } else {
            System.out.println("Input пустой");
        }

        // В задачах поставить чекбокс на "Заказ билетов"
        String orderTicketsCheckboxXPath = "//label[text() = 'Заказ билетов']/../input";
        WebElement orderTicketsCheckbox = driver.findElement(By.xpath(orderTicketsCheckboxXPath));
        waitUntilElementToBeClickable(orderTicketsCheckbox);
        orderTicketsCheckbox.click();
        Assert.assertTrue("Поле было заполнено некорректно", orderTicketsCheckbox.isSelected());

        // Указать города выбытия и прибытия
        String cityXPath = "//input[contains(@id, '%s')]";
        fillInputField(driver.findElement(By.xpath(String.format(cityXPath, "crm_business_trip_departureCity"))),
                "Россия, Москва");
        fillInputField(driver.findElement(By.xpath(String.format(cityXPath, "crm_business_trip_arrivalCity"))),
                "Россия, ИБС");

        fillInputField(
                driver.findElement(
                        By.xpath(String.format(cityXPath, "date_selector_crm_business_trip_departureDatePlan"))),
                "01.01.2024");
        closeCalendar();

        fillInputField(
                driver.findElement(
                        By.xpath(String.format(cityXPath,
                                "date_selector_crm_business_trip_returnDatePlan"))),
                "10.01.2024");
        closeCalendar();

        // Нажать "Сохранить и закрыть"
        String saveAndCloseButtonXPath = "//button[contains(text(),'Сохранить и закрыть')]";
        clickOnButton(saveAndCloseButtonXPath);

        // Проверить, что на странице появилось сообщение: "Список командируемых
        // сотрудников не может быть пустым"
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format("//span[@class='validation-failed']"))),
                "Список командируемых сотрудников не может быть пустым");
    }

    @After
    public void after() {
        driver.quit();
    }

    /**
     * Скрол до элемента на js коде
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Явное ожидание того что элемент станет кликабельный
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void waitUntilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видимым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который
     *                виден на странице
     */
    private void waitUntilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Явное ожидание того что элемент станет невидимым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который
     *                не виден на странице
     */
    private void waitUntilElementToBeNotVisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Заполнение полей определённым значений
     *
     * @param element - веб элемент (поле какое-то) которое планируем заполнить)
     * @param value   - значение которы мы заполняем веб элемент (поле какое-то)
     */
    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUntilElementToBeClickable(element);
        element.click();
        element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assert.assertTrue("Поле было заполнено некорректно", checkFlag);
    }

    /**
     * Проверка ошибка именно у конкретного поля
     *
     * @param element      веб элемент (поле какое-то) которое не заполнили
     * @param errorMessage - ожидаемая ошибка под полем которое мы не заполнили
     */
    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
                errorMessage, element.getText());
    }

    /**
     * Найти элемент по Xpath и кликнуть на него
     *
     * @param buttonXpath - xpath локатор элемента button
     */
    private void clickOnButton(String buttonXpath) {
        waitUntilElementToBeVisible(By.xpath(buttonXpath));
        WebElement button = driver.findElement(By.xpath(buttonXpath));
        waitUntilElementToBeClickable(button);
        button.click();
    }

    /**
     * Закрыть календарь
     */
    private void closeCalendar() {
        String calendarXPath = "//div[@id='ui-datepicker-div' and contains(@style, 'display: block;')]";
        waitUntilElementToBeVisible(By.xpath(calendarXPath));

        String bodyXPath = "//body[@class='desktop-version lang-ru']";
        WebElement body = driver.findElement(By.xpath(bodyXPath));
        waitUntilElementToBeClickable(body);
        body.click();

        waitUntilElementToBeNotVisible(By.xpath(calendarXPath));
    }

}