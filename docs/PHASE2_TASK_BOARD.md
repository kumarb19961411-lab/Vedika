# Phase 2 Task Board

| ID | Task | Owner Agent | Module/Path | Status | Priority | Dependency | Notes |
|---|---|---|---|---|---|---|---|
| P2-01 | Repo Audit & Docs Bootstrap | product_architect | /docs/ | done | High | None | Bootstrapping core docs |
| P2-02 | Create staging & prod source sets | firebase_backend_engineer | /app/src/ | done | High | P2-01 | Moving live bindings out of main |
| P2-03 | Build FirebaseAuthRepository | firebase_backend_engineer | /core/data/ | done | High | P2-02 | |
| P2-04 | Build FirebaseVendorRepository | firebase_backend_engineer | /core/data/ | done | High | P2-02 | |
| P2-05 | Build FirebaseBookingRepository | firebase_backend_engineer | /core/data/ | done | High | P2-02 | |
| P2-06 | Build FirebaseInventoryRepository | firebase_backend_engineer | /core/data/ | done | High | P2-02 | |
| P2-07 | DI LiveRepositoryModule | firebase_backend_engineer | /app/src/staging & prod | done | High | P2-06 | |
| P2-08 | Firebase Emulator Config Toggle | firebase_backend_engineer | /core/data/ | done | Medium | P2-07 | |
| P2-09 | Firestore Rules & Security Config | security_access_guard | /core/data/ | done | High | P2-08 | Rules created in project root |
| P2-10 | Compile & Verify all flavors | qa_reliability_engineer | / | done | High | P2-09 | assembleStagingDebug fully compiles |
