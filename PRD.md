# Vedika PRD

## 1. Product Name
Vedika

## 2. Product Summary
Vedika is a production-grade Android platform for the wedding ecosystem, starting with a **vendor-first operating system** for venues, decorators, caterers, photographers, priests, and related wedding service providers. The app will later support user-side discovery and booking journeys, but the immediate implementation priority is the **vendor management suite**.

The app should help vendors:
- onboard quickly,
- maintain a trusted digital profile,
- manage availability,
- prevent booking conflicts,
- manage inventory,
- track leads and bookings,
- manage gallery/media,
- view earnings and request payouts.

## 3. Why We Are Building This
Wedding vendors currently manage bookings, stock, customer inquiries, and payments across fragmented tools such as WhatsApp, notebooks, spreadsheets, and ad-hoc phone calls. Vedika aims to become the operating layer for those businesses by combining profile management, availability, booking control, inventory protection, and financial visibility in a single mobile app.

## 4. Current Implementation Reality
The current GitHub repository is still a **starter Android project**, not yet the final product architecture. Right now the repo has a root `.agents` directory, one `app` module, Gradle wrapper/config files, and no visible multi-module Android structure yet. The Gradle settings include only `:app`, the app namespace is `com.example.vedika`, and the current app setup is still basic Android Views/AppCompat rather than Jetpack Compose or Firebase-integrated production architecture. The current `MainActivity` is an `AppCompatActivity` calling `setContentView(...)`, and the app dependencies currently include AppCompat, Material, Activity, and ConstraintLayout, but not Compose, Hilt, or Firebase libraries yet. citeturn435851view0turn886843view0turn124906view0turn124906view2turn560073view0turn560073view1

## 5. UI/UX Review Summary From Uploaded ZIP
The uploaded design ZIP contains a large set of iterative UI explorations rather than a single locked screen system. From review, the design library clearly clusters into these flows:
- authentication and OTP login,
- vendor category selection and onboarding,
- venue/decorator dashboards,
- availability calendar,
- manual booking entry and booked-date detail views,
- inventory hub,
- gallery/portfolio management,
- payments/earnings,
- vendor profile,
- user-side discovery and marketplace views.

The strongest and most implementation-ready visual direction is the **modern heritage vendor experience** using warm cream, ochre, brown, and muted accent colors with a premium yet practical operational UI.


## 5A. Canonical Design Source
The uploaded ZIP is useful for review, but the **canonical design source for implementation is the connected Stitch project via MCP in Antigravity**.

- **Stitch Project ID:** `12128442228619418215`
- Agents must treat this Stitch project as the primary source of truth for screen structure, naming, variants, and latest UI refinements.
- The uploaded ZIP should be treated as a supporting snapshot/reference, not the final authority when Stitch and ZIP differ.
- During PRD review, screen mapping, and implementation planning, agents should read from the connected Stitch project first and use the ZIP only for cross-checking.

## 6. Product Direction Decision
Because the UI package includes both **vendor** and **user discovery** screens, scope must be controlled.

### Decision
Implementation will proceed in this order:
1. **Vendor app shell and vendor workflows first**
2. **Developer mode and mock data support first**
3. **Firebase wiring later, in controlled phases**
4. **User discovery / marketplace later**
5. **Google sign-in added after core vendor flows are stable**

This avoids blocking front-end progress on backend/auth complexity.

## 7. Core Personas
### Primary Persona: Venue Owner
Needs to:
- manage hall profile,
- update calendar,
- add manual bookings,
- track leads,
- upload gallery/media,
- review earnings,
- manage profile branding.

### Primary Persona: Service Vendor
Examples:
- decorator,
- caterer,
- photographer,
- priest,
- rental vendor.

Needs to:
- manage service packages,
- manage inventory/stock,
- track availability,
- protect same-day inventory from overbooking,
- maintain gallery and profile,
- view earnings and payouts.

### Secondary Persona: End User / Customer
Needs to:
- discover vendors,
- browse categories,
- view profiles and galleries,
- eventually inquire / initiate bookings.

This persona is **not Phase 1 priority**.

## 8. Product Goals
### Business Goals
- Create a sticky vendor operating system
- Improve vendor retention via daily operational use
- Reduce overbooking and stock conflict incidents
- Improve discovery-to-booking conversion through strong profile and gallery presentation

### Product Goals
- Ship a highly polished Android vendor app
- Allow frontend development to continue independently through dev mode and mocks
- Introduce Firebase gradually without slowing UI delivery
- Preserve correctness for availability, stock, and finance logic

## 9. Non-Goals For Initial Build
These are not first-build priorities:
- full production admin console,
- web dashboard,
- advanced CRM automation,
- complete user-side booking checkout,
- multilingual expansion,
- automated payout settlement integrations,
- large-scale recommendation engine.

