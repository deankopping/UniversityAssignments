SELECT customerName as CustomerName, round(sum(amount),2) as total 
from customers, payments
where customers.customerNumber = payments.customerNumber
group by payments.customerNumber 
having count(payments.customerNumber) > 4;