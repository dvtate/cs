
# CS425 - Homework Assignment 3
- Name: Dustin Van Tate Testa
- Date: 2019.11.13
- SQL: mariadb  Ver 15.1 Distrib 10.4.7-MariaDB, for Linux (x86_64) using readline 5.1

## Relational Algebra
- 1.1.1. π model (σ speed >= 3.00 (PC))
- 1.1.2. π maker (σ hd >=100 (Product ⋈ Laptop))
- 1.1.3. π model (σ color=true (Printer))
- 1.1.4. π maker (σ count(speed) >= 3 (PC ⋈ Product))
- 1.1.5. π maker (σ model = (π model (σ speed= ((σ speed (PC) ∪ σ speed (Laptop)) - σ s1 > s2 (ρ s1 (σ speed (PC) ∪ σ speed (Laptop)) X ρ s2 (σ speed (PC) ∪ σ speed (Laptop)))) (π model, speed (PC) ∪ π model, speed (Laptop)))) (Product))


## SQL - DDL 
- 1.2.1.
``` sql
-- using mysql because it's what I use at work
CREATE TABLE Product (
	model INT UNSIGNED PRIMARY KEY, 
	maker CHAR(1) NOT NULL,
	type ENUM('pc', 'laptop', 'printer') NOT NULL
);

CREATE TABLE PC (
	model INT UNSIGNED PRIMARY KEY REFERENCES Product,
	speed DECIMAL(4, 2) NOT NULL,
	ram INT UNSIGNED NOT NULL,
	hd INT UNSIGNED NOT NULL,
	price INT UNSIGNED NOT NULL
);

CREATE TABLE Laptop (
	model INT UNSIGNED PRIMARY KEY REFERENCES Product,
	speed DECIMAL(4, 2) NOT NULL,
	ram INT UNSIGNED NOT NULL,
	hd INT UNSIGNED NOT NULL,
	screen DECIMAL(4, 2) NOT NULL,
	price INT UNSIGNED NOT NULL
);

CREATE TABLE Printer (
	model INT UNSIGNED PRIMARY KEY REFERENCES Product,
	color TINYINT(1) NOT NULL,
	type ENUM('ink-jet', 'laser'),
	price INT UNSIGNED NOT NULL
);
```

## SQL - Queries
- 1.3.1. 
```sql
SELECT maker, speed FROM Laptop NATURAL JOIN Product WHERE hd >= 30;
```
- 1.3.2. 
```sql
SELECT model, price FROM Laptop WHERE model in (
	SELECT model FROM Product WHERE maker = 'B')
UNION SELECT model, price FROM Printer WHERE model in (
	SELECT model FROM Product WHERE maker = 'B')
UNION SELECT model, price FROM PC WHERE model in (
	SELECT model FROM Product WHERE maker = 'B');
```
- 1.3.3.
```sql
SELECT DISTINCT maker FROM Product WHERE 
	maker IN (SELECT maker FROM (
		SELECT maker, count(*) AS n 
		FROM Product p INNER JOIN Laptop l on p.model = l.model 
		GROUP BY maker) AS ltct WHERE n > 0)
	AND
	maker IN (SELECT maker FROM (
		SELECT maker, count(*) AS n 
		FROM Product p INNER JOIN PC l on p.model = l.model 
		GROUP BY maker) as pcct WHERE n = 0);
```
- 1.3.4.
```sql
SELECT hd FROM (
	SELECT hd, count(*) AS ct FROM PC GROUP BY hd) AS hdct 
WHERE ct >= 2;
```

- 1.3.5.
```sql
SELECT DISTINCT maker FROM
	(SELECT maker, count(*) AS ct FROM
		(SELECT model, maker FROM Laptop NATURAL JOIN Product WHERE speed >= 3.00 
			UNION SELECT model, maker FROM PC NATURAL JOIN Product 
			WHERE speed >= 3.00) 
		AS mms) 
AS mkct WHERE ct >= 2;
```

- 1.3.6. 
```sql
SELECT model FROM Printer WHERE price = (SELECT MAX(price) AS price FROM Printer);
```

- 1.3.7.
```sql
SELECT model FROM Laptop WHERE speed < (SELECT MIN(speed) AS speed FROM PC);
```

- 1.3.8.
```sql 
SELECT maker FROM Product NATURAL JOIN Printer 
WHERE price = (SELECT MIN(price) AS price FROM Printer) AND color = 1;
```

