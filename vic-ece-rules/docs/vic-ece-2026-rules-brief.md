# Victoria 2026 Early Childhood Education Funding – Unambiguous Rules Brief (Eligibility & Calculation)

Purpose: A self-contained, unambiguous engineering brief for Victoria's kindergarten funding landscape (around 2026), suitable for rule engines (e.g., IBM ODM/CP4BA) and AI-assisted code generation.

This version applies the ambiguity fixes you requested:
- Clear separation between base programs and supplements.
- Explicit provider/service approvals for Victorian funded kindergarten.
- Externalized age cut-offs and Pre-Prep hours by LGA/cohort/year.
- Eligibility vs allocation outputs kept separate.
- ASCII text and tilde-fenced code blocks to avoid rendering issues.

---

## 1) Taxonomy and Scope

### 1.1 Program taxonomy
- Base programs (exactly one per child per programYear at a given service):
  - `THREE_YEAR_OLD_KINDER`
  - `PRE_PREP`  (4-year-old program rolling out from 2026; hours vary by LGA/cohort/year)
- Supplements (zero or more may apply in addition to the base program):
  - `ESK`  (Early Start Kindergarten)
  - `ADDITIONAL_YEAR`
  - `KFS`  (Kindergarten Fee Subsidy)
  - `INCLUSION`

### 1.2 Years and cut-offs
- `fundingYear`: the policy/rates context (e.g., 2026), drives rate tables and operational settings.
- `programYear`: the attendance year of the child at the service (often same as fundingYear).
- Age eligibility uses `AGE_ELIGIBILITY.csv` with explicit `asAtDate` per scope (provider/LGA/statewide).

---

## 2) Canonical Data Model (facts/attributes)

### 2.1 Person (parent class)
- `personId`: string
- `firstName`, `lastName`, `dateOfBirth` (ISO), `gender`: enum
- `indigenousStatus`: enum = `ABORIGINAL`, `TORRES_STRAIT`, `BOTH`, `NONE`, `UNSTATED`
- `immigrationStatusCodes[]`: array of codes (config-driven membership set will define refugee/asylum grouping)
- `outOfHomeCareStatus`: boolean
- `disabilityOrAddNeeds`: boolean
- `diagnoses[]`: list
- `supportPlanRefs[]`: list (URIs/immutable hashes)

### 2.2 Child (extends Person)
- `veylfDelays[]`: set of VEYLDF outcome codes
- `fundedKinderYearsCompleted.THREE_YEAR_OLD_KINDER`: int
- `fundedKinderYearsCompleted.PRE_PREP`: int

### 2.3 Guardian
- `guardianId`
- `relationshipToChild`
- `concessions[]`: e.g., HCC, PCC, DVA (actual eligibility driven by KFS table)

### 2.4 Family
- `householdId`, `address`, `lgaCode`
- `seifaQuintile` (optional)
- `primaryLanguage` (optional), `interpreterRequired` (optional)

### 2.5 ServiceProvider
- `providerId`, `name`, `abn`
- `isNQFApprovedProvider`: boolean
- `isVicKinderFundingApprovedProvider`: boolean

### 2.6 Service
- `serviceId`, `providerId`, `name`, `address`, `lgaCode`
- `deliverySetting`: enum = `SESSIONAL_STANDALONE`, `LONG_DAY_CARE_INTEGRATED`, `SCHOOL_BASED_KINDER`
- `deliversVicGovFundedKindergartenProgramInYear`: boolean
- Program config (data-driven, not code):
  - `prePrepHoursByCohort`: derived from `PREP_HOURS.csv` by fundingYear + LGA + cohortKey

### 2.7 EnrolmentApplication
- `applicationId`, `childId`, `serviceId`, `intendedStartDate`
- `requestedBaseProgramType`: `THREE_YEAR_OLD_KINDER` | `PRE_PREP`
- `requestedWeeklyHours`: int
- `allocationPriorityCodes[]`: e.g., ABORIGINAL_TSI, OOHC, REFUGEE_ASYLUM_SEEKER, DISABILITY, SIBLING, LOCAL (from `ALLOCATION_PRIORITY.csv`)
- `supportingEvidenceRefs[]`

### 2.8 FundingDetermination (output)
- `baseProgramType`: `THREE_YEAR_OLD_KINDER` | `PRE_PREP`
- `supplementCodes[]`: 0..n from {ESK, ADDITIONAL_YEAR, KFS, INCLUSION}
- `eligibilityResults[]`: array of per-scheme objects:
  - `{ scheme: <BASE|ESK|ADDITIONAL_YEAR|KFS|INCLUSION>, status: ELIGIBLE|INELIGIBLE|CONDITIONAL, reasons[], policyRefs[] }`
- `calculationBreakdown[]`: list of `FundingComponent` (see below)
- `hoursPerWeekApproved`, `weeksPerYear`
- `effectiveFrom`, `effectiveTo`
- `policyRefs[]`, `decisionEvidenceRefs[]`

