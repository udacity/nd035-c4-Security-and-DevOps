package com.udacity.examples.Testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ HelperTest.class, ParameterizedHelperTest.class })
public class AllTests {

}
