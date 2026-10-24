### CART-01 — Check cart page structure

**Purpose**

Verify that the cart page opens correctly after adding a product and core cart UI blocks are present.

**Preconditions**

- Storefront is available.
- The "Clothes" category is accessible.
- At least one product can be added to cart.

**Scenario**

1. Open the storefront home page.
2. Open the Clothes category.
3. Open the first product from the listing.
4. Add the product to cart.
5. Navigate to the cart page.
6. Validate cart page structure.

**Expected result**

- Cart page is displayed.
- Core cart structure is present (cart list, totals/summary area, checkout-related controls).

**Coverage**

- Category -> PDP -> cart navigation
- Cart page core layout

---

### CART-02 — Add single product to cart

**Purpose**

Verify that adding one product creates a single cart line with quantity 1.

**Preconditions**

- Storefront is available.
- Product "Hummingbird printed t-shirt" is available in Clothes category.

**Scenario**

1. Open the storefront home page.
2. Open the Clothes category.
3. Open product "Hummingbird printed t-shirt".
4. Add the product to cart.
5. Navigate to the cart page.
6. Verify cart product count and item quantity.

**Expected result**

- Cart contains one product line.
- The product line for "Hummingbird printed t-shirt" has quantity 1.

**Coverage**

- Add-to-cart basic path
- Cart item/quantity verification

---

### CART-03 — Add multiple quantities from Product Page

**Purpose**

Verify that changing quantity on the product page affects quantity added to cart.

**Preconditions**

- Product "Hummingbird printed t-shirt" is available and orderable.

**Scenario**

1. Open product "Hummingbird printed t-shirt" from Clothes category.
2. Verify initial header cart count is 0.
3. Set product quantity to 3 on Product Page.
4. Add product to cart.
5. Navigate to the cart page.
6. Verify cart product count and quantity.

**Expected result**

- Cart contains one product line.
- The product line quantity is 3.

**Coverage**

- PDP quantity control -> cart transfer
- Cart quantity persistence

---

### CART-04 — Remove product from cart

**Purpose**

Verify that a user can remove an item and the cart becomes empty.

**Preconditions**

- Product "Hummingbird printed t-shirt" can be added to cart.

**Scenario**

1. Open product "Hummingbird printed t-shirt" from Clothes category.
2. Add product to cart.
3. Navigate to cart page.
4. Remove the product from cart.
5. Validate empty cart state.

**Expected result**

- Product line is removed from cart.
- Empty cart structure/message is shown.

**Coverage**

- Cart line removal
- Empty cart state

---

### CART-05 — Update product quantity in cart

**Purpose**

Verify that quantity updates in cart recalculate subtotal and total.

**Preconditions**

- Product "Hummingbird printed t-shirt" is available.
- Store pricing/tax configuration matches baseline expected by current test implementation.

**Scenario**

1. Open product "Hummingbird printed t-shirt" from Clothes category.
2. Add product to cart.
3. Navigate to cart page.
4. Verify initial quantity is 1.
5. Verify initial subtotal and total.
6. Update product quantity to 2 in cart.
7. Verify updated quantity, subtotal, and total.

**Expected result**

- Product quantity changes from 1 to 2.
- Cart subtotal and total are recalculated to expected values.

**Coverage**

- Quantity update on cart page
- Total recalculation

**Notes**

- This scenario currently depends on fixed numeric totals from demo data. It is sensitive to catalog price, currency, tax, and rounding configuration changes.

---

### CART-06 — Continue shopping from cart

**Purpose**

Verify that using "Continue shopping" from cart returns the user to shopping flow while preserving cart count.

**Preconditions**

- Product "Hummingbird printed t-shirt" is available.

**Scenario**

1. Open product "Hummingbird printed t-shirt" from Clothes category.
2. Add product to cart.
3. Navigate to cart page.
4. Click "Continue shopping".
5. Verify header cart count.
6. Add another product to cart and verify cart count increments.

**Expected result**

- User exits cart and returns to browsing context.
- Header cart count equals 1 after first add, and increments to 2 after second add.

**Coverage**

- Post-cart navigation
- Cart badge persistence

**Notes**

- Current automated coverage validates cart count persistence. A stricter destination assertion (URL/page identity) is a useful future extension.

---

### CART-07 — Verify cart totals for multiple variants

**Purpose**

Verify that cart totals and line quantities are correct when adding multiple variant combinations of the same product.

**Preconditions**

- Product "Hummingbird printed t-shirt" provides size and color combinations.
- Combinations M/Black and XL/White are available.
- Store pricing/tax configuration matches baseline expected by current test implementation.

**Scenario**

1. Open product "Hummingbird printed t-shirt" from Clothes category.
2. Select size M and color Black, set quantity to 2, add to cart, continue shopping.
3. Select size XL and color White, set quantity to 3, add to cart.
4. Navigate to cart page.
5. Verify two distinct cart lines by attributes.
6. Verify per-line quantities.
7. Verify products subtotal and total.

**Expected result**

- Cart contains two lines for the same product name with different attribute combinations.
- Quantities match configured values (2 and 3).
- Subtotal and total match expected values.

**Coverage**

- Variant handling in cart
- Multi-line totals validation
- Continue shopping flow between adds

**Notes**

- This scenario is intentionally data-dependent due to fixed expected totals in implementation; it may fail after catalog or tax changes without functional regression.
