INSERT INTO inventory (product_name, quantity) VALUES
    ('Iphone 18 Pro', 5),
    ('Iphone 18 Pro Max Burgundy', 10),
    ('Mac Pro', 6)
ON CONFLICT (product_name) DO NOTHING;

CREATE TABLE processed_order_id (
    order_id VARCHAR(36) PRIMARY KEY,
    processed_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE outbox_event (
    key VARCHAR(36) PRIMARY KEY,
    topic VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    sent BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_outbox_event_sent ON outbox_event(sent, created_at);