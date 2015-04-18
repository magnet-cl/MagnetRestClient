# MagnetRestClient

<b>MagnetRestClient</b> is an Android networking library built on top of [Volley](http://developer.android.com/training/volley/index.html), which uses Square's [OkHttp](http://square.github.io/okhttp/) client as its transport layer. 

Volley is really great, but is missing a few features that should be by default and that in every Android project here at Magnet we need to implement, like a default GsonRequest, a default BitmaplLruCache for Volley ImageLoader and a persistent cookie store. This project aims to add those, and a few more features to Volley toolkit to make it a more complete HTTP solution.

## Features
- Support for custom HTTP headers
- Custom User-Agent header that sends the application package, application version, android version and cellphone model in every request. For example: ```com.example.app/1.0 (Android 4.4.4; XT1032)```. This is useful to implement an Upgrade Required feature to the application, where the server responds with an specific error if the application version is not supported any more.
- Persistent cookie store, which stores cookies in SharedPreferences
- Custom error listener for relogin in case of an unauthorized request
- RequestQueue and other Volley functionality are encapsulated in VolleyManager
- Default GsonRequest implementation
- Default BitmapLruCache implementation added to ImageLoader

## Download
Grab via Maven:
```xml
<dependency>
  <groupId>cl.magnet.magnetrestclient</groupId>
  <artifactId>magnetrestclient</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'cl.magnet.magnetrestclient:magnetrestclient:1.0.0'
```

## Documentation
- [Making a Request](https://github.com/magnet-cl/MagnetRestClient/wiki/Making-a-Request)

## License
MagnetRestClient is available under the MIT license. See the LICENSE file for more information.
