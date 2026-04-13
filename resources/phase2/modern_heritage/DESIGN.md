# Design System Strategy: Modern Heritage

## 1. Overview & Creative North Star
The Creative North Star for this design system is **"The Digital Heirloom."** 

Unlike generic event-planning platforms that feel like utility tools, this system is designed to feel like an invitation to a premium experience. We are blending the warmth of Indian hospitality with the precision of modern editorial design. We break the "template" look by utilizing intentional asymmetry—placing high-end serif typography against generous white space and overlapping decorative motifs (mandala and paisley) with clean, rounded UI containers. 

The goal is to move away from rigid, boxy grids toward a "fluid editorial" layout where content feels curated, not just listed. This is achieved through a dramatic contrast in scale, using oversized display type and deep, tonal layering that guides the user’s eye with authority and grace.

---

## 2. Color & Surface Philosophy
The palette is a sophisticated reimagining of traditional festive hues, balanced by a warm, paper-like neutral base.

*   **Primary (#8f4e00 / Deep Saffron):** Used for primary actions and moments of celebration. 
*   **Secondary (#006a6a / Rich Teal):** Provides a grounding, trustworthy contrast to the warmth of the saffron.
*   **Tertiary (#795900 / Marigold Gold):** Reserved for highlights, luxury badges, and decorative accents.
*   **The "No-Line" Rule:** We strictly prohibit the use of 1px solid borders to define sections. All separation must be achieved through background shifts. For example, a search section using `surface_container_low` should sit directly against a `surface` background. 
*   **Surface Hierarchy:** We treat the UI as stacked sheets of fine handmade paper. Use `surface_container` tiers (Lowest to Highest) to create depth. A card component should use `surface_container_lowest` to "lift" off a `surface_container_low` background naturally.
*   **Glass & Gradient Rule:** For floating headers or mobile navigation bars, use **Glassmorphism**. Apply `surface` at 80% opacity with a 16px backdrop-blur. Use subtle gradients (Primary to Primary-Container) for Hero CTAs to add "soul" and depth that flat color lacks.

---

## 3. Typography
The typography system relies on the interplay between the authoritative **Noto Serif** and the contemporary **Plus Jakarta Sans**.

*   **Display & Headlines (Noto Serif):** These are the "voice" of the brand. Use `display-lg` for hero sections and `headline-md` for event categories. Large-scale serif text communicates heritage and premium quality.
*   **Titles & Body (Plus Jakarta Sans):** Used for functional clarity. `title-md` is perfect for property names (Function Halls, Catering Services), while `body-lg` ensures high readability for service descriptions.
*   **The Hierarchy Strategy:** Establish a clear visual anchor. A massive `display-sm` headline should often be paired with a much smaller, uppercase `label-md` to create a "High-Fashion Editorial" look.

---

## 4. Elevation & Depth
We move away from the "shadow-on-everything" approach of the early 2010s in favor of **Tonal Layering**.

*   **Layering Principle:** Instead of shadows, use the color tokens. Place a `surface_container_highest` card inside a `surface` section to create a soft, recessed look.
*   **Ambient Shadows:** For elements that truly "float" (like the "Book Now" floating action button), use an extra-diffused shadow: `box-shadow: 0 12px 32px rgba(30, 27, 19, 0.06)`. Note the use of a tinted shadow color—never pure black.
*   **The Ghost Border:** If a form input requires a boundary for accessibility, use the `outline_variant` token at **20% opacity**. It should be felt, not seen.
*   **Visual Accents:** Incorporate mandala motifs as semi-transparent watermarks behind containers, using `outline_variant` at 5% opacity. This roots the modern layout in its cultural context.

---

## 5. Components

### Buttons
*   **Primary:** Rounded (ROUND_EIGHT), `primary` background with `on_primary` text. Use a subtle Marigold gradient for a "gold-leaf" finish on hover.
*   **Tertiary:** No background or border. Use `primary` text weight 600 with a subtle paisley icon as a trailing element.

### Cards & Lists
*   **Modern Heritage Cards:** Use `surface_container_lowest` and `ROUND_EIGHT`. **Forbidden:** Divider lines. 
*   **Separation:** Use `spacing-6` (2rem) of vertical white space or a subtle shift to `surface_container_low` to separate service items like "Catering" from "Photography."

### Inputs & Search
*   **Text Fields:** Use a "pill" shape (`rounded-full`) for the search bar to feel modern. The background should be `surface_variant` to distinguish it from the page background without using a border.

### Chips & Tags
*   **Category Chips:** Use for "Wedding," "B-day," or "Priest." These should use `secondary_container` with `on_secondary_container` text to provide a refreshing teal "pop" against the warm saffron theme.

### Featured Components (App Specific)
*   **Service Curators:** A horizontal scrolling carousel where cards overlap slightly, using the Layering Principle to show hierarchy.
*   **Heritage Divider:** Instead of a line, use a small, centered Paisley or Mandala icon with 40px of white space on either side.

---

## 6. Do's and Don'ts

### Do:
*   **DO** use whitespace as a luxury. Middle and upper-middle-class users associate "breathing room" with premium service.
*   **DO** use high-contrast scale in typography (very large vs. very small).
*   **DO** ensure all touch targets for booking services use the `ROUND_EIGHT` corner radius for a friendly, approachable feel.

### Don't:
*   **DON'T** use 1px black or grey borders. They break the "Modern Heritage" illusion and make the UI look like a basic template.
*   **DON'T** use generic "Material Blue" or "System Grey." Stick strictly to the saffron, teal, and off-white palette.
*   **DON'T** clutter the screen with icons. Use icons sparingly, and ensure they are thin-stroke, high-quality vectors that match the editorial aesthetic.