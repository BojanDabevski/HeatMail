ALTER TABLE users
    ADD PRIMARY KEY (id) ;
ALTER TABLE heat_mail
    ADD FOREIGN KEY (user_id) REFERENCES users(id);