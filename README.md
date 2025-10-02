# BlockSim Wallet

## 📌 Project Description

**BlockSim Wallet** is a **Java 8** console application that simulates a cryptocurrency wallet for **Bitcoin** and **Ethereum**.
It helps users manage wallets, create transactions, and optimize transaction fees according to network congestion and priority levels.

The application simulates a **mempool** and provides fee calculations to estimate transaction confirmation times.

Key concepts included in the project:

* **Transaction:** Transfer of cryptocurrency from one address to another.
* **Transaction Fees:** Paid to miners to prioritize transaction validation. Higher fees mean faster confirmation.
* **Wallet:** Manages crypto addresses and transactions.
* **Mempool:** Queue of pending transactions, sorted by fees.
* **Priority Levels:** ECONOMICAL (slow), STANDARD (medium), RAPIDE (fast).
* **Blockchain Types:** Bitcoin and Ethereum, each with specific fee calculation logic.

The project follows a **layered architecture** (presentation, business, data, utilities) and applies **SOLID principles**.

---

## 🛠️ Technologies Used

* **Language:** Java 8
* **Database:** MySQL (via JDBC)
* **Logging:** `java.util.logging`
* **Collections API:** `ArrayList`, `HashMap`, `HashSet`
* **Date Management:** `java.time` API
* **Unique Identifiers:** `UUID`
* **Unit Testing:** JUnit
* **Shell Scripts:** `run-tests.sh` for executing tests
* **Containerization:** Docker & Docker Compose
* **Version Control:** Git & GitHub

---

## 📂 Project Structure

```
BlockSimWallet/
├── docs/                           # Documentation files
│   ├── db/                         # Database scripts
│   │   └── schema.sql
│   └── uml/                        # UML diagrams
│       ├── .gitkeep
│       └── class diagram blocksim wallet.png
├── lib/                            # External libraries
│   ├── junit-platform-console-standalone-1.9.3.jar
│   └── mysql-connector-j-9.4.0.jar
├── src/
│   ├── main/java/com/blocksim/application/service/util/   # Service utilities for validations and calculations
│   │   ├── TransactionValidator.java
│   │   ├── WalletValidator.java
│   │   ├── MempoolService.java
│   │   ├── WalletService.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/domain/entity/             # Domain entities representing wallets and transactions
│   │   ├── BitcoinWallet.java
│   │   ├── EthereumWallet.java
│   │   ├── Transaction.java
│   │   ├── Wallet.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/domain/enums/              # Enums for transaction priorities and status
│   │   ├── TransactionPriority.java
│   │   ├── TransactionStatus.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/domain/exception/          # Custom exceptions
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/domain/repository/         # Repository interfaces
│   │   ├── TransactionRepository.java
│   │   ├── WalletRepository.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/infrastructure/config/     # Configuration classes for database and logging
│   │   ├── ApplicationContext.java
│   │   ├── DatabaseConfig.java
│   │   ├── LoggerConfig.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/infrastructure/persistence/ # Repository implementations for DB operations
│   │   ├── TransactionRepositoryImpl.java
│   │   ├── WalletRepositoryImpl.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/presentation/console/      # Console entry point
│   │   └── MainApp.java
│   ├── main/java/com/blocksim/presentation/controller/   # Controllers to handle user commands
│   │   ├── MempoolController.java
│   │   ├── WalletController.java
│   │   └── .gitkeep
│   ├── main/java/com/blocksim/presentation/dto/request/  # DTOs for request data
│   │   ├── MempoolRequestDTO.java
│   │   ├── TransactionRequestDTO.java
│   │   └── WalletRequestDTO.java
│   ├── main/java/com/blocksim/presentation/dto/response/ # DTOs for response data
│   │   ├── FeeComparisonResponseDTO.java
│   │   ├── MempoolResponseDTO.java
│   │   ├── MempoolTransactionDTO.java
│   │   ├── TransactionResponseDTO.java
│   │   ├── WalletResponseDTO.java
│   │   └── .gitkeep
│   └── test/java/com/blocksim/domain/entity/            # Unit tests
│       ├── WalletTest.java
│       └── .gitkeep
├── .dockerignore
├── .env
├── .env.example
├── .gitignore
├── BlockSimWallet.iml
├── build-run.sh                   # Build and run script (used in Dockerfile)
├── docker-compose.yml             # Docker Compose configuration
├── Dockerfile                     # Docker image definition
└── run-tests.sh                   # Run JUnit tests inside container
```

---

## 📄 Class Diagram

<img width="1080" height="464" alt="class diagram blocksim walle" src="https://github.com/user-attachments/assets/bae328e1-9eda-46e4-9317-45bf00cb3d83" />

---

## ✅ Main Features

* Create crypto wallets (Bitcoin / Ethereum)
* Generate unique crypto addresses for each wallet
* Create new transactions specifying source, destination, amount, and fee level
* Calculate transaction fees based on cryptocurrency type and priority
* Validate transaction details (amounts, addresses, priority levels)
* Compute transaction position in mempool according to fees
* Estimate confirmation time based on mempool position
* Compare different fee levels (ECONOMICAL, STANDARD, RAPIDE) with position and cost analysis
* Display current mempool state including user transaction and other pending transactions
* Persist wallets and transactions in MySQL using JDBC
* Logging using `java.util.logging` for operations, errors, and critical events
* Unit testing with JUnit (at least 2 tests implemented)
* Docker & Docker Compose for containerization and easy deployment

---

## 📸 Screenshots

<img width="1098" height="456" alt="image" src="https://github.com/user-attachments/assets/64538998-2d50-4ad2-a13f-a509faf9bda5" />

---

## 🏆 Performance Criteria

* Developed exclusively with **Java 8**
* Layered architecture respected (UI, business, data, utilities)
* Clean, maintainable, and well-documented code
* Logging correctly separates console output from errors and operations
* Proper usage of enums to avoid magic numbers/strings
* Unit tests included
* Git history demonstrates regular commits with multiple branches
* Docker & Docker Compose allow easy deployment
* SOLID principles and design patterns applied (Singleton, Repository Pattern)

---

## ▶️ Installation & Execution

### 1️⃣ Clone the repository

```bash
git clone https://github.com/ahmedbenkrarayc/blocksim-wallet.git
cd BlockSimWallet
```

### 2️⃣ Build and run with Docker Compose

```bash
docker compose build
docker compose up -d
```

### 3️⃣ Run the application

```bash
docker compose attach app sh
```

> The application will run inside the container.

### 4️⃣ Run unit tests

```bash
docker compose exec app sh
./run-tests.sh
```

> This executes tests inside the running container using the `run-tests.sh` script.
