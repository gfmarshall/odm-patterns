# ODM Project Guidelines: Example Project

## Project Structure
ID | Category | Rule
--- | --- | ---
PRJ.STR.001 | Project Structure | Project must be organized into separate rule packages for different business domains.
PRJ.STR.002 | Project Structure | Common validation logic must be isolated in a shared package accessible to all rule packages.
PRJ.STR.003 | Project Structure | Each business domain must have its own dedicated rule package to allow independent maintenance.
PRJ.STR.004 | Project Structure | Test cases must be organized by domain with separate regression test suites.

## Naming Conventions
ID | Category | Rule
--- | --- | ---
PRJ.NAM.001 | Naming Conventions | Decision tables must follow naming pattern: `[Domain]_[Purpose]_Rules`.
PRJ.NAM.002 | Naming Conventions | Rule variables must use business terminology matching the official business documentation.
PRJ.NAM.003 | Naming Conventions | Data tables must include effective date in name: `[Domain]_Data_[YYYYMMDD]`.
PRJ.NAM.004 | Naming Conventions | All business terms must have standardized names across all rule artifacts.

## Decision Table Design
ID | Category | Rule
--- | --- | ---
PRJ.DT.001 | Decision Tables | All data tables must include columns for effective start date and effective end date.
PRJ.DT.002 | Decision Tables | Decision tables must include version tracking columns with author and approval information.
PRJ.DT.003 | Decision Tables | Each rule row must have a unique identifier that remains constant across versions.
PRJ.DT.004 | Decision Tables | Time-specific rules must be implemented as separate rows with appropriate date ranges, not by modifying existing rows.
PRJ.DT.005 | Decision Tables | When rule changes occur, the entire table must be versioned, with previous version archived but accessible.

## Validation Rules
ID | Category | Rule
--- | --- | ---
PRJ.VAL.001 | Validation Rules | Validation criteria must be separated from business logic in distinct decision tables.
PRJ.VAL.002 | Validation Rules | Each validation rule must provide a specific reason code when validation fails.
PRJ.VAL.003 | Validation Rules | Validation rules must be organized hierarchically from basic to complex criteria.

## Business Logic Rules
ID | Category | Rule
--- | --- | ---
PRJ.BLR.001 | Business Logic Rules | Each business logic component must be traceable to its source requirement or policy.
PRJ.BLR.002 | Business Logic Rules | Intermediate processing steps must be preserved for audit purposes.
PRJ.BLR.003 | Business Logic Rules | Rules must handle time-based overlaps with explicit precedence rules.
PRJ.BLR.004 | Business Logic Rules | Temporary exceptions must be clearly marked with their justification in metadata.
