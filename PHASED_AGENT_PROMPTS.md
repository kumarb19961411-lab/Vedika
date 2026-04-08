# Vedika Phase-Wise Antigravity Prompts

These prompts assume the specialist agents already exist in `.agents/skills/` and the workflow/rules are available.

## Canonical Design Source
The Antigravity workspace has Stitch connected through MCP. Agents must use the connected Stitch project as the primary UI source of truth during planning and implementation.

- **Stitch Project ID:** `12128442228619418215`
- Use Stitch first for screen review, naming, and variant resolution
- Use the uploaded ZIP only as a supporting snapshot/reference


---

## Phase 0 Prompt - Lock Scope, Architecture, and Canonical Screens

Use this first.

```text
Task: Start Phase 0 for the Vedika Android app using the uploaded UI/UX direction and the current repository baseline.

Inputs:
- PRD.md at repo root
- Existing agent skills in .agents/skills/
- Existing rules and workflows in .agents/
- Current repo baseline is still a starter Android project
- Connected Stitch project via MCP: `12128442228619418215`

Goal:
Lock the implementation direction before coding.

Instructions:
1. product_architect must review PRD.md and produce:
   - canonical scope for Phase 0 and Phase 1
   - final vendor-first screen inventory
   - features explicitly deferred to later phases
   - module boundaries
   - app shell boundaries
   - navigation boundaries
   - state ownership boundaries

2. android_compose_engineer must review the connected Stitch project `12128442228619418215` as the canonical UI source and use the uploaded ZIP only as secondary reference. It must produce:
   - canonical screen mapping from Stitch
   - reusable component inventory
   - design system token plan
   - bottom navigation proposal
   - list of duplicate/variant screens that should NOT be built now

3. firebase_backend_engineer must produce only a high-level data-source strategy for now:
   - mock repository plan
   - live repository plan
   - dev/staging/prod environment strategy
   - what remains mocked in Phase 1
   - what is deferred to Firebase phases

4. qa_reliability_engineer must define the minimum test strategy for Phase 0/1
5. security_access_guard must define the dev-bypass guardrails so debug bypasses never leak into production

Required outputs:
- Phase 0 scope summary
- Phase 1 scope summary
- canonical screen list sourced from Stitch
- module plan
- design system plan
- dev mode strategy
- risk list
- approval/block decision from product_architect

Do not start full feature implementation yet.
First lock the plan.
```

---

## Phase 1 Prompt - Build Vendor App Shell With Developer Mode and Mock Data

Use this after Phase 0 is approved.

```text
Task: Execute Phase 1 for the Vedika Android app.

Goal:
Build the vendor-facing frontend shell end-to-end with developer mode, mock authentication, mock repositories, and no hard dependency on live Firebase flows.

Important constraints:
- frontend-first
- developer mode mandatory
- auth/API bypass allowed only in dev mode
- no production auth shortcuts
- no heavy backend coupling in Compose screens
- no business logic in UI
- use Stitch project `12128442228619418215` as the canonical source for Phase 1 screen implementation

Phase 1 scope:
- login gateway screen
- OTP verification screen
- role/category flow as needed for vendor onboarding
- partner setup / registration
- vendor dashboard
- availability calendar
- new booking form
- booking detail / booking history
- inventory hub
- gallery hub
- finance dashboard (display-only mock state)
- profile screen
- bottom navigation and app shell

Agent instructions:
1. product_architect
   - confirm final Phase 1 scope
   - assign module ownership
   - define acceptance criteria for each screen group

2. android_compose_engineer
   - build the Compose app shell
   - implement Vedika theme/design system
   - implement canonical vendor screens only from Stitch project `12128442228619418215`
   - use reusable components
   - include loading/empty/error/offline placeholders where relevant

3. firebase_backend_engineer
   - create repository interfaces and environment-aware data source boundaries
   - create mock repository contracts for dashboard, calendar, bookings, inventory, gallery, finance, and profile
   - do NOT force live Firebase integration yet

4. booking_inventory_rules_engineer
   - define presentation-safe mock domain behavior for calendar, booking states, and inventory summaries
   - do not push full backend enforcement yet

5. finance_ledger_engineer
   - define mock display states for finance screens without treating client data as authoritative

6. security_access_guard
   - define strict dev bypass constraints
   - ensure bypass logic is debug/dev only

7. qa_reliability_engineer
   - require navigation, state rendering, and critical mock flow coverage

Required deliverables:
- working vendor app shell
- developer mode entry
- mock login/session flow
- bottom navigation
- mock repository implementation
- canonical screen implementations
- compile/run instructions
- known gaps list deferred to Firebase phases
```

---

## Phase 2 Prompt - Add Firebase Foundation Without Breaking Frontend

Use this after Phase 1 UI is stable.

```text
Task: Execute Phase 2 for the Vedika Android app.

Goal:
Introduce Firebase foundation cleanly behind abstractions without destabilizing the already working frontend shell.

Important constraints:
- preserve dev mode and mock mode
- do not break existing screen flows
- all live integrations must sit behind repository boundaries
- sensitive flows may remain incomplete if backend-trusted enforcement is not ready yet

Phase 2 scope:
- Firebase project wiring
- environment configuration for dev/staging/prod
- Firebase Auth foundation
- Firestore foundation
- Cloud Storage foundation
- Cloud Functions skeleton
- emulator-friendly local setup
- Google sign-in preparation if not fully enabled yet

Agent instructions:
1. product_architect
   - review integration boundaries
   - ensure mock/live switching remains clean

2. firebase_backend_engineer
   - integrate Firebase Auth, Firestore, Storage, and Functions skeleton
   - define environment config approach
   - create live repository skeletons matching mock contracts
   - create emulator-friendly local flow guidance

3. security_access_guard
   - define Firebase security baseline implementation tasks
   - define App Check/dev-debug posture
   - define auth and protected resource boundaries

4. android_compose_engineer
   - update app startup/session handling only as needed
   - do not rework stable screens unnecessarily

5. qa_reliability_engineer
   - define emulator-backed validation plan
   - ensure mock mode and live mode are both testable

Required deliverables:
- live data source skeletons
- environment config strategy
- dev/staging/prod switching plan
- emulator setup notes
- auth/session integration plan
- risk list for later phases
```

