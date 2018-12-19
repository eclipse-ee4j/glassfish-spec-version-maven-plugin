# API Specification Version Maven Plugin

The API Specification Version Plugin provides goals to set properties for manifest file OSGI headers and to verify and enforce specification properties to be compliant with [JakartaEE Maven Versioning Rules](https://wiki.eclipse.org/JakartaEE_Maven_Versioning_Rules).

**Goals:**
* `set-spec-properties` (validate phase)
* `check-module` (package phase)

## Spec object

Spec object is mandatory parameter of all plugin goals. But set of mandatory object properties depends on specific goal and values of `jarType` and `nonFinal` properties.

| Property | Type | Default<br/>Value | Description |
| --- | --- | --- | --- |
| nonFinal | boolean | false | `true` for version numbers of non-final specifications</br>`false` for version numbers of final specifications
| jarType | `api`&vert;`impl`| `api` | `api`: specification with a separate API jar file <br/>`impl`: specification with a standalone API jar file |
| apiPackage | java package | | primary Java package defining the API, with `javax` replaced by `jakarta` |
| implNamespace | java package | | primary Java package or namespace used by the implementation of the API, or with which the implementation of the API is associated |
| specVersion | version | | version number of the last final specification, always of the form `<major>.<minor>` |
| newSpecVersion | version | | version number of the specification under development |
| specImplVersion | version | | version number of the API classes, derived from specVersion by adding an optional micro version number |
| implVersion | version | | version number of the implementation |
| newImplVersion | version | | version number of the implementation that will be used when the implementation is final
 |
| specBuild | version | | number of a particular build of the API jar file, e.g., "01", "02", etc |
| implBuild | version | | number of a particular build of the implementation jar file, e.g., "01", "02", etc. |

Properties must be encapsulated in `<spec>` section to be part of Spec object when defined in `pom.xml`. See examples.

## Goal: `set-spec-properties`

Sets properties for manifest file OSGI headers.

### Properties

| Property | Type | Default<br/>Value | Description |
| --- | --- | --- | --- |
| specMode | `jakarta`&vert;`javax` | `jakarta` | `jakarta`: Jakarta EE projects mode<br/>`javaee`: legacy mode for java.net projects (deprecated) |
| spec | object |  | API specification properties |
| project | object | `${project}` | Maven project object model |

### Properties Mapping for `api` jarType

| Property | OSGI Header | Source |
| --- | --- | --- |
| `spec.bundle.symbolic-name` | Bundle-SymbolicName | `apiPackage` |
| `spec.bundle.spec.version`| BundleSpecVersion | `specVersion` |
| `spec.bundle.version`| Bundle-Version | `specImplVersion` |
| `spec.extension.name`| Extension-Name | `apiPackage` |
| `spec.specification.version`| Specification-Version | `specVersion` |
| `spec.implementation.version`| Implementation-Version | `specImplVersion` |

### Properties Mapping for `impl` jarType

| Property | OSGI Header | Source |
| --- | --- | --- |
| `spec.bundle.symbolic-name` | Bundle-SymbolicName | `implNamespace '.' apiPackage` |
| `spec.bundle.spec.version`| BundleSpecVersion | `specVersion` |
| `spec.bundle.version`| Bundle-Version | `implVersion` |
| `spec.extension.name`| Extension-Name | `apiPackage` |
| `spec.specification.version`| Specification-Version | `specVersion` |
| `spec.implementation.version`| Implementation-Version | `${project.version}` |

## Goal: `check-module`

Validates specification properties consistency with [JakartaEE Maven Versioning Rules](https://wiki.eclipse.org/JakartaEE_Maven_Versioning_Rules).

### Properties

| Property | Type | Default<br/>Value | Description |
| --- | --- | --- | --- |
| specMode | `jakarta`&vert;`javax` | `jakarta` | `jakarta`: Jakarta EE projects mode<br/>`javaee`: legacy mode for java.net projects (deprecated) |
| module | file | *artifact* | API specification properties |
| ignoreErrors | boolean | false | whether this goal should just print warnings or fail |
| spec | object |  | API specification properties |
| project | object | `${project}` | Maven project object model |

*artifact*: `${project.build.directory}/${project.build.finalName}.${project.packaging}`

## Example

### Specification Version Plugin Configuration

```xml
<plugin>
  <groupId>org.glassfish.build</groupId>
  <artifactId>spec-version-maven-plugin</artifactId>
  <configuration>
    <spec>
      <nonFinal>false</nonFinal>
      <jarType>api</jarType>
      <specVersion>1.1</specVersion>
      <specImplVersion>1.1.3</specImplVersion>
      <apiPackage>jakarta.jpa</apiPackage>
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

Properties with `spec.` prefix are generated by `set-spec-properties` goal.

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
