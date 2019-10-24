/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

art {
    providedModules {
        applicationCore()
        applicationEntity()
        applicationLogging()
        applicationService()
        applicationHttp()
        applicationMetrics()
    }
}

dependencies {
    with(art.externalDependencyVersionsConfiguration) {
        embedded("org.apache.tomcat.embed", "tomcat-embed-core", tomcatVersion)
                .exclude("org.apache.tomcat", "tomcat-juli")
                .exclude("org.apache.httpcomponents", "httpcore")
                .exclude("org.parboiled", "parboiled-java")
        embedded("org.apache.tomcat", "tomcat-servlet-api", tomcatVersion)
        embedded("org.zalando", "logbook-servlet", logbookVersion)
        embedded("org.jtwig", "jtwig-web", jtwigVersion)
                .exclude("com.google.guava", "guava")
                .exclude("org.apache.httpcomponents", "httpclient")
                .exclude("org.parboiled", "parboiled-java")
        embedded("com.google.guava", "guava", guavaVersion)
        embedded("org.apache.httpcomponents", "httpclient", apacheHttpClientVersion)
                .exclude("org.apache.httpcomponents", "httpcore")
        embedded("org.parboiled", "parboiled-java", "1.3.+")
    }
}