ALTER TABLE heatmail.heat_mail ADD mail_attachment_title longtext NULL;

CREATE TABLE Heat_Mail_Attachment (
                           id varchar(255) NOT NULL,
                           user_id varchar(255) DEFAULT NULL,
                           mail_attachment_body longtext DEFAULT NULL,
                           mail_attachment_title longtext DEFAULT NULL,
                           PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;