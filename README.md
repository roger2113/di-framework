# di-framework

Bean annotations:
* @Component - defines application plain bean
* @Repository - defines repository interfaces, which methods will be proxied and resolved SQL query will be logged

Bean dependency annotation
* @Autowired - field applicable, injects other bean as dependency


Repository interfaces can extend nothing, other repository interface, or framework provided CRUDRepository.
In last case default implementation of common CRUD methods will be provided, but other methods will be resolved dynamically.

---
Two runnable sample classes provided with own beans for every sample. 
Application will log main steps for bean instantiation. 

Build application

```
./gradlew build
```


Run application samples
```
java -cp ./build/libs/di-framework.jar com.makarov.samples.Sample1
java -cp ./build/libs/di-framework.jar com.makarov.samples.Sample2

```
