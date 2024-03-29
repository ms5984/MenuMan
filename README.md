## Welcome to MenuMan
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ms5984.lib/menu-man)](https://oss.sonatype.org/#nexus-search;gav~com.github.ms5984.lib~menu-man~~~)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.github.ms5984.lib/menu-man?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/#nexus-search;gav~com.github.ms5984.lib~menu-man~~~)

###### This project has been archived. Please feel free to contact me.

### Implementation
Create a new MenuBuilder object and follow the fluent interface where it takes you.

`ClickAction` describes a functional interface that accepts `MenuClick` objects,
a native encapsulation of `InventoryClickEvent`. You'll find that class comes with
an easy-to-use (by method reference), static helper method for the simple action of
closing a player's inventory. It looks like this in the wild:

```java
import com.github.ms5984.lib.menuman.Menu;
import org.bukkit.plugin.java.JavaPlugin;

class MyMenuClass {
    public MyMenuClass(JavaPlugin plugin) {
        Menu menu = new MenuBuilder(rows, title)
                .addElement(new ItemStack(Material.DIRT))
                .setText("I've got a jar of dirt!")
                .setLore("I've got a jar of dirt!")
                .setAction(ClickAction::close) // Right here, it's a beaut.
                .assignToSlots(0, 2).create(plugin);
    }
}
```
### Relocation (READ THIS SECTION)
It is important to include a valid `maven-shade-plugin` configuration to avoid
colliding with other plugin jars that also use and provide this resource.
Take moment to look over this example:
```xml
    <!-- In your pom.xml -->
    <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-shade-plugin</artifactId>
          <version>3.1.0</version>
          <!-- This is the important element -->
          <configuration>
            <relocations>
              <relocation>
                <pattern>com.github.ms5984.lib.menuman</pattern>
                <!-- Replace this with your package! -->
                <shadedPattern>com.github.ms5984.anotherplugin.menuman</shadedPattern>
              </relocation>
            </relocations>
          </configuration>
          <executions>
            <execution>
              <phase>package</phase>
              <goals>
                <goal>shade</goal>
              </goals>
              <!-- Note that there are two areas labeled configuration! -->
              <!-- Relocations must go up in the first one -->
              <configuration>
                <createDependencyReducedPom>false</createDependencyReducedPom>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
```

### Importing
#### Releases: Just add the following into your `pom.xml`. No additional repository needed!
```xml
<project>
    <!-- For Maven Central releases -->
    <dependencies>
        <dependency>
            <groupId>com.github.ms5984.lib</groupId>
            <artifactId>menu-man</artifactId>
            <version><!--Maven-central version here--></version>
        </dependency>
    </dependencies>
</project>
```
#### Snapshots (requires sonatype repository)
```xml
<project>
    <!-- For Sonatype Nexus snapshots (primary development here) -->
    <repositories>
        <repository>
            <id>sonatype-snapshots</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            <snapshots>
                <enabled>true</enabled>
                <!-- Configure a custom artifact timeout -->
                <updatePolicy>interval:2</updatePolicy>
                <!-- re-downloads artifacts over two minutes old -->
            </snapshots>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>com.github.ms5984.lib</groupId>
            <artifactId>menu-man</artifactId>
            <version><!--nexus snapshot version here--></version>
        </dependency>
    </dependencies>
</project>
```
#### GitHub+Jitpack (requires jitpack repository)
```xml
<project>
    <!-- For Jitpack pre-release, custom commit builds -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>com.github.ms5984</groupId>
            <artifactId>MenuMan</artifactId>
            <!--commit hash; example below-->
            <version>364bd89</version>
        </dependency>
    </dependencies>
</project>
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
