--- message tracker 
DROP TABLE IF EXISTS "message_templates";
CREATE TABLE "message_templates" (
  "message_template_id" SERIAL PRIMARY KEY,
  "event" varchar(16) not null,
  "template" text not null,
  "source" varchar(32) not null,
  "delivery_mode" varchar(16) not null REFERENCES "delivery_modes" ("delivery_mode_name"),
  "country_code" varchar(3) not null REFERENCES "countries" ("iso_code"),
  "created_at" timestamptz not null default current_timestamp,
  "updated_at" timestamptz default current_timestamp,
  "created_by" varchar(128),
  "updated_by" varchar(128)
);
insert into message_templates("event", "template", "source", "delivery_mode", "country_code")
values ('OTP', 'Welcome to imalipay. Your one time pin is ${OTP}', 'ImaliPay', 'SMS', 'KE'),
	   ('OTP', 'Welcome to imalipay. Your one time pin is ${OTP}', 'Imalipay', 'SMS', 'NG'),
	   ('REFERRAL', 'Hey ${REFERRAL_NAME}, join ${REFERRED_BY_NAME} on ImaliPay to access Okoa fuel, smartphones and much more to keep your hustle moving.Click on this link https://bit.ly/3cCyW6P to register and get free airtime from ImaliPay. Call 0715292929 for inquiries', 'ImaliPay', 'SMS', 'KE'),
	   ('REFERRAL', 'Hey ${REFERRAL_NAME}, join ${REFERRED_BY_NAME} on ImaliPay to access Okoa fuel, smartphones and much more to keep your hustle moving.Click on this link https://bit.ly/3cCyW6P to register and get free airtime from ImaliPay. Call 0706 229 0096 or 0703 129 3439 for inquiries', 'Imalipay', 'SMS', 'NG');