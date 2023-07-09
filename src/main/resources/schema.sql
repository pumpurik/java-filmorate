CREATE TABLE IF NOT EXISTS  "films" (
  "id" bigint auto_increment  PRIMARY KEY,
  "mpa_id" BIGINT,
  "name" varchar,
  "description" text,
  "releaseDate" date,
  "duration" BIGINT
);

CREATE TABLE IF NOT EXISTS "genres" (
  "id" bigint auto_increment  PRIMARY KEY,
  "name_genre" varchar UNIQUE
);

CREATE TABLE IF NOT EXISTS "likes" (
  "id" bigint auto_increment  PRIMARY KEY,
  "user_id" BIGINT,
  "film_id" BIGINT
);

CREATE TABLE IF NOT EXISTS "users" (
  "id" bigint auto_increment  PRIMARY KEY,
  "email" varchar UNIQUE,
  "login" varchar,
  "name" varchar,
  "birthday" date
);

CREATE TABLE IF NOT EXISTS "friendship" (
  "id" bigint auto_increment  PRIMARY KEY,
  "user_id" integer,
  "friends_id" integer,
  "friends_status" varchar
);

CREATE TABLE IF NOT EXISTS "genres_films" (
  "genres_id" BIGINT,
  "films_id" BIGINT,
  PRIMARY KEY ("genres_id", "films_id")
);
CREATE TABLE IF NOT EXISTS "mpa" (
	"id" bigint auto_increment  PRIMARY KEY,
	"name" varchar UNIQUE
);

ALTER TABLE "genres_films" ADD FOREIGN KEY ("genres_id") REFERENCES "genres" ("id");

ALTER TABLE "genres_films" ADD FOREIGN KEY ("films_id") REFERENCES "films" ("id");


ALTER TABLE "friendship" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friendship" ADD FOREIGN KEY ("friends_id") REFERENCES "users" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "films" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("id");

