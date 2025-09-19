# Best Practices for ODM BRL Rule Authoring

This document outlines best practices for authoring business rules in IBM ODM Rule Designer using the Business Rule Language (BRL) format, aligning with the guidelines in `odm-workspace-rules.md`.

## Rule Structure

1. **One Rule Per File**
   - Each rule should be in its own separate .brl file
   - File name should match rule name in lowercase with underscores (e.g., `minimum_credit_score.brl`)

2. **XML Structure**
   - Use standard XML header: `<?xml version="1.0" encoding="UTF-8"?>`
   - Use the `ilog.rules.studio.model.brl:ActionRule` element as the root
   - Include a unique UUID for each rule
   - Specify locale (typically `en_US`)
   - Wrap rule content in CDATA section

3. **Rule Naming**
   - Use clear, business-oriented names that describe the rule's purpose
   - Example: "Three Year Old Kindergarten Age Eligibility"
   - Avoid technical terms or implementation details in names

## Rule Content

1. **Natural Language**
   - Write rules in business-friendly natural language
   - Avoid technical syntax (no `$variable` references or Java code)
   - Reference business objects with friendly terms (e.g., 'the child' instead of '$child')

2. **Condition Structure**
   - Start with "if" followed by conditions
   - Connect conditions with "and" or "or" keywords
   - Use simple, declarative statements
   - Place each condition on its own line for readability

3. **Action Structure**
   - Start with "then" followed by actions
   - End each action with a semicolon
   - Use verbs to describe actions (create, add, set)
   - Reference objects with consistent terms

4. **Business Concepts**
   - Use consistent business terminology throughout
   - Reference business policy numbers where applicable
   - Structure conditions to mirror how business users think

## Vocabulary Support

1. **Maintain Vocabulary Files**
   - Create and update .voc files for all domain objects
   - Define navigation phrases for properties and methods
   - Define action phrases for modifying operations
   - Use concept labels to provide friendly terms

2. **Verbalization Patterns**
   - Define consistent patterns for common operations:
     - Navigation: `{property} of {this}`
     - Action: `set the {property} of {this} to {value}`
     - Condition: `{this} is {state}`

3. **Rule Metadata**
   - Include meaningful category and property metadata
   - Document rule purpose and business policy references
   - Maintain traceability to requirements

## Integration with Rule Designer

1. **Testing in Rule Designer**
   - Create BOM entries for all XOM classes
   - Define rule projects with proper structure
   - Use rule flow to organize rule execution
   - Test rules with diverse scenarios

2. **Rule Maintenance**
   - Keep rules focused on single business decisions
   - Maintain rule versioning with clear history
   - Document rule changes and rationale

## Example

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
