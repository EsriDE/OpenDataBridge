## OpenData Bridge ##

The OpenData Bridge is a small stand-alone Java application which harvests metadata sets from OpenData catalogues (Ckan based) and OGC CSW catalogues, transforms them into ArcGIS Online items (http://bit.ly/1457Ve9) and publishes these items into ArcGIS Online. The current version of the OpenData Bridge tool supports the WMS, KML and CSV item type. 
The tool could be used for a single process (running as a batch script directly by the user)

## Building the application ##

* check out the source code from the repository
* create a Maven project with your IDE
* make a copy namend buildLocal.properties from the file build.properties
* change the properties test.service.username and test.service.pwd with your AcrGIS Online Account
* The Maven property local.configfile must point to the buildLocal.properties file
* Activate the env-dev profile
