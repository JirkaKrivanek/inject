# Factory

## Single factory

For most applications, the single factory is sufficient. So the `Factory` class
supports static methods which simplifies the factory usage.

```java
public class Main {
    public static void main(String [] args) {
        Factory.registerModule(new MyModule());
        final Service service = Factory.getInstance(Service.class);
    }
}
```

**Note:** Singleton is factory wide

The singleton instance is actually single only within one factory instance.
In other words, each factory has its own set of singletons.

## Multiple factories

Some applications may require multiple independent instances of some engine/facility.
For these, the `Factory` class supports the instantiation and non-static usage.

```java
public class Main {
    public static void main(String [] args) {
        // First factory
        final Factory factory1 = Factory.createFactory();
        factory1.register(new MyModule());
        final Service service1 = factory1.get(Service.class);

        // Second factory
        final Factory factory2 = Factory.createFactory();
        factory2.register(new MyModule());
        final Service service2 = factory2.get(Service.class);
    }
}
```

## Define bindings directly with the factory

Although it is not too practical, sometimes it can be useful: The binding between the
interface and its implementation can be defined directly with the factory object:

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.getSingleton().whenRequestedInstanceOf(Context.class).thenReturn(getApplicationContext());
    }
}
```

As shown in the example just above, the Android context, which is otherwise very hard to pass
everywhere it is needed can be registered for injection in a very easy way.

However, it is recommended to use the modules for registering the bindings - as shown below.

## Register module classes or instances

The factory can be defined in two ways:
* The module objects can be registered to factories
* The module class can be added to the factory before its instantiation

### Registering module objects

The module is first instantiated and then added registered to the factory.
Single particular module can only be registered to the single factory.

```java
public class Main {
    public static void main(String [] args) {
        final Factory factory = Factory.createFactory();
        factory.register(new MyModule());
    }
}
```

### Adding module classes

The module class is added to the factory. This defines the basic factory modules.

When the factory is instantiated, all added module classes are automatically instantiated
too, so every factory instance has the same set of module objects.

```java
public class Main {
    public static void main(String [] args) {
        Factory.addModuleClass(MyModule.class);
        final Factory factory1 = Factory.createFactory();
        final Factory factory2 = Factory.createFactory();
    }
}
```

## Factory injection

Factory object itself can be injected too:

```java
public class Service {
    @Inject private Factory mFactory;
}
```

The injected factory is exactly that instance which was used for the injection.
So no factory clash and multiple factories are still supported.

## Merging factories

More complex projects can easily consist of multiple independent libraries delivered
without the source code.

To be able to use it effectively, the multiple factories can be merged into one other
factory which then serves instances of all former (merged) factories.

_**This feature is still TBD**_
