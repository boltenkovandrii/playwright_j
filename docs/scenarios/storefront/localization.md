### LOCA-01 — Switch storefront language

**Purpose**

Verify that a user can change the active storefront language through the language selector and that the selected language is applied to the current page.

**Preconditions**

- Storefront is available.
- At least two storefront languages are enabled.
- The storefront is initially displayed in English.
- The target language has translations configured for the storefront header/navigation.

**Steps**

1. Open the storefront home page.
2. Verify that the current language is English.
3. Open the storefront language selector.
4. Select the configured non-English language.
5. Wait for the page to finish updating.
6. Verify that the language selector now shows the selected language.
7. Verify that at least two known shared UI labels are displayed in the selected language.
8. Navigate to the Catalog page.
9. Verify that the selected language is still active.

**Expected result**

- The language selector allows the user to select the configured language.
- The storefront switches to the selected language without an error.
- The selected language is reflected by the language selector.
- Known translated shared UI elements are displayed in the selected language.
- After navigating to the Catalog page, the selected language remains active.

**Coverage**

- Language selector
- Language switching
- Immediate application of locale
- Locale persistence across navigation

---


### LOCA-02 — Preserve storefront language across core pages

**Purpose**

Verify that the selected storefront language remains active when navigating through the main storefront areas.

**Preconditions**

- At least two storefront languages are enabled.
- English and one non-English language have translations configured.
- Required test data exists for catalog/product/cart/checkout navigation.

**Steps**

1. Open the storefront home page.
2. Switch the storefront language from English to the configured non-English language.
3. Verify the selected language on the Home page.
4. Navigate to a Catalog page.
5. Verify the selected language is still active and shared navigation is translated.
6. Open a Product page.
7. Verify the selected language is still active.
8. Add the product to the cart.
9. Open the Cart page.
10. Verify the selected language is still active.
11. Proceed to Checkout.
12. Verify that checkout UI is displayed in the selected language.

**Expected result**

- The selected language remains active across all visited pages.
- Shared storefront elements remain translated after navigation.
- No page unexpectedly falls back to English.
- The language selection survives the normal storefront browsing flow.

**Coverage**

- Home → Catalog → Product → Cart → Checkout

---

**LOCA-03 — Verify localization of core storefront pages**

**Purpose**

Verify that the main storefront pages expose localized user-facing UI when a non-default language is selected.

**Preconditions**

- A non-English storefront language is enabled.
- Known translations exist for the selected language.
- The required test data/pages are available.

**Steps**

1. Switch the storefront to the configured non-English language and verify representative localized UI on each page:

| Page | Verify |
|------|--------|
| Home | Header/navigation and at least one page-specific label |
| Catalog | Page heading, sorting/filtering controls and product-list UI |
| Product | Add-to-cart control, quantity/product-related labels |
| Search | Search input/button and search-result/empty-result UI |
| Cart | Cart heading, quantity/remove/checkout controls |
| Checkout | Checkout step headings and primary action labels |
| Login | Login heading, field labels and submit button |
| Registration | Registration heading, field labels and submit button |
| Account | Account heading and account-navigation/action labels |

**Expected result**

- Each page is accessible in the selected language.
- The selected language is reflected in the page's user-facing UI.
- Representative page-specific labels are translated.
- Shared elements such as header/navigation remain translated across pages.
- No page in the covered set unexpectedly displays English fallback text for the verified labels.

**Coverage**

- Home
- Account
- Cart
- Catalog
- Checkout
- Login
- Product
- Registration
- Search




  
