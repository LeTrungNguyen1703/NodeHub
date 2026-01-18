-- Users (UPDATE)
ALTER TABLE users
    ADD COLUMN phone VARCHAR(20),
ADD COLUMN address TEXT,
ADD COLUMN date_of_birth DATE,
ADD COLUMN avatar VARCHAR(255),
ADD COLUMN balance BIGINT DEFAULT 0 NOT NULL,
ADD COLUMN role ENUM('client_admin', 'client_user') DEFAULT 'client_user' NOT NULL,
ADD COLUMN is_anonymous BOOLEAN DEFAULT FALSE NOT NULL;

-- Categories
CREATE TABLE categories (
                            category_id INT PRIMARY KEY AUTO_INCREMENT,
                            category_name VARCHAR(100) NOT NULL,
                            description TEXT
);

-- Products
CREATE TABLE products (
                          product_id INT PRIMARY KEY AUTO_INCREMENT,
                          seller_id VARCHAR(36) NOT NULL,
                          category_id INT,
                          product_name VARCHAR(255) NOT NULL,
                          description TEXT,
                          starting_price BIGINT NOT NULL,
                          buy_now_price BIGINT,
                          step_price BIGINT NOT NULL,
                          status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
                          version INT DEFAULT 0,
                          FOREIGN KEY (seller_id) REFERENCES users(user_id),
                          FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Product Images
CREATE TABLE product_images (
                                image_id INT PRIMARY KEY AUTO_INCREMENT,
                                product_id INT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                is_primary BOOLEAN DEFAULT FALSE,
                                FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Auctions
CREATE TABLE auctions (
                          auction_id INT PRIMARY KEY AUTO_INCREMENT,
                          product_id INT NOT NULL,
                          host_id VARCHAR(36) NOT NULL,
                          start_time DATETIME NOT NULL,
                          end_time DATETIME NOT NULL,
                          original_end_time DATETIME NOT NULL,
                          current_price BIGINT NOT NULL,
                          winner_id VARCHAR(36),
                          status ENUM('scheduled', 'ongoing', 'completed', 'cancelled') DEFAULT 'scheduled',
                          is_host_anonymous BOOLEAN DEFAULT FALSE,
                          version INT DEFAULT 0,
                          FOREIGN KEY (product_id) REFERENCES products(product_id),
                          FOREIGN KEY (host_id) REFERENCES users(user_id),
                          FOREIGN KEY (winner_id) REFERENCES users(user_id)
);

-- Bids
CREATE TABLE bids (
                      bid_id INT PRIMARY KEY AUTO_INCREMENT,
                      auction_id INT NOT NULL,
                      bidder_id VARCHAR(36) NOT NULL,
                      bid_amount BIGINT NOT NULL,
                      bid_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                      is_auto_bid BOOLEAN DEFAULT FALSE,
                      is_buy_now BOOLEAN DEFAULT FALSE,
                      FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                      FOREIGN KEY (bidder_id) REFERENCES users(user_id)
);

-- Auto Bids
CREATE TABLE auto_bids (
                           auto_bid_id INT PRIMARY KEY AUTO_INCREMENT,
                           auction_id INT NOT NULL,
                           bidder_id VARCHAR(36) NOT NULL,
                           max_amount BIGINT NOT NULL,
                           is_active BOOLEAN DEFAULT TRUE,
                           FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                           FOREIGN KEY (bidder_id) REFERENCES users(user_id)
);

-- Notifications
CREATE TABLE notifications (
                               notification_id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id VARCHAR(36) NOT NULL,
                               auction_id INT,
                               product_id INT,
                               type ENUM('bid_outbid', 'auction_starting', 'auction_ending', 'auction_won', 'auction_lost', 'product_approved', 'account_created') NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               message TEXT NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               FOREIGN KEY (user_id) REFERENCES users(user_id),
                               FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                               FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Email Logs
CREATE TABLE email_logs (
                            email_id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id VARCHAR(36) NOT NULL,
                            auction_id INT,
                            product_id INT,
                            email_type ENUM('auction_result', 'auction_start', 'product_approved', 'account_created') NOT NULL,
                            subject VARCHAR(255) NOT NULL,
                            content TEXT NOT NULL,
                            sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES users(user_id),
                            FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                            FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- PayOS Payments
CREATE TABLE payos_payments (
                                payment_id INT PRIMARY KEY AUTO_INCREMENT,
                                user_id VARCHAR(36) NOT NULL,
                                order_code BIGINT NOT NULL UNIQUE,
                                amount BIGINT NOT NULL,
                                description VARCHAR(255),
                                account_number VARCHAR(50),
                                reference VARCHAR(255),
                                transaction_datetime DATETIME,
                                payment_status ENUM('pending', 'success', 'failed', 'cancelled') DEFAULT 'pending',
                                payos_data TEXT,
                                FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Transactions
CREATE TABLE transactions (
                              transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                              user_id VARCHAR(36) NOT NULL,
                              auction_id INT,
                              payos_payment_id INT,
                              type ENUM('deposit', 'withdraw', 'bid_hold', 'bid_release', 'refund', 'payment') NOT NULL,
                              amount BIGINT NOT NULL,
                              balance_before BIGINT NOT NULL,
                              balance_after BIGINT NOT NULL,
                              description TEXT,
                              FOREIGN KEY (user_id) REFERENCES users(user_id),
                              FOREIGN KEY (auction_id) REFERENCES auctions(auction_id),
                              FOREIGN KEY (payos_payment_id) REFERENCES payos_payments(payment_id)
);

-- Held Balances
CREATE TABLE held_balances (
                               hold_id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id VARCHAR(36) NOT NULL,
                               auction_id INT NOT NULL,
                               amount BIGINT NOT NULL,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               released_at DATETIME,
                               FOREIGN KEY (user_id) REFERENCES users(user_id),
                               FOREIGN KEY (auction_id) REFERENCES auctions(auction_id)
);