## 10. Product Principles
- Vendor-first execution
- Frontend-first with controlled backend integration
- Strong architecture before scale
- No business logic in UI
- Backend-trusted correctness for sensitive flows
- Offline-friendly where possible
- Dev-mode-friendly for rapid iteration
- Production hardening before release

## 11. Developer Mode Requirement
This is a hard requirement.

The app must support a **developer mode** so the team can build and test the full UI and navigation without depending on live Firebase auth, APIs, or Cloud Functions.

### Developer Mode Must Support
- bypass login/OTP during local development,
- bypass Google sign-in during local development,
- inject mock user and vendor session state,
- use fake repositories for bookings, calendar, inventory, finance, profile, and gallery,
- allow toggling between mock and live data sources,
- support screen previews and demo flows without backend setup,
- never leak dev bypass behavior into production builds.

### Build Strategy
Use build flavors or equivalent environment gating:
- `dev` -> mock-first, bypass-capable,
- `staging` -> Firebase-connected but safe test environment,
- `prod` -> no bypass, full auth, full backend protection.

## 12. Authentication Strategy
### Phase 1
- Developer bypass login
- Mock OTP flow
- Role selection: vendor or user
- Mock session injection

### Phase 2
- Firebase Phone Auth for OTP login
- Firebase-backed vendor/user profile bootstrap
- Session restoration

### Phase 3
- Google Sign-In / Sign-Up support
- Account linking strategy if phone auth and Google auth both exist
- Role-aware onboarding after sign-in

### Hard Rule
UI should not block on auth integration. Auth can be layered in after the app shell is stable.

## 13. Scope Structure
### 13.1 Phase 0 - Repo and Architecture Foundation
Goal: convert the starter project into a real production-ready app foundation.

Includes:
- move from starter single-screen setup toward production structure,
- enable Kotlin + Jetpack Compose app architecture,
- establish app shell,
- establish navigation,
- establish theme/design system,
- lock canonical screens and variants from Stitch project `12128442228619418215`,
- establish modular direction,
- add DI, state, and environment configuration foundations,
- add developer mode and fake repositories.

### 13.2 Phase 1 - Vendor Frontend MVP With Mocks
Goal: build the full vendor-facing UI flow end-to-end with no heavy backend dependency.

Includes:
- login gateway,
- OTP verification screen,
- category selection,
- partner setup,
- dashboard,
- availability calendar,
- manual booking form,
- booking detail/history,
- inventory hub,
- gallery hub,
- finance dashboard,
- vendor profile.

This phase must be fully navigable with mock data and must follow the canonical screen mapping from Stitch project `12128442228619418215`.

### 13.3 Phase 2 - Firebase Foundation
Goal: wire the app to Firebase incrementally.

Includes:
- Firebase project integration,
- environment config for dev/staging/prod,
- Firebase Auth setup,
- Firestore setup,
- Cloud Storage setup,
- Cloud Functions skeleton,
- security rules baseline,
- emulator-friendly local testing.

### 13.4 Phase 3 - Vendor Core Operations
Goal: connect vendor operational features to trusted backend flows.

Includes:
- calendar source of truth,
- booking state machine,
- booking create/edit/cancel,
- inventory reservation logic,
- availability transitions,
- same-date stock protection,
- gallery upload metadata flow,
- dashboard counters.

### 13.5 Phase 4 - Finance and Payout Layer
Goal: introduce finance correctness after operational flows are stable.

Includes:
- finance ledger model,
- booking-linked advance entries,
- pending balances,
- payout request flow,
- settlement states,
- KYC/bank/UPI data handling,
- finance reconciliation after booking edits or cancellations.

### 13.6 Phase 5 - User Discovery Layer
Goal: add user-facing discovery after vendor workflows are strong.

Includes:
- event type selection,
- vendor discovery,
- venue/service category browsing,
- vendor preview/profile viewing,
- inquiry entry points.

### 13.7 Phase 6 - Production Hardening
Goal: release readiness.

Includes:
- Crashlytics / observability,
- performance tuning,
- test coverage hardening,
- release config,
- security and rules hardening,
- build cleanup,
- content/data seeding cleanup,
- QA signoff.

## 14. Canonical MVP Screen Map
The design ZIP includes many alternate screens. For implementation, the agents should lock onto this canonical vendor flow:

### Auth
1. Login gateway
2. OTP verification
3. Role selection if needed

### Onboarding
4. Vendor category selection
5. Business setup / registration
6. Service specialization details

### Vendor Core Tabs
7. Dashboard
8. Calendar
9. Inventory or Gallery hub (category dependent)
10. Finance
11. Profile

### Supporting Screens
12. New booking form
13. Booking detail
14. Booking history
15. Inventory detail / stock management
16. Gallery category detail
17. Edit profile / branding
18. Payout settings

## 15. Functional Modules
### A. App Shell
- splash / startup routing,
- nav graph,
- bottom navigation,
- session-aware entry,
- environment-aware startup.

