### CAT-01 — Check catalog page structure

**Purpose**

Verify that the catalog page opens correctly and key catalog blocks are displayed.

**Preconditions**

- Storefront is available.
- The "Clothes" category exists and is accessible from the top navigation.

**Scenario**

1. Open the storefront home page.
2. Open the Clothes category.
3. Validate the catalog page structure.

**Expected result**

- The catalog page is displayed.
- The category heading is visible.
- The product list section is visible.
- At least one product card is displayed.

**Coverage**

- Storefront navigation
- Category landing page
- Catalog core UI blocks

### CAT-02 — Browse products in a category

**Purpose**

Verify that a user can browse products inside a category.

**Preconditions**

- The "Clothes" category contains at least two products.

**Scenario**

1. Open the Clothes category.
2. Inspect the product grid.
3. Move through visible products in the grid.

**Expected result**

- Products are displayed as a list/grid.
- Product cards contain basic information (name and price).
- User can access multiple products from the category page.

**Coverage**

- Catalog browsing
- Product grid
- Product card rendering

### CAT-03 — Open product details page

**Purpose**

Verify that a user can open a product details page from the category listing.

**Preconditions**

- The "Clothes" category contains at least one product.

**Scenario**

1. Open the Clothes category.
2. Click any product card.
3. Wait for product details page to load.

**Expected result**

- Product details page is displayed.
- Product title is visible.
- Main purchase block (price/add to cart area) is present.

**Coverage**

- Product navigation
- PDP opening
- Category to PDP transition

### CAT-04 — Change product sorting

**Purpose**

Verify that sorting options change the order of products on the catalog page.

**Preconditions**

- The "Clothes" category contains enough products for sorting differences.
- Sorting control is available on the category page.

**Scenario**

1. Open the Clothes category.
2. Capture the initial order of visible products.
3. Select another sorting option (for example, price or name).
4. Observe the updated product order.

**Expected result**

- Selected sorting option is applied.
- Product order is updated accordingly.
- Product list remains visible and interactive.

**Coverage**

- Catalog controls
- Sorting behavior
- Product ordering

### CAT-05 — Navigate through catalog pagination

**Purpose**

Verify that a user can navigate between catalog pages using pagination controls.

**Preconditions**

- Storefront is available.
- The "Accessories" category is accessible.
- Category contains more products than the per-page limit when configured.

**Scenario**

1. Open the storefront home page.
2. Open the Accessories category.
3. Verify that pagination is not visible if results fit on a single page.
4. Set the per-page result limit to 8 products to trigger pagination.
5. Verify that pagination controls are now visible.
6. Navigate to page 2 using pagination controls.
7. Observe the results on the new page.

**Expected result**

- Initially, pagination controls are hidden when all results fit on one page.
- When results exceed the per-page limit, pagination controls become visible.
- Navigating to another page displays the correct set of results for that page.
- The page counter or pagination indicators reflect the current page.
- Results are properly scoped to the selected page.

**Coverage**

- Pagination
- Catalog navigation
- Multi-page product lists

### CAT-06 — Navigate using breadcrumbs

**Purpose**

Verify that a user can return to the parent category using
the breadcrumb navigation.

**Preconditions**

- The "Clothes" category contains at least one product.

**Scenario**

1. Open the Clothes category.
2. Open any product.
3. Navigate back to Clothes using the breadcrumb.

**Expected result**

- The Clothes category page is displayed.
- The category contains products.

**Coverage**

- Storefront navigation
- Breadcrumbs
- Product to category navigation

### CAT-07 — Filter products by price

**Purpose**

Verify that price filtering narrows product results according to selected range.

**Preconditions**

- The category contains products with different prices.
- Price filter control is available.

**Scenario**

1. Open the Clothes category.
2. Apply a price filter/range.
3. Observe filtered product results.

**Expected result**

- Product list is updated after applying price filter.
- Displayed products match selected price constraints.
- Active filter state is visible in the UI.

**Coverage**

- Faceted navigation
- Price filtering
- Result refinement

### CAT-08 — Filter products by manufacturer

**Purpose**

Verify that manufacturer filtering narrows product results to selected brand(s).

**Preconditions**

- The category contains products from at least two manufacturers.
- Manufacturer filter control is available.

**Scenario**

1. Open the Clothes category.
2. Apply a manufacturer filter.
3. Observe filtered product results.

**Expected result**

- Product list is updated after applying manufacturer filter.
- Displayed products belong to selected manufacturer(s).
- Active filter state is visible in the UI.

**Coverage**

- Faceted navigation
- Manufacturer filtering
- Result refinement
