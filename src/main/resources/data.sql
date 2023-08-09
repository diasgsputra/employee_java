CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    department VARCHAR(255)
);

INSERT INTO employees (name, email, department) VALUES
    ('Mawar', 'mawar@gmail.com', 'HR'),
    ('Melati', 'melati@gmail.com', 'IT');
