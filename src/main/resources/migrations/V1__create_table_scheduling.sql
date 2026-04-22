CREATE TABLE tb_scheduling (

    id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    client VARCHAR(80) NOT NULL,
    status scheduling_status NOT NULL DEFAULT 'SCHEDULED',
    createdAt TIMESTAMP NOT NULL DEFAULT NOW(),
    attAt TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_intervalo CHECK (start_date < end_date )
);

CREATE INDEX idx_ag_client_start_end
 ON tb_scheduling (client, start_date, end_date);

CREATE OR REPLACE FUNCTION set_atualization_in()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualization_in := NOW();
    RETURN NEW;
END;
$$  LANGUAGE plpgsql;

CREATE TRIGGER trg_set_atualization_in
BEFORE UPDATE ON tb_scheduling
FOR EACH ROW
EXECUTE FUNCTION set_atualization_in();