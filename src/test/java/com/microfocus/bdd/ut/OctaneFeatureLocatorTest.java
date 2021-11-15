package com.microfocus.bdd.ut;

import com.microfocus.bdd.OctaneFeatureLocator;
import com.microfocus.bdd.api.OctaneFeature;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class OctaneFeatureLocatorTest {

    @Test
    public void testGetFeatureByPath() {
        String featureName = "Some terse yet descriptive text of what is desired like bla bla";
        String featureName01 = "Some terse yet descriptive text of what is desired: like bla bla01";
        String featureName02 = "Some terse yet descriptive text of what is desired like bla bla02";
        String featureNameInCN = "一个功能";
        String featureNameInDE = "Funktion";
        OctaneFeatureLocator cache = new OctaneFeatureLocator(Arrays.asList(
                "src/test/resources/features/robustgherkin.feature",
                "src/test/resources/features/robustgherkin_cn.feature",
                "src/test/resources/features/robustgherkin_de.feature",
                "src/test/resources/features/robustgherkin01.feature",
                "src/test/resources/features/robustgherkin02.feature"));

        Assert.assertEquals("find robustgherkin.feature by path: " + "src/test/resources/features/robustgherkin.feature", featureName,
                cache.getOctaneFeatureByPath("src/test/resources/features/robustgherkin.feature").getName());
        Assert.assertEquals("find robustgherkin01.feature by path: " + "src/test/resources/features/robustgherkin01.feature", featureName01,
                cache.getOctaneFeatureByPath("src/test/resources/features/robustgherkin01.feature").getName());
        Assert.assertEquals("find robustgherkin02.feature by path: " + "src/test/resources/features/robustgherkin02.feature", featureName02,
                cache.getOctaneFeatureByPath("src/test/resources/features/robustgherkin02.feature").getName());
        Assert.assertEquals("find robustgherkin_cn.feature by path: " + "src/test/resources/features/robustgherkin_cn.feature", featureNameInCN,
                cache.getOctaneFeatureByPath("src/test/resources/features/robustgherkin_cn.feature").getName());
        Assert.assertEquals("find robustgherkin_de.feature by path: " + "src/test/resources/features/robustgherkin_de.feature", featureNameInDE,
                cache.getOctaneFeatureByPath("src/test/resources/features/robustgherkin_de.feature").getName());
    }

    @Test
    public void testGetFeatureByName() throws IOException {
        String featureName = "Some terse yet descriptive text of what is desired like bla bla";
        String featureName01 = "Some terse yet descriptive text of what is desired: like bla bla01";
        String featureName02 = "Some terse yet descriptive text of what is desired like bla bla02";
        String featureNameInCN = "一个功能";
        String featureNameInDE = "Funktion";
        String fileName = "src/test/resources/features/robustgherkin.feature";
        String fileNameCN = "src/test/resources/features/robustgherkin_cn.feature";
        String fileNameDE = "src/test/resources/features/robustgherkin_de.feature";
        String fileName01 = "src/test/resources/features/robustgherkin01.feature";
        String fileName02 = "src/test/resources/features/robustgherkin02.feature";
        OctaneFeatureLocator locator = new OctaneFeatureLocator(Arrays.asList(
                fileName,
                fileNameCN,
                fileNameDE,
                fileName01,
                fileName02));

        testGetFeatureByName(featureName, fileName, locator);
        testGetFeatureByName(featureName01, fileName01,locator);
        testGetFeatureByName(featureName02, fileName02,locator);
        testGetFeatureByName(featureNameInCN, fileNameCN, locator);
        testGetFeatureByName(featureNameInDE, fileNameDE, locator);

        OctaneFeature feature = locator.getOctaneFeatureByName(featureName).get();
        locator.remove(feature);
        OctaneFeature feature1 = locator.getOctaneFeatureByName(featureName).get();
        Assert.assertTrue("feature1 is NOT same as feature as cache is cleared", feature1 != feature);

    }

    private void testGetFeatureByName(String featureName, String featureFile, OctaneFeatureLocator locator) throws IOException {
        Optional<OctaneFeature> feature = locator.getOctaneFeatureByName(featureName);
        Assert.assertTrue("feature has correct file name", feature.get().getFeatureFile().equals(featureFile));
        Optional<OctaneFeature> feature1 = locator.getOctaneFeatureByName(featureName);
        Assert.assertTrue("feature1 is same as feature as it is from locator", feature1.get() == feature.get());
    }


    @Test
    public void testGetFeatureByScenarioName() throws IOException {
        String featureName = "Some terse yet descriptive text of what is desired like bla bla";
        OctaneFeatureLocator cache = new OctaneFeatureLocator(Arrays.asList(
                "src/test/resources/features/robustgherkin.feature", // this file is to add some salt to the test case
                "src/test/resources/features/robustgherkin_cn.feature",// this file is to add some salt to the test case
                "src/test/resources/features/feature_name_is_empty.feature"
        ));

        cache.getOctaneFeatureByName(featureName);
        OctaneFeature feature = cache.getFeatureByScenarioName("Some determinable business situation (empty feature name)").get();
        Assert.assertTrue("Feature name is empty", feature.getName().isEmpty() );
        OctaneFeature feature2 = cache.getFeatureByScenarioName("Some another scenario 2 (empty feature name)").get();
        Assert.assertTrue("Got the same feature as previous because locator caches the feature", feature2 == feature);
        OctaneFeature feature3 = cache.getFeatureByScenarioName("feeding a cow cow1 yum yum yum (empty feature name)").get();
        Assert.assertTrue("Got the same feature as previous because locator caches the feature", feature3 == feature);
        OctaneFeature feature4 = cache.getFeatureByScenarioName("feeding a cow cow2 yum yum yum (empty feature name)").get();
        Assert.assertTrue("Got the same feature as previous because locator caches the feature", feature4 == feature);
    }

}
