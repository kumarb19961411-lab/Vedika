# Firebase Rules Test Matrix (V2.1)

| Actor / Scenario | Target Resource | Expected Outcome |
| :--- | :--- | :--- |
| **Auth'd Consumer** | `bookings` (Create self with valid fields) | `ALLOW` |
| **Auth'd Consumer** | `bookings` (Create self but includes `totalAmount`) | `DENY` |
| **Auth'd Consumer** | `bookings` (Update own booking `status` to CANCELLED) | `ALLOW` |
| **Auth'd Consumer** | `bookings` (Update own booking `eventDate`) | `DENY` |
| **Auth'd Vendor** | `bookings` (Create manual booking, `status` == CONFIRMED) | `ALLOW` |
| **Auth'd Vendor** | `bookings` (Create manual booking, includes `userId`) | `DENY` |
| **Auth'd Vendor** | `bookings` (Update booking `status` to CONFIRMED) | `ALLOW` |
| **Auth'd Vendor** | `bookings` (Update booking `userId` or `eventDate`) | `DENY` |
| **Public** | `public_vendors/{vendorId}` | `ALLOW (Read)` |
| **Vendor** | `public_vendors/{vendorId}` | `DENY (Write)` |
| **Vendor** | `storage:/vendors/vid/private/kyc` (Upload PDF < 5MB) | `ALLOW` |
| **Vendor** | `storage:/vendors/vid/private/kyc` (Upload Video > 5MB) | `DENY` |
| **Auth'd Client** | `finance/{doc}` | `DENY` |
