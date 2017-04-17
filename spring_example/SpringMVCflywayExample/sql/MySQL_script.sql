CREATE TABLE  `customer` (
  `cust_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) default NULL,
  `email` varchar(45) default NULL,
  `address` varchar(45) default NULL,
  `telephone` varchar(45) default NULL,
  PRIMARY KEY  (`cust_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;