### 2.9 FundingComponent
- `componentCode`: e.g., PER_CAPITA, PER_HOUR_BASE, ESK_GRANT, KFS_OFFSET, INCLUSION_LOADING
- `basis`: `PER_CHILD` | `PER_HOUR` | `PER_SERVICE` | `ONE_OFF`
- `quantity`, `rate`, `amount`

---

## 3) Rule Semantics (strict)

### 3.1 Eligibility rules

A) Base program resolution (exactly one base program):
1) Check age eligibility using `AGE_ELIGIBILITY.csv` for the matching scope (provider overrides LGA; LGA overrides statewide).  
2) If eligible for both base programs by age (rare), prefer the program requested in `requestedBaseProgramType` and the service offering.  
3) Result: `baseProgramType` is set to `THREE_YEAR_OLD_KINDER` or `PRE_PREP`.

B) ESK supplement (3-year-olds):
- Eligible if any holds: (i) `indigenousStatus != NONE`, or (ii) `outOfHomeCareStatus = true`, or (iii) child has `immigrationStatusCodes[]` intersecting the configured `REFUGEE_OR_ASYLUM_SEEKER_SET`.
- Requires enrolment at a service delivering Vic Govt funded kindergarten in `fundingYear`.

C) Additional Year supplement:
- Requires evidence of delays in >=2 VEYLDF outcomes (`veylfDelays[]` size >=2).
- Requires teacher assessment + parent agreement + plan artefacts in `supportingEvidenceRefs[]`.
- Deny if the child has already completed an additional funded year for the intended `baseProgramType` (use `fundedKinderYearsCompleted.*`).

D) KFS supplement:
- Eligibility from `KFS_CONCESSIONS.csv` (and any cohort flags, if configured).
- Applies only against residual family fees after government contribution is computed; never negative family fee.

E) Inclusion supplement:
- Requires disability/additional needs AND a separate service approval (not the same as disability flag). If approved, yield inclusion loading(s).

F) Allocation vs eligibility:
- Allocation (place offers) is separate: resolve `allocationPriorityCodes[]` to `rank` using `ALLOCATION_PRIORITY.csv`. Allocation never negates eligibility.

### 3.2 Hours and rates

- Hours per week for `PRE_PREP` are resolved by: `PREP_HOURS[fundingYear][lgaCode][cohortKey]`.  
  CohortKey resolution order:
  1) If ESK OR Aboriginal/TSI OR OOHC OR Refugee/Asylum criteria match a dedicated cohort key, use it.
  2) Else use `UNIVERSAL` if defined.
  3) If neither found: hard fail (no silent default).
- Baseline 3YO hours are configurable (do not hard-code 15). Keep in `RATES.csv` or a separate hours table if used.
- `weeksPerYear` must be supplied from config (either `RATES.csv` or a delivery calendar file). No magic numbers.

### 3.3 Calculation order

1) Compute base program component(s): per-hour or per-capita as configured.  
2) Add supplements (ESK grant, inclusion loadings, etc.) as separate `FundingComponent`s.  
3) Compute residual family fee (if any).  
4) Apply KFS offset up to the residual fee cap.  
5) Sum to totals.  

---

## 4) Ruleflows (ODM-friendly)

### EligibilityFlow
1) Validate inputs (DOB, LGA/service linkage, provider/service approvals).  
2) Age check using `AGE_ELIGIBILITY.csv` (scope precedence: provider > LGA > statewide).  
3) Resolve `baseProgramType`.  
4) Evaluate supplements (ESK, Additional Year, KFS, Inclusion) independently.  
5) Build `eligibilityResults[]` and `policyRefs[]`.  
6) Produce `FundingDetermination` with explainability and evidence URIs.

### CalculationFlow
1) Load rates/hours (`RATES.csv`, `PREP_HOURS.csv`) for `fundingYear`, `baseProgramType`, `deliverySetting`, `lgaCode`, `cohortKey`.  
2) Compute base components.  
3) Add supplement components.  
4) Apply KFS offset to residual fee (cap at 0).  
5) Finalize totals, `weeksPerYear`, and evidence linkage.

---

## 5) Payload Contracts (examples)

