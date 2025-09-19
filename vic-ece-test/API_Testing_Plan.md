# Victorian ECE Rules API Testing Plan

## Project Structure

```
vic-ece-test/
├── postman/
│   ├── collections/
│   │   └── ECE_Eligibility_Tests.postman_collection.json
│   └── environments/
│       ├── dev.postman_environment.json
│       ├── test.postman_environment.json
│       └── prod.postman_environment.json
├── test-data/
│   ├── eligibility/
│   │   ├── age_tests/
│   │   │   ├── child_under_3.json
│   │   │   ├── child_3_to_4.json
│   │   │   ├── child_4_to_5.json
│   │   │   └── child_over_6.json
│   │   ├── service_tests/
│   │   └── supplement_tests/
│   └── reference-data/
│       └── config_tables/
└── scripts/
    ├── run-tests.sh
    └── generate-report.js
```

## Test Case Design

### 1. Unit Tests

Test each rule in isolation with specific inputs to verify individual rule behavior.

#### Age Eligibility Tests:

| Test Case | Age | Expected Base Program | Expected Status | Notes |
|-----------|-----|----------------------|----------------|-------|
| Child under 3 | 2y 11m | None | INELIGIBLE | Testing minimum age boundary |
| Child exactly 3 | 3y 0m | THREE_YEAR_OLD_KINDER | ELIGIBLE | Testing exact age boundary |
| Child 3-4 | 3y 6m | THREE_YEAR_OLD_KINDER | ELIGIBLE | Middle of valid range |
| Child exactly 4 | 4y 0m | PRE_PREP | ELIGIBLE | Testing eligibility for both programs |
| Child 4-5 | 4y 6m | PRE_PREP | ELIGIBLE | Middle of valid range |
| Child exactly 6 | 6y 0m | None | INELIGIBLE | Testing maximum age boundary |

### 2. Integration Tests

Test multiple rules together to verify proper interaction and flow execution.

#### Full Eligibility Flow Tests:

| Test Case | Scenario | Expected Outcome |
|-----------|----------|-----------------|
| Standard 3yo Eligibility | 3yo child, standard service | THREE_YEAR_OLD_KINDER eligible |
| Indigenous Child Eligibility | 3yo Indigenous child | THREE_YEAR_OLD_KINDER eligible + ESK supplement |
| Service Provider Not Approved | 3yo child, unapproved provider | INELIGIBLE with appropriate reason |

### 3. Edge Case Tests

Test boundary conditions and unusual scenarios.

| Test Case | Scenario | Expected Outcome |
|-----------|----------|-----------------|
| Missing Date of Birth | Request with null DOB | Validation error response |
| Date Boundary | Child turning 3 on reference date | THREE_YEAR_OLD_KINDER eligible |
| Future Reference Date | Reference date set 6 months ahead | Age calculated based on future date |

## Postman Collection Structure

### Folders

1. **Setup**
   - Environment Validation
   - Authentication (if needed)

2. **Unit Tests**
   - Age Eligibility Tests
   - Service Eligibility Tests
   - Program Rules Tests
   - Supplement Eligibility Tests

3. **Integration Tests**
   - Full Eligibility Flow Tests

4. **Edge Cases**
   - Error Handling Tests
   - Boundary Condition Tests

### Test Structure

Each test should include:

1. Clear descriptive name (e.g., "Child age 3y2m - Should be eligible for 3YO Kinder")
2. Pre-request scripts for setup if needed
3. Request body with test-specific payload
4. Tests script to validate:
   - HTTP status code
   - Response structure
   - Business rule outcomes
   - Performance metrics (optional)

## Environment Setup

Create environment files for different deployment targets:

- **Development**
  - URL: http://localhost:9090/DecisionService
  - Headers: Content-Type: application/json
  - Timeouts: 30s

- **Test**
  - URL: https://test-server/DecisionService
  - Headers: Content-Type: application/json, Authorization: Bearer {{token}}
  - Timeouts: 60s

- **Production**
  - URL: https://prod-server/DecisionService
  - Headers: Content-Type: application/json, Authorization: Bearer {{token}}
  - Timeouts: 30s

## Test Data Management

- Store test data as JSON files
- Use parameterization for variations
- Include realistic sample data based on Victorian ECE brief requirements
- Use reference date variations to test time-based calculations

## Automation Strategy

1. Run tests via Newman for CI/CD integration
2. Schedule regular test runs to detect regressions
3. Generate test reports for stakeholder review

## Best Practices

1. Use descriptive test names
2. Include detailed assertions
3. Maintain test data separately from test logic
4. Test both positive and negative scenarios
5. Verify all response fields, not just status
6. Track test coverage against requirements
