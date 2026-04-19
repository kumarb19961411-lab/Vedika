# Archive Index

## Overview
This directory serves as the final resting place for all obsolete, discarded, or phase-specific documentation that was once canonical but has since been superseded.

## Guidelines for Archival
1. **Never Delete Historical Information**: Documentation representing key architectural battles (e.g., how the V1 backend migrated to V2) holds extreme value for future context.
2. **Name Prefixing**: If you move an active document here, add a prefix noting the version (e.g., `v2_auth_workflow.md`).
3. **Link Purging**: When moving a document to this archive namespace, ensure that no active elements in the system (`README.md`, `ProjectIndex.md`) point to it anymore. 

## Key Graveyard Files
- `v2_auth_workflow.md` - Original documentation representing manual navigation workflows before `AuthViewModel` strictly clamped deterministic states in Phase 3.
- `v2_scope.md` - Extremely early phase map defining purely UI logic parameters before the backend integration map was formalized. 
- `mock_data_flow.md` - Traces early testing flows.
