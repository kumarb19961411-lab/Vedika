---
title: Obsidian MCP Setup Guide
type: guide
status: active
owner: Product Architect
last_updated: 2026-04-20
---

# Obsidian MCP Setup Guide

This guide details how to integrate the **Piotr1215/mcp-obsidian** server into your local development workflow. This integration allows AI agents (like Antigravity) to read, search, and update the Vedika documentation vault (`docs/`) directly during coding tasks.

## Prerequisites
- **Node.js** (v18+) and **npm** (Installed: Node v24.14.1, npm v11.11.0)
- An MCP-compatible AI client (e.g., Claude Desktop, Cursor, or VS Code with MCP extension).

## Installation

Run the following command to ensure the MCP server is available:

```bash
# We recommend using npx to run the latest version, 
# but you can also install it globally if preferred.
npx @piotr1215/mcp-obsidian@latest
```

## Configuration

Add the following configuration to your AI client's MCP settings file (e.g., `%APPDATA%\Claude\claude_desktop_config.json`).

> [!IMPORTANT]
> Replace the `OBSIDIAN_VAULT_PATH` with your actual absolute path to the `docs` folder.

```json
{
  "mcpServers": {
    "obsidian": {
      "command": "npx",
      "args": [
        "-y",
        "@piotr1215/mcp-obsidian@latest"
      ],
      "env": {
        "OBSIDIAN_VAULT_PATH": "C:\\Users\\Welcome\\Documents\\GitHub\\Vedika\\docs"
      }
    }
  }
}
```

### Why use repo-local `docs/`?
We point the MCP server to the `docs/` folder inside the Vedika repository rather than a global Obsidian vault. This ensures:
1. **Context Isolation**: The AI only sees documentation relevant to this project.
2. **Version Control**: Every documentation update made by the AI is captured in the git history alongside the code changes.
3. **Architecture Sync**: Implementation plans and architecture decisions move together with the feature modules.

## Verification
Once configured, ask your AI agent:
> "List the notes in my Obsidian vault using the obsidian MCP tools."

If configured correctly, the agent should return a list of files from your `docs/` directory.

## Troubleshooting
- **Path Issues**: On Windows, ensure you use double backslashes (`\\`) in the JSON config path.
- **Node Permissions**: If `npx` fails, ensure your terminal has execution permissions (see `docs/guides/windows_execution_policy.md` if applicable).
- **Restart Client**: Most AI clients require a full restart to pick up changes in the `claude_desktop_config.json`.
