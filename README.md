# java-faker

Generate realistic **fake data in one line** — ideal for tests, demos, seeding databases, or anywhere you need quick sample data.

## 🚀 Why java-faker?

- Zero-config: just add the dependency and start faking
- Supports primitives, enums, collections, custom classes, nullable fields & more
- Tiny (<60 KB) and pure Java 8+, no extra runtime deps

## ⚡️ 3-Step Quick Start

1. **Add dependency**

```xml
<dependency>
  <groupId>io.github.regychang</groupId>
  <artifactId>java-faker</artifactId>
  <version>0.2.5</version>
</dependency>
```

2. **Create a Faker**

```java
Faker faker = new Faker();
```

3. **Fake anything**

```java
int     n   = faker.randomInt();           // primitives
String  s   = faker.randomString();        // strings
Status  st  = faker.randomEnum(Status.class); // enums

// Full POJO in one call
User user = faker.fakeData(User.class);

// Customise on the fly
Options opt = new Options().withRandomStringLength(8).withNullProbability(0.3);
List<String> names = Faker.createList(String.class, opt);
```

## ✨ Features at a Glance

| Feature         | Example                                            |
| --------------- | -------------------------------------------------- |
| Primitives      | `faker.randomDouble()`                             |
| Collections     | `Faker.createMap(String.class, Long.class)`        |
| Nullable fields | `new Options().withNullProbability(0.2)`           |
| Ranges / bounds | `new Options().withRandomIntegerBoundaries(10,99)` |
| Custom classes  | `faker.fakeData(MyDto.class)`                      |
| Field override  | `opt.withFieldProvider("id", f -> 42)`             |

## 📚 Minimal Example – Custom Class

```java
public class User {
  @JFaker("id") Integer id;
  String name;
  Boolean active;
}

// Generate
User u = faker.fakeData(User.class,
         new Options().withFieldProvider("id", f -> 1001));
```

> Happy faking!
