create database barber_shop;
use barber_shop;

create table barber_shop(client_phone BIGINT(10) NOT NULL, client_name VARCHAR(50), app_day VARCHAR(10),        
app_time VARCHAR(10), service VARCHAR(50), price INT, PRIMARY KEY(client_phone));

desc barber_shop;

insert into barber_shop(client_phone,client_name,app_day,app_time,service,price)VALUES
(4455778589, "Rocky Mountain","Sat","6 pm", "Cut & Wash",15);
