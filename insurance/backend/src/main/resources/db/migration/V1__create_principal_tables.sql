CREATE TABLE "user"(
  login TEXT PRIMARY KEY NOT NULL,
  password_hash TEXT NOT NULL
);

CREATE TABLE role(
  role_name TEXT PRIMARY KEY NOT NULL
);

CREATE TABLE user_roles(
  login TEXT REFERENCES "user"(login),
  role TEXT REFERENCES role(role_name),
  PRIMARY KEY (login, role)
);

CREATE TABLE persistent_logins (
  username varchar(64) not null,
  series varchar(64) not null,
  token varchar(64) not null,
  last_used timestamp not null,
  PRIMARY KEY (series)
);