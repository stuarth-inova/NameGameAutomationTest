package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        Capabilities capabilities = DesiredCapabilities.chrome();
        //Capabilities capabilities = new ChromeOptions();
        driver = new ChromeDriver(capabilities);
        //driver.navigate().to("http://www.ericrochester.com/name-game/");
        driver.get("http://www.ericrochester.com/name-game/");
        WebElement waitForPicsToLoad = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("photo")));
    }

    @Test
    public void test_validate_title_is_present() {
        new HomePage(driver)
                .validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        new HomePage(driver)
                .validateClickingFirstPhotoIncreasesTriesCounter();
    }

    @Test
    public void test_streak_counter_incremented() {
        new HomePage(driver)
                .validateStreakIncrementedOnCorrectSelection();
    }

    @Test
    public void test_multiple_streak_and_streak_counter_reset() {
        new HomePage(driver)
                .validateMultipleStreakCounterOperation();
    }

    @Test
    public void test_counters_through_ten_random_selections() {
        new HomePage(driver)
                .validateAllCountersTenRandomSelections();
    }

    @Test
    public void test_load_of_new_target_and_photos_after_correct_selection() {
        new HomePage(driver)
                .verifyNewMatchNameAndPhotosAfterCorrectSelection();
    }

    @Test
    public void test_occurence_of_wrong_selections_increases() {
        new HomePage(driver)
                .verifyChangesInFrequencyCorrectAndIncorrectSelections();
    }

    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

}
