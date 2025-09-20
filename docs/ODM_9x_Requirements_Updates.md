# Updates to Business Requirements for ODM 9.5.x Implementation

## 1. Program Changes Requiring Updates

### Pre-Prep Implementation Timeline
- **Existing Requirement:** General rollout of Pre-Prep starting 2026
- **Updated Information:** Pre-Prep is now confirmed to roll out in stages from 2025-2036, with specific LGAs starting in 2025
- **Required Update:** Add `rolloutYear` to the `Service` class based on LGA to determine eligibility timing

### Pre-Prep Hours
- **Existing Requirement:** Pre-Prep hours vary by LGA/cohort/year
- **Updated Information:** Clear progression path for hours confirmed:
  - Initial: Between 16-20 hours 
  - Mid-stage: Between 16-25 hours
  - Final: Between 16-30 hours
- **Required Update:** Update `PREP_HOURS.csv` structure to include rollout progression

### Priority Groups
- **Existing Requirement:** ESK supplement eligibility criteria defined
- **Updated Information:** Specific priority groups now defined for Pre-Prep with staged access:
  - 2026 priority groups (Aboriginal/TSI, refugee/asylum seeker, Child Protection/OOHC, ESK/Access to Early Learning)
  - 2028 priority groups (Commonwealth concession card holders, multiple births)
- **Required Update:** Add `priorityGroup` field to indicate which priority group the child belongs to

### Free Kinder Integration
- **Existing Requirement:** No explicit reference to Free Kinder
- **Updated Information:** Free Kinder confirmed as applying to both Three-Year-Old and Four-Year-Old programs (including Pre-Prep)
- **Required Update:** Add `freeKinderEligible` field and include offset calculations in funding determination

## 2. Business Rules Requiring Updates

### Eligibility Rules
- **Add Rule:** Check service's Pre-Prep rollout year based on LGA against application's `programYear`
- **Extend Rule:** Add priority group determination to eligibility flow
- **Update Rule:** Update Pre-Prep hours determination based on rollout year, LGA, and priority group status

### Funding Calculation Rules
- **Add Rule:** Calculate Free Kinder offset based on hours and program type
- **Add Rule:** Apply service type conditional logic (sessional vs long day care) to funding calculations
- **Update Rule:** Enhanced fee offset calculation to include Commonwealth Childcare Subsidy interaction

### Allocation Rules
- **Update Rule:** Add new prioritization criteria based on confirmed priority groups

## 3. Data Model Updates

### Service Provider
- Add `participatesInFreeKinder: boolean`

### Service
- Add `prePrep: { rolloutYear: int, rolloutGroup: string, initialHours: int }`
- Add `feeStructure: { hourlyRate: decimal, weeklyRate: decimal, annualRate: decimal }`

### EnrolmentApplication
- Add `priorityGroup: enum = ABORIGINAL_TSI, REFUGEE_ASYLUM, CHILD_PROTECTION, ESK_AEL, CONCESSION_CARD, MULTIPLE_BIRTH, UNIVERSAL`

### FundingDetermination
- Add `freeKinderOffset: decimal`
- Add `residualFeeAfterOffset: decimal`
- Add `effectiveWeeklyRate: decimal`

## 4. Configuration Updates

### PREP_HOURS.csv
- Add columns: `rolloutYear`, `rolloutPhase`, `priorityGroup`
- Update structure to reflect progressive increases in hours

### KFS_CONCESSIONS.csv
- Add `priorityGroupCode` column to align with new priority groups

### New table: FREE_KINDER_RATES.csv
- Structure: `programYear`, `programType`, `serviceType`, `maxHours`, `annualOffset`

## 5. Testing Requirements

In addition to existing test cases:

- Test Pre-Prep eligibility based on LGA rollout schedule
- Test priority group determination and hour allocation
- Test Free Kinder offset calculations for both sessional and long day care
- Test progressive hour increases as per rollout schedule
- Test interaction between Free Kinder offset and KFS supplement
