---
tags:
  - guide
  - vendor
  - dashboard
  - hardening
---
# 💎 Vendor Dashboard Hardening

This document details the hardening efforts for the Vendor Dashboard to ensure performance and reliability.

## 🧱 Data Flow
The dashboard uses a reactive pattern where `DashboardViewModel` observes multiple data streams:
- **Bookings**: Real-time stream from `/bookings` filtered by vendor ID.
- **Inventory Status**: Summary of available vs. occupied units.
- **Inquiries**: Pending customer inquiries.

## 🛠️ Hardening Measures
1. **Shimmer States**: Implemented consistent loading skeletons to improve perceived performance.
2. **Error Boundaries**: Granular error handling for each dashboard section (e.g., if bookings fail to load, inventory can still be displayed).
3. **Empty States**: Curated empty state illustrations and call-to-actions for new vendors.

## 📊 Metrics Calculation
- **Total Earnings**: Sum of `amount` from confirmed bookings.
- **Occupancy Rate**: Percentage of inventory units currently booked for the selected period.

---
[[Vendor_Flow_Hub|🏢 Vendor Flow Hub]] | [[Project_Hub|🏠 Project Hub]]
