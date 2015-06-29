-- drop table st_BattleFields;
create hadoop  table st_BattleFields
( 
--id_key int,
ListEntry int,
Name	varchar(90),
RegDate	varchar(10),
AmendDate	varchar(10),
LegacyUID	varchar(10),
NGR		varchar(12),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
AREA_HA float,
latitude float,
longitude float
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/Battlefields2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_BattleFields overwrite;

-- drop table st_bpnp;
create hadoop  table st_bpnp
( 
--id_key int,
ListEntry int,
Name	varchar(90),
LegacyUID	varchar(10),
BPNStart	varchar(10),
BPNExpire	varchar(10),
NGR		varchar(10),
CaptureSca	varchar(20),
Easting	  float,
Northing	float,
latitude float,
longitude float
) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_BuildingPreservationNoticePoints2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_bpnp overwrite;

--drop table st_COIP;
create hadoop table st_COIP
(
ListEntry int,
Name	varchar(90),
COIStart varchar(10),
COIExpire varchar(10),
LegacyUID	varchar(10),
NGR		varchar(12),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_CertificateOfImmunityPoints2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_COIP overwrite;
--select * from st_coip;

-- drop table st_LBP;
select count(*) from st_LBP;
create hadoop table st_LBP
(

ListEntry int,
Name	varchar(200),
Grade varchar(4),
ListDate varchar(10),
AmendDate varchar(10),
LegacyUID	varchar(10),
NGR		varchar(12),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_ListedBuildingPointsFinal.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_LBP overwrite;

-- drop table st_ProtectedWrek;
create hadoop table st_ProtectedWrek
(
ListEntry int,
Name	varchar(90),
DesigDate varchar(10),
LegacyUID	varchar(20),
SInumber varchar(20),
AmendDate varchar(10),
Latitude		float,
Longitude	float,
Easting	  float,
Northing	float,
area_ha   float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_ProtectedWreck.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_ProtectedWrek overwrite;

-- select * from st_ProtectedWrek;
-- drop table st_ScheduledMonuments;
create hadoop table st_ScheduledMonuments
(
ListEntry int,
Name	varchar(90),
SchedDate varchar(10),
AmendDate varchar(10),
LegacyUID	varchar(10),
NGR		varchar(10),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
AREA_HA 	float,
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_ScheduledMonument2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_ScheduledMonuments overwrite;
-- select count(*) from st_ScheduledMonuments;

--drop table  st_WHS;
create hadoop table st_WHS
(
ListEntry int,
Name	varchar(90),
InscrDate varchar(10),
AmendDate varchar(10),
LegacyUID	varchar(10),
Notes varchar(40),
NGR		varchar(10),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
AREA_HA 	float,
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/20150313_WorldHeritageSite2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_WHS overwrite;
-- select count(*) from st_WHS;

-- drop table st_ParksGardens;
create hadoop table st_ParksGardens
(
ListEntry double,
Name	varchar(90),
Grade varchar(4),
RegDate varchar(10),
AmendDate varchar(10),
LegacyUID	varchar(10),
NGR		varchar(12),
CaptureSca	varchar(14),
Easting	  float,
Northing	float,
AREA_HA 	float,
latitude float,
longitude float
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
load hadoop using file url 'sftp://biadmin:biadmin@bivm:22/home/biadmin/Desktop/data/Events/parksandgardens2.txt'    with SOURCE PROPERTIES ('field.delimiter'='\t') into table st_ParksGardens overwrite;
