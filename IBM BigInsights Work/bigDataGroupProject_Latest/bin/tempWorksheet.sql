select count(*) from bigdatagroupproject.osgb36facts join
bigdatagroupproject.osgb36maineventdimension on
eventid=eventcode where eventtype like 'L%';

select count(*) from bigdatagroupproject.osgb36maineventdimension where
eventtype like 'L%';

truncate table bigdatagroupproject.OSGB36Facts

insert into bigdatagroupproject.OSGB36Facts

select fact_id,ngr,eventid,nvl(listdate,'N/A'),nvl(amenddate,'N/A'),CaptureSca,Easting,Northing,latitude,longitude,Area_HA  
from 
(
	select 
	row_number()over()+4000 as fact_id,
	 dim.name,
	 dim.eventID 
	 from bigdatagroupproject.osgb36maineventdimension dim
)dimension
join
(
select name,NGR,listdate,nvl(amenddate,'N/A')amenddate,CaptureSca,Easting,Northing, round(latitude,6) latitude, round(longitude,6)longitude,0.0 Area_HA from st_LBP
)stagings
on
stagings.name=dimension.name;

select count(*) from bigdatagroupproject.osgb36facts