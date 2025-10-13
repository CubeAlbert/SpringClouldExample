function GetInputOrDefault {
    $key = Read-Host
    if ($key.Length -eq 0) {
        Write-Host "Your input is empty, we will generate a random key for you:" -ForegroundColor Yellow
        $key = [System.Guid]::NewGuid().toString().Replace("-","")
    }
    if ($key.Length -lt 32) {
        Write-Host "Your input is too short, please input at least 32 characters:" -ForegroundColor Red
        $key = GetInputOrDefault
    }
    return $key
}

Write-Host "Please input your key(minimum 32 characters):" -ForegroundColor Yellow
$key = GetInputOrDefault

Write-Host "The original key is:" $key -ForegroundColor Green

#Base64
$base64key = [System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes($key))
Write-Host "The Base64 key is(Added to your clipboard):" -ForegroundColor Green
Write-Host $base64key
Set-Clipboard $base64key
