# ODM Migration Guide: 8.x to 9.5.x

## Current Project Structure (ODM 8.x)

This document describes the current ODM 8.x "Classic Rule Project" structure and outlines the steps needed to migrate to ODM 9.5.x.

### Project Components

The current project has the following key components:

#### 1. Project Structure
- **Rule Project**: `vic-ece-rules` (Classic Rule Project format)
- **XOM Project**: `vic-ece-xom` (Java project containing execution object model)
- **Test Components**: `vic-ece-test` (Test cases and validation)

#### 2. Rule Assets
- **Business Rules**: `.brl` files in the `rules/funding` directory hierarchy
- **Decision Tables**: Referenced in the code but not directly visible in file extensions
- **Ruleflows**: `.ruleflow` files in the `ruleflows` directory
- **BOM Files**: `.bom` files in the `bom` directory
- **Vocabulary**: `.voc` files like `funding_en.voc`

#### 3. Deployment Assets
- **Deployment Operations**: `.dop` files in the `deployment` directory
- **Deployment Configurations**: `.dep` files in the `deployment` directory

#### 4. XOM Structure
- Java classes implementing the execution object model
- Business domain objects following the guidelines in the workspace rules

### Technical Details

#### XML Namespace Issues
The current project uses XML namespaces from ODM 8.x, which are incompatible with ODM 9.5.x:

```xml
<ilog.rules.studio.model.ruleflow:RuleFlow xmi:version="2.0" 
  xmlns:xmi="http://www.omg.org/XMI" 
  xmlns:ilog.rules.studio.model.ruleflow="http://ilog.rules.studio/model/ruleflow.ecore">
```

#### File Format Differences
- Ruleflow files use `.ruleflow` extension instead of `.rfl` in newer versions
- Rule task references use older path formats
- BOM references use older path notation

#### Deployment Configuration
Deployment operations reference ruleflow paths that use a format incompatible with ODM 9.5.x:
```xml
<ruleflow href="../rules/funding/eligibility/flows/Main_Eligibility_Flow.rfl#23fb0b10-349d-4785-9de7-75bca489a6f2"/>
```

## Migration Branches

We've created two git branches to manage the migration:

1. `odm-8.x` - Preserves the original project in ODM 8.x format
2. `odm-9.x` - Will contain the migrated project in ODM 9.5.x format

## Migration Steps

### 1. Project Structure Migration

#### ODM 9.5.x Decision Service Project Structure

To migrate to ODM 9.5.x, we need to create a new project structure using the Decision Service format instead of the Classic Rule Project format:

1. **Create new Decision Service project**
   - Create a new project with the same name (`vic-ece-rules`) but with Decision Service structure
   - Update the `.project` file with proper facet configurations
   - Update the project properties file to specify ODM 9.5.x compatibility

2. **Update XOM references**
   - Replace `SystemXOMPathEntry` references with newer format
   - Update path references to the XOM project

3. **Update folder structure**
   - Decision Service projects use a slightly different folder structure
   - Migrate existing folders to match the new structure

### 2. Business Rule Migration

#### Business Rule Files (.brl)

Business rule files need minimal changes but must be updated for namespace compatibility:

1. **Update XML namespaces**
   - Replace older namespaces with ODM 9.5.x compatible ones
   - Update rule structure to use newer attribute formats

2. **Update rule references**
   - Modify any references to other rules to use the new path format
   - Update any embedded conditions or actions to match newer syntax

3. **Verify rule logic**
   - Most rule logic should remain identical
   - Check for any deprecated functions or methods

#### Decision Tables

Decision tables require more substantial changes:

1. **Create new decision tables**
   - Export data from existing tables
   - Create new decision tables in ODM 9.5.x format
   - Import the data into the new format

2. **Update column definitions**
   - Recreate column definitions with updated references
   - Preserve business metadata (descriptions, etc.)

3. **Update rule conditions and actions**
   - Recreate conditions and actions in the 9.5.x syntax
   - Verify logical equivalence

### 3. Ruleflow Migration

#### Converting .ruleflow Files to .rfl Format

Ruleflow files require complete recreation:

1. **Create new ruleflow files**
   - Create new .rfl files in the ODM 9.5.x format
   - Do not attempt to directly convert existing .ruleflow files

2. **Recreate flow structure**
   - Recreate each node in the flow with the same structure
   - Update task references to point to the correct rule artifacts
   - Recreate connections between nodes

3. **Update node properties**
   - Configure execution mode, priorities, and other settings
   - Update any custom properties or metadata

4. **Update flow logic**
   - Recreate decision points with the same conditions
   - Verify flow paths match the original design

### 4. Deployment Configuration Migration

#### Updating Deployment Artifacts

1. **Create new deployment operations**
   - Create new .dop files in the ODM 9.5.x format
   - Update ruleflow references to the new paths and formats

2. **Update deployment configurations**
   - Recreate deployment configurations with updated references
   - Update ruleset names and properties

3. **Update rule application configurations**
   - Update any RuleApp configurations to use the new format
   - Update version information and dependencies

### 5. Testing and Validation

1. **Create comparison tests**
   - Create test cases to compare output between ODM 8.x and 9.5.x versions
   - Verify identical results for the same inputs

2. **Execution performance testing**
   - Compare rule execution performance
   - Identify any optimization opportunities

3. **Integration testing**
   - Verify that the migrated rules work correctly in the target environment
   - Test deployment and execution

## ODM 8.x vs. 9.5.x Key Differences

### Architecture Changes
- ODM 9.x introduced Decision Services as the primary project type
- Rule Designer project replaces Classic Rule Project
- More focus on RESTful service interfaces

### XML Structure Changes
- Updated XML namespaces for all rule artifacts
- More standardized attribute naming
- Enhanced metadata support

### Rule Execution Changes
- Improved rule engine performance
- Enhanced decision service capabilities
- Better support for complex decision logic

### Tooling Changes
- Updated Rule Designer interface
- Enhanced testing capabilities
- Improved deployment options

## Conclusion

This migration guide provides a structured approach to convert the Victoria ECE Funding Rules project from ODM 8.x Classic Rule Project format to ODM 9.5.x Decision Service format. By following these steps, we can ensure a successful migration while preserving all business logic and rule behavior.
