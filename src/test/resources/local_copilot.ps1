
$model = "qwen3.5:9b"

#$contextSize = 16384
$contextSize = 32768
$keepAlive = "30m"

$baseUrl = "http://127.0.0.1:11434"
$providerName = "ollama-http-127.0.0.1-11434"

# ------------------------------------------------------------
# Configure Ollama server environment.
# These values apply when this script starts a new server.
# ------------------------------------------------------------

$env:OLLAMA_HOST = "127.0.0.1:11434"
$env:OLLAMA_CONTEXT_LENGTH = "$contextSize"

function Test-Ollama {
    try {
        Invoke-RestMethod `
            -Uri "$baseUrl/api/tags" `
            -TimeoutSec 3 `
            -ErrorAction Stop | Out-Null

        return $true
    }
    catch {
        return $false
    }
}

# ------------------------------------------------------------
# Start Ollama if necessary.
# ------------------------------------------------------------

if (Test-Ollama) {
    Write-Host "Ollama is already running."
    Write-Host "Will explicitly load $model with context $contextSize."
}
else {
    Write-Host "Starting Ollama server..."

    $ollama = (Get-Command "ollama.exe" -ErrorAction Stop).Source

    Start-Process `
        -FilePath $ollama `
        -ArgumentList "serve" `
        -WindowStyle Hidden

    $ready = $false

    for ($i = 0; $i -lt 30; $i++) {
        Start-Sleep -Seconds 1

        if (Test-Ollama) {
            $ready = $true
            break
        }
    }

    if (-not $ready) {
        throw "Ollama did not become available at $baseUrl."
    }

    Write-Host "Ollama server is ready."
}

# ------------------------------------------------------------
# Ensure the model is installed.
# ------------------------------------------------------------

$ollama = (Get-Command "ollama.exe" -ErrorAction Stop).Source

$models = (Invoke-RestMethod "$baseUrl/api/tags").models.name

if ($models -notcontains $model) {
    Write-Host "Downloading model $model..."

    & $ollama pull $model

    if ($LASTEXITCODE -ne 0) {
        throw "Failed to pull model $model"
    }
}

# ------------------------------------------------------------
# Stop an existing instance of this model.
# This allows Ollama to load it with the requested context.
# Other models are not stopped.
# ------------------------------------------------------------

Write-Host "Preparing model with context $contextSize..."

& $ollama stop $model 2>$null | Out-Null

# ------------------------------------------------------------
# Preload the model with the desired context.
# This also handles the case where Ollama was already running
# with a different server environment.
# ------------------------------------------------------------

$body = @{
    model      = $model
    prompt     = "Reply with OK."
    stream     = $false
    think      = $false
    keep_alive = $keepAlive
    options    = @{
        num_ctx     = $contextSize
        num_predict = 1
        temperature = 0
    }
} | ConvertTo-Json -Depth 6

$response = Invoke-RestMethod `
    -Method Post `
    -Uri "$baseUrl/api/generate" `
    -ContentType "application/json" `
    -Body $body `
    -TimeoutSec 180

Write-Host "Model loaded."
Write-Host "Response: $($response.response)"

# ------------------------------------------------------------
# Verify the actual loaded context.
# ------------------------------------------------------------

Write-Host "`nCurrent Ollama model status:"

& $ollama ps

# ------------------------------------------------------------
# Start Copilot with the configured model.
# providers.json contains the provider and token limits.
# ------------------------------------------------------------

Write-Host "`nStarting Copilot with $model..."

& copilot --model "$providerName/$model"
