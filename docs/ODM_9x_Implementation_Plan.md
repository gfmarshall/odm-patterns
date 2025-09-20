# ODM 9.x Migration Implementation Plan

## Implementation Steps

This document outlines the specific implementation steps we'll take to migrate from ODM 8.x to 9.5.x on the `odm-9.x` branch.

### Phase 1: Project Structure Setup

1. Create the basic project directory structure for `vic-ece-rules-9x`:
   ```
   mkdir -p vic-ece-rules-9x/META-INF
   mkdir -p vic-ece-rules-9x/.settings
   mkdir -p vic-ece-rules-9x/rules/funding/eligibility/age
   mkdir -p vic-ece-rules-9x/rules/funding/eligibility/program
   mkdir -p vic-ece-rules-9x/rules/funding/eligibility/service
   mkdir -p vic-ece-rules-9x/rules/funding/eligibility/supplements
   mkdir -p vic-ece-rules-9x/rules/funding/eligibility/flows
   mkdir -p vic-ece-rules-9x/rules/funding/calculation/base
   mkdir -p vic-ece-rules-9x/rules/funding/calculation/kfs
   mkdir -p vic-ece-rules-9x/rules/funding/calculation/supplements
   mkdir -p vic-ece-rules-9x/rules/funding/calculation/flows
   mkdir -p vic-ece-rules-9x/bom
   mkdir -p vic-ece-rules-9x/output
   mkdir -p vic-ece-rules-9x/resources/vocabulary
   mkdir -p vic-ece-rules-9x/deployment
   ```

2. Create the `.project` file with Decision Service facets:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
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

3. Create the Eclipse settings files:
   ```xml
   <!-- .settings/org.eclipse.wst.common.project.facet.core.xml -->
   <?xml version="1.0" encoding="UTF-8"?>
   <faceted-project>
     <runtime name="IBM Operational Decision Manager 9.5"/>
     <fixed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet"/>
     <installed facet="com.ibm.rules.studio.model.decisionservice.ui.decisionservicefacet" version="9.5"/>
   </faceted-project>
   ```

4. Create the updated `MANIFEST.MF` file:
   ```
   Manifest-Version: 1.0
   Bundle-ManifestVersion: 2
   Bundle-Name: VicEceFundingRules
   Bundle-SymbolicName: vic-ece-rules-9x
   Bundle-Version: 1.0.0
   Bundle-RequiredExecutionEnvironment: JavaSE-1.8
   Bundle-Vendor: IBM
   Require-Bundle: vic-ece-xom;bundle-version="1.0.0"
   ```

5. Create the updated `.ruleproject` file:
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

### Phase 2: Rule Migration

1. Copy and update business rule files (.brl):
   - Copy each `.brl` file from the original project to the new structure
   - Update XML namespaces in each file to use the ODM 9.x format
   - Example command for copying and initial XML namespace update:
     ```bash
     for file in $(find vic-ece-rules/rules -name "*.brl"); do
       newpath=$(echo $file | sed 's/vic-ece-rules/vic-ece-rules-9x/')
       mkdir -p $(dirname $newpath)
       cp $file $newpath
       # XML namespace updates would need to be done with proper XML tools
     done
     ```

2. Create new ruleflow files to replace .ruleflow files:
   - For each `.ruleflow` file in the original project:
     - Create a corresponding `.rfl` file in the new project
     - Use the ODM 9.x format for the XML structure
     - Recreate each node, connection, and decision point

### Phase 3: BOM and Deployment Migration

1. Migrate the BOM file:
   ```bash
   cp vic-ece-rules/bom/funding.bom vic-ece-rules-9x/bom/
   # Update internal references in the BOM file
   ```

2. Update vocabulary files:
   ```bash
   cp vic-ece-rules/rules/funding/funding_en.voc vic-ece-rules-9x/resources/vocabulary/
   # Update any internal references in the vocabulary file
   ```

3. Create updated deployment operations:
   - Create new `.dop` files in the ODM 9.x format
   - Update ruleflow references to the new paths and formats
   - Example for `ECEEligibilityServiceOperation.dop`:
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

4. Update deployment configuration files:
   - Create new `.dep` files in the ODM 9.x format
   - Update ruleset references to match the new structure

### Phase 4: Testing and Validation

1. Test ruleflow execution:
   - Create test cases to verify rule execution
   - Compare results between ODM 8.x and 9.x versions
   - Test each rule path to ensure it works as expected

2. Validate deployment:
   - Test deployment package creation
   - Verify ruleset execution in ODM runtime

### Phase 5: Documentation and Finalization

1. Document any issues encountered during migration
2. Create a summary of changes made
3. Update project documentation with new information

## Implementation Timeline

| Phase | Estimated Duration | Dependencies |
|-------|-------------------|--------------|
| Project Structure Setup | 1-2 days | None |
| Rule Migration | 2-3 days | Phase 1 |
| BOM and Deployment Migration | 1-2 days | Phase 2 |
| Testing and Validation | 2-3 days | Phase 3 |
| Documentation and Finalization | 1 day | Phase 4 |

## Special Considerations

1. **XML Namespace Updates**: Most XML updates will require careful editing to maintain attributes and structure while only changing namespaces.

2. **Ruleflow Recreation**: Ruleflow files cannot be automatically converted; they must be recreated manually in the new format.

3. **BOM Compatibility**: The BOM may require updates to be compatible with ODM 9.x rulesets.

4. **Testing Approach**: Comprehensive testing is essential to verify that the migration has preserved all business logic.

## Rollback Plan

If issues are encountered during migration:
1. Document the specific issue
2. Return to the `odm-8.x` branch for reference
3. Attempt to resolve the issue based on ODM 9.x documentation
4. If unresolvable, consider maintaining both versions for a period during transition
