GitHub Actions CI mapping for selenium_pipeline_cp

Secrets and permissions
- GHCR (GitHub Container Registry): use GITHUB_TOKEN by default. If pushing/pulling across organizations or needing finer scope, create a Personal Access Token with `write:packages, read:packages` and store it as GHCR_PAT in repository secrets.
- Required repository secrets (recommended):
  - GHCR_PAT (optional) — PAT for ghcr.io if GITHUB_TOKEN is insufficient

Workflow inputs -> mapped variables
- build-images.yml
  - image-tag -> IMAGE_TAG (used to tag images pushed to ghcr)

- tests-run.yml
  - browser -> BROWSER (chrome|firefox|edge)
  - threads -> THREADS (parallel nodes to start)
  - tags -> TAGS (JUnit @Tag to include; defaults to 'regression')
  - image-tag -> IMAGE_TAG (image tag to pull from ghcr)

Environment and runtime variables
- DOWNLOADS_FOLDER: Downloads (created during workflow run)
- Use Grid flag: -DuseGrid=true passed to Gradle to enable remote WebDriver usage

Artifacts
- public-archives.tar.gz contains public/allureReport and public/Downloads. Uploaded as artifact 'public-artifacts' with 3-day retention.
- Container logs uploaded as artifact 'container-logs' with 3-day retention.

Notes about permissions and runners
- Workflows use ubuntu-latest runners and require Docker and docker-compose to be available. The workflows install docker-compose; ensure runner image/permissions are compatible.
- For heavy Docker workloads or if privileged access is required, consider using a self-hosted runner with Docker installed.

Manual run examples (using gh CLI)
- Build images with tag 'ci-20260410':
  gh workflow run build-images.yml -f image-tag=ci-20260410

- Run tests with Firefox, 2 threads, using latest images:
  gh workflow run tests-run.yml -f browser=firefox -f threads=2 -f tags=regression -f image-tag=latest

Updating secrets
- To set GHCR_PAT (if needed):
  - Go to repo Settings > Secrets > Actions > New repository secret
  - Name: GHCR_PAT
  - Value: <personal access token with write:packages, read:packages>

