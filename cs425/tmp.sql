 select title from course where dept_name='Comp. Sci.';
Select max(enrollment) from (
    select sec_id, count(*) as enrollment
    from takes natural join section 
    where semester=’Fall’ and year=2017 group by sec_id
) as enroll;

select sec_id, count(*) 
from takes natural join section 
where semester=’Spring’ and year=2017 group by sec_id;

with sec_enrollment as (select sec_id, count(*) as enrollment 
    from takes natural join section 
    where semester=’Fall’ and year=2007 group by sec_id)
select sec_id from sec_enrollment where
    enrollment in (select max(enrollment) from sec_enrollment);



select id, name from student where id not in (
    select id from takes where course_id not in (
        select course_id from section where year < 2017));
select id, name from student where id not in (
    select id from takes where course_id in (
        select course_id from section where year < 2017));

select min(salary) from (select dept_name, max(salary) as salary from instructor group by dept_name) as maxSalaries;

select id, name from student where dept_name = 'Accounting' and id in (
    select s_id as id from advisor where i_id in (
        select id as i_id from instructor where dept_name='Physics'));

select id, name from student where id in (
    select id from takes group by id, course_id having count(*) > 2);


select distinct id 
    from (
        select distinct id, course_id 
            from takes
            group by id, course_id 
            having count(*) > 2 
            order by course_id) as slackers 
    group by id
    having count(course_id) > 2;


select course_id from course where dept_name=(select dept_name from instructor where instructor)

select name, id from student where dept_name='History' and name like 'D%' and id not in (
    select id from takes where course_id in (
        select course_id from course where dept_name='Music')
    group by id 
    having count(course_id) >= 5
);


create table Branch (
	branch_name VARCHAR(30) PRIMARY KEY, 
	branch_city VARCHAR(86), -- https://en.wikipedia.org/wiki/Llanfairpwllgwyngyll
	assets BIGINT
);

create table Account (
	account_number BIGINT PRIMARY KEY,
	branch_name varchar(30) references Branch,
	balance bigint
);

create table Customer (
	id BIGINT PRIMARY KEY,
	customer_name varchar(50),
	customer_street varchar(30),
	customer_city varchar(86)
);

create table Depositor (
	id bigint references Customer,
	account_number bigint references Account
);

create table Loan (
	loan_number bigint primary key,
	branch_name varchar(30) references Branch,
	amount bigint
);

create table Borrower (
	id bigint references Customer,
	loan_number bigint references Loan
);


insert into course (course_id, title, dept_name, credits) values (1, 'Weekly Seminar', 'Comp. Sci.', 0);
insert into section (course_id, sec_id, semester, year) values (1, 1, 'Fall', 2017);

insert into takes (id, course_id, sec_id, semester, year) select
    id, 1 as course_id, 1 as sec_id, 'Fall' as semester, 2017 as year from student; 


delete from takes where course_id in (
    select course_id from section where sec_id in (
        select sec_id from section where course_id in (
            select course_id from course where lower(title) like lower('%advanced%'))));


Find the names and IDs of those instructors who teach every course taught in his or her department (i.e., every
course that appears in the course relation with the instructor’s department name). Order result by name. rows.

Im thinking `instructor inner join course on dept_name=dept_name`

select count(*) from courses where dept_name=dept_name;

Group by instructor count courses to see if they have enough
select id, name from instructor where id in (
    select id from (
        select i.id as id, count(c.course_id) courses, i.dept_name as dept 
            from instructor i inner join course c on i.dept_name=c.dept_name
            where i.id in (select id from teaches where course_id=c.course_id)
            group by id) as tryhardScore
    where courses = (select count(*) from course where dept_name=dept));