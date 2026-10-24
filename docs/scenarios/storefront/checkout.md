### CHK-01 — Guest checkout page structure and checkout flow

**Purpose**

Verify that a guest user can open the checkout flow and go through it without being logged in, and that the checkout page structure is correct.

**Preconditions**

- Storefront is available.
- At least one orderable product can be added to cart.
- Guest checkout is enabled for the storefront.

**Scenario**

1. Open the storefront home page.
2. Open any orderable product.
3. Add the product to cart.
4. Navigate to the cart page.
5. Proceed to checkout as a guest.
6. Validate the general checkout page structure.
7. Validate personal information section structure
8. Fill personal information section with valid guest data and continue to the next step.
9. Validate addresses section structure
10. Fill addresses section with valid guest data and continue to the next step.
11. Validate shipping method section structure
12. Fill shipping method section with valid data and continue to the next step.
13. Validate payment section structure
14. Select a payment method and finish placing order.
15. Validate order confirmation page structure and content.

**Expected result**

- The checkout page is displayed.
- The checkout flow steps show the expected guest-checkout context.
- Core checkout blocks are present, such as personal information, address/shipping section and shipping method section.
- The page is usable for the guest checkout flow.
- After placing the order, the order confirmation page is displayed with the correct information.

**Coverage**

- Cart-to-checkout navigation
- Guest checkout flow
- Checkout page core layout

**Notes**

- The exact section titles and arrangement may vary slightly by theme or PrestaShop version, but the guest checkout flow should remain understandable and actionable.

