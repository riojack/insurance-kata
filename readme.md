# Insurance Kata

Requirements can be found [here](./requirements.md).

## Running the tests
Note: this project was coded using Gradle.  It is not required to be installed on your computer.

1. Ensure you have Java 21 installed.  Recommendation is to use [Adoptium's Temurin release](https://adoptium.net/temurin/releases?version=21&os=any&arch=any).
2. Run the tests and generate coverage information with this command: './gradlew clean test jacocoTestCoverage'.
3. Coverage (HTML format) can be viewed by opening this in your browser: `./app/build/reports/tests/test/index.html`.

## Improvements if there was more time...

- Starting with [Single Responsibility](https://www.baeldung.com/java-single-responsibility-principle#single-responsibility-principle) first rather than refactoring to it.  I tend to start new code bases very simply, with everything in one class because usually the code is small and manageable at first.  But starting with classes of responsibility already separated out is probably easier on refactoring.
- Avoid **over-refactoring**.  I sometimes obsess with refactoring to the nth degree.
- There are more **cases to test** that go outside the basic business requirements.
- Use a **different programming language**.  Java is a verbose language compared to something like Kotlin, Scala, or even Javascript/Typescript.
- Pull in [Lombok](https://projectlombok.org/) for more flexible POJO creation.
