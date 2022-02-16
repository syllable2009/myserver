Employee表中有salary字段，求出第二高，第N高薪水

// order by + limit，如果是第一，可用max，min等函数
select DISTINCT salary from Employee
order by salary desc
limit n-1,1;

select IFNULL(
(select DISTINCT salary
from Employee
order by salary desc
limit n-1 OFFSET 1
),null) as salary;


rank()

row_number()

dense_rank()


SELECT version,rank() over(partition by version ORDER BY version desc) as 'Rank'
FROM psp_user;

