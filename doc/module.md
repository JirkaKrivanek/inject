# Module

## Bindings

Binding is an assignment between the interface and implementation.

So ideally the actual Java interface and implementing class:

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
```

## Defining bindings

Bindings can be defined in multiple ways:
* Manually in the module
* Using `@Provides` annotation in module
* Using `@Provides` annotation in any other class

### Manual bindings

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(User.class).thenInstantiate(UserImpl.class);
    }
}
```

#### With multiple classes

The binding below will inject user implementation when either the user interface or the user
implementation is requested from the factory:

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(User.class).addForClass(UserImpl.class).thenInstantiate(UserImpl.class);
    }
}
```

### Annotated bindings in module

```java
class MyModule extends Module
{
    @Provides User createUser() {
        return new UserImpl();
    }
}
```

**Note:**The provided objects are **NOT** automatically injected with field and method dependencies!

Use `Factory#inject()` to inject it manually if this is what you need:

```java
class MyModule extends Module
{
    @Provides User createUser() {
        final User user = new UserImpl();
        getFactory().inject(user);
        return user;
    }
}
```

### Annotated bindings in any other objects

```java
class MyProviders
{
    @Provides User createUser() {
        return new UserImpl();
    }
}

class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        addProviderObject(new MyProviders());
    }
}
```

**Note 1:** The provider is also subject of injection when being registered to the factory.

**Note 2:**As with the module providers, also here the provided objects are **NOT** automatically injected with field and method dependencies!

Use `Factory#inject()` to inject it manually if this is what you need (note that factory can be injected too):

```java
class MyProviders
{
    @Inject private Factory mFactory;

    @Provides User createUser() {
        final User user = new UserImpl();
        mFactory.inject(user);
        return user;
    }
}
```

## Multiple modules

Multiple modules can be registered into a factory.

Bindings defined by the later registered modules override former ones.
This allows to inject mock instead of real dependencies for testing.
