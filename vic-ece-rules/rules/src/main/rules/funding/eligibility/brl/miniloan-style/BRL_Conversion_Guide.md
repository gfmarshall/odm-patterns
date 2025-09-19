# Converting to Miniloan-style BRL Rules

This guide outlines how to convert the existing vic-ece rules to match the miniloan BRL format, ensuring alignment with the ODM workspace rules.

## BRL Structure

### Miniloan Format (Target)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ilog.rules.studio.model.brl:ActionRule xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:ilog.rules.studio.model.brl="http://ilog.rules.studio/model/brl.ecore">
  <n>Rule Name</n>
  <uuid>generated-uuid</uuid>
  <locale>en_US</locale>
  <definition><![CDATA[if
    condition 1
    and condition 2
  then
    action 1;
    action 2;]]></definition>
</ilog.rules.studio.model.brl:ActionRule>
```

### Vic-ECE Format (Current)
```java
rule "Rule Name" {
    property "task" = "property-value"
    when {
        // conditions
    } then {
        // actions
    }
}
```

## Conversion Steps

1. **Create individual files** - Split each rule from the ruleset into its own file
2. **Convert to XML format** - Use the XML template with CDATA section
3. **Convert conditions** - Transform technical conditions to natural language
4. **Convert actions** - Transform technical actions to natural language
5. **Remove technical elements** - Remove variable bindings and Java-specific code

## Natural Language Transformation

### Conditions
| Current Format | Miniloan Format |
| --- | --- |
| `$request: EligibilityRequest()` | `the eligibility request` |
| `$child: Child() from $request.getChild()` | `the child from 'the eligibility request'` |
| `eval(calculateAgeOnReferenceDate($child, $request.getProgramYear(), 4, 30) >= 3)` | `the child's age on April 30 of the program year is at least 3` |

### Actions
| Current Format | Miniloan Format |
| --- | --- |
| `EligibilityResult result = new EligibilityResult("THREE_YEAR_OLD_KINDER", EligibilityStatus.ELIGIBLE)` | `create a new eligibility result with program type "THREE_YEAR_OLD_KINDER" and status ELIGIBLE` |
| `result.addReason("Child will be...")` | `add reason "Child will be..." to 'the eligibility result'` |
| `$determination.setBaseProgramType(BaseProgramType.THREE_YEAR_OLD_KINDER)` | `set the base program type of 'the funding determination' to THREE_YEAR_OLD_KINDER` |

## Vocabulary Requirements

To support natural language rules, you need a vocabulary file (.voc) with phrases for your domain objects:

```
# funding.Child
funding.Child#concept.label = child
funding.Child.dateOfBirth#phrase.navigation = {date of birth} of {this}
funding.Child.getAgeAt(java.time.LocalDate)#phrase.navigation = {this}'s age on {0}

# funding.EligibilityRequest
funding.EligibilityRequest#concept.label = eligibility request
funding.EligibilityRequest.getChild()#phrase.navigation = {child} from {this}
funding.EligibilityRequest.programYear#phrase.navigation = {program year} of {this}
```

## Rule Example

### Before (Current Format)
```java
rule "Check Three Year Old Kindergarten Age Eligibility" {
    property "task" = "funding-eligibility-age-check"
    when {
        $request: EligibilityRequest();
        $child: Child() from $request.getChild();
        $determination: FundingDetermination($determination.getBaseProgramType() == BaseProgramType.THREE_YEAR_OLD_KINDER || 
                                     $determination.getBaseProgramType() == null);
        $response: EligibilityResponse($response.getProcessingStatus().equals("IN_PROGRESS"));
        
        // Age calculation - must be between 3 and 5
        eval(calculateAgeOnReferenceDate($child, $request.getProgramYear(), 4, 30) >= 3);
        eval(calculateAgeOnReferenceDate($child, $request.getProgramYear(), 4, 30) < 5);
    } then {
        // Create eligibility result
        EligibilityResult result = new EligibilityResult("THREE_YEAR_OLD_KINDER", EligibilityStatus.ELIGIBLE);
        result.addReason("Child will be between 3 and 4 years old on April 30 of the program year");
        result.addPolicyRef("AGE_ELIGIBILITY_POLICY_1.2");
        
        // Update determination
        $determination.setBaseProgramType(BaseProgramType.THREE_YEAR_OLD_KINDER);
        $determination.addEligibilityResult(result);
    }
}
```

### After (Miniloan Format)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ilog.rules.studio.model.brl:ActionRule xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:ilog.rules.studio.model.brl="http://ilog.rules.studio/model/brl.ecore">
  <n>Three Year Old Kindergarten Age Eligibility</n>
  <uuid>5e2a9c54-ca36-4b71-8de4-a9b72fc89e14</uuid>
  <locale>en_US</locale>
  <definition><![CDATA[if
    the eligibility request has program year of the funding year
    and the child from 'the eligibility request' has date of birth such that
    the child's age on April 30 of the program year is at least 3
    and the child's age on April 30 of the program year is less than 5
    and the processing status of 'the eligibility response' is "IN_PROGRESS"
then
    create a new eligibility result with program type "THREE_YEAR_OLD_KINDER" and status ELIGIBLE ;
    add reason "Child will be between 3 and 4 years old on April 30 of the program year" to 'the eligibility result' ;
    add policy reference "AGE_ELIGIBILITY_POLICY_1.2" to 'the eligibility result' ;
    set the base program type of 'the funding determination' to THREE_YEAR_OLD_KINDER ;
    add 'the eligibility result' to 'the funding determination' ;]]></definition>
</ilog.rules.studio.model.brl:ActionRule>
```
