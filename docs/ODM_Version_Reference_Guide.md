# IBM ODM Version Reference Guide: 8.x vs 9.5.x

## Introduction

This document serves as a comprehensive reference guide detailing the differences between IBM ODM 8.x and 9.5.x versions. It can be used as a general reference for any ODM migration project.

## 1. Project Structure Changes

### ODM 8.x (Classic Rule Project)

- **Project Type**: Classic Rule Project
- **Core Project Files**:
  - `.project` file with Classic Rule Project facets
  - `.ruleproject` file with older namespace formats
  - Workspace structure follows Classic Rule Project conventions

### ODM 9.x/9.5.x (Decision Service)

- **Project Type**: Decision Service (DS)
- **Core Project Files**:
  - `.project` file with Decision Service facets
  - `.ruleproject` file with updated namespace formats and migrationFlag attribute
  - Workspace structure follows Decision Service conventions with enhanced organization

### Key Differences

| Aspect | ODM 8.x | ODM 9.5.x |
|--------|---------|-----------|
| Project Nature | `ilog.rules.studio.model.ruleProjectNature` | `com.ibm.rules.studio.model.decisionservice.ui.decisionServiceNature` |
| Build Commands | Classic Rule Project builders | Decision Service-specific builders |
| Folder Structure | More flexible, less standardized | More structured organization with defined model folders |
| Migration Marker | Not present | `migrationFlag="3"` attribute in project files |

## 2. XML Namespace Changes

### ODM 8.x Namespaces

```xml
xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore"
xmlns:ilog.rules.studio.model.bom="http://ilog.rules.studio/model/bom.ecore"
xmlns:ilog.rules.studio.model.query="http://ilog.rules.studio/model/query.ecore"
xmlns:ilog.rules.studio.model.rule="http://ilog.rules.studio/model/rule.ecore"
xmlns:ilog.rules.studio.model.xom="http://ilog.rules.studio/model/xom.ecore"
xmlns:ilog.rules.studio.model.ruleflow="http://ilog.rules.studio/model/ruleflow.ecore"
```

### ODM 9.5.x Namespaces

```xml
xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore"
xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore"
xmlns:ilog.rules.studio.model.bom="http://ilog.rules.studio/model/bom.ecore"
xmlns:ilog.rules.studio.model.query="http://ilog.rules.studio/model/query.ecore"
xmlns:ilog.rules.studio.model.rule="http://ilog.rules.studio/model/rule.ecore"
xmlns:ilog.rules.studio.model.xom="http://ilog.rules.studio/model/xom.ecore"
```

### Key Differences

- Addition of Decision Service namespace (`com.ibm.rules.studio.model.decisionservice`)
- Some root elements changed from `ilog.*` to `com.ibm.rules.studio.*` format
- XML structure enhanced for Decision Service metadata

## 3. Rule Asset Changes

### Ruleflow Files

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| File Extension: `.ruleflow` | File Extension: `.rfl` |
| Root Element: `<ilog.rules.studio.model.ruleflow:RuleFlow>` | Root Element: `<com.ibm.rules.studio.model.decisionservice:Ruleflow>` |
| Node Reference Format: Package-based | Node Reference Format: Fully qualified path |
| Flow Structure: Basic model | Flow Structure: Enhanced with more metadata |

### Business Rule Files (.brl)

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Format: XML with older namespaces | Format: XML with updated namespaces |
| Rule Structure: Classic format | Rule Structure: Enhanced with additional metadata |
| References: Package-based | References: More structured path references |

### Decision Tables

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Format: Limited metadata | Format: Enhanced metadata support |
| Column Configuration: Basic | Column Configuration: Enhanced with more properties |
| Integration: Limited | Integration: Better support for decision services |

## 4. XOM References

### ODM 8.x

```xml
<entries xsi:type="ilog.rules.studio.model.xom:SystemXOMPathEntry" 
  name="vic-ece-xom" url="platform:/vic-ece-xom" kind="JAVA_PROJECT"/>
```

### ODM 9.5.x

```xml
<entries xsi:type="ilog.rules.studio.model.xom:JavaProjectXOMPathEntry" 
  name="vic-ece-xom" url="platform:/vic-ece-xom" kind="JAVA_PROJECT"/>
```

### Key Differences

- `SystemXOMPathEntry` type replaced with `JavaProjectXOMPathEntry`
- Enhanced XOM metadata and configuration options
- Better support for Java project references

## 5. Deployment Structure

### ODM 8.x (.dop files)

```xml
<ilog.rules.studio.model.base:RuleProject xmi:version="2.0" 
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore"
    buildMode="DecisionEngine" isADecisionService="false">
```

### ODM 9.5.x (.dop files)

