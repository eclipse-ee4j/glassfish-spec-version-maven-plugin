# API Specification Version Maven Plugin

The API Specification Version Plugin provides goals to set properties for manifest file OSGI headers and to verify consistence of specification, API and API implementation package names and versions.

Goals:
* set-spec-properties (validate phase)
* check-module (package phase)

## Configuration properties

| Property | Type | Description |
| --- | --- | --- |
| specMode | `jakarta` or `javax` | `jakarta` Jakarta EE projects mode<br/>`javaee` Legacy mode for java.net projects (deprecated) |
| ignoreErrors | boolean | whether `check-module` target should just print warnings or fail the build on errors |
| spec.nonFinal | boolean | `true` for snapshot build, `false` for release build
| spec.jarType | `api`&nbsp;or&nbsp;`impl`| `api`: specification with a separate API jar file <br/>`impl`: specification with a standalone API jar file |
| spec.specVersion | version | specification version |
| spec.newSpecVersion | version | specification version for snapshot builds (non final API) |
| spec.specImplVersion | version | specification implementation version |
| spec.newImplVersion | version | specification implementation version for snapshot builds (non final API) |
| spec.apiPackage | java package | specification API package prefix |

## set-spec-properties Goal

Sets properties for manifest file OSGI headers.

| Property | OSGI Header |
| --- | --- |
| `spec.bundle.symbolic-name` | Bundle-SymbolicName |
| `spec.bundle.spec.version`| BundleSpecVersion |
| `spec.bundle.version`| Bundle-Version |
| `spec.extension.name`| Extension-Name |
| `spec.specification.version`| Specification-Version |
| `spec.implementation.version`| Implementation-Version |

## check-module Goal

Validates maven properties consistency.

## Example

### Specification Version Plugin Configuration

```xml
<plugin>
  <groupId>org.glassfish.build</groupId>
  <artifactId>spec-version-maven-plugin</artifactId>
  <configuration>
    <spec>
      <nonFinal>${non.final}</nonFinal>
      <jarType>api</jarType>
      <specVersion>${spec.version}</specVersion>
      <specImplVersion>${project.version}</specImplVersion>
      <apiPackage>${extension.name}</apiPackage>
    </spec>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>set-spec-properties</goal>
        <goal>check-module</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

### OSGI Headers Generated Using `spec.` Properties

```xml
<plugin>
    <groupId>org.apache.felix</groupId>
    <artifactId>maven-bundle-plugin</artifactId>
    <configuration>
        <supportedProjectTypes>
            <supportedProjectType>jar</supportedProjectType>
        </supportedProjectTypes>
        <instructions>
            <Bundle-Version>${spec.bundle.version}</Bundle-Version>
            <Bundle-SymbolicName>${spec.bundle.symbolic-name}</Bundle-SymbolicName>
            <Extension-Name>${spec.extension.name}</Extension-Name>
            <Implementation-Version>${spec.implementation.version}</Implementation-Version>
            <Specification-Version>${spec.specification.version}</Specification-Version>                               
        </instructions>
    </configuration>
    <executions>
        <execution>
            <id>bundle-manifest</id>
            <phase>process-classes</phase>
            <goals>
                <goal>manifest</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
