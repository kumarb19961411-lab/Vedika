---
title: Windows Execution Policy Guide
type: guide
status: active
owner: QA Reliability Engineer
last_updated: 2026-04-22
---

# 🛡️ Windows Execution Policy Guide

If you see an error like `File C:\Program Files\nodejs\npx.ps1 cannot be loaded because running scripts is disabled on this system`, it means your Windows PowerShell execution policy is too restrictive for running Node.js tools.

## The Problem
By default, Windows prevents PowerShell scripts from running as a security measure. Since `npx` and `npm` on Windows are actually PowerShell scripts, they are blocked by this policy.

## The Fix (Permanent)

To resolve this for your user account, run the following command in an **Admin PowerShell** window:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### What this does:
- **RemoteSigned**: Allows scripts created on your machine to run, and requires scripts from the internet to be signed by a trusted publisher.
- **Scope CurrentUser**: Only affects your user account, not the entire system.

## The Workaround (Temporary)

If you cannot change the policy, you can use the `.cmd` version of Node tools which bypasses PowerShell entirely:

Instead of:
```bash
npx mcp-obsidian-vault
```

Use:
```bash
npx.cmd mcp-obsidian-vault
```

## Verification
After applying the fix, verify that `npx` works by running:
```bash
npx --version
```
If it returns a version number instead of an error, the policy has been correctly updated.
