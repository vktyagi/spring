-- This is empty initial version of flyway migration.

CREATE TABLE  `customer` (
  `cust_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `email` varchar(100) default NULL,
  `address` varchar(100) default NULL,
  `telephone` varchar(100) default NULL,
  PRIMARY KEY  (`cust_id`)
);

insert into customer(cust_id, name, email, address, telephone) values(101, 'Vinit Tyagi', 'test@gmail.com','noida', 9876543210);