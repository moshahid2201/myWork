use bigdatagroupproject;
create hadoop table area_Details
(
name varchar(40),
detail_code varchar(20)
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
