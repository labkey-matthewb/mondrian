/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2010-2012 Pentaho
// All Rights Reserved.
//
// The Jenkins CI engine runs this file to set up the environment to run
// Mondrian's build and test suite.
*/
def workspace = this.args[0]
def buildTag = this.args[1]
def testToRun = "all"

println "workspace: ${workspace}"
println "buildTag: ${buildTag}"
println "testToRun: ${testToRun}"

dbProps = """
# mysql database properties
driver.classpath=/usr/share/java/mysql-connector-java.jar
mondrian.foodmart.jdbcURL=jdbc:mysql://localhost/foodmart
mondrian.foodmart.jdbcUser=foodmart
mondrian.foodmart.jdbcPassword=foodmart
mondrian.jdbcDrivers=com.mysql.jdbc.Driver
mondrian.foodmart.catalogURL=${workspace}/demo/FoodMart.mondrian.xml
"""

//
// Setup 'native' properties
//
def nativeOn = true
println "nativeOn = ${nativeOn}"

def nativeProps = """
# native computation properties
mondrian.native.crossjoin.enable=${nativeOn}
mondrian.native.topcount.enable=${nativeOn}
mondrian.native.filter.enable=${nativeOn}
mondrian.native.nonempty.enable=${nativeOn}
"""

//
// Setup aggregate properties
//
def aggOn = false
println "aggOn = ${aggOn}"

def aggProperties = """
# aggregate properties
mondrian.rolap.aggregates.Use=${aggOn}
mondrian.rolap.aggregates.Read=${aggOn}
#mondrian.rolap.aggregates.ChooseByVolume=${aggOn} #it's unclear whether we should tie this to the aggregate param
"""

//
// Setup SsasCompatibleNaming properties
//
def scnOn = false
println "scnOn = ${scnOn}"

def scnProperties = """
# SsasCompatibleNaming properties
#mondrian.olap.SsasCompatibleNaming=${scnOn}
# Always on on 4.0.
mondrian.olap.SsasCompatibleNaming=true
"""

//
// Setup test case narrowing
//
def testClassProperty
if (testToRun != 'all') {
  testClassProperty = """
# Run this specific test only
mondrian.test.Class=${testToRun}
"""
  
}

//
//Write to the mondrian.properties file
//
def mondrianPropsFile = new File(workspace, 'mondrian.properties')
mondrianPropsFile.write("# Mondrian properties generated by ${buildTag}\n")
mondrianPropsFile << dbProps
mondrianPropsFile << nativeProps
mondrianPropsFile << aggProperties
mondrianPropsFile << scnProperties
if(testClassProperty) {
  mondrianPropsFile << testClassProperty
}

// End jenkins.groovy