### 5.1 Eligibility Request (JSON)
~~~json
{
  "fundingYear": 2026,
  "programYear": 2026,
  "child": {
    "personId": "C-123",
    "firstName": "Mila",
    "lastName": "Nguyen",
    "dateOfBirth": "2022-06-15",
    "indigenousStatus": "NONE",
    "immigrationStatusCodes": [],
    "outOfHomeCareStatus": false,
    "disabilityOrAddNeeds": false,
    "veylfDelays": [],
    "fundedKinderYearsCompleted": {
      "THREE_YEAR_OLD_KINDER": 0,
      "PRE_PREP": 0
    }
  },
  "family": {
    "householdId": "H-77",
    "address": "1 Example St, Suburb VIC",
    "lgaCode": "LGA_301",
    "seifaQuintile": 2
  },
  "guardians": [
    {
      "guardianId": "G-1",
      "relationshipToChild": "PARENT",
      "concessions": ["HCC"]
    }
  ],
  "serviceProvider": {
    "providerId": "P-9",
    "isNQFApprovedProvider": true,
    "isVicKinderFundingApprovedProvider": true
  },
  "service": {
    "serviceId": "S-9",
    "providerId": "P-9",
    "deliverySetting": "LONG_DAY_CARE_INTEGRATED",
    "lgaCode": "LGA_301",
    "deliversVicGovFundedKindergartenProgramInYear": true
  },
  "application": {
    "applicationId": "A-88",
    "requestedBaseProgramType": "THREE_YEAR_OLD_KINDER",
    "requestedWeeklyHours": 15,
    "allocationPriorityCodes": []
  },
  "supportingEvidenceRefs": []
}
~~~

### 5.2 Eligibility Response (JSON)
~~~json
{
  "baseProgramType": "THREE_YEAR_OLD_KINDER",
  "supplementCodes": ["KFS"],
  "eligibilityResults": [
    {"scheme": "BASE", "status": "ELIGIBLE", "reasons": ["Age meets 3YO cutoff"], "policyRefs": ["cfg://age-eligibility/2026/statewide"]},
    {"scheme": "KFS", "status": "ELIGIBLE", "reasons": ["Guardian HCC"], "policyRefs": ["cfg://kfs-concessions/2026"]},
    {"scheme": "ESK", "status": "INELIGIBLE", "reasons": ["No ESK criteria met"], "policyRefs": ["cfg://esk/2026"]}
  ],
  "hoursPerWeekApproved": 15,
  "weeksPerYear": 0,
  "calculationBreakdown": [],
  "effectiveFrom": "2026-01-01",
  "effectiveTo": "2026-12-31",
  "policyRefs": ["cfg://age-eligibility/2026/statewide","cfg://kfs-concessions/2026"],
  "decisionEvidenceRefs": []
}
~~~

### 5.3 Calculation Response (JSON)
~~~json
{
  "baseProgramType": "THREE_YEAR_OLD_KINDER",
  "hoursPerWeekApproved": 15,
  "weeksPerYear": 0,
  "calculationBreakdown": [
    {
      "componentCode": "PER_CAPITA",
      "basis": "PER_CHILD",
      "quantity": 1,
      "rate": 0.0,
      "amount": 0.0
    },
    {
      "componentCode": "KFS_OFFSET",
      "basis": "PER_CHILD",
      "quantity": 1,
      "rate": 0.0,
      "amount": 0.0
    }
  ],
  "totalAmount": 0.0,
  "policyRefs": [
    "cfg://rates/2026/3yo/ldc",
    "cfg://kfs-concessions/2026"
  ],
  "decisionEvidenceRefs": []
}
~~~

Note: numeric values above are placeholders. Drive them from the CSV config tables.

---

## 6) Config Tables (externalized)

Provide and version these tables outside the ruleset. Sample CSV stubs are included in the ZIP.

- `AGE_ELIGIBILITY.csv` — age cutoff dates by scope and base program.
- `PREP_HOURS.csv` — Pre-Prep hours per week by fundingYear, LGA, and cohort key.
- `ALLOCATION_PRIORITY.csv` — priority codes and ranks for capacity-constrained allocations.
- `RATES.csv` — per-year rates, basis, weeksPerYear, by base program and delivery setting.
- `KFS_CONCESSIONS.csv` — concession codes and date ranges.
- `INCLUSION_MENU.csv` — inclusion components and rates.
- `VEYLDF_CODES.csv` — canonical VEYLDF outcome codes.

---

## 7) Minimal Rules Guarantees

1) Exactly one `baseProgramType` per child per programYear per service.  
2) Supplements are additive and never redefine the base program.  
3) Calculation order: Base -> Supplements -> KFS offset (cap at zero fee).  
4) Hours lookup precedence: cohort-specific -> universal -> hard fail.  
5) Age eligibility: always from `AGE_ELIGIBILITY.csv` (provider > LGA > statewide precedence).  
6) Additional Year: VEYLDF >=2 + teacher and parent agreement + plan evidence + funded year count check for intended base program.  
7) Eligibility and allocation are separate outputs.

---

## 8) Testing Checklist (suggested)

- Edge DOB around cut-off dates for both base programs.  
- ESK eligibility with each criterion independently true.  
- Additional Year pass/fail (counts and VEYLDF).  
- KFS concession present/absent and interaction with residual fees.  
- Inclusion approval present/absent.  
- Pre-Prep hours lookup for priority vs universal cohorts.  
- Allocation ranks with multiple priority codes.  

