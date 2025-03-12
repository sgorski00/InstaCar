# Instrukcja instalacji make na Windows:
# 1. Otworzyć powershell jako administrator
# 2. Uruchomić komendę Set-ExecutionPolicy AllSigned, zatwierdzić [A]
# 3. Zainstalować Chocolatey:
# Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
# 4. Zainstalować make przy użyciu komendy:
# choco install make

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

redis:
	docker exec -it redis redis-cli

flyway-clean:
	docker exec -it app mvn flyway:clean

flyway-migrate:
	docker exec -it app mvn flyway:migrate

flyway-info:
	docker exec -it app mvn flyway:info

restart:
	docker compose restart

test:
	docker exec -it app mvn test

test-class:
	docker exec -it app mvn test -Dtest=${class}
