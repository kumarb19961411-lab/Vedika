# Role Resolution Guide

## Problem
A phone number signs in via OTP. Is it a Vendor or a User?

## Resolution Logic
1. **Login Trigger:** OTP successful.
2. **Database Query:** Client checks `users/{uid}` and `vendors/{uid}`.
3. **Routing:**
   - If found in `vendors`, route -> Vendor Dashboard.
   - If found in `users`, route -> User Dashboard.
   - If neither, show Role Selection Screen (Vendor or User?).
