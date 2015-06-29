truncate table bigdatagroupproject.osgb36maineventdimension;
--   Battle Fields 
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(select distinct 
name,
'BattleFields' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate,
cnt
from st_BattleFields,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d ;

-- Building Preservation Notice Points 
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(select  distinct
name,
'BPNP' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate,cnt
from st_bpnp,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d ;
-- Certificate of immunity points

insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(select distinct
name,
'COIP' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate,cnt
from st_COIP,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d ;

-- Listed Building Points
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(
select distinct
name,
'LBP' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate,cnt
from st_LBP,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d ;


-- Protected Wreck Sites
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A')  
from 
(select distinct
name,
'ProtectedWreckSites' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate,cnt
from st_ProtectedWrek,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d ;

-- Scheduled Monuments
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(
select distinct
name,
'ScheduledMonuments' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate ,cnt
from st_ScheduledMonuments,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b)d;



 --World Heritage Sites
insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(
select distinct
name,
'WorldHeritageSites' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate ,b.cnt
from st_WHS,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b);

-- Parks and Gardens

insert into bigdatagroupproject.osgb36maineventdimension
select 
row_number()over()+(cnt+1000) rownumber,
name,eventtype,shape,nvl(startdate,'N/A'),nvl(enddate,'N/A') 
from 
(
select distinct
name,
'ParkAndGardens' EventType, 
0.0 shape,
current_date startdate,
DATE(TIMESTAMP_FORMAT('01-01-9999', 'DD-MM-YYYY')) enddate ,b.cnt
from st_ParksGardens,
(select count(*)+1 cnt 
from bigdatagroupproject.osgb36maineventdimension)b
);
