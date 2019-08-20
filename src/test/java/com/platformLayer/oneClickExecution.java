package com.platformLayer;

import org.testng.TestNG;

public class oneClickExecution {

	public static void main(String[] args) {
		TestNG testng = new TestNG();
		@SuppressWarnings("rawtypes")
		Class[] classes = new Class[]{driverScript.class};
		testng.setTestClasses(classes);
		testng.run();
	}
}
