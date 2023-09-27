create database messagewall;

use messagewall;

create table if not exists messages  (
    who varchar(20),
    towho varchar(20),
    msg varchar(50)
);


