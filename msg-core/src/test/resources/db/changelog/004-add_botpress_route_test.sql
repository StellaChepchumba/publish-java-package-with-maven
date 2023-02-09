insert into "delivery_modes"( "delivery_mode_name")
values ('BOT');

INSERT INTO "vendors" ("vendor_code", "vendor_name")
values('BOTPRESS', 'BOTPRESS');

INSERT INTO "routes" ("vendor_code", "country_code", "delivery_mode")
values('BOTPRESS', 'NG', 'BOT');