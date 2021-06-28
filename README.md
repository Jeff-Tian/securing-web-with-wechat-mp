securing-web-with-wechat-mp

---

> https://wechat-mp.herokuapp.com/

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=Jeff-Tian_securing-web-with-wechat-mp)

## API Spec

https://app.swaggerhub.com/apis/UniHeart/wechat-mp/

Design api spec in the above link first.

## Build swagger

```shell
./gradlew clean build
```

To generate the api models and api mappings to the build folder, and then write the implementation controllers based on the generated api interfaces like so:

```java
@RestController
public final class ImplementController implements GeneratedApiController {
    
    @Override
    xxxMethod() {
    }
}
```

## Run from local

```shell
./gradlew clean bootRun
```
