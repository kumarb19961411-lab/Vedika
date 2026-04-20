---
title: Agent Workflow & Obsidian Handshake
type: documentation
status: active
owner: Product Architect
last_updated: 2026-04-20
---

# Agent Workflow & Obsidian Handshake

This document defines the canonical workflow for AI agents (Antigravity) interacting with the Vedika repository. To maintain architectural integrity, every coding task must follow the **Obsidian Handshake**.

## The Read -> Act -> Update Loop

The agent must follow these three phases for every non-trivial task:

### 1. The Handshake (Read)
Before writing any code, the agent **must** use the Obsidian MCP tools to:
- **Search**: Find relevant notes in `docs/architecture` or `docs/notes`.
- **Verify**: Cross-check the user's request against the current architecture state recorded in the vault.
- **Plan**: Update or create a plan that references these notes.

### 2. The Implementation (Act)
Code changes are made in the Android modules (`app`, `feature`, `core`). During this phase:
- The agent should refer back to the docs if implementation decisions arise.
- Major deviations from the docs should be flagged for user review.

### 3. The Synchronization (Update)
After the code is verified (builds/tests pass), the agent **must** update the vault:
- Update `SYSTEM_STATUS.md` or `project_status.md`.
- Update relevant architecture docs (e.g., if a new repository is added, update `repo_map.md`).
- Log the session in `docs/notes/daily/YYYY-MM-DD.md`.

## Standard Prompt Requirement

Developers should include the following block in their mission prompts to enforce this behavior:

```markdown
### Mission Requirement: Obsidian Context Sync
- [ ] **Search** the local Obsidian `docs/` vault for relevant architecture notes using Obsidian MCP.
- [ ] **Verify** implementation plans against existing architecture and open questions in the vault.
- [ ] **Synchronize** documentation by updating relevant `.md` files in the vault after code changes are verified.
```

## Source of Truth
- **Code** is the source of truth for execution.
- **Obsidian Vault (`docs/`)** is the source of truth for **intent** and **architecture**.

If the code and the docs disagree, the next task must be to resolve the discrepancy.
