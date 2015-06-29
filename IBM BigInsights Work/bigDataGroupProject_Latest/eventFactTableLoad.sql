--truncate table bigdatagroupproject.OSGB36Facts;

insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(regdate,'N/A'),nvl(enddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join 
(
select name,NGR,regdate,amenddate enddate,CaptureSca,Easting,Northing, latitude, longitude,Area_HA from st_BattleFields
) staging
on
staging.name=dimension.name;

insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(bpnstart,'N/A'),nvl(bpnexpire,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join 
( 
select name,NGR,bpnstart,bpnexpire,CaptureSca,Easting,Northing, latitude, longitude,0.0 Area_HA  from  st_bpnp
) staging
on
staging.name=dimension.name;


insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(coistart,'N/A'),nvl(coiexpire,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
( 
select name,NGR,coistart,coiexpire,CaptureSca,Easting,Northing, latitude, longitude,0.0 Area_HA from st_COIP
) staging
on
staging.name=dimension.name;


insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(inscrdate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,NGR,inscrdate,amenddate,CaptureSca,Easting,Northing, latitude, longitude, Area_HA from st_WHS
)staging
on
staging.name=dimension.name;


insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(regdate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,NGR,regdate,amenddate,CaptureSca,Easting,Northing, latitude, longitude, Area_HA from st_ParksGardens
)stagings
on
stagings.name=dimension.name;


--insert into bigdatagroupproject.OSGB36Facts
--select fact_id,ngr,eventid,nvl(listdate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
--from 
--(
--	select 
--	row_number()over()+4000+cnt as fact_id,
--	 dim.name,--
--	 dim.eventID 
--	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
--)dimension
--join
--(
--select name,NGR,listdate,amenddate,CaptureSca,Easting,Northing, latitude, longitude,0.0 Area_HA from st_LBP
--)stagings
--on
--stagings.name=dimension.name;



insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(desigdate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	row_number()over()+4000+cnt as fact_id,
	 dim.name,
	 dim.eventID 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,'N/A' NGR,desigdate,amenddate,'N/A' CaptureSca,Easting,Northing,latitude, longitude, Area_HA from st_ProtectedWrek
)stagings
on
stagings.name=dimension.name;

insert into bigdatagroupproject.OSGB36Facts
select fact_id,ngr,eventid,nvl(scheddate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	row_number()over()+4000+cnt as fact_id,
	 dim.name,
	 dim.eventID 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name, NGR,scheddate,amenddate, CaptureSca,Easting,Northing, latitude, longitude, Area_HA from st_ScheduledMonuments
)stagings
on
stagings.name=dimension.name;

