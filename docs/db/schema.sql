CREATE TABLE wallet (
    id CHAR(36) PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE bitcoin_wallet (
   wallet_id CHAR(36) PRIMARY KEY,
   CONSTRAINT fk_bitcoin_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE CASCADE
);

CREATE TABLE ethereum_wallet (
    wallet_id CHAR(36) PRIMARY KEY,
    CONSTRAINT fk_ethereum_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE CASCADE
);

CREATE TABLE transaction (
    id CHAR(36) PRIMARY KEY,
    wallet_id CHAR(36) NOT NULL,
    sourceAddress VARCHAR(255) NOT NULL,
    destinationAddress VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL,
    fee DOUBLE NOT NULL,
    createdAt DATETIME NOT NULL,
    sizeInBytes INT NOT NULL,
    priority ENUM('ECONOMIQUE', 'STANDARD', 'RAPIDE') NOT NULL DEFAULT 'STANDARD',
    status ENUM('PENDING', 'CONFIRMED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id) ON DELETE CASCADE
);