### B. Auth and Session
- dev-mode bypass,
- phone auth,
- Google sign-in,
- role resolution,
- onboarding completion status.

### C. Vendor Profile
- business name,
- category,
- address,
- service specializations,
- about section,
- branding assets,
- badges and ratings.

### D. Dashboard
- profile views,
- leads,
- booking highlights,
- earnings snapshot,
- recent inquiries,
- quick actions.

### E. Calendar / Availability
- available, blocked, booked states,
- monthly calendar,
- date detail view,
- manual block/unblock,
- manual booking add,
- conflict handling.

### F. Booking Management
- create booking,
- edit booking,
- cancel booking,
- booking detail,
- booking list/history,
- lead-to-booking conversion path later.

### G. Inventory
- item master,
- available stock,
- reserved stock by date,
- low-stock alert,
- category filters,
- stock allocation logic.

### H. Gallery / Portfolio
- categorized photo/video upload,
- asset metadata,
- reorder / cover image later,
- portfolio analytics later.

### I. Finance
- earnings dashboard,
- transaction/ledger list,
- pending balance,
- payout settings,
- payout request flow,
- reconciliation.

### J. User Discovery (Later)
- browse vendors,
- category filters,
- event type flows,
- venue/service previews,
- inquiry CTA.

## 16. High-Level Data Ownership
### Client-Owned
- UI state,
- navigation state,
- local form state,
- cached display data,
- dev-mode mock state.

### Backend-Owned
- authenticated identity,
- vendor records,
- booking truth,
- availability truth,
- inventory reservations,
- finance ledger,
- payout state,
- role-sensitive actions.

### Trusted Backend Only
- final booking confirmation rules,
- same-date stock validation,
- finance mutation correctness,
- payout approval/execution,
- privileged overrides,
- role mutation.

## 17. Architecture Direction
### Android
- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Hilt
- repository pattern
- environment-aware data source switching

### Backend
- Firebase Auth
- Firestore
- Cloud Storage
- Cloud Functions
- emulator-friendly local setup

### Testing
- mock repositories for UI-first work,
- emulator-backed backend tests later,
- strong regression focus on auth, booking, stock, and finance.

## 18. Mock Data and Demo Data Requirements
The app must have seeded mock data for:
- vendor profile,
- dashboard counters,
- booking history,
- calendar states,
- inventory items,
- gallery categories,
- finance entries,
- payout settings,
- user discovery cards.

This data should allow full demo flows without backend connectivity.

## 19. UX Direction
### Visual Language
- warm heritage palette,
- elegant but practical,
- rich but uncluttered,
- premium business dashboard feel.

### UX Characteristics
- bottom-tab driven vendor app,
- clear status chips and state colors,
- strong empty states,
- strong form UX,
- fast scanning for operational screens,
- consistent cards and section blocks.

## 20. Critical Risks
- scope sprawl from trying to build both vendor and user app simultaneously,
- backend delays blocking UI progress,
- auth complexity blocking shell implementation,
- weak inventory rules causing overbooking,
- finance logic being mixed too early into frontend-only work,
- design duplication from too many alternate screens,
- dev bypass accidentally surviving in production,
- unclear boundary between display-only data and source-of-truth data.

## 21. Decisions Locked By This PRD
- Vendor-first implementation
- Frontend-first delivery with mock support
- Developer bypass mode is mandatory
- Firebase comes in controlled phases
- Google sign-in comes after basic auth/session shell exists
- User-side discovery is later scope
- Sensitive logic must remain backend-authoritative

## 22. Success Criteria By Phase
### Phase 1 Success
- entire vendor flow is navigable,
- screens are visually stable,
- mock data supports demos,
- no backend dependency for basic testing.

### Phase 2 Success
- Firebase environments wired,
- auth and basic persistence skeleton works,
- dev and live modes are separable.

### Phase 3 Success
- calendar, booking, and inventory work safely,
- same-day stock conflicts are blocked,
- dashboard reflects live vendor activity.

### Phase 4 Success
- finance entries are traceable,
- payout requests are safe,
- pending balances remain consistent.

### Phase 5 Success
- user discovery works without destabilizing vendor flows.

### Phase 6 Success
- app is secure, testable, stable, and release-ready.

## 23. Agent Execution Guidance
All agents should follow these execution rules:
- do not try to build the entire app in one step,
- work phase by phase,
- preserve developer mode from the beginning,
- prefer fake repositories first when frontend flow is the focus,
- add Firebase only behind clean abstractions,
- do not mix backend correctness logic into Compose screens,
- do not expand user-side scope before vendor-side stabilization,
- treat booking, inventory, finance, and security as high-risk domains.

## 24. First Implementation Target
The first real execution target after this PRD is:

**Phase 0 + Phase 1 vendor shell with developer mode**

That means the agents should first produce:
- app architecture blueprint,
- screen inventory lock,
- design system setup,
- nav shell,
- mock repositories,
- frontend implementation plan,
- phase-specific build prompts.
