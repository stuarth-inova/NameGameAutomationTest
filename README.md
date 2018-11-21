# Name Game Test Automation

### Tools
- [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  - Setting up `JAVA_HOME` [tutorial link](http://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux)
- Intellij Community Edition
  - [macOS](https://www.jetbrains.com/idea/download/#section=mac)
  - [Windows](https://www.jetbrains.com/idea/download/#section=windows)
- [Google Chrome](https://www.google.com/chrome/browser/desktop/index.html)
  - [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/) - might need to download the latest drivers if the ones provided on the project get out of date.

### Importing
- Clone/Download this project
- On Linux, running Java jdk 1.8.0_191, I upgraded Chromedriver to latest version; I have included it in my repo
- Intellij
  - File > Open
  - Navigate to this project
  - Double click on the `build.gradle`
  - Click on `Open as project`
  - Click on `Delete existing project and import` (if prompted)
  
### Running Tests
- Intellij
  - `test > java > com.willowtreeapps > WebTest`
  - Right click on any method tagged with `@Test`
  - Click on `Run 'test_...()`
