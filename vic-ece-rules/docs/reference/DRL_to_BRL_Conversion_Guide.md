# Drools Rule (DRL) to Business Rule Language (BRL) Conversion Guide

This document outlines the process for converting Drools Rule Language (.drl) files to Business Rule Language (BRL) format to align with IBM ODM best practices and the miniloan implementation approach.

## Rule Conversion Steps

### 1. Identify Rule Components

For each Drools rule in a .drl file:

- **Rule Name**: The name in quotes after `rule`
- **Conditions**: Everything in the `when` section
- **Actions**: Everything in the `then` section

### 2. Create BRL File Structure

Create an XML file with the following structure:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ilog.rules.studio.model.brl:ActionRule xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:ilog.rules.studio.model.brl="http://ilog.rules.studio/model/brl.ecore">
  <n>Rule Name</n>
  <uuid>generate-a-unique-uuid</uuid>
  <locale>en_US</locale>
  <definition><![CDATA[if
    condition1
    and condition2
then
    action1 ;
    action2 ;]]></definition>
</ilog.rules.studio.model.brl:ActionRule>
```

### 3. Convert Conditions

Convert Drools conditions to natural language:

| Drools Syntax | BRL Natural Language |
|---------------|---------------------|
| `$request : EligibilityRequest(child != null)` | `'the eligibility request' is defined and the child from 'the eligibility request' is defined` |
| `$child : Child() from $request.getChild()` | _(handled in above condition)_ |
| `eval(calculateAgeOnReferenceDate(...) >= 3)` | `the child's age at the reference date of 'the eligibility request' is at least 3` |

### 4. Convert Actions

Convert Drools actions to natural language:

| Drools Syntax | BRL Natural Language |
|---------------|---------------------|
| `EligibilityResult result = new EligibilityResult(...)` | `create a new eligibility result with program type "..." and status ...` |
| `result.addReason("...")` | `add reason "..." to 'the eligibility result'` |
| `$determination.setBaseProgramType(...)` | `set the base program type of 'the funding determination' to ...` |

### 5. Remove Technical Elements

- Remove all `ruleflow-group` declarations
- Remove `no-loop` and other technical directives
- Remove variable bindings (`$variable : Type()`)
- Replace helper functions with vocabulary expressions
- Remove `System.out.println` statements

### 6. Save to Proper Location

Save the file in the appropriate folder structure:
- `/rules/src/main/rules/funding/eligibility/criteria/[category]/[rule_name].brl`

## Example Conversion

### Original DRL Rule:

```java
rule "Check Three Year Old Kindergarten Age Eligibility"
    no-loop true
    when
        $request : EligibilityRequest(child != null, fundingYear > 0)
        $child : Child() from $request.getChild()
        $determination : FundingDetermination(baseProgramType == BaseProgramType.THREE_YEAR_OLD_KINDER || baseProgramType == null)
        $response : EligibilityResponse(processingStatus == "IN_PROGRESS")
        
        eval(calculateAgeOnReferenceDate($child, $request.getProgramYear(), 4, 30) >= 3)
        eval(calculateAgeOnReferenceDate($child, $request.getProgramYear(), 4, 30) < 5)
    then
        EligibilityResult result = new EligibilityResult("THREE_YEAR_OLD_KINDER", EligibilityStatus.ELIGIBLE);
        result.addReason("Child will be between 3 and 4 years old on April 30 of the program year");
        result.addPolicyRef("AGE_ELIGIBILITY_POLICY_1.2");
        
        modify($determination) {
            setBaseProgramType(BaseProgramType.THREE_YEAR_OLD_KINDER),
            addEligibilityResult(result)
        }
```

### Converted BRL Rule:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ilog.rules.studio.model.brl:ActionRule xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:ilog.rules.studio.model.brl="http://ilog.rules.studio/model/brl.ecore">
  <n>Three Year Old Kindergarten Age Eligibility</n>
  <uuid>d7e45c3b-3f21-4c02-a5b8-9e75f23a7c48</uuid>
  <locale>en_US</locale>
  <definition><![CDATA[if
    'the eligibility request' is defined
    and the child from 'the eligibility request' is defined 
    and the child from 'the eligibility request' has date of birth
    and the reference date of 'the eligibility request' is known
    and the child's age at the reference date of 'the eligibility request' is at least 3
    and the child's age at the reference date of 'the eligibility request' is less than 5
    and the processing status of 'the eligibility response' is "IN_PROGRESS"
    and the base program type of 'the funding determination' is none or the base program type of 'the funding determination' is THREE_YEAR_OLD_KINDER
then
    create a new eligibility result with program type "THREE_YEAR_OLD_KINDER" and status ELIGIBLE ;
    add reason "Child will be between 3 and 4 years old on April 30 of the program year" to 'the eligibility result' ;
    add policy reference "AGE_ELIGIBILITY_POLICY_1.2" to 'the eligibility result' ;
    set the base program type of 'the funding determination' to THREE_YEAR_OLD_KINDER ;
    add 'the eligibility result' to 'the funding determination' ;]]></definition>
</ilog.rules.studio.model.brl:ActionRule>
```

## Remaining DRL Files to Convert

1. **Service_Eligibility_Rules.drl** → Convert to individual .brl files in `/criteria/service/` folder
2. **Program_Specific_Rules.drl** → Convert to individual .brl files in `/criteria/program/` folder
3. **Supplement_Eligibility_Rules.drl** → Convert to individual .brl files in `/criteria/supplements/` folder
4. **Funding_Eligibility_Ruleflow.drl** → Adjust ruleflow to use the updated hierarchical packages
5. **Input_Validation_Rules.drl** → Convert to individual .brl files in `/validation/` folder

## Vocabulary Considerations

Ensure the vocabulary (.voc) file includes all the natural language phrases used in the BRL rules. Check that the following categories of phrases are defined:

1. **Navigation phrases** - `{property} of {this}`
2. **Condition phrases** - `{this} is {value}`, `{this} has {property}`
3. **Action phrases** - `add {value} to {this}`, `set the {property} of {this} to {value}`
