CREATE TABLE Heat_Mail (
                          id varchar(255) NOT NULL,
                          month varchar(2) DEFAULT NULL,
                          year varchar(4) DEFAULT NULL,
                          user_id varchar(255) DEFAULT NULL,
                          status varchar(100) DEFAULT null,
                          mail_body longtext DEFAULT NULL,
                          mail_title longtext DEFAULT NULL,
                          mail_receiver varchar(255) DEFAULT NULL,
                          inserted_at date DEFAULT NULL,
                          sent_at date null,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

