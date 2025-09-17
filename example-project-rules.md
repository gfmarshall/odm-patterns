# ODM Project Guidelines: Example Project

## Java XOM Development
ID | Category | Rule
--- | --- | ---
PRJ.XOM.001 | XOM Architecture | All XOM classes must implement java.io.Serializable to support rule engine persistence.
PRJ.XOM.002 | XOM Architecture | XOM classes must include serialVersionUID to ensure deserialization compatibility across versions.
PRJ.XOM.003 | XOM Design | Use immutable value objects with builder patterns for thread safety in rule execution.
PRJ.XOM.004 | XOM Design | Externalize all configuration data (rates, eligibility criteria) to decision tables, not Java code.
PRJ.XOM.005 | XOM Implementation | Create reusable domain model classes that map directly to business concepts.
PRJ.XOM.006 | XOM Implementation | Implement proper inheritance hierarchies for extensibility (e.g., Person → Child, Guardian).
PRJ.XOM.007 | XOM Structure | Organize XOM in a src/main/java directory structure with proper package naming.
PRJ.XOM.008 | XOM Versioning | Use semantic versioning (MAJOR.MINOR.PATCH) for XOM releases.
PRJ.XOM.009 | XOM Testing | Provide unit tests for all business logic in the XOM.
PRJ.XOM.010 | XOM Packaging | Package the XOM as a JAR artifact for reuse across rule projects.
PRJ.XOM.011 | XOM Compatibility | All XOM classes must be Java 8 compatible to ensure wide platform support.
PRJ.XOM.012 | XOM Naming | Use short package names for XOM classes (e.g., "funding" instead of lengthy hierarchical namespaces).

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

## Decision Object Design
ID | Category | Rule
--- | --- | ---
PRJ.DO.001 | Decision Objects | Each rule project must define clear domain-specific determination objects (e.g., `FundingDetermination`).
PRJ.DO.002 | Decision Objects | Determination objects must contain both eligibility results and calculation components when applicable.
PRJ.DO.003 | Decision Objects | Determination objects must include audit data (evidence references, policy references, timestamps).
PRJ.DO.004 | Decision Objects | Determination objects must follow a consistent naming pattern: `[Domain]Determination`.
PRJ.DO.005 | Decision Objects | Determination objects must be serializable to support persistence between rule executions.

## Request-Response Design
ID | Category | Rule
--- | --- | ---
PRJ.RR.001 | Request-Response | Each rule service must have corresponding request and response classes following `[Purpose]Request/Response` naming.
PRJ.RR.002 | Request-Response | Request classes must contain all inputs needed for rule execution with no external dependencies.
PRJ.RR.003 | Request-Response | Response classes must wrap determination objects with metadata (status, errors, processing info).
PRJ.RR.004 | Request-Response | Multi-phase decisions must use separate request/response pairs for each phase (e.g., eligibility vs. calculation).
PRJ.RR.005 | Request-Response | Request and response classes must implement serialization and trace ID propagation.

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
