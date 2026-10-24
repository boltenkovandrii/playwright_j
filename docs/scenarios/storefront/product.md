### PDP-01 — Check product page structure

**Purpose**

Verify that the product details page opens correctly and all key UI blocks are present.

**Preconditions**

- Storefront is available.
- The category that contains at least one product.

**Scenario**

1. Open the storefront home page.
2. Open a category.
3. Open the first product from the listing.
4. Validate the product page structure.

**Expected result**

- The product details page is displayed.
- The product title is visible.
- The product image area is visible.
- The product information block (description area, price) is visible.
- The "Add to Cart" button is visible.
- The header and footer are present.

**Coverage**

- Product details page core layout
- Navigation from catalog to PDP
- Global page structure (header, footer)

**Notes**

- Profile dimension is relevant: header layout and product layout differ between desktop and mobile viewports.

---

### PDP-02 — View product information

**Purpose**

Verify that a user can read all key product information on the product details page.

**Preconditions**

- The product has a short description, price, and data on Description tab and Product Details tab.

**Scenario**

1. Open a product from a category.
2. Observe the product name.
3. Observe the short description in the product information block.
4. Observe the price.
5. Open the "Description" tab.
6. Observe the full description content.
7. Open the "Product Details" tab.
8. Observe the product specification content.

**Expected result**

- Product name is displayed prominently.
- Short description is visible in the product information block.
- Price is visible.
- "Description" tab opens and shows text content.
- "Product Details" tab opens and shows specification content.

**Coverage**

- Product description
- Product tabs
- Price display

**Notes**

- Tabbed content requires JavaScript. This scenario may be fragile if tab content is loaded asynchronously or changes with product data updates.

---

### PDP-03 — Change product quantity

**Purpose**

Verify that a user can change the product quantity before adding to cart.

**Preconditions**

- The product has a quantity input field with a default value of 1.

**Scenario**

1. Open a product from a category.
2. Observe the quantity input field (default value: 1).
3. Increase the quantity to 2 using arrows.
4. Observe the quantity field.
5. Decrease the quantity back to 1 using arrows.
6. Observe the quantity field.
7. Attempt to decrease the quantity below 1 (e.g., to 0 or negative) using arrows
8. Observe the quantity field.
9. Attempt to manually enter a value below 1 using direct input.
10. Observe the quantity field.
11. Attempt to manually enter a value of greater than 1 using direct input.
12. Observe the quantity field.

**Expected result**

- Quantity input is editable.
- After increasing, the field shows the updated value.
- After decreasing, the field reflects the corrected value.
- The quantity field does not accept values below the minimum (1).

**Coverage**

- Quantity input interaction
- Input validation (minimum quantity)

**Notes**

- Boundary behavior: entering 0 or a negative value should either be rejected or reset to the minimum. Whether this happens via validation or only on form submission should be verified against the live app during implementation.

---

### PDP-04 — Select product combination

**Purpose**

Verify that a user can select a different size and color combination and the page updates accordingly.

**Preconditions**

- The product has at least two size options (e.g., S, M, L, XL) and at least two color options (e.g., White, Black).

**Scenario**

1. Open the "Hummingbird printed t-shirt" from the Clothes category.
2. Observe the currently selected size (default: S) and color (default: White).
3. Select a different size (e.g., M).
4. Observe the page response.
5. Select a different color (e.g., Black).
6. Observe the page response.

**Expected result**

- Size selector shows available options (S, M, L, XL).
- Color selector shows available color options.
- Selecting a new size updates the selected state in the size control.
- Selecting a new color updates the selected state in the color control.
- The page does not lose the product context (title, add-to-cart button remain visible).

**Coverage**

- Product variant selection
- Size and color combination controls
- Page stability after variant change

**Notes**

- Combination changes update the URL fragment and may trigger an image reload. These side effects are worth asserting during implementation if the page object supports them.
- Selecting an unavailable combination (if any) may disable the add-to-cart button — worth noting as a boundary case.

---

### PDP-05 — Add product to cart

**Purpose**

Verify that a user can add a product to the cart and receive a visual confirmation.

**Preconditions**

- The selected product is in stock and the "Add to Cart" button is enabled.

**Scenario**

1. Open a product.
2. Note the product name and price.
3. Update item quantity 
4. Click "Add to Cart".
5. Observe the confirmation dialog/modal.

**Expected result**

- The cart confirmation modal (or overlay) appears.
- The modal shows the product name that was added.
- The modal shows the cart total and subtotal.
- The cart item count in the header updates to reflect the added item.

**Coverage**

- Add to cart flow
- Cart confirmation modal
- Cart counter update

**Notes**

- The cart modal is rendered by JavaScript after the add-to-cart request. It is not in the page source on initial load. This makes it a potential source of timing-related flakiness.
- If the product has required combinations (size/color), they must be selected before adding — the default pre-selected values should satisfy this on the "Hummingbird printed t-shirt".

---

### PDP-06 — Continue shopping after adding a product

**Purpose**

Verify that a user can dismiss the cart confirmation dialog and continue browsing on the product page.

**Preconditions**

- A product has been added to the cart and the cart confirmation modal is displayed.

**Scenario**

1. Open a product from a category.
2. Click "Add to Cart".
3. Wait for the cart confirmation modal to appear.
4. Click "Continue Shopping" (dismiss the modal).
5. Observe the current page.
6. Add another product to the cart and repeat the process to ensure user can add more items to the cart.

**Expected result**

- The cart confirmation modal closes.
- The user remains on the product details page (or is returned to it).
- The product information and "Add to Cart" button are still visible.
- The cart item count in the header reflects the added item.
- New items could be added to a cart after dismissing the modal.

**Coverage**

- Post-add-to-cart flow: continue shopping path
- Modal dismissal behavior
- Page state after modal close

---

### PDP-07 — Navigate to cart from the confirmation dialog

**Purpose**

Verify that a user can proceed from the cart confirmation dialog directly to the cart page.

**Preconditions**

- A product has been added to the cart and the cart confirmation modal is displayed.

**Scenario**

1. Open a product from a category.
2. Click "Add to Cart".
3. Wait for the cart confirmation modal to appear.
4. Click "Proceed to Checkout" in the modal.
5. Observe the resulting page.

**Expected result**

- The user is navigated to the cart page.
- The cart page shows the product that was added.
- The product name and price are visible in the cart.
- Cart totals are displayed.

**Coverage**

- Post-add-to-cart flow: checkout path
- Cart modal navigation
- Cart page content verification

**Notes**

- This scenario is a natural extension of PDP-06. Both PDP-07 and PDP-08 depend on the cart modal (PDP-06 precondition), so they can share setup steps with PDP-06 during implementation if the fixture supports it.
- The cart page structure is already verified by the existing `CartPage.check_structure()` method.

