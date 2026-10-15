INSERT INTO inventory (product_name, quantity) VALUES
    ('Iphone 18 Pro', 5),
    ('Iphone 18 Pro Max Burgundy', 10),
    ('Mac Pro', 6)
ON CONFLICT (product_name) DO NOTHING;

