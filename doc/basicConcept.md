# Basic concept

The dependency injection here works like in other libraries - perhaps the terminology is a bit different.

To incorporate, the application has to:
1. Code the functionality classes
1. Annotate the code wherever the dependency injection is needed
1. Define one or more modules providing the bindings
1. Register those modules with the factory
1. Instantiate the factory (or use the single factory simplification)
1. Use the factory to obtain the instances fully injected (whole object tree can be automatically created by single factory request)

## Functionality classes with annotations

```java
interface User
{
    String getName();
}

class UserImpl implements User
{
    @Override
    String getName() {
        return "John Doe";
    }
}

interface Service
{
    User getUser();
}

class ServiceImpl implements Service
{
    @Inject
    private User mUser;

    @Override
    User getUser() {
        return mUser;
    }
}
```

More details: [Injections](injections.md)

## Define module

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(User.class).thenInstantiate(UserImpl.class);
        whenRequestedInstanceOf(Service.class).thenInstantiate(ServiceImpl.class);
    }
}
```

More details: [Module](module.md)

## Register module and instantiate object

```java
public class Main {
    public static void main(String [] args) {
        Factory.addModuleClass(MyModule.class);
        final Service service = Factory.getInstance(Service.class);
        final User user = service.getUser();
        System.out.println(user.getName());
    }
}
```

More details: [Factory](factory.md)
