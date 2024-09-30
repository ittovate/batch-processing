CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50),
                        description TEXT
);
CREATE OR REPLACE PROCEDURE generate_orders(row_count INTEGER)
LANGUAGE plpgsql
AS $$
BEGIN
FOR i IN 1..row_count LOOP
        INSERT INTO orders (name, description)
        VALUES ('order ' || i, 'Sample description');
END LOOP;
END;
$$;