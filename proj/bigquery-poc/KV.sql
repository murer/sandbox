create table `dsasource.DSA_POC.TestV1` as 
SELECT 'r' as id, "raoni" as name, [STRUCT("name" as key, "raoni" as value), STRUCT("age" as key, "39" as value)] as info, ["r", "x"] as tags union all
SELECT 'p' as id, "paulo" as name, [STRUCT("name" as key, "paulo" as value), STRUCT("age" as key, "39" as value), STRUCT("guest" as key, "abc" as value)] as info, ["p", "x"] as tags union all
SELECT 'e' as id, "everton" as name, [STRUCT("name" as key, "everton" as value), STRUCT("age" as key, "37" as value)] as info, ["e", "x"] as tags


with main as (select 
    id,
    (select value from unnest(info) where key = 'name') as name,
    (select value from unnest(info) where key = 'age') as age
from `dsasource.DSA_POC.TestV1`)
select * from main where age = '39'

create table `dsasource.DSA_POC.TestV2` as
select 'r' as id, '{"name":"raoni","age":39}' as info union all
select 'p' as id, '{"name":"paulo","age":39}' as info union all
select 'e' as id, '{"name":"everton","age":37}' as info 

select 
    id, 
    JSON_VALUE(info, "$.name") as name,
    JSON_VALUE(info, "$.age") as age
from `dsasource.DSA_POC.TestV2`
where CAST(JSON_VALUE(info, "$.age") as int64) = 39
