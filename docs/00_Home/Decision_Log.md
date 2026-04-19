---
title: Decision Log
type: log
status: active
owner: Product Architect
phase: all
last_updated: 2026-04-15
tags: [decisions, architecture]
---

# ⚖️ Decision Log

Track architectural and product decisions here.

## 2026-04-15: Auth State Scoping
**Context**: Auth state was lost across screens.
**Decision**: Scope `AuthViewModel` to the full nested navigation graph.
