---
tags: [auth, architecture, hub]
---

# 🔐 Auth Hub

The definitive source of truth for Vedika's "Namaste" entry flow and session lifecycle.

## 🛠️ Workflows
- **[[auth_master_workflow|📜 Auth Master Workflow]]**: The end-to-end logic from login to role resolution.
- **[[session_restoration|♻️ Session Restoration]]**: How the app recovers state after process death or cold start.
- **[[role_resolution_guide|🎭 Role Resolution Guide]]**: Validating user types against Firestore claims.

## 🎨 UI Implementation
- **Auth Screen Fidelity**: Design standards for login and signup screens.
- **[[user_signup_implementation_guide|📝 User Signup Guide]]**: Implementation details for the B2C flow.
- **[[vendor_signin_implementation_guide|🏢 Vendor Sign-in Guide]]**: Implementation details for the B2B flow.

## 🏗️ Technical Decisions
- **[[auth_decisions|⚖️ Auth Architecture Decisions]]**: Why we chose specific Firebase Auth patterns.

---
## 🔄 Related Hubs
- [[Architecture_Hub|🏗️ Architecture Hub]]
- [[Firebase_Hub|🔥 Firebase Hub]]

---
[[Project_Hub|Back to Project Hub]] | [[Auth_Hub|Top]]
