# ODM 9.x Decision Service Project Structure

This document outlines the structure of an ODM 9.x Decision Service project that we'll create as part of the migration process.

## Project File Structure

```
vic-ece-rules-9x/
├── META-INF/
│   └── MANIFEST.MF                   # Updated manifest for ODM 9.x
├── .settings/
│   ├── org.eclipse.core.resources.prefs
│   └── org.eclipse.wst.common.project.facet.core.xml   # Updated facets
├── .project                          # Project file with DS facets
├── rules/
│   └── funding/
│       ├── eligibility/
│       │   ├── age/                  # Age eligibility rules
│       │   ├── program/              # Program eligibility rules
│       │   ├── service/              # Service eligibility rules
│       │   ├── supplements/          # Supplement eligibility rules
│       │   └── flows/               
│       │       └── Main_Eligibility_Flow.rfl  # New format ruleflow
│       └── calculation/
│           ├── base/                 # Base calculation rules
│           ├── kfs/                  # KFS-specific calculations
│           ├── supplements/          # Supplement calculations
│           └── flows/
│               └── Main_Calculation_Flow.rfl  # New format ruleflow
├── bom/
│   └── funding.bom                   # Updated BOM definition
├── output/                           # Compiler output directory
├── resources/
│   └── vocabulary/
│       └── funding_en.voc            # Updated vocabulary
└── deployment/
    ├── ECEEligibilityServiceOperation.dop  # Updated deployment operation
    ├── ECECalculationServiceOperation.dop  # Updated deployment operation
    ├── Development.dep               # Updated deployment descriptor
    └── Production.dep                # Updated deployment descriptor
```

## Key Structure Differences

### 1. Decision Service Project Type

The ODM 9.x project uses the Decision Service project type which is indicated in the `.project` file:

```xml
<projectDescription>
  <name>vic-ece-rules-9x</name>
  <comment></comment>
  <projects>
    <project>vic-ece-xom</project>
  </projects>
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

### 2. Facet Configuration

Decision Service projects require specific facet configuration in `.settings/org.eclipse.wst.common.project.facet.core.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<faceted-project>
  <runtime name="IBM Operational Decision Manager 9.5"/>
  <fixed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet"/>
  <installed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet" version="9.5"/>
</faceted-project>
```

### 3. Ruleflow Structure

The ruleflow files in ODM 9.x use a different XML structure:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<com.ibm.rules.studio.model.decisionservice:Ruleflow xmi:version="2.0" 
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore" 
    xmlns:ilog.rules.studio.model.ruleflow="http://ilog.rules.studio/model/ruleflow.ecore">
  <name>Main_Eligibility_Flow</name>
  <uuid>23fb0b10-349d-4785-9de7-75bca489a6f2</uuid>
  <locale>en_US</locale>
  <categories>funding</categories>
  <rfModel>
    <!-- Rule nodes will go here -->
  </rfModel>
</com.ibm.rules.studio.model.decisionservice:Ruleflow>
```

### 4. Deployment Operation Structure

The deployment operations in ODM 9.x use updated XML structure:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<com.ibm.rules.studio.model.decisionservice:Operation xmi:version="2.0" 
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore">
  <name>ECEEligibilityServiceOperation</name>
  <uuid>33a64099-9f21-4b28-9d24-b61f1ae28e50</uuid>
  <rulesetName>ECEEligibilityRuleset</rulesetName>
  <targetRuleProjectName>vic-ece-rules-9x</targetRuleProjectName>
  <ruleflow href="../rules/funding/eligibility/flows/Main_Eligibility_Flow.rfl#23fb0b10-349d-4785-9de7-75bca489a6f2"/>
  <parameters name="request"/>
  <parameters name="response" type="funding.EligibilityResponse" direction="OUT"/>
</com.ibm.rules.studio.model.decisionservice:Operation>
```

## Execution Object Model (XOM) References

The XOM references in ODM 9.x use updated XML format in the `.ruleproject` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ilog.rules.studio.model.base:RuleProject xmi:version="2.0" 
    xmlns:xmi="http://www.omg.org/XMI" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:com.ibm.rules.studio.model.decisionservice="http://com.ibm.rules.studio/model/decisionservice.ecore" 
    xmlns:ilog.rules.studio.model.base="http://ilog.rules.studio/model/base.ecore" 
    xmlns:ilog.rules.studio.model.bom="http://ilog.rules.studio/model/bom.ecore" 
    xmlns:ilog.rules.studio.model.xom="http://ilog.rules.studio/model/xom.ecore" 
    buildMode="DecisionEngine" isADecisionService="true" migrationFlag="3">
  <name>vic-ece-rules-9x</name>
  <uuid>e572f3ca-9b12-4c08-a3de-0c5742d8b4e7</uuid>
  <outputLocation>output</outputLocation>
  <categories>any</categories>
  <paths xsi:type="ilog.rules.studio.model.xom:XOMPath" pathID="XOM">
    <entries xsi:type="ilog.rules.studio.model.xom:LibraryXOMPathEntry" name="org.eclipse.jdt.launching.JRE_CONTAINER" url="file:org.eclipse.jdt.launching.JRE_CONTAINER" kind="LIBRARY"/>
    <entries xsi:type="ilog.rules.studio.model.xom:JavaProjectXOMPathEntry" name="vic-ece-xom" url="platform:/vic-ece-xom" kind="JAVA_PROJECT"/>
  </paths>
  <paths xsi:type="ilog.rules.studio.model.bom:BOMPath" pathID="BOM">
    <entries xsi:type="ilog.rules.studio.model.bom:BOMEntry" name="funding" url="platform:/vic-ece-rules-9x/bom/funding.bom"/>
  </paths>
  <modelFolders xsi:type="ilog.rules.studio.model.base:SourceFolder">
    <name>rules</name>
  </modelFolders>
  <modelFolders xsi:type="ilog.rules.studio.model.base:ResourceFolder">
    <name>resources</name>
  </modelFolders>
  <modelFolders xsi:type="ilog.rules.studio.model.bom:BOMFolder">
    <name>bom</name>
  </modelFolders>
  <modelFolders xsi:type="com.ibm.rules.studio.model.decisionservice:OperationFolder">
    <name>deployment</name>
  </modelFolders>
</ilog.rules.studio.model.base:RuleProject>
```

## Migration Notes

Key differences to note when creating this new project structure:

1. The `JavaProjectXOMPathEntry` type replaces `SystemXOMPathEntry` for XOM references
2. The `migrationFlag="3"` attribute indicates an ODM 9.x compatible project
3. The Decision Service nature must be included in the project file
4. The facet configuration must specify ODM 9.x runtime
5. XML namespaces are updated to use the newer ODM 9.x format
