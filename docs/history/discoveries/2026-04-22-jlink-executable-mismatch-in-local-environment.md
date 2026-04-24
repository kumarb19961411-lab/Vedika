---
title: jlink executable mismatch in local environment
category: tool
impact: high
created: '2026-04-22T21:47:48.629Z'
updated: '2026-04-22T21:47:48.629Z'
source: agent
tags:
  - gradle
  - jlink
  - jdk
  - windows
project: 'Milestone 4: Stabilization & Test Foundation'
---
## Discovery

jlink executable mismatch in local environment. Gradle attempts to use a JRE from an extension ('redhat.java') that lacks jlink.exe, causing build failures during 'JdkImageTransform' even when 'org.gradle.java.home' is set in 'gradle.properties'.

## Recommendation

Ensure the environment's JAVA_HOME is consistently honored or explicitly point to a full JDK in the shell execution context. Use 'gradlew -Dorg.gradle.java.home=...' with proper quoting in PowerShell.
