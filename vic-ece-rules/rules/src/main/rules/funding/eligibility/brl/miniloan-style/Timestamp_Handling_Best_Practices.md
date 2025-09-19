# Timestamp Handling Best Practices in ODM Rules

## Key Principle: Never Access System Time in Rules

IBM explicitly recommends against using `System.currentTimeMillis()`, `new Date()`, or similar constructs directly in rules. This design pattern has several critical issues:

1. **Timezone Inconsistency**: Rules may execute in different timezones than where the data was collected
2. **Cloud Deployment**: In cloud environments, rules may execute on servers in different regions
3. **Reproducibility**: Time-based calculations should be reproducible for auditing and testing
4. **Batch Processing**: When processing historical data, system time is irrelevant

## Solution: Reference Timestamps in Request Objects

We've implemented this best practice by adding a `referenceDate` field to all request objects:

```java
@XmlElement
private java.time.LocalDate referenceDate;
```

### Benefits:

1. **Consistent Calculations**: All time-based calculations use the same reference point
2. **Deterministic Rules**: Rules produce consistent results when rerun with the same inputs
3. **Testing Flexibility**: Easy to test with different dates without changing system clock
4. **Batch Processing Support**: Enables processing of historical data with appropriate timestamps

## Implementation in BRL Rules

Rules should use the reference date from the request objects:

```
if
  the child from 'the eligibility request' has date of birth
  and the reference date of 'the eligibility request' is known
  and the child's age at the reference date of 'the eligibility request' is at least 3
then
  // Actions
```

## Setting Reference Date in Client Applications

Client applications must set an appropriate reference date in request objects before calling the rule service:

```java
// Setting reference date in request
EligibilityRequest request = new EligibilityRequest();
request.setReferenceDate(LocalDate.now());  // Current date for live processing
// OR
request.setReferenceDate(LocalDate.of(2025, 4, 30));  // Specific reference date

// For batch processing of historical data
request.setReferenceDate(recordTimestamp);  // Use timestamp from each record
```

## Common Use Cases

1. **Current Date Processing**: Use today's date as reference date for live transactions
2. **Future Date Projection**: Use future reference dates for predictive scenarios
3. **Historical Analysis**: Use historical reference dates for retrospective analysis
4. **Batch Processing**: Use different reference dates for each record in batch jobs

## Testing Date-Dependent Rules

Test date-dependent rules with multiple reference dates to verify behavior across edge cases:

1. Test with reference dates at age boundaries
2. Test with dates around policy change periods
3. Test with edge cases (leap years, month transitions)

By following these best practices, rules remain deterministic and consistent regardless of execution environment.
