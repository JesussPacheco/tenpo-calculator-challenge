-- Response type lookup table
CREATE TABLE response_type
(
    code        VARCHAR(10) PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

INSERT INTO response_type (code, description)
VALUES ('SUCCESS', 'Successful operation'),
       ('ERROR', 'Operation failed');

-- Calculation history table
CREATE TABLE calculation_history
(
    id                 BIGSERIAL PRIMARY KEY,
    endpoint           TEXT        NOT NULL,
    method             VARCHAR(8)  NOT NULL,
    status             INT         NOT NULL,
    response_type_code VARCHAR(10) NOT NULL,
    request_params     JSONB       NOT NULL,
    outcome_json       JSONB       NOT NULL,
    executed_at        TIMESTAMP   NOT NULL,
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_response_type
        FOREIGN KEY (response_type_code)
            REFERENCES response_type (code)
);

-- Indexes for query performance
CREATE INDEX idx_calculation_history_executed_at ON calculation_history (executed_at DESC);
CREATE INDEX idx_calculation_history_response_type ON calculation_history (response_type_code);
CREATE INDEX idx_calculation_history_endpoint ON calculation_history (endpoint);
CREATE INDEX idx_calculation_history_status ON calculation_history (status);

-- GIN index for JSONB queries (if needed for filtering by params)
CREATE INDEX idx_calculation_history_request_params ON calculation_history USING GIN (request_params);
CREATE INDEX idx_calculation_history_outcome ON calculation_history USING GIN (outcome_json);