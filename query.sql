 create table pens(
 ID serial primary key,
name varchar (32),
 creation_date timestamp,
 width_of_kernel decimal,
 amount integer,
 length_of_kernel integer,
 weight decimal,
 exists boolean,
ID_user integer references users(ID) on delete cascade
);
 create table users (
 ID SERIAL PRIMARY KEY,
 username VARCHAR (255),
 password VARCHAR (255)
 );
 INSERT INTO users(username, password)
 VALUES('root', '435b41068e8665513a20070c033b08b9c66e4332');