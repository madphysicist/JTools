JTools
======

Collection of Java tools and utilities.

#Usage:

This package is intended as a utility library. Some classes have main methods to demo various components. The Javadocs for the packages are not complete yet, but the main classes are pretty well documented.

#Dependencies

This package requires the following to be installed on the local system for the and build and test targets to run:

- tools.jar that comes with your JDK.
- testng.jar and any dependencies that it may require.

Update local paths to these files in config/local.properties in the section containing the test.*.jar properties

If testng requires other dependencies than some version jcommander.jar (older versions require qdox.jar and bsh.jar), edit the testng-init target in config/setup.xml

Note that config/local.properties is in .gitignore, so feel free to modify it in your local repository (hence the file name).
