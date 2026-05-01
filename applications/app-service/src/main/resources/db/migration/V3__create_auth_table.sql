CREATE TABLE IF NOT EXISTS auth (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    token VARCHAR(500) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE,
    expires_in INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_email_token (email, token)
);