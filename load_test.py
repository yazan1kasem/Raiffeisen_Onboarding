import asyncio
import httpx
import random
import string
from faker import Faker

BASE_URL = "http://localhost:8081"
NUM_SIGNUP = 10
NUM_LOGIN = 10
NUM_CHECKLIST = 10

fake = Faker()

# Speichert Benutzer und ihre JWT-Tokens
users = []
tokens = []

async def signup():
    """Registriert einen Benutzer und speichert das Passwort-Hash."""
    # Zufälliger Username erstellen
    username = fake.user_name() + ''.join(random.choices(string.digits, k=3))  # Zufälliger Username
    password = "root"

    async with httpx.AsyncClient() as client:
        response = await client.post(f"{BASE_URL}/auth/signup", json={"username": username, "password": password})

        if response.status_code == 200:
            # Extrahieren des Passwort-Hashes aus der Antwort
            response_json = response.json()  # Die Antwort als JSON lesen
            hashed_password = response_json.get('password')  # Hier gehen wir davon aus, dass der Hash im 'password' Feld ist

            if hashed_password:
                # Benutzer mit Hash-Passwort speichern
                users.append({
                    "username": username,
                    "password": password,
                    "hash_password": hashed_password
                })
                print(f"✅ Signup erfolgreich: {username}")
            else:
                print(f"❌ Kein Passwort-Hash in der Antwort gefunden.")
        else:
            print(f"❌ Signup fehlgeschlagen: {response.text}")

async def login():
    """Loggt einen Benutzer ein und speichert den JWT-Token"""
    user = random.choice(users)  # Wähle einen zufälligen registrierten Benutzer
    async with httpx.AsyncClient() as client:
        response = await client.post(f"{BASE_URL}/auth/login", json={"username": user["username"], "password": user["password"]})
        if response.status_code == 200:
            token = response.json().get("token")
            tokens.append(token)
            print(f"🔑 Login erfolgreich: {user['username']}")
        else:
            print(f"❌ Login fehlgeschlagen: {response.text}")

async def create_userchecklist():
    """Erstellt eine UserChecklist"""
    user = random.choice(users)  # Wähle einen zufälligen registrierten Benutzer
    token = random.choice(tokens)  # Wähle einen zufälligen JWT-Token

    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    payload = {
        "ueberschrift": "new Checklist",
        "originalChecklist": {
            "id": "1",
            "position": "Manager",
            "abteilungsname": "Manager",
            "items":[
                {
                    "id": "3",
                    "name": "mobile contact",
                    "type": "Hardware",
                    "suchbegriff": "ITSM: A1 Voice SIM Card"
                }
            ]
        },
        "user": {
            "username": user["username"],
            "password": user["hash_password"],
            "createdAt": "2025-02-11T12:38:33.404+00:00",
            "updatedAt": "2025-02-11T12:38:33.404+00:00",
            "enabled": True,
            "role": "ADMIN",
            "credentialsNonExpired": True,
            "accountNonExpired": True,
            "accountNonLocked": True,
            "authorities": ["ROLE_ADMIN"]
        },
        "useritems": [
            {
                "originalItem": {
                    "id": "3",
                    "name": "mobile contact",
                    "type": "Hardware",
                    "suchbegriff": "ITSM: A1 Voice SIM Card"
                },
                "isChecked": False
            },
            # Weitere Useritems hier hinzufügen
        ],
        "status": "IN_PROGRESS",
        "createdAt": "2025-02-14T11:02:15.902967",
        "updatedAt": "2025-02-14T11:02:15.955726",
        "locked": False
    }

    async with httpx.AsyncClient() as client:
        response = await client.post(f"{BASE_URL}/userchecklist", headers=headers, json=payload)

        if response.status_code == 200:
            print("✅ UserChecklist erfolgreich erstellt")
        else:
            print(f"❌ Fehler beim Erstellen der UserChecklist: {response.text}")

async def get_userchecklist():
    """Holt die UserChecklists mit JWT-Token"""
    if not tokens:
        print("⚠️ Kein Token verfügbar, überspringe Anfrage.")
        return
    token = random.choice(tokens)  # Nutze einen zufälligen gespeicherten Token
    headers = {"Authorization": f"Bearer {token}"}
    async with httpx.AsyncClient() as client:
        response = await client.get(f"{BASE_URL}/userchecklist", headers=headers)
        if response.status_code == 200:
            print(f"📄 Erfolgreich UserChecklists geholt ({len(response.json())} Items)")
        else:
            print(f"❌ Fehler bei UserChecklists: {response.text}")

async def main():
    # 1️⃣ Signup für 100 Benutzer
    print("🚀 Starte Signup...")
    await asyncio.gather(*[signup() for _ in range(NUM_SIGNUP)])

    # 2️⃣ Login für 40 Benutzer
    print("\n🔑 Starte Login...")
    await asyncio.gather(*[login() for _ in range(NUM_LOGIN)])

    print("\n📝 Starte UserChecklist-Erstellung...")
    await asyncio.gather(*[create_userchecklist() for _ in range(NUM_CHECKLIST)])
    # 3️⃣ UserChecklists abrufen (100 Anfragen)
    print("\n📄 Starte UserChecklist-Abfragen...")
    await asyncio.gather(*[get_userchecklist() for _ in range(NUM_CHECKLIST)])

# Starte den Test
asyncio.run(main())
