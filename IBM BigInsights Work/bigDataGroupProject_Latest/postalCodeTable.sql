use bigdatagroupproject;
create hadoop table postCode_Table
(
Postcode varchar(10),
Positional_quality_indicator int,
Eastings float,
Northings float,
Country_code varchar(20) ,
NHS_regional_HA_code varchar(20),
NHS_HA_code varchar(20),
Admin_county_code varchar(20),
Admin_district_code varchar(20),
Admin_ward_code varchar(20),
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
 