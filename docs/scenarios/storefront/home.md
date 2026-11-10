### HOME-01 - Check home page structure

**Purpose**

Verify that the storefront home page opens correctly and core layout blocks are visible.

**Preconditions**

- Storefront is available.

**Scenario**

1. Open the storefront home page.
2. Validate the home page structure.

**Expected result**

- The home page is displayed.
- The key home page blocks are visible.
- Shared layout elements required by the page object structure check are present.

---

### HOME-02 - Check featured products block structure

**Purpose**

Verify that the featured products section is displayed correctly on the home page.

**Preconditions**

- Storefront is available.
- Featured products section is enabled in the storefront configuration.

**Scenario**

1. Open the storefront home page.
2. Validate the featured products section structure.

**Expected result**

- The featured products section is visible.
- Featured product cards are rendered according to the component structure checks.

---

### HOME-03 - Navigate to category from main menu

**Purpose**

Verify that a user can open the Clothes category from the home page navigation and land on the expected catalog view.

**Preconditions**

- Storefront is available.
- The "Clothes" category exists and is accessible from the main navigation.
- The category contains "Men" and "Women" subcategories.

**Scenario**

1. Open the storefront home page.
2. Open the "Clothes" category from the main menu.
3. Validate the opened category page.

**Expected result**

- The catalog page for "Clothes" is displayed.
- The category name is "Clothes".
- The subcategories list includes "Men" and "Women".
- Displayed results count is 2.
- The catalog page structure is valid.

**Notes**

- This scenario is data-dependent and may fail if demo catalog structure changes.

---

### HOME-04 - Open featured product details page

**Purpose**

Verify that a user can open a product details page from the featured products section on the home page.

**Preconditions**

- Storefront is available.
- The featured product "Hummingbird printed t-shirt" is present on the home page.

**Scenario**

1. Open the storefront home page.
2. Open product "Hummingbird printed t-shirt" from featured products.
3. Validate the product details page.

**Expected result**

- Product details page is displayed.
- Product name is "Hummingbird printed t-shirt".
- Product page structure is valid.

**Notes**

- This scenario is sensitive to demo catalog changes (renamed or replaced featured product).

