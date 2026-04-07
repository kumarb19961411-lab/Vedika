# Workflow: Vendor Feature from PRD

## Goal
To systematically convert a Vedika product requirement (PRD) into a highly scrutinized, securely engineered, and production-ready feature slice via a structured multi-agent workflow.

## When to Use
Trigger this workflow anytime a new vendor-facing feature is proposed, including:
- Onboarding configurations
- Dashboard updates
- Availability Calendar logic
- Manual Booking entry
- Inventory Hub actions
- Gallery expansions
- Finance Ledgers
- Payout Requests
- Profile Management modifications

## Required Inputs
- A formal Product Requirements Document (PRD) or detailed feature brief.
- Accompanying context encompassing business value, scaling bounds, and any explicit design/wireframe expectations.

## Step-by-Step Flow

### Step 1: Product Framing
**Agent:** `product_architect`
- Assimilates the PRD/Feature request.
- Deconstructs scope into user stories, technical modules, architectures, and hard acceptance criteria.
- Flags dependencies intersecting Bookings, Inventory, Finance, or Security rules.

### Step 2: Backend/Data Framing
**Agent:** `firebase_backend_engineer`
- Constructs baseline proposals for Firestore schema additions, query structures, index footprints, and Cloud Function triggers.
- Explicitly partitions client-rendered UI models mapping away from the backend mutation source-of-truth.

### Step 3: Domain Rules Review
**Agents:** `booking_inventory_rules_engineer` & `finance_ledger_engineer`
- **Booking/Inventory Rules Engine:** Activates if availability, booking overlaps, or inventory manipulations dictate constraint checking.
- **Finance Ledger Engine:** Activates if payout pipelines, balances, reconciliation traces, or ledger impacts arise.
- These agents firmly lock the absolute truth regarding domain calculation rules completely insulating logic prior to UI creation.

### Step 4: Security Review
**Agent:** `security_access_guard`
- Scrutinizes authorization requirements, Firestore security rule layouts, KYC/financial identifier constraints, and overall App Check validation parameters.
- Exposes risks where elevated trust defaults inappropriately towards client execution layers.

### Step 5: UI Implementation Framing
**Agent:** `android_compose_engineer`
- Acquires unified architecture/rules specs mapping UI presentation elements (Jetpack Compose).
- Validates structural guarantees ensuring complete UI lifecycle implementations spanning `loading`, `error`, `empty`, `success`, and `offline`.
- Adheres strictly to rejecting all business logic generation within the composition tree natively.

### Step 6: QA / Reliability Validation Planning
**Agent:** `qa_reliability_engineer`
- Architects empirical testing parameters combining edge-cases scenarios, regression protection tests, UI State assertions, and integration/emulator testing parameters.
- Evaluates systemic release risks encompassing ANR faults, crash states, and race conditions.

### Step 7: Final Architecture Review
**Agent:** `product_architect`
- Scans all composite peer-reviewed elements.
- Exercises primary gatekeeper veto authority generating an absolute command: **Approve**, **Conditionally Approve**, or **Block**.
- Definitively blocks feature progression whenever layer architecture, domain validity, security constraints, or test-planning arrays lack essential rigor.

## Decision Gates
The workflow is instantly suspended if any of the following arise:
- **STOP** if top-layer architecture ownership presents fundamental ambiguity.
- **STOP** if the Backend source of truth demonstrates glaring scalability constraints.
- **STOP** if Inventory, Booking, or Financial formulas operate possessing poorly mapped execution definitions.
- **STOP** if Sensitive operational mechanisms exhibit dependency on unverified/insecure client parameters.
- **STOP** if empirical test coverage strategies inadequately govern high-risk application components.

## Output Artifacts
This workflow completes by rendering the following deliverables universally:
- Target Feature Scope Summary.
- Technical Acceptance Criteria markers.
- Module Impact and Wiring summaries.
- Absolute Backend/Data Contract directives.
- Explicit Domain/Finance Rule definitions (if triggered).
- Hardened Security Review approvals.
- Final UI implementation topologies.
- Rigorous QA Test Coverage configurations.
- Actionable decision notes detailing ultimate Risk / Approval statuses.
- Open risks and follow-up items.

## Escalation Conditions
Manual human arbitration is explicitly demanded should the following emerge:
- Ambiguous or conflicted Source of Truth hierarchies cross-engine.
- Gridlocked agent reviews triggering paradoxical structural goals.
- Pervasive or irresolvable security-vulnerabilities.
- Intractable financial integrity logic threatening ledger consistency natively.
- Undocumented overbooking vulnerabilities inside availability management nodes.
- Disastrous local Android offline modeling.
- Refusal or inability to synthesize appropriate programmatic testing scenarios.
- Significant uncontrolled feature-scope creep discovered amidst primary planning gates.

## Definition of Workflow Completion
This framework finishes strictly when all listed constituent agents submit validated output artifacts cleanly navigating through the Step 7 Final Architecture Review without encountering an unresolved decision gate stoppage.
