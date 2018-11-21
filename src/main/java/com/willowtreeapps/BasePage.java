package com.willowtreeapps;

import org.assertj.swing.assertions.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 5/23/17.
 */
public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage validateAttribute(String css, String attr, String regex) {
        return validateAttribute(By.cssSelector(css), attr, regex);
    }

    public BasePage validateAttribute(By by, String attr, String regex) {
        return validateAttribute(driver.findElement(by), attr, regex);
    }

    public BasePage validateAttribute(WebElement element, String attr, String regex) {
        String actual = null;
        try {
            actual = element.getAttribute(attr);
            if (actual.equals(regex)) {
                return this; // test passes
            }
        } catch (Exception e) {
            Assertions.fail(String.format("Attribute not fount! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s] [Message: %s]",
                    attr,
                    regex,
                    actual,
                    element.toString(),
                    e.getMessage()), e);
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(actual);

        Assertions.assertThat(m.find())
                .withFailMessage("Attribute doesn't match! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s]",
                        attr,
                        regex,
                        actual,
                        element.toString())
                .isTrue();
        return this;
    }

    public BasePage validateText(String css, String text) {
        return validateText(By.cssSelector(css), text);
    }

    /**
     * Validate Text ignores white spaces
     */
    public BasePage validateText(By by, String text) {
        Assertions.assertThat(text).isEqualToIgnoringWhitespace(getText(by));
        return this;
    }

    public String getText(By by) {
        WebElement e = driver.findElement(by);
        return e.getTagName().equalsIgnoreCase("input")
                || e.getTagName().equalsIgnoreCase("select")
                || e.getTagName().equalsIgnoreCase("textarea")
                ? e.getAttribute("value")
                : e.getText();
    }

    public BasePage validatePresent(String css) {
        return validatePresent(By.cssSelector(css));
    }

    public BasePage validatePresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("Element not present: [Element: %s]", by.toString())
                .isGreaterThan(0);
        return this;
    }

    public void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Input arg: String either "correct" to match or "incorrect" (or any other string) to pick a non-matching photo
    //
    // Return: nothing (void)
    public void pickCorrectOrIncorrectPhoto(String whatToPick) {
        // Get correct name
        String nameToMatch = driver.findElement(By.id("name")).getText();
        //System.out.println( "Name to match:" + nameToMatch );

        //Grab all of the photos
        List<WebElement> photos = driver.findElements(By.className("photo"));

        //Look for the correct match and click it to increment the streak counter
        for(int i=0; i<photos.size(); i++) {
            WebElement photoName = photos.get(i).findElement(By.className("name"));
            // Debugging print
            //System.out.println( "In the loop, counter: " + i + " name associated w photo: " + photoName.getText() );

            if (photoName.getText().equals(nameToMatch)) {
                if (whatToPick.equals("correct")) {
                    photos.get(i).click();
                }
            } else {
                if (!whatToPick.equals("correct")) {
                    photos.get(i).click();
                    break;
                }
            }
        }
    }

    // No input args
    //
    // returns: int: index of clicked photo
    public int clickRandomPhotoFromArrayList(ArrayList<WebElement> photos) {
        // Pick random photo number X out of a ArrayList of arbitrarary length and click it
        Random rand = new Random();
        int photoNumSelection = rand.nextInt(photos.size());

        photos.get(photoNumSelection).click();
        return photoNumSelection;
    }

    // No input args
    //
    // returns: Integer array: the attempt stats counters; order: tries, correct, streak
    public int[] getStatsCounters() {
        int[] statsCounters;
        statsCounters = new int[3];
        statsCounters[0] = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        statsCounters[1] = Integer.parseInt(driver.findElement(By.className("correct")).getText());
        statsCounters[2] = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        return statsCounters;
    }

    // input arg: String: nameToMatch - name of colleague that player is trying to pick correct photo of
    //
    // No returns
    public void pickFourWrongBeforePickingCorrectLast(String nameToMatch) {
        ArrayList<Integer> selectionIndices = new ArrayList<Integer>();
        for(int j=0; j<5; j++) {
            selectionIndices.add(j);
        }

        ArrayList<WebElement> photos = new ArrayList<>(driver.findElements(By.className("photo")));

        // Remove the correct selection index from the list of selection indices
        for(int i=0; i<photos.size(); i++) {
            if (photos.get(i).findElement(By.className("name")).getText().equals(nameToMatch)) {
                selectionIndices.remove(i);
//                System.out.println("Trimmed selections array has length: " + selectionIndices.size());
                break;
            }
        }

        // Cycle through clicking incorrect photos
        for(int k=0; k<4; k++) {
            photos.get(selectionIndices.get(k)).click();
            sleep(2000);
        }
        // Now pick correct photo
        for(int i=0; i<photos.size(); i++) {
            if (photos.get(i).findElement(By.className("name")).getText().equals(nameToMatch)) {
//                System.out.println("About to click correct photo: " + i);
                photos.get(i).click();
                sleep(9000);
                break;
            }
        }
    }
}
