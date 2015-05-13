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




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists mock;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists mock_seq;

