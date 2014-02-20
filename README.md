## OpenData Bridge ##

The OpenData Bridge is a small stand-alone Java application which harvests metadata sets from OpenData catalogues (Ckan based) and OGC CSW catalogues, transforms them into ArcGIS Online items (http://bit.ly/1457Ve9) and publishes these items into ArcGIS Online. The current version of the OpenData Bridge tool supports the WMS, KML and CSV item type. 
The tool could be used for a single process (running as a batch script directly by the user)

## Running the application ##
If yout want to execute the latest release please go to the following page 

## Building the application (for Developers) ##

* check out the source code from the repository
* create a Maven project with your IDE
* make a copy namend buildLocal.properties from the file build.properties
* place this file in the same directory as the build.properties
* change the properties test.service.username and test.service.pwd with your AcrGIS Online Account
* The Maven property local.configfile must point to the buildLocal.properties file in your IDE
* activate the env-dev profile in your IDE
* execute the maven process (phase package)

## Requirements ##
* JAVA 1.6 or higher
* Maven 2.2.10 or higher
 

## Issues ##
Find a bug or want to request a new feature? Please let us know by submitting an issue.

## Contributing ##
Anyone and everyone is welcome to contribute.

## Licensing ##
Copyright 2012 Esri

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

A copy of the license is available in the repository's [license.txt]( https://github.com/EsriDE/OpenDataBridge/edit/master/license.txt) file.
<open>
