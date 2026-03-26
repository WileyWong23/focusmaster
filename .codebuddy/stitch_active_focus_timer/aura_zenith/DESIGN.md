# Design System Document

## 1. Overview & Creative North Star: "The Celestial Flow"
This design system is anchored in the concept of **"The Celestial Flow."** Rather than a static utility, the interface is treated as a living, breathing nebula of productivity. We move away from the rigid, "boxed-in" layout of traditional productivity apps to create an immersive, editorial experience that feels premium and focused.

**Creative Principles:**
*   **Intentional Asymmetry:** Break the predictable 12-column grid. Use the Spacing Scale to create "breathing pockets" where content feels curated rather than crowded.
*   **Tonal Depth:** We do not use lines to separate ideas. We use light and depth. Layers of varying navy and charcoal shades define the user's journey.
*   **The "Aura" Effect:** Emphasize focus and energy through soft, diffused glows (`primary_dim` and `tertiary_dim`) that emanate from active states, mimicking the energy of a deep-space star.

---

## 2. Colors & Surface Logic
The palette is rooted in a "Deep Space" foundation, using high-contrast rank colors to provide the "gamified" energy.

### The "No-Line" Rule
**Explicit Instruction:** Designers are prohibited from using 1px solid borders for sectioning or containment. Boundaries must be defined solely through background color shifts.
*   *Example:* A list of tasks should not have lines between them; instead, use a `surface_container_low` background for the list item sitting on a `surface` background.

### Surface Hierarchy & Nesting
Treat the UI as a series of physical layers—stacked sheets of frosted glass.
*   **Base Layer:** `surface` (#060e20)
*   **Secondary Sectioning:** `surface_container_low` (#091328)
*   **Active/Interactive Components:** `surface_container_high` (#141f38) or `surface_bright` (#1f2b49)
*   **The Nesting Rule:** When placing an element inside a container, the inner element must always move one step *higher* or *lower* in the surface tier to create definition.

### The "Glass & Gradient" Rule
To achieve the "Immersive" feel, floating elements (Modals, Hovering Timers) must use **Glassmorphism**:
*   **Fill:** `surface_variant` at 60% opacity.
*   **Backdrop Blur:** 12px to 20px.
*   **Signature Texture:** Use a subtle linear gradient for CTA buttons transitioning from `primary` (#81ecff) to `primary_dim` (#00d4ec) at a 135-degree angle.

### Rank Color Tokens (Gamification)
*   **Bronze:** Use `on_secondary_fixed_variant` (#665500)
*   **Silver:** Use `outline` (#6d758c)
*   **Gold:** Use `secondary` (#ffd709)
*   **Platinum:** Use `primary` (#81ecff) with high-reflectance `on_primary_container`
*   **Diamond:** Use `primary_dim` (#00d4ec)
*   **Starry:** Transition from `tertiary` (#a68cff) to `primary` (#81ecff)
*   **King:** Transition from `secondary` (#ffd709) to `error` (#ff6e84)

---

## 3. Typography: Editorial Authority
We use a high-contrast scale to move the app from "utility" to "experience." 

*   **Display (Manrope):** Used for "Big Moments"—timer completions, rank-ups, and daily summaries. `display-lg` (3.5rem) should be used with `-0.04em` letter spacing for a dense, high-fashion feel.
*   **Headline (Manrope):** Used for section titles. These should always be `on_surface` to command attention.
*   **Body (Inter):** The workhorse. Use `body-md` (0.875rem) for most text. Inter's tall x-height ensures readability against dark backgrounds.
*   **Labels (Inter):** Used for metadata and rank titles. Use `label-md` with `0.05em` letter spacing and All Caps for a "premium technical" look.

---

## 4. Elevation & Depth
In this design system, elevation is a product of light, not physics.

*   **Tonal Layering:** Avoid drop shadows for standard cards. Instead, use the `surface_container` tiers. 
*   **Ambient Shadows:** For floating action buttons or high-priority modals, use an extra-diffused shadow:
    *   *Blur:* 32px | *Spread:* -4px | *Color:* `surface_container_lowest` at 40% opacity.
*   **The Ghost Border Fallback:** If accessibility requires a stroke (e.g., in high-glare environments), use `outline_variant` at **15% opacity**. Never 100%.
*   **Glow States:** Focused elements (like a running timer) should have an outer glow using `surface_tint` (#81ecff) with a 20px blur at 10% opacity.

---

## 5. Components

### Buttons (The Energy Cells)
*   **Primary:** Gradient fill (`primary` to `primary_dim`). Roundedness: `full`. No border. Text: `on_primary`.
*   **Secondary:** `surface_container_highest` fill. Roundedness: `md`. 
*   **Tertiary:** Ghost style. No fill. Text: `primary`. On hover, a subtle `surface_bright` background fade.

### The Focus Timer (Signature Component)
*   **Styling:** Use `display-lg` typography. Surround the timer with a thin, non-solid "Progress Orbit" using a `tertiary` to `primary` gradient. Apply a `backdrop-blur` of 20px to the timer container.

### Cards & Lists
*   **Forbid Dividers:** Use `1.4rem` (Spacing 4) of vertical whitespace to separate items.
*   **Interactive Cards:** Use `surface_container_low`. On hover, shift to `surface_container_high` and add a subtle `primary` glow to the left edge (2px width).

### Input Fields
*   **Base:** `surface_container_lowest`. 
*   **Focus State:** The "Ghost Border" becomes 40% opaque `primary`. No heavy outlines.
*   **Error State:** Text shifts to `error`. A soft `error_container` glow appears behind the input.

---

## 6. Do's and Don'ts

### Do
*   **Do** use overlapping elements. A rank badge can slightly overlap the edge of a profile card to create depth.
*   **Do** use "Negative Space" as a functional element to reduce cognitive load during focus sessions.
*   **Do** use `on_surface_variant` for secondary information to maintain the dark-mode hierarchy.

### Don't
*   **Don't** use pure black (#000000) for anything other than `surface_container_lowest`. It kills the "Deep Navy" atmosphere.
*   **Don't** use standard "Material Design" shadows. They feel too heavy for this "Celestial" aesthetic.
*   **Don't** use 100% opaque borders. They create "visual noise" that interrupts the user's flow.
*   **Don't** use more than two font weights on a single screen. Let size and color do the heavy lifting.