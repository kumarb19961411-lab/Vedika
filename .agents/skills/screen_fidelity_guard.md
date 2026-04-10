# Screen Fidelity Guard — Agent Skill

This skill enforces strict visual and interaction precision for Vedika V2 screen implementations. Agents must use this as their primary verification checklist when modifying or creating UI.

## 📐 Visual Fidelity (Pixel Perfect)
- **Constraint**: The 5 Locked Phase 1 screens must maintain **1:1 alignment** with source image exports from `resources/phase1`.
- **Spacing**: Use standard 8dp-grid multiples.
- **Color**: Only use tokens defined in `VedikaTheme` (e.g., `MaterialTheme.colorScheme.primary` for Brand Orange).
- **Glassmorphism**: Ensure `Modifier.background()` uses translucent brushes and `Modifier.blur()` where appropriate to achieve the "Premium" look.

## 🕹 Interaction Contract
- **No Placeholders**: Never leave `onClick = {}` empty. Every interaction must either trigger a navigation event or update a reactive `UiState`.
- **Validation**: "Primary CTA" buttons must be disabled if required fields are blank. Use `isNotBlank()` and `isValid()` checks effectively.
- **Feedback**: Every destructive or successful long-running action (Save, Register, Submit) **must** provide user feedback via a `Snackbar` or visible transition.

## 💾 State Shaping
- **Share, Don't Duplicate**: Cross-screen state (User ID, Auth Flow) must live in a **Shared ViewModel** (typically `AuthViewModel` in Phase 1).
- **Single Source of Truth**: UI components must strictly reflect the `uiState` collected from the ViewModel. Do not use local `MutableState` for logic that needs to persist across screen transitions.

## 🛡 Regression Guard Verification
Before finalizing any screen modification:
- [ ] Check `docs/android_build_regression_guard.md` for plugin-order requirements.
- [ ] Verify imports are explicit (no `*`).
- [ ] Verify `OptIn(ExperimentalMaterial3Api::class)` is present if using Material3 Top bars.

---
*Fidelity is not just how it looks; it's how it feels and how it holds together.*
