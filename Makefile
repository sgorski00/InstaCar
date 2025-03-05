# Instrukcja instalacji make na Windows:
# 1. Otworzyć powershell jako administrator
# 2. Uruchomić komendę Set-ExecutionPolicy AllSigned, zatwierdzić [A]
# 3. Zainstalować Chocolatey:
# Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
# 4. Zainstalować make przy użyciu komendy:
# choco install make

.PHONY: up down build logs psql bash restart

up:
	docker compose up -d

down:
	docker compose down

build:
	docker compose build

logs:
	docker compose logs -f

psql:
	docker exec -it db psql -U root -d InstaCar

bash:
	docker exec -it app sh

restart:
	docker compose restart
