# --- !Ups

create table mock (
  id                        bigint not null,
  uri                       varchar(255),
  method                    varchar(255),
  requestbody               text,
  responsebody              text,
  constraint pk_mock primary key (id))
;

create sequence mock_seq;

