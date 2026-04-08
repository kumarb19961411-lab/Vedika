# Phase 2 Architecture & Implementation Decisions

| Date | Decision | Rationale | Impacted Modules | Alternatives Rejected | Follow-up Required |
|---|---|---|---|---|---|
| Phase 2 Start | **Flavor DI Separation** | Live bindings go exclusively in `app/src/staging/` and `app/src/prod/` source sets. Mock bindings remain in `app/src/dev/`. No `LiveRepositoryModule` in `main`. | `/app/src/` | *Rejected*: Placing live bindings in `main` and hacking `dev` config to exclude them via exclude rules. (Too brittle and anti-pattern). | Create staging/prod directories and `LiveRepositoryModule.kt` implementations inside them. |
| Phase 2 Start | **Emulator Local Routing** | Emulators will be supported but must be opt-in strictly for debugging. Will not be the default for staging/prod. | `/core/data/` | *Rejected*: Hardcoding emulator addresses globally inside `main` module causing production leaks. | Define a `FirebaseConfig` wrapper or use strict `BuildConfig` flags that staging can trigger. |
