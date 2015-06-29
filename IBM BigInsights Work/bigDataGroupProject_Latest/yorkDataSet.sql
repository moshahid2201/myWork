use bigdatagroupproject;
drop table NGRDetailDimension;
CREATE HADOOP TABLE 
NGRDetailDimension 
(
rowID int,
NGRCode varchar(2), 
Easting int,
Northing int,
Easting_Offset int,
Northing_Offset int,
e float,
n float,
latitude float,
longitude float
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/york/cleaned_WholeUK2.csv'    with SOURCE PROPERTIES ('field.delimiter'=',') into table NGRDetailDimension overwrite;
