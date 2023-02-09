CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA "messaging";

DROP TABLE IF EXISTS "countries";
CREATE TABLE "countries" (
  "iso_code" varchar(3) not null PRIMARY KEY, 
  "name" varchar(30) not null,
  "flag_url" text,
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
insert into "countries"("iso_code", "name")
values ('NG', 'Nigeria'),
       ('KE', 'Kenya'),
       ('ZA', 'South Africa');


DROP TABLE IF EXISTS "message_statuses";
CREATE TABLE "message_statuses" (
  "message_status_name" varchar(16) NOT NULL PRIMARY KEY, 
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
insert into "message_statuses"("message_status_name")
values ('SENT'),('DELIVERED'),('FAILED'),('SCHEDULED'), ('CREATED');


CREATE type message_type as ENUM('PROMOTIONAL', 'TRANSACTIONAL');

DROP TABLE IF EXISTS "delivery_modes";
CREATE TABLE "delivery_modes" (
  "delivery_mode_name" varchar(16) NOT NULL PRIMARY KEY, 
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
insert into "delivery_modes"( "delivery_mode_name")
values ('EMAIL'),('SMS'),('WHATSAPP'),('SLACK'),('IN_APP');

DROP TABLE IF EXISTS "vendors";
CREATE TABLE "vendors" (
  "vendor_code" VARCHAR(16) NOT NULL PRIMARY KEY, 
  "vendor_name" VARCHAR(32) NOT NULL,
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
INSERT INTO "vendors" ("vendor_code", "vendor_name")
values('UWAZII', 'UWAZII'),
	('GMAIL', 'GMAIL'),
	('FIREBASE', 'FIREBASE'),
	('GUPSHUP', 'GUPSHUP'),
	('INFOBIP', 'INFOBIP');


DROP TABLE IF EXISTS "routes";
CREATE TABLE "routes" (
  "route_id" SERIAL PRIMARY KEY,
  "vendor_code" varchar(16) not null REFERENCES "vendors" ("vendor_code"),
  "country_code" varchar(3) not null REFERENCES "countries" ("iso_code"),
  "delivery_mode" varchar(16) not null REFERENCES "delivery_modes" ("delivery_mode_name"),
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
INSERT INTO "routes" ("vendor_code", "country_code", "delivery_mode")
values('UWAZII', 'KE', 'SMS'),
	('GMAIL', 'KE', 'EMAIL'),
	('GUPSHUP', 'KE', 'WHATSAPP'),
	('FIREBASE', 'KE', 'IN_APP'),
	('INFOBIP', 'NG', 'SMS'),
	('GMAIL', 'NG', 'EMAIL'),
	('GUPSHUP', 'NG', 'WHATSAPP'),
	('FIREBASE', 'NG', 'IN_APP');

DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
  "user_id" SERIAL PRIMARY KEY,
  "country_code" varchar(3) not null REFERENCES "countries" ("iso_code"),
  "external_user_id" varchar(256) NOT NULL UNIQUE,
  "default_delivery_mode" varchar(16) not null REFERENCES "delivery_modes" ("delivery_mode_name") DEFAULT 'SMS',
  "user_name" varchar(128) NOT NULL,
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
CREATE INDEX "users_created_at" ON "users"("created_at");
CREATE INDEX "users_name" ON "users"("user_name");

--- message tracker 
DROP TABLE IF EXISTS "messages";
CREATE TABLE "messages" (
  "message_id" UUID  PRIMARY KEY,
  "external_id" varchar(128) null,
  "source" varchar(256) not null,
  "recipient" varchar(256) not null,
  "body" text not null,
  "description" text NULL,
  "sent_at" timestamptz NULL,
  "number_of_sends" INT NOT NULL DEFAULT 0,
  "max_number_of_sends" INT NOT NULL DEFAULT 3,
  "route_id" int not null REFERENCES "routes" ("route_id"),
  "status" varchar(16) not null REFERENCES "message_statuses" ("message_status_name"),
  "country_code" varchar(3) not null REFERENCES "countries" ("iso_code"),
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
CREATE INDEX "messages_created_at" ON "messages"("created_at");
CREATE INDEX "messages_sent_at" ON "messages"("sent_at");
