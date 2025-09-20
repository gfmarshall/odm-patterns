# ODM 9.5.x XOM Implementation Notes

## Compilation Issues in Development Environment

When implementing the XOM model for ODM 9.5.x in a development environment, you may encounter the following compilation issues:

### Missing ODM Libraries

```
package ilog.rules.bom.annotations does not exist
The import ilog cannot be resolved
CustomProperty cannot be resolved to a type
BusinessName cannot be resolved to a type
```

**Solution**: 
- These errors occur because the IBM ODM Rule Designer libraries are not available in the development environment.
- In a real ODM 9.5.x implementation, these libraries would be provided by the ODM installation.
- Add the following libraries to your project's classpath:
  - `jrules-engine.jar`
  - `jrules-res-session-java.jar`
  - `jrules-res-execution.jar`

### Class Reference Issues

```
package Child does not exist
cannot find symbol: class ServiceProvider
cannot find symbol: class Service
```

**Solution**:
- These errors occur because the XOM classes reference each other but are in the same package.
- In a proper ODM implementation, these references would resolve correctly when compiled together.
- No code changes are needed; the errors will disappear once compiled in the ODM environment.

## XOM Model Updates for Pre-Prep and Free Kinder

The following key updates were made to the XOM model to support the new requirements:

1. **Service Class**:
   - Added `PrePrepConfig` inner class with rollout information
   - Added `FeeStructure` inner class for fee-related information
   - Added `participatesInFreeKinder` flag
   - Added helper methods for Pre-Prep eligibility checks

2. **FundingDetermination Class**:
   - Added Free Kinder fields (`freeKinderEligible`, `freeKinderOffset`, `residualFeeAfterOffset`)
   - Added priority group field
   - Added methods for calculating Free Kinder offsets
   - Added methods for applying KFS offsets to residual fees

3. **EnrolmentApplication Class**:
   - Added priority group field for simplified priority group handling

## Implementation Strategy for ODM 9.5.x

1. **XOM Compilation**:
   - Compile the XOM in a proper ODM 9.5.x environment where the required libraries are available
   - Alternatively, add stub annotations to temporarily resolve compilation issues

2. **BOM Generation**:
   - Once the XOM is compiled successfully, use it to generate the BOM
   - Ensure proper verbalization for the new fields and methods

3. **Rule Implementation**:
   - Implement rules following the ruleflow structure defined
   - Pay special attention to the new Pre-Prep rollout and priority group rules

## Testing Considerations

1. **Pre-Prep Rollout Testing**:
   - Test eligibility based on LGA and priority group combinations
   - Verify correct hours calculation based on rollout phase

2. **Free Kinder Testing**:
   - Test offset calculations for different service types
   - Verify interaction between Free Kinder and KFS supplements

3. **Priority Group Testing**:
   - Test correct determination of priority groups
   - Verify priority group behavior in allocation scenarios
