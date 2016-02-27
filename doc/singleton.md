# Singleton

The objects can be marked as singleton to ensure that only one instance is created.

**Note:**The singleton is factory wide - so each factory instance has its own set of singletons.

The singleton can be created multiple ways:
* Using the `@Singleton` annotation on
  * Either interface
  * Or implementation
* Forcing it on the binding definition

**Note:**The single instance will be ensured if at least one method (above) is used for that binding.

## Annotating interface

```java
@Singleton
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

## Annotating implementation

```java
interface User
{
    String getName();
}

@Singleton
class UserImpl implements User
{
    @Override
    String getName() {
        return "John Doe";
    }
}
```

## On binding

```java
class MyModule extends Module
{
    @Override
    protected void defineBindings() {
        whenRequestedInstanceOf(User.class).singleton().thenInstantiate(UserImpl.class);
    }
}
```
