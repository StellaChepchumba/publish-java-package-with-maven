INSERT INTO "routes" ("vendor_code", "country_code", "delivery_mode")
	values('BOTPRESS', 'ZA', 'BOT'),
		  ('BOTPRESS', 'KE', 'BOT'),
		  ('BOTPRESS', 'NG', 'USSD');

alter table routes add column extra_data JSONB not null default '{}'::JSONB;

update routes set extra_data = '{"GUPSHUP_APP_NAME":"ImaliPayNigeria", "BOTPRESS_BOT_ID": "nigeria_customer_bot", "WHATSAPP_NUMBER": "2348182593775"}' where vendor_code = 'BOTPRESS' and delivery_mode = 'BOT' and country_code = 'NG';
update routes set extra_data = '{"GUPSHUP_APP_NAME":"SouthAfricaBot", "BOTPRESS_BOT_ID": "imalipay-sa-bot", "WHATSAPP_NUMBER": "27739815889"}' where vendor_code = 'BOTPRESS' and delivery_mode = 'BOT' and country_code = 'SA';
update routes set extra_data = '{"GUPSHUP_APP_NAME":"ImaliPay", "BOTPRESS_BOT_ID": "imalipay-ke-bot", "WHATSAPP_NUMBER": "254103919088"}' where vendor_code = 'BOTPRESS' and delivery_mode = 'BOT' and country_code = 'KE';

update routes set extra_data = '{"USSD_CODE":"*347*797#", "BOTPRESS_BOT_ID": "nigeria_customer_bot"}' where vendor_code = 'BOTPRESS' and delivery_mode = 'USSD' and country_code = 'NG';