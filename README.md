objectify-appengine-memcacheclient
----

[![JitPack](https://jitpack.io/v/takemikami/objectify-appengine-memcacheclient.svg)](https://jitpack.io/#takemikami/objectify-appengine-memcacheclient)
[![Build Status](https://travis-ci.com/takemikami/objectify-appengine-memcacheclient.svg?branch=master)](https://travis-ci.com/takemikami/objectify-appengine-memcacheclient)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e7ca8b4b11d44b30987979b0511e31ac)](https://www.codacy.com/app/takemikami/objectify-appengine-memcacheclient?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=takemikami/objectify-appengine-memcacheclient&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/takemikami/objectify-appengine-memcacheclient/badge.svg?branch=master)](https://coveralls.io/github/takemikami/objectify-appengine-memcacheclient?branch=master)

objectify-appengine-memcacheclient is AppEngine Memcache Client Service for Objectify v6+.
Objectify is a Java data access API specifically designed for the Google Cloud Datastore.
See the [GitHub Objectify Repository](https://github.com/objectify/objectify) for about Objectify.

## How to setup

Add objectify-appengine-memcacheclient to your application dependencies.

build.gradle snippet

```
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io/' }
}
dependencies {
    compile group: 'com.googlecode.objectify', name: 'objectify', version: '6.0.2'
    runtime group: 'com.github.takemikami', name: 'objectify-appengine-memcacheclient', version: '0.0.2'
    runtime group: 'com.google.appengine', name: 'appengine-api-1.0-sdk', version: '1.9.65'
}
```

Initialize Objectify with objectify-appengine-memcacheclient.

Objectify init code snippet

```
final static String MEMCACHE_SERVICE = "com.github.takemikami.objectify.appengine.AppEngineMemcacheClientService";
ObjectifyService.init(new ObjectifyFactory(
    DatastoreOptions.newBuilder().setHost("http://localhost:8484")
        .setProjectId("my-project")
        .build().getService(),
    (MemcacheService) Class.forName(MEMCACHE_SERVICE).getDeclaredConstructor().newInstance()
));
```


## How to execute Example Application

Example Application helps you to understand objectify-appengine-memcacheclient.
You can execute example application by following process.

install gcloud sdk. (see. https://cloud.google.com/sdk/docs/)

install app-engine-java component.

```
gcloud components install app-engine-java
```

execute cloud datastore emulator.

```
gcloud beta emulators datastore start --host-port=localhost:8484
```

change example directory.

```
cd example
```

execute sample application.

```
gradle appengineRun
```

access to sample application. http://localhost:8080/
 