```xml
<com.ibm.rules.studio.model.decisionservice:Operation xmi:version="2.0" 
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore">
```

### Key Differences

- Deployment operations use Decision Service-specific format
- Enhanced support for operation parameters and metadata
- Better integration with rule execution servers
- Support for RESTful service deployments

## 6. Rule Execution Changes

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Rule Execution Server: Basic RES | Rule Execution Server: Enhanced RES with better performance |
| Decision Service Support: Limited | Decision Service Support: First-class support |
| REST API: Basic | REST API: Enhanced with OpenAPI support |
| Performance: Good | Performance: Improved with optimized rule engine |
| Ruleset Execution: Traditional | Ruleset Execution: Enhanced with decision service context |

## 7. Tooling and Interface Changes

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Rule Designer: Classic interface | Rule Designer: Updated interface with better usability |
| Decision Center: Basic | Decision Center: Enhanced with more governance features |
| Testing Tools: Basic | Testing Tools: Enhanced with better debugging |
| Integration: Limited | Integration: Better support for Cloud Pak for Business Automation |

## 8. Business Rules Vocabulary

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Vocabulary Files: Basic structure | Vocabulary Files: Enhanced with better metadata |
| BOM Integration: Basic | BOM Integration: Improved with better type support |
| Language Support: Limited | Language Support: Enhanced multilingual capabilities |

## 9. Decision Service Features (9.5.x only)

ODM 9.5.x introduces several features not present in 8.x:

- **Decision Service Interface**: Standardized input/output interface
- **Enhanced Metadata**: Better support for business metadata
- **Improved Governance**: Better versioning and deployment control
- **REST API Generation**: Automatic OpenAPI specification generation
- **Enhanced Monitoring**: Better runtime monitoring capabilities

## 10. File Format Reference

### Project File (.project)

**ODM 8.x Format**:
```xml
<projectDescription>
  <name>rule-project</name>
  <buildSpec>
    <buildCommand>
      <name>ilog.rules.studio.model.ruleBuilder</name>
    </buildCommand>
  </buildSpec>
  <natures>
    <nature>ilog.rules.studio.model.ruleProjectNature</nature>
  </natures>
</projectDescription>
```

**ODM 9.5.x Format**:
```xml
<projectDescription>
  <name>rule-project</name>
  <buildSpec>
    <buildCommand>
      <name>com.ibm.rules.studio.model.decisionservice.ui.decisionServiceBuilder</name>
    </buildCommand>
    <buildCommand>
      <name>com.ibm.rules.studio.model.decisionservice.ui.decisionServiceProjectBuilder</name>
    </buildCommand>
  </buildSpec>
  <natures>
    <nature>com.ibm.rules.studio.model.decisionservice.ui.decisionServiceNature</nature>
    <nature>com.ibm.rules.studio.model.decisionservice.ui.decisionServiceProjectNature</nature>
  </natures>
</projectDescription>
```

### Rule Project File (.ruleproject)

**ODM 8.x Format**:
```xml
<ilog.rules.studio.model.base:RuleProject xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore"
    buildMode="DecisionEngine" isADecisionService="false">
  <!-- Project properties -->
</ilog.rules.studio.model.base:RuleProject>
```

**ODM 9.5.x Format**:
```xml
<ilog.rules.studio.model.base:RuleProject xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore" 
    xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore"
    buildMode="DecisionEngine" isADecisionService="true" migrationFlag="3">
  <!-- Project properties -->
</ilog.rules.studio.model.base:RuleProject>
```

### Facet Configuration

**ODM 8.x Format**:
```xml
<faceted-project>
  <runtime name="IBM Operational Decision Manager 8.10.0"/>
  <fixed facet="ilog.rules.studio.model.rulesfacet"/>
  <installed facet="ilog.rules.studio.model.rulesfacet" version="8.10.0"/>
</faceted-project>
```

**ODM 9.5.x Format**:
```xml
<faceted-project>
  <runtime name="IBM Operational Decision Manager 9.5"/>
  <fixed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet"/>
  <installed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet" version="9.5"/>
</faceted-project>
```

## 11. Decision Center Integration

| ODM 8.x | ODM 9.5.x |
|---------|-----------|
| Basic integration | Enhanced integration with improved governance |
| Limited versioning | Better versioning and lifecycle management |
| Basic rule repository | Enhanced rule repository with better search |
| Limited user management | Improved user management and access control |

## Conclusion

ODM 9.5.x represents a significant evolution from ODM 8.x, with a shift toward the Decision Service paradigm. The primary changes involve XML namespace updates, project structure reorganization, and enhanced metadata support. While business rule logic remains largely compatible, the project infrastructure requires significant updates during migration.
