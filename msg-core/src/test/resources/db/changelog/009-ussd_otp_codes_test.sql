DROP TABLE IF EXISTS "ussd_otp_codes";
CREATE TABLE "messaging"."ussd_otp_codes" (
  "ussd_otp_code_id" SERIAL PRIMARY KEY,
  "ussd_code" VARCHAR(32) NOT NULL,
  "country_code" VARCHAR(3) NOT NULL REFERENCES "countries" ("iso_code"),
  "created_at" timestamptz NOT NULL DEFAULT current_timestamp,
  "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
  "deleted_at" timestamptz NULL,
  "created_by" VARCHAR(128),
  "updated_by" VARCHAR(128)
);

insert into messaging.ussd_otp_codes(ussd_code, country_code)
	values('*347*797*365#', 'NG'),
		  ('*347*797*365#', 'ZA')