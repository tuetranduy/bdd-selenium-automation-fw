Add those property to VM Options in order to execute test framework

```
-DpropertiesFiles="local.credentials,config.properties"
-Dbrowser=Chrome
-Dcucumber.filter.tags="@UI"
```