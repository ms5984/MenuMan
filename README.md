## Welcome to MenuMan
![Maven Central](https://img.shields.io/maven-central/v/com.github.ms5984.api/MenuMan)
![GitHub tag (latest pre-release)](https://img.shields.io/github/v/tag/ms5984/MenuMan?include_prereleases)

### Implementation
Create a new MenuBuilder object and follow the fluent interface where it takes you.

`ClickAction` describes a functional interface that accepts `MenuClick` objects,
a native encapsulation of `InventoryClickEvent`. You'll find that class comes with
an easy-to-use (by method reference), static helper method for the simple action of
closing a player's inventory. It looks like this in the wild:

```java
import com.github.ms5984.api.menuman.Menu;
import org.bukkit.plugin.java.JavaPlugin;

class MyMenuClass {
    public MyMenuClass(JavaPlugin plugin) {
        Menu menu = new MenuBuilder(rows, title)
                .addElement(new ItemStack(Material.DIRT))
                .setText("I've got a jar of dirt!")
                .setLore("I've got a jar of dirt!")
                .setAction(ClickAction::CloseInventory) // Right here, it's a beaut.
                .assignToSlots(0, 2).create(plugin);
    }
}
```

### Importing
Just add the following into your `pom.xml`. No additional repository needed!
```xml
    <dependency>
        <groupId>com.github.ms5984.api</groupId>
        <artifactId>MenuMan</artifactId>
        <version>1.0.1</version>
    </dependency>
```

As always, you are welcome to use the issue tracker, open a PR or contact me directly.

Thanks, Matt.



---
##### Licensing
My favorite license, the GNU LGPLv3.0. You are welcome to link from proprietary code as
long as you disclose the linking and provide end users a way to find the source code
they are using. Practically, this is as easy as providing said information with your
usual packaging--and a quite simple way to get exact details down to the version is
simply providing users a copy of your project's `pom.xml`. Note that I did not use
the word "publishing"; the GPL and LGPL are specifically about empowering end users--and
ultimately you still determine who those are and what (if anything) to charge them for
it.
