# log-desensitization

desensitization sensitive data in output logs

## log message

### pure text

the sensitive data should output with below format.

```text
requestType: [C], email:[anonymous@google.com], mobile: [+1-12345678], lastName: [John], firstName: [Doe]
```

### JAVA object string

```text
TestingLogData.Obj(normalDataName1=normalDataValue1, normalDataName2=normalDataValue2, sensitiveDataName1=sensitiveDataValue1, sensitiveDataName2=sensitiveDataValue2)
```

### JSON string

```json
{
  "requestType": "C",
  "email": "anonymous@google.com",
  "mobile": "+1-12345678",
  "lastName": "John",
  "firstName": "Doe"
}
```

### XML String

```xml
<?xml version="1.0" encoding="UTF-8"?>
<user>
    <requestType>C</requestType>
    <email>anonymous@google.com</email>
    <mobile>+1-12345678</mobile>
    <lastName>John</lastName>
    <firstName>Doe</firstName>
</user>
```