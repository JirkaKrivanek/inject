# Injections

In order to bring the dependency injection alive,
the code classes should be marked properly with at least the `@Inject` annotation.

## Inject annotation

The `@Inject` annotation can be placed at three different statements:
* Constructor
* Field
* Setter method

### Injecting via constructor

When the factory is requested to get the instance, the implementation is
instantiated using the constructor marked with the `@Inject` annotation.

```java
public class ServiceImpl implements Service {

    private final User mUser;

    @Inject
    public Service(final User user)
    {
        mUser = user;
    }
}
```

All the parameters of the constructor are automatically provided by the same factory.

### Injecting via field

After instantiating the object, the factory scans the fields of the object and performs
the automatic injection of all of those marked with the `@Inject` annotation.

```java
public class ServiceImpl implements Service {

    @Inject
    private User mUser;
}
```

This brings a few benefits over the constructor:
* It is briefer (less code)
* The injection is done in second loop, so it allows the cyclic injection (not very good programming practice)
* This injection can be done also on foreign objects

### Injecting via setter method

After instantiating the object, the factory scans the methods of the object and performs
the automatic injection of all of those marked with the `@Inject` annotation.

```java
public class ServiceImpl implements Service {

    private String mUserName;
    private String mPassword;

    @Inject
    public void setup(final String userName, final String passWord passWord) {
        mUserName = userName;
        mPassword = passWord;
    }
}
```

**Note:** The example above intentionally does not use simple setter (which would take
one parameter only). This is to demonstrate that the injection works on multiple
parameters too.

### Injection of foreign objects

Factory supports also injection on objects created with traditional `new` operator.

```java
public class Main {
    public static void main(String [] args) {
        Factory.registerModule(new MyModule());
        final Service service = new ServiceImpl();
        Factory.injectInstance(service);
    }
}
```

## More specific injections

The method injection example above (`setup()`) apparently contains a problem:
* The injection cannot distinguish which value to inject into which parameter
* As both of them are of the same type - `String`

This can be solve by one of the ways demonstrated below.

This can be useful for injecting the configuration parameters.

### Named annotation

The `@Named` annotation can be used to be more specific with the injection.

```java
public class ServiceImpl implements Service {
    private String mUserName;
    private String mPassword;

    @Inject
    public void setup(@Named('userName) final String userName, @Named('password') final String passWord passWord) {
        mUserName = userName;
        mPassword = passWord;
    }
}
```

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifNamed('userName').thenReturn('John');
        whenRequestedInstanceOf(String.class).ifNamed('password').thenReturn('1234');
    }
}
```

**Note:**The named annotations can also be placed with the fields and providers.

### Other annotations

The `@Named` annotation is not exactly structured so the application specific
annotations can be used instead to achieve the same effect.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface UserName {}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface Password {}
```

```java
public class ServiceImpl implements Service {
    private String mUserName;
    private String mPassword;

    @Inject
    public void setup(@UserName final String userName, @Password final String passWord passWord) {
        mUserName = userName;
        mPassword = passWord;
    }
}
```

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(String.class).ifAnnotatedWith(UserName.class).thenReturn('John');
        whenRequestedInstanceOf(String.class).ifAnnotatedWith(Password.class).thenReturn('1234');
    }
}
```

**Note:**The other annotations can also be placed with the fields and providers.
