/*
 * © Copyright [2021] Micro Focus or one of its affiliates.
 * Licensed under Apache License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.microfocus.bdd.it;

import com.microfocus.bdd.Bdd2Octane;
import com.microfocus.bdd.FilesLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Bdd2OctaneHandlerITCase extends Bdd2OctaneITCase {
    private List<String> reportFiles, featureFiles;

    @Parameterized.Parameters(name = "test{index} - framework - {0}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //single feature file
                {Framework.CUCUMBER_JS, "src/test/resources/cucumber-js/cucumber-junit-test-with-examples-has-same-name.xml", "src/test/resources/features/robustgherkin03.feature", "singleResults/cucumber-js-final.xml", "**/singleResults/standard-single-cucumber-js-final.xml"},
                {Framework.CUCUMBER_JVM, "src/test/resources/cucumber-jvm/junit-passed.xml", "src/test/resources/features/robustgherkin.feature", "singleResults/cucumber-jvm-final.xml", "**/singleResults/standard-single-cucumber-jvm-final.xml"},
                {Framework.CUCUMBER_RUBY, "src/test/resources/cucumber-ruby/passed_skipped_failed.xml", "src/test/resources/features/robustgherkin.feature", "singleResults/cucumber-ruby-final.xml", "**/singleResults/standard-single-cucumber-ruby-final.xml"},
                {Framework.PHP_BEHAT, "src/test/resources/php-behat/fail-assert-1.xml", "src/test/resources/features/robustgherkin.feature", "singleResults/php-behat-final.xml", "**/singleResults/standard-single-php-behat-final.xml"},
                {Framework.PYTHON_BEHAVE, "src/test/resources/python-behave/TESTS-python-behave-pass-skip-fail.xml", "src/test/resources/features/dealer.feature", "singleResults/python-behave-final.xml", "**/singleResults/standard-single-python-behave-final.xml"},
                {Framework.PYTHON_RADISH, "src/test/resources/python-radish/python-radish-skipped-passed-failed.xml", "src/test/resources/features/robustgherkin03.feature", "singleResults/python-radish-final.xml", "**/singleResults/standard-single-python-radish-final.xml"},
                //multiple feature files - different file path
                {Framework.CUCUMBER_JS, "src/test/resources/cucumber-js/cucumber*.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/cucumber-js-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-cucumber-js-final.xml"},
                {Framework.CUCUMBER_JVM, "src/test/resources/cucumber-jvm/**/*passed.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/cucumber-jvm-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-cucumber-jvm-final.xml"},
                {Framework.CUCUMBER_RUBY, "src/test/resources/cucumber-ruby/*passed.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/cucumber-ruby-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-cucumber-ruby-final.xml"},
                {Framework.PHP_BEHAT, "src/test/resources/php-behat/*pass*.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/php-behat-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-php-behat-final.xml"},
                {Framework.PYTHON_BEHAVE, "src/test/resources/python-behave/*skipped*.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/python-behave-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-python-behave-final.xml"},
                {Framework.PYTHON_RADISH, "src/test/resources/python-radish/*all-pass*.xml", "src/test/resources/features/*.feature", "multipleDifferentResults/python-radish-final.xml", "**/multipleDifferentResults/standard-multipleDifferent-python-radish-final.xml"},
                //multiple feature files - same file path
                {Framework.CUCUMBER_JS, "src/test/resources/cucumber-js/*test-report.xml", "**/*.feature", "multipleSameResults/cucumber-js-final.xml", "**/multipleSameResults/standard-multipleSame-cucumber-js-final.xml"},
                {Framework.CUCUMBER_JVM, "**/junit-passed.xml", "**/*.feature", "multipleSameResults/cucumber-jvm-final.xml", "**/multipleSameResults/standard-multipleSame-cucumber-jvm-final.xml"},
                {Framework.CUCUMBER_RUBY, "src/test/resources/cucumber-ruby/TEST*.xml", "src/test/resources/features/*.feature", "multipleSameResults/cucumber-ruby-final.xml", "**/multipleSameResults/standard-multipleSame-cucumber-ruby-final.xml"},
                {Framework.PHP_BEHAT, "src/test/resources/php-behat/*1.xml", "src/test/resources/features/*.feature", "multipleSameResults/php-behat-final.xml", "**/multipleSameResults/standard-multipleSame-php-behat-final.xml"},
                {Framework.PYTHON_BEHAVE, "src/test/resources/python-behave/*skip-*.xml", "src/test/resources/features/dealer.feature", "multipleSameResults/python-behave-final.xml", "**/multipleSameResults/standard-multipleSame-python-behave-final.xml"},
                {Framework.PYTHON_RADISH, "src/test/resources/python-radish/python*.xml", "src/test/resources/features/*.feature", "multipleSameResults/python-radish-final.xml", "**/multipleSameResults/standard-multipleSame-python-radish-final.xml"},
                //separate feature file
                //2 consecutive junit reports
                {Framework.CUCUMBER_JVM, "src/test/resources/cucumber-jvm/separate-test1/*.xml", "src/test/resources/features/robustgherkin.feature", "separateResults/cucumber-jvm-final1.xml", "**/separateResults/standard-cucumber-jvm-final1.xml"},
                //2 consecutive junit reports + 1 repeated junit report
                {Framework.CUCUMBER_JVM, "src/test/resources/cucumber-jvm/separate-test2/*.xml", "src/test/resources/features/robustgherkin.feature", "separateResults/cucumber-jvm-final2.xml", "**/separateResults/standard-cucumber-jvm-final2.xml"},
                //2 discrete junit reports + 1 unrelated junit report
                {Framework.CUCUMBER_JVM, "src/test/resources/cucumber-jvm/separate-test3/*.xml", "src/test/resources/features/*.feature", "separateResults/cucumber-jvm-final3.xml", "**/separateResults/standard-cucumber-jvm-final3.xml"}
        });

    }

    @Before
    public void setup() {
        reportFiles = FilesLocator.getReportFiles(reportFilesPath);
        featureFiles = FilesLocator.getFeatureFiles(featureFilesPath);
        reportFiles.forEach(System.out::println);
        featureFiles.forEach(System.out::println);
    }

    @Test
    public void bdd2OctaneITCase() throws CompareXmlException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, XMLStreamException, InstantiationException {
        String resultFilePath = "target/generated-test-sources-" + resultFilesPath;
        System.out.println("Comparing result file " + resultFilePath + " with expected file " + standardResultPath);
        new Bdd2Octane(reportFiles, featureFiles, resultFilePath, framework.getValue()).run();
        validate(resultFilePath, standardResultPath);
    }
}