---

## Phase 3 Prompt - Implement Calendar, Booking, and Inventory With Trusted Logic

Use this after Firebase foundation is ready.

```text
Task: Execute Phase 3 for the Vedika Android app.

Goal:
Implement the vendor operational core: availability calendar, booking lifecycle, and inventory protection.

Important constraints:
- no client-only enforcement for booking/inventory correctness
- same-date stock protection is mandatory
- calendar must act as a clear source of truth
- UI must reflect backend-approved states, not invent them

Phase 3 scope:
- availability calendar data flow
- booking state machine
- manual booking creation
- booking detail/history integration
- date/slot status handling
- inventory item and reservation flows
- stock release on cancellation/edit where defined
- dashboard operational counters as feasible

Agent instructions:
1. booking_inventory_rules_engineer
   - define and lock the booking state machine
   - define date status rules
   - define inventory reservation and release rules
   - define conflict validation rules

2. firebase_backend_engineer
   - implement Firestore models and trusted backend write paths
   - use transactions/functions where required
   - map repository contracts to live behavior

3. android_compose_engineer
   - connect screens to approved state models
   - keep UI free from business logic

4. security_access_guard
   - review privileged overrides and protected write paths

5. qa_reliability_engineer
   - require edge-case, retry, and emulator-backed tests

6. product_architect
   - perform final review before approving phase completion

Required deliverables:
- booking/calendar/inventory integrated flow
- source-of-truth definition notes
- backend write-path notes
- test matrix
- unresolved edge-case list
```

---

## Phase 4 Prompt - Implement Finance and Payout Safety Layer

Use this only after Phase 3 is stable.

```text
Task: Execute Phase 4 for the Vedika Android app.

Goal:
Implement finance ledger visibility, payout requests, and reconciliation-safe finance flows without trusting the client as the source of truth.

Important constraints:
- balances are server-authoritative
- finance mutations must be auditable
- payout requests must be duplicate-safe
- booking-linked financial changes must reconcile cleanly
- no casual exposure of banking or KYC data

Phase 4 scope:
- finance ledger data model
- display of credits/debits/advances/pending balances
- payout settings
- payout request flow
- payout status tracking
- reconciliation behavior after booking edits/cancellations/refunds
- KYC/bank/UPI handling boundaries

Agent instructions:
1. finance_ledger_engineer
   - define ledger behavior, payout states, and reconciliation logic
   - define display-only vs authoritative values

2. firebase_backend_engineer
   - implement backend model and trusted write paths
   - ensure auditability and idempotency support

3. security_access_guard
   - review sensitive data handling and payout authorization

4. android_compose_engineer
   - connect finance UI to approved state contracts
   - do not invent finance logic

5. qa_reliability_engineer
   - define duplicate-protection, retry, and reconciliation test coverage

6. product_architect
   - approve only if boundaries remain clean

Required deliverables:
- finance integration plan
- payout flow notes
- sensitive data handling notes
- test/risk notes
- approval/block decision
```

---

## Phase 5 Prompt - Add User Discovery and Marketplace Layer

Use this after vendor-side flows are strong.

```text
Task: Execute Phase 5 for the Vedika Android app.

Goal:
Introduce the user-facing discovery layer without destabilizing the vendor operating system.

Important constraints:
- vendor-side workflows remain primary
- user-side scope should start discovery-first, not full checkout-heavy
- shared design system and clean navigation boundaries must be preserved

Phase 5 scope:
- event type selection
- category-based vendor discovery
- venue/service listing screens
- vendor preview/detail screens
- inquiry entry points if approved

Agent instructions:
1. product_architect
   - lock the user-side MVP scope
   - define what is view-only vs interactive

2. android_compose_engineer
   - implement discovery screens using shared design system components

3. firebase_backend_engineer
   - define read models and query paths for discovery

4. security_access_guard
   - review public vs protected data exposure boundaries

5. qa_reliability_engineer
   - define browse/search/filter flow validation

Required deliverables:
- user discovery scope summary
- screen implementation plan
- safe read model plan
- quality checklist
```

---

## Phase 6 Prompt - Hardening, QA, Security, and Release Preparation

Use this before any serious beta.

```text
Task: Execute Phase 6 for the Vedika Android app.

Goal:
Harden the app for release by closing architecture debt, security gaps, reliability gaps, and production-readiness gaps.

Important constraints:
- no debug bypass behavior in production
- no ambiguous source of truth for high-risk domains
- no release without quality evidence

Phase 6 scope:
- release config cleanup
- observability/crash monitoring integration
- test hardening
- security rule hardening
- performance tuning
- UX polish pass
- production checklist and known-issues review

Agent instructions:
1. qa_reliability_engineer
   - define and run final quality gate expectations

2. security_access_guard
   - perform final access-control and sensitive-data review

3. product_architect
   - review architecture consistency and technical debt

4. firebase_backend_engineer
   - verify production backend readiness and environment safety

5. android_compose_engineer
   - resolve polish and consistency issues without architectural drift

6. finance_ledger_engineer and booking_inventory_rules_engineer
   - verify high-risk domain integrity one final time

Required deliverables:
- release readiness report
- architecture debt list
- security findings list
- QA findings list
- go/no-go recommendation
```
