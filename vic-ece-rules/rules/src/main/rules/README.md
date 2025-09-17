# Victorian ECE Funding Rules Project

This folder contains the IBM ODM rules implementation for the Victorian Early Childhood Education (ECE) funding eligibility and calculation rules.

## Project Structure

```
/funding
├── eligibility
│   ├── brl             # IBM ODM Business Rule Language files
│   ├── flows           # Ruleflows for eligibility determination
│   ├── tables          # Decision tables for eligibility criteria
│   ├── criteria        # Rule categories for specific eligibility criteria
│   └── validation      # Input validation rules
└── calculation         # Future location for funding calculation rules
```

## Rule Components

### Eligibility Determination

1. **Flow Control Rules**
   - `Flow_Control_Rules.brl` - Controls overall flow of rule execution including initialization and output

2. **Validation Rules**
   - `Input_Validation_Rules.brl` - Validates request structure and required fields

3. **Age Eligibility Rules**
   - `Age_Eligibility_Rules.brl` - Implements age-based eligibility for different programs

4. **Service Eligibility Rules**
   - `Service_Eligibility_Rules.brl` - Checks service provider and service site eligibility

5. **Program Specific Rules**
   - `Program_Specific_Rules.brl` - Program-specific eligibility and hour requirements

6. **Supplement Eligibility Rules**
   - `Supplement_Eligibility_Rules.brl` - Rules for various funding supplements

7. **Decision Tables**
   - `Supplement_Eligibility_Table.dta` - Criteria for supplement eligibility
   - `Hours_Entitlement_Table.dta` - Standard hours entitlement by program

8. **Ruleflows**
   - `Funding_Eligibility_Ruleflow.rfl` - Main eligibility determination flow

## Execution Flow

The eligibility determination follows this sequence:

1. **Initialization** - Create response object
2. **Input Validation** - Check required inputs
3. **Age Eligibility** - Check age against program requirements
4. **Service Eligibility** - Verify service provider approval
5. **Program Rules** - Apply program-specific criteria
6. **Supplement Eligibility** - Determine eligibility for additional funding
7. **Output Preparation** - Finalize response with results

## Decision Object

The determination results are captured in the `FundingDetermination` object, which contains:

- Program eligibility status
- Supplement eligibility status
- Approved hours and weeks
- Evidence references
- Policy references
- Effective date range

## IBM ODM Specific Patterns

1. **Rule Tasks** - Rules are organized into logical tasks that match the ruleflow
2. **Decision Tables** - Used for tabular decision logic with versioning by date range
3. **Rule Properties** - Use "task" property to associate rules with ruleflow tasks
4. **XOM Access** - Rules use getter/setter methods on domain objects

## Configuration

Configuration data is externalized in decision tables to support:

- Program hour entitlements by year
- Supplement eligibility criteria
- Policy references and validation

## Testing

Unit tests should be created to validate rule execution using:

1. Sample eligibility requests with different scenarios
2. Age boundary test cases
3. Service eligibility variations
4. Supplement combination tests
