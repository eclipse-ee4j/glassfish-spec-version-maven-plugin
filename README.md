# API Specification Version Maven Plugin

The API Specification Version Plugin provides goals to set properties for manifest file OSGI headers and to verify consistence of specification, API and API implementation package names and versions.

**Goals:**
* `set-spec-properties` (validate phase)
* `check-module` (package phase)

## Configuration properties

| Property | Type | Default<br/>Value | Description |
| --- | --- | --- | --- |
| specMode | `jakarta`&vert;`javax` | `jakarta` | `jakarta`: Jakarta EE projects mode<br/>`javaee`: legacy mode for java.net projects (deprecated) |
| ignoreErrors | boolean | false | whether `check-module` target should just print warnings or fail the build on errors |
| spec.nonFinal | boolean | false | `true` for snapshot build, `false` for release build
| spec.jarType | `api`&vert;`impl`| `api` | `api`: specification with a separate API jar file <br/>`impl`: specification with a standalone API jar file |
| spec.apiPackage | java package | | specification API package prefix |
| spec.specVersion | version | | specification version |
| spec.newSpecVersion | version | | specification version for snapshot builds (non final API) |
| spec.specImplVersion | version | | specification implementation version |
| spec.newImplVersion | version | | specification implementation version for snapshot builds (non final API) |

## set-spec-properties Goal

Sets properties for manifest file OSGI headers.

Configuration properties always required:
* `spec.specVersion`
* `spec.apiPackage`

### Properties Mapping for `api` jarType

| Property | OSGI Header | Source |
| --- | --- |
| `spec.bundle.symbolic-name` | Bundle-SymbolicName | `spec.apiPackage` |
| `spec.bundle.spec.version`| BundleSpecVersion | `spec.specVersion` |
| `spec.bundle.version`| Bundle-Version | `spec.specImplVersion` |
| `spec.extension.name`| Extension-Name | `spec.apiPackage` |
| `spec.specification.version`| Specification-Version | `spec.specVersion` |
| `spec.implementation.version`| Implementation-Version | `spec.specImplVersion` |

### Properties Mapping for `impl` jarType

| Property | OSGI Header | Source |
| --- | --- |
| `spec.bundle.symbolic-name` | Bundle-SymbolicName | `spec.implNamespace '.' spec.apiPackage` |
| `spec.bundle.spec.version`| BundleSpecVersion | `spec.specVersion` |
| `spec.bundle.version`| Bundle-Version | `spec.implVersion` |
| `spec.extension.name`| Extension-Name | `spec.apiPackage` |
| `spec.specification.version`| Specification-Version | `spec.specVersion` |
| `spec.implementation.version`| Implementation-Version | `project.version`?? |

## check-module Goal

Validates maven properties consistency.

Properties always required:
* `spec.specVersion`
* `spec.apiPackage`
* `spec.newSpecVersion` when `nonFinal == true`

Properties required for `api` jarType:
* `spec.specImplVersion` when `nonFinal == false`

Properties required for `impl` jarType:
* `spec.implNamespace`
* `spec.implVersion`
* `spec.newImplVersion` when `nonFinal == true`

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
