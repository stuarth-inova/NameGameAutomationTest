package com.willowtreeapps;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void validateTitleIsPresent() {
        // No longer need to resolve this element b/c of use of BasePage text comparison
        //WebElement title = driver.findElement(By.cssSelector("h1"));
        //Assert.assertTrue(title != null);

        // More powerful test assertion; perhaps more fragile as well
        //Assert.assertTrue( title.getText().equals("name game" ));

        // Using the function from BasePage to support the more powerful assertion
        validateText(By.cssSelector("h1"), "name game");
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        //Wait for page to load - replaced w/ wait.until on initial load in WebTest
        //sleep(30000);

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        sleep(6000);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }


    public void validateStreakIncrementedOnCorrectSelection() {
        // ToDo: replace sleeps with appropriate wait.until conditions - but determining them turns out to take time
        int originalStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        pickCorrectOrIncorrectPhoto("correct");
        sleep(8000);

        int newStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        Assert.assertTrue(newStreakCount > originalStreakCount);

    }

    public void validateMultipleStreakCounterOperation() {
        // ToDo: replace sleeps with appropriate wait.until conditions - but determining them turns out to take time
        int preActionStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        pickCorrectOrIncorrectPhoto("correct");
        sleep(8000);

        int postActionStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        try {
            Assert.assertTrue(postActionStreakCount > preActionStreakCount);
            preActionStreakCount = postActionStreakCount;
        } catch(AssertionError e) {
            System.out.println("First streak counter increment test FAILED");
            throw e;
        }

        // Increment counter again 2 more times, just to be sure
        pickCorrectOrIncorrectPhoto("correct");
        sleep(8000);

        postActionStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        try {
            Assert.assertTrue(postActionStreakCount > preActionStreakCount);
            preActionStreakCount = postActionStreakCount;
        } catch(AssertionError e) {
            System.out.println("Second streak counter increment test FAILED");
            throw e;
        }

        pickCorrectOrIncorrectPhoto("correct");
        sleep(8000);

        postActionStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        try {
            Assert.assertTrue(postActionStreakCount > preActionStreakCount);
            preActionStreakCount = postActionStreakCount;
        } catch(AssertionError e) {
            System.out.println("Third counter increment test FAILED");
            throw e;
        }

        pickCorrectOrIncorrectPhoto("incorrect");
        sleep(8000);

        // Check counter reset on incorrect selection
        postActionStreakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());

        try {
            Assert.assertEquals(postActionStreakCount, 0);
        } catch(AssertionError e) {
            System.out.println("Streak counter reset test FAILED");
            throw e;
        }

    }

    public void validateAllCountersTenRandomSelections() {
        // Get initial counter values - verify they are all zero!
        // Order as it appears across the screen: tries, correct, streak
        int clicked_index = 9;
        int[] pre_action_counters;
        pre_action_counters = new int[3];
        int[] expected_action_counters;
        expected_action_counters = new int[3];

        // Get initial list of photos
        ArrayList<WebElement> photos = new ArrayList<>(driver.findElements(By.className("photo")));

        // Loop through 10 iterations of photo selection
        for(int iteration=1; iteration<=10; iteration++) {
            String nameToMatch = driver.findElement(By.id("name")).getText();

//            System.out.println("Iteration " + iteration + "'s target name: " + nameToMatch);

            pre_action_counters = getStatsCounters();
            clicked_index = clickRandomPhotoFromArrayList(photos);

//            System.out.println("Clicked name: " + photos.get(clicked_index).findElement(By.className("name")).getText());

            // On a correct selection, all 3 counters increment 1
            // On an incorrect selection: 1st counter increments 1; 2nd does not change; 3rd is reset to zero
            if (photos.get(clicked_index).findElement(By.className("name")).getText().equals(nameToMatch)) {
                for (int i = 0; i < pre_action_counters.length; i++) {
                    expected_action_counters[i] = pre_action_counters[i] + 1;
                }
//                System.out.println("In the 'correct' selection branch: " + nameToMatch);
                sleep(15000);

                // Try something other than sleep - but can't seem to get this to work.
                //
                // Even with this I *still* sometimes get int parsing errors on the "correct" counter after a correct match!
                // Instead of the updated counter get a string with the previous correct photo position (1 through 5)
                // and the target/matched name. Waiting on "correct" works better than any other condition...
                //
                // Leaving this with obnoxious sleeps and the retry on verification b/c that passes 1000 iterations no prob

//                Boolean waitForUpdatedStreakCounter = (new WebDriverWait(driver, 30))
//                        .until(ExpectedConditions.textToBePresentInElementLocated(By.className("correct"),
//                                Integer.toString(expected_action_counters[1])));
//
//                System.out.println("'correct' " + nameToMatch + " waitForUpdated bool is: " + waitForUpdatedStreakCounter);

                photos = new ArrayList<>(driver.findElements(By.className("photo")));
            } else {
                expected_action_counters[0] = pre_action_counters[0] + 1;
                expected_action_counters[2] = 0;
                // Trim seleced element to prevent reselecting a previously selected element
                photos.remove(clicked_index);

//                System.out.println("In the 'incorrect' selection branch: " + nameToMatch);
                sleep(5000);

                // This condition is not foolproof, so I left the retry below in
//                Boolean waitForUpdatedStreakCounter = (new WebDriverWait(driver, 30))
//                        .until(ExpectedConditions.textToBePresentInElementLocated(By.className("attempts"),
//                                Integer.toString(expected_action_counters[0])));

//                System.out.println("Passed 'incorrect' waitForUpdated... - bool is: " + waitForUpdatedStreakCounter);
            }
            pre_action_counters = getStatsCounters();

            // Even with the wait.until conditions I've applied, sometimes the "incorrect" case still has old counters
            // so left this retry in, which seems to handle the issue - tested to 1000's of iterations
            try {
                Assert.assertArrayEquals(expected_action_counters, pre_action_counters);
            } catch(AssertionError e) {
                System.out.println("First check failed; wait 7 seconds for counters to load and re-test...");
//                System.out.println("Failed a check... manually compare indexes, wait 5 seconds, and try again - expected" +
//                        " and non-matching sets:");
//                System.out.println("Expected tries: " + expected_action_counters[0] + "; right: " + expected_action_counters[1] +
//                        "; streak: " + expected_action_counters[2]);
//                System.out.println("Actual tries: " + pre_action_counters[0] + "; right: " + pre_action_counters[1] +
//                        "; streak: " + pre_action_counters[2]);
                sleep(7000);
                pre_action_counters = getStatsCounters();
                Assert.assertArrayEquals(expected_action_counters, pre_action_counters);
                System.out.println("Passed 2nd check after 7 sec additional wait");
            }
        }
    }

    public void verifyNewMatchNameAndPhotosAfterCorrectSelection() {
        // Get initial list of photos and associated names, and name to match
        List<WebElement> originalPhotos = driver.findElements(By.className("photo"));
        String originalMatchName = driver.findElement(By.id("name")).getText();

        // Make correct selection
        pickCorrectOrIncorrectPhoto("correct");
        sleep(9000);

        List<WebElement> newPhotos = driver.findElements(By.className("photo"));
        String newMatchName = driver.findElement(By.id("name")).getText();

        Assert.assertNotEquals(originalMatchName, newMatchName);
        Assert.assertNotSame(originalPhotos, newPhotos);
    }

    public void verifyChangesInFrequencyCorrectAndIncorrectSelections() {
        /* Will pick 4 pre-selected names incorrectly each time: when one of these comes up, will pick 4 wrong before
         picking the right one. For any other name, will pick correctly on the first try. Count the number each name
         comes up as the target for all names.   */
        List<String> incorrectlyChosenNames = new ArrayList<>();
        incorrectlyChosenNames.add("Ryan Grigsby");
        incorrectlyChosenNames.add("Jospeh Cherry");
        incorrectlyChosenNames.add("Anne Fry");
        incorrectlyChosenNames.add("Alex Ramey");
        incorrectlyChosenNames.add("Ben Frye");
        incorrectlyChosenNames.add("Will Ellis");
        incorrectlyChosenNames.add("Will Mayo");
        incorrectlyChosenNames.add("Chris Stroud");
        incorrectlyChosenNames.add("Tucker Legard");
        incorrectlyChosenNames.add("William Zantzinger");

        Hashtable<String, Integer> occurenceCounter = new Hashtable<String, Integer>();

        for(int mainCounter=0; mainCounter<50; mainCounter++) {
            String nameToMatch = driver.findElement(By.id("name")).getText();

            // This counts the number of occurrences of each name to match; but if a wrong selection is given doesn't count nameToMatch twice
            if (occurenceCounter.containsKey(nameToMatch)) {
                int oldCount = occurenceCounter.get(nameToMatch);
                int newCount = oldCount + 1;
                occurenceCounter.replace(nameToMatch, newCount);
            } else {
                occurenceCounter.put(nameToMatch, 1);
            }

            // This selects the right photo if nameToMatch isn't one of the 4 incorrect names
            if (incorrectlyChosenNames.contains(nameToMatch)) {
                // Do complex selection of 4 wrong choices before choosing correctly
                pickFourWrongBeforePickingCorrectLast(nameToMatch);
            } else {
                pickCorrectOrIncorrectPhoto("correct");
                sleep(9000);
            }

        }
        // Print out totals per user; also accumulate avg number of appearances for correct and incorrect users
        int numCorrectColleagues = 0;
        int runningTotalCorrect = 0;
        int numIncorrectColleagues = 0;
        int runningTotalIncorrect = 0;
        double avgCorrectAppearances = 0.0;
        double avgIncorrectAppearances = 0.0;
        Enumeration targetNames = occurenceCounter.keys();
        while( targetNames.hasMoreElements() ) {
            Object targetName = targetNames.nextElement();
            Integer occurrences = occurenceCounter.get(targetName);
            System.out.println("Name: " + targetName + " was the target " + occurrences + " times");
            if (incorrectlyChosenNames.contains(targetName)) {
                numIncorrectColleagues++;
                runningTotalIncorrect = runningTotalIncorrect + occurrences;
            } else {
                numCorrectColleagues++;
                runningTotalCorrect = runningTotalCorrect + occurrences;
            }
        }
        try {
            avgCorrectAppearances = (double) runningTotalCorrect / numCorrectColleagues;
        } catch (ArithmeticException e) {
            avgCorrectAppearances = 0.0;
        }
        try {
            avgIncorrectAppearances = (double) runningTotalIncorrect / numIncorrectColleagues;
        } catch (ArithmeticException e) {
            System.out.println("In div0 exception block");
            avgIncorrectAppearances = 0.0;
        }

        System.out.println("Number of incorrect selection colleagues is " + numIncorrectColleagues +
                " with an average number of appearances as the target of " + avgIncorrectAppearances);
        System.out.println("Number of correct selection colleagues is " + numCorrectColleagues +
                " with an average number of appearances as the target of " + avgCorrectAppearances);
        Assert.assertTrue(avgIncorrectAppearances > avgCorrectAppearances);
    }
}
