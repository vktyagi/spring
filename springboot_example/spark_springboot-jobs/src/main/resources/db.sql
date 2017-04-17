use db_app;

CREATE TABLE  `s3_log_line_segments` (
  `segment_id` int(11) NOT NULL,
  `url_id` int(11) unsigned default NULL,
  `request_type` varchar(20) NOT NULL,
  `created_at` date NOT NULL,
  `asset_id` int(11) default NULL,
  `created_at_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `group_id` double default '0',
  KEY `Index_created_at_time` USING BTREE (`created_at_time`),
  KEY `index_group_id` (`group_id`)
);

CREATE TABLE  `ttag`.`segment_fires_test` (
  `fire_count` bigint(20) NOT NULL,
  `segment_id` int(11) NOT NULL,
  `fire_date` date default NULL
);