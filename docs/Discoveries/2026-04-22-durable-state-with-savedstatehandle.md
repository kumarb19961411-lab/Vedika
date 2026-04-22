---
title: Durable State with SavedStateHandle
category: gotcha
impact: high
created: '2026-04-22T21:42:34.313Z'
updated: '2026-04-22T21:42:34.313Z'
source: agent
---
## Discovery

Using SavedStateHandle in Hilt ViewModels provides a robust way to persist navigation state (like pendingDestination) across process death without requiring manual save/restore logic in the Activity.

## Recommendation

Prefer SavedStateHandle for small, durable UI states that must survive process death.
