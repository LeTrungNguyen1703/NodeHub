CREATE INDEX idx_auction_end_time ON auctions (end_time);

CREATE INDEX idx_auction_status ON auctions (status);

CREATE INDEX idx_notification_is_read ON notifications (is_read);

CREATE INDEX idx_product_name ON products (product_name);

CREATE INDEX idx_product_status ON products (status);

CREATE INDEX idx_user_email ON users (email);

CREATE INDEX idx_user_username ON users (username);