# ART
ART is a kit of modules for developing multifunctional applications.


## Badges
![Build Status](https://travis-ci.com/art-community/ART.svg)
[![Gitter](https://badges.gitter.im/art-community/community.svg)](https://gitter.im/art-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
![Bintray](https://img.shields.io/bintray/v/art-community/art/io.github.art)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3a5d459c173a4109b2d039c8f7cd3cce)](https://app.codacy.com/app/antonbashir/ART?utm_source=github.com&utm_medium=referral&utm_content=art-community/ART&utm_campaign=Badge_Grade_Dashboard)
![GitHub repo size](https://img.shields.io/github/repo-size/art-community/art)

## Build and Binaries
Releases are available via Maven Central.

Example:
```kotlin
plugins {
    id("io.github.art.project") version "1.0.68"
}

art {
  idea()
  lombok()  
  
// Modules that includes into result project *.jar 
  // Alternatives: providedModules (not included into jar)  and 
  // testModules (use in tests sources)
  embeddedModules {
      // For specify version. Default version is "1.+" 
      //useVersion("1.+")

      // For full kit of modules 
      kit()    
  }  
 }
```
## Requirements
- Java 8 - heavy dependence on Java 8 functional APIs
- Gradle 5.+
- If working with web - installed nodejs and npm
- If using tarantool - installed tarantool
- If using sql - installed SQL db

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/art-community/art/issues).

## Documentation

* [How To](https://github.com/art-community/ART/tree/latest/documentation/how-to-get-started-with-art.md)
* [Examples](https://github.com/art-community/ART/tree/latest/documentation/examples.md)
* [Concepts & motivation](https://github.com/art-community/ART/tree/latest/documentation/concept-&-motivation.md)
* [Use cases](https://github.com/art-community/ART/tree/latest/documentation/use-cases.md)
* [Modules](https://github.com/art-community/ART/tree/latest/documentation/modules-&-capabilities.md)
* [Universal value model](https://github.com/art-community/ART/tree/latest/documentation/universal-value-model.md)
* [Auto configuring](https://github.com/art-community/ART/tree/latest/documentation/agile-auto-configuring.md)
* [Configuration specifications](https://github.com/art-community/ART/tree/latest/documentation/configuration-specifications.md)
* [Public API](https://github.com/art-community/ART/tree/latest/documentation/public-api.md)
* [Constraints & Development](https://github.com/art-community/ART/tree/latest/documentation/constraints-&-development.md)
 

## LICENSE
ART Java

Copyright 2019 ART

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
