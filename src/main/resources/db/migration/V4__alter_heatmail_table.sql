ALTER TABLE heatmail.heat_mail ADD mail_body_variables longtext NULL;
ALTER TABLE heatmail.heat_mail MODIFY COLUMN inserted_at timestamp DEFAULT NULL NULL;
ALTER TABLE heatmail.heat_mail MODIFY COLUMN sent_at timestamp DEFAULT NULL NULL;
