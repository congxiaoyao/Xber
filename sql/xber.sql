/*==============================================================*/
/* Database name:  xber_db                                   */
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/12/4 22:01:43                           */
/*==============================================================*/


drop database if exists xber_db;

/*==============================================================*/
/* Database: xber_db                                         */
/*==============================================================*/
create database xber_db character set utf8;

use xber_db;

/*==============================================================*/
/* Table: rel_car_route                                         */
/*==============================================================*/
create table rel_car_route
(
   car_id               bigint,
   route_id             bigint
);

alter table rel_car_route comment '车辆、预定路线的关联表';

/*==============================================================*/
/* Table: tab_car                                               */
/*==============================================================*/
create table tab_car
(
   car_id               bigint not null auto_increment,
   driver_id            bigint,
   plate_no             varchar(32),
   car_no               varchar(32),
   spec                 varchar(32),
   status               int,
   primary key (car_id)
);

alter table tab_car comment '车辆信息表';

/*==============================================================*/
/* Table: tab_driver                                            */
/*==============================================================*/
create table tab_driver
(
   driver_id            bigint not null auto_increment,
   driver_name          varchar(64),
   gender               tinyint,
   birthday             datetime,
   driver_no            varchar(64),
   id_card              varchar(18),
   primary key (driver_id)
);

alter table tab_driver comment '司机表';

/*==============================================================*/
/* Table: tab_position_trace                                    */
/*==============================================================*/
create table tab_position_trace
(
   car_id               bigint,
   latitude             double,
   longitude            double,
   time                 datetime
);

alter table tab_position_trace comment '位置轨迹历史记录';

/*==============================================================*/
/* Table: tab_route                                             */
/*==============================================================*/
create table tab_route
(
   route_id             bigint not null auto_increment,
   route_name           varchar(32),
   begin_spot           bigint,
   end_spot             bigint,
   primary key (route_id)
);

alter table tab_route comment '路线信息表';

/*==============================================================*/
/* Table: tab_route_line                                        */
/*==============================================================*/
create table tab_route_line
(
   route_id             bigint not null,
   latitude             double,
   longitude            double,
   seqno                int
);

alter table tab_route_line comment '预定路线坐标点';

/*==============================================================*/
/* Table: tab_spot                                              */
/*==============================================================*/
create table tab_spot
(
   spot_id              bigint not null auto_increment,
   spot_name            varchar(64),
   latitude             double,
   longitude            double,
   primary key (spot_id)
);

alter table tab_spot comment '站点信息表';

/*==============================================================*/
/* Table: tab_user                                              */
/*==============================================================*/
create table tab_user
(
   user_id              bigint not null auto_increment,
   username             varchar(64) not null,
   password             varchar(64) not null,
   salt                 varchar(64),
   name                 varchar(64),
   gender               tinyint,
   avatar               varchar(255),
   age                  int,
   primary key (user_id)
);

alter table tab_user comment '用户表';
