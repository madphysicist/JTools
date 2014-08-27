JTools
======

A collection of Java tools and utilities. These tools are written in pure Java. They do not depend on any third party libraries except for test code, which uses TestNG. Additional tools that do have external dependencies are available in the JTools-extras project.

Usage
-----

This package is intended as a utility library. Some classes have main methods to demo various components. The Javadocs for the packages are not complete yet, but the main classes are pretty well documented.

Dependencies
------------

Local dependencies (specifically TestNG) can be edited in config/local.properties in the section containing the test.*.jar properties

If testng requires additional dependencies (older versions require qdox.jar and bsh.jar, while some newer versions require jcommander.jar), edit the `testng-jars` path in the `compile-test-init` target in config/setup.xml

Versions of config/local.properties suitable for Ubuntu and Arch based systems are present in the repository. They should be renamed to config/local.properties before use.
