### SRCH-01 — Search by full product name

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

**Coverage**

- Full-text product search
- Search results rendering
- Product discovery via search

**Notes**

- This scenario depends on the exact search behavior of the storefront and may be sensitive to indexing or catalog data changes.

---

### SRCH-02 — Search by partial product name

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

**Coverage**

- Partial-text search
- Search result relevance
- Product discovery via keyword search

**Notes**

- This scenario is useful for validating search tolerance, but exact matching rules may vary by backend/search implementation.

---

### SRCH-03 — Search with no matching results

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

**Coverage**

- Empty search results state
- Search validation behavior
- User feedback for no matches

**Notes**

- The exact no-results message may differ by localization and storefront theme.

---

### SRCH-04 — Search from catalog page

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

**Coverage**

- Search entry from catalog pages
- Shared header/search usability
- Cross-page storefront navigation

**Notes**

- This scenario helps verify that search is available consistently across storefront pages, not only on the home page.

---

### SRCH-05 — Navigate through search pagination

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

**Coverage**

- Search result pagination
- Multi-page result navigation
- Pagination visibility logic
- Per-page result configuration

**Notes**

- The number of available products and per-page result limit affect pagination visibility; this scenario may be sensitive to catalog data changes.
- The exact pagination UI (e.g., page numbers, next/previous buttons) may vary by storefront theme.

