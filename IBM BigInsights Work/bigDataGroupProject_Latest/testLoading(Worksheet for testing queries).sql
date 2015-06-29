select * from bigdatagroupproject.area_Details;

select fact_id,ngr,eventid,regdate,enddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join 
(
select name,NGR,regdate,amenddate enddate,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude,Area_HA from st_BattleFields
) staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,bpnstart,bpnexpire,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join 
( 
select name,NGR,bpnstart,bpnexpire,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude,0.0 Area_HA  from  st_bpnp
) staging
on
staging.name=dimension.name;



select fact_id,ngr,eventid,coistart,coiexpire,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
( 
select name,NGR,coistart,coiexpire,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude,0.0 Area_HA from st_COIP
) staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,listdate,amenddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
( 
select name,NGR,listdate,amenddate,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude,0.0 Area_HA from st_LBP
) staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,desigdate,amenddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
( 
select name,null NGR,desigdate, amenddate,null CaptureSca,Easting,Northing, latitude, longitude,Area_HA from st_ProtectedWrek
)staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,scheddate,amenddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,NGR,scheddate,amenddate,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude, Area_HA from st_ScheduledMonuments
)staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,inscrdate,amenddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,NGR,inscrdate,amenddate,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude, Area_HA from st_WHS
)staging
on
staging.name=dimension.name;

select fact_id,ngr,eventid,regdate,amenddate,CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	(row_number()over()+cnt+4000) as fact_id,
	 dim.* 
	 from bigdatagroupproject.osgb36maineventdimension dim,(select count(*)cnt from bigdatagroupproject.OSGB36Facts)d
)dimension
join
(
select name,NGR,regdate,amenddate,CaptureSca,Easting,Northing,0.0 latitude,0.0 longitude, Area_HA from st_ParksGardens
)stagings
on
stagings.name=dimension.name;

