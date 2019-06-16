CREATE TABLE bankAccount (
	id INT NOT NULL AUTO_INCREMENT,
	balance DECIMAL(8,2),
	PRIMARY KEY(id)
);

CREATE TABLE creditCard (
	id INT NOT NULL AUTO_INCREMENT,
	account_id INT,
    last_used DATETIME,
    pin_code INT,
    wrong_pin_code_attempts INT,
    blocked BIT,
	PRIMARY KEY(id)
);

INSERT INTO bankAccount (balance) VALUES (100000.00), (0.00);
INSERT INTO creditCard (account_id, last_used, pin_code, wrong_pin_code_attempts, blocked) VALUES (1, current_date(), 1234, 0, 0), (1, current_date(), 4321, 0, 0), (2, current_date(), 5678, 0, 0);