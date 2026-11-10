### SRCH-01 - Search by full product name

**Purpose**

Verify that a user can find a product by entering its full name into the storefront search.

**Preconditions**

- Storefront is available.
- The product "Hummingbird printed t-shirt" exists in the catalog.
- Search is available from the storefront header or search entry point.

**Scenario**

1. Open the storefront home page.
2. Open the search input.
3. Enter the full product name.
4. Submit the search.
5. Observe the search results.

**Expected result**

- Search results are displayed.
- The matching product is present in the results list.
- The product name is shown in the result card or result list.

---

### SRCH-02 - Search by partial product name

**Purpose**

Verify that a user can find a product using a partial product name.

**Preconditions**

- Storefront is available.
- At least one product name can be matched by a meaningful partial term.
- Search is available from the storefront header or search entry point.

**Scenario**

1. Open the storefront home page.
2. Open the search input.
3. Enter a partial product name or keyword.
4. Submit the search.
5. Observe the search results.

**Expected result**

- Search results are displayed.
- Relevant products matching the partial term are included.
- Search results remain usable for navigation to a product page.

---

### SRCH-03 - Search with no matching results

**Purpose**

Verify that the storefront handles a search query with no matches in a user-friendly way.

**Preconditions**

- Storefront is available.
- Search is available from the storefront header or search entry point.

**Scenario**

1. Open the storefront home page.
2. Open the search input.
3. Enter a query that should not match any product.
4. Submit the search.
5. Observe the results area.
6. Modify the query so it matches at least one product and submit again to verify that results are displayed.

**Expected result**

- No products are displayed.
- A clear no-results message or empty state is shown.
- The user can refine the query or start another search.
- After refining the query to match a product, results are displayed as expected.

---

### SRCH-04 - Search from catalog page

**Purpose**

Verify that a user can start a search while browsing a catalog page and receive relevant results.

**Preconditions**

- Storefront is available.
- The Clothes category is accessible.
- Search is available from the catalog page or its shared header.

**Scenario**

1. Open the storefront home page.
2. Open the Clothes category.
3. Start a search from the catalog page context.
4. Enter a product name or keyword.
5. Submit the search.
6. Observe the search results.

**Expected result**

- Search results are displayed.
- Relevant products are returned.
- The search flow works from within the catalog browsing context.

---

### SRCH-05 - Navigate through search pagination

**Purpose**

Verify that a user can navigate through multiple pages of search results when the result set exceeds the per-page limit.

**Preconditions**

- Storefront is available.
- Search results contain more products than the per-page limit.
- Pagination controls are available when results span multiple pages.

**Scenario**

1. Open the storefront home page.
2. Perform a search that returns multiple results.
3. Verify that pagination is not visible if results fit on a single page.
4. Adjust the results per page to trigger pagination (e.g., display 8 results per page).
5. Verify that pagination controls are now visible.
6. Navigate to the page 2 using pagination controls.
7. Observe the results on the new page.

**Expected result**

- Initially, pagination controls are hidden when all results fit on one page.
- When results exceed the per-page limit, pagination controls become visible.
- Navigating to another page displays the correct set of results for that page.
- The page counter or pagination indicators reflect the current page.
- Results are properly scoped to the selected page.

**Notes**

- The number of available products and per-page result limit affect pagination visibility; this scenario may be sensitive to catalog data changes.
- The exact pagination UI (e.g., page numbers, next/previous buttons) may vary by storefront theme.

