CREATE TABLE Users (
   id varchar(100) DEFAULT NULL,
   full_name varchar(100) DEFAULT NULL,
   email varchar(100) DEFAULT NULL,
   password varchar(100) DEFAULT NULL,
   created_at date DEFAULT NULL,
   updated_at date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;