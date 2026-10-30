
#$model = "gemma4:e4b"
$model = "qwen3.5:9b"
$baseUrl = "http://localhost:11434"

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

# Reuse an existing server; start one only if needed.
if (Test-Ollama) {
    Write-Host "Ollama is already running."
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
        throw "Ollama did not become available at $baseUrl. Check whether another process owns port 11434."
    }
}

# Ensure the model is installed.
$models = (Invoke-RestMethod "$baseUrl/api/tags").models.name
if ($models -notcontains $model) {
    ollama pull $model
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to pull model $model"
    }
}

# Configure and launch Copilot.
$env:COPILOT_PROVIDER_TYPE = "openai"
$env:COPILOT_PROVIDER_BASE_URL = "http://localhost:11434/v1"
$env:COPILOT_PROVIDER_API_KEY = "ollama"
$env:COPILOT_PROVIDER_WIRE_API = "completions"
#$env:COPILOT_PROVIDER_WIRE_API = "responses"
$env:COPILOT_MODEL = $model
#$env:COPILOT_PROVIDER_MAX_PROMPT_TOKENS = "32768"
#$env:COPILOT_PROVIDER_MAX_OUTPUT_TOKENS = "4096"



copilot
#copilot --reasoning-effort none