SELECT customerNumber, round(sum(amount),2) as total FROM customers NATURAL JOIN payments group by customerNumber;

-- SELECT customerNumber FROM customers;