# --- !Ups

CREATE TABLE "university"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "location" varchar(200));
INSERT INTO "university" values (1,'HCU', 'Hyderabad');
INSERT INTO "university" values (2,'BHU', 'Banaras');
INSERT INTO "university" values (3,'PUNE University', 'Pune');

CREATE TABLE "student"("id" SERIAL PRIMARY KEY ,"name" varchar(200) , "email" varchar(200)  ,"university_id" int );
INSERT INTO "student" values (1,'Bob', 'bob@xyz.com',3);
INSERT INTO "student" values (2,'Rob', 'rob@abc.com',2);
INSERT INTO "student" values (3,'Joe', 'joe@xyz.com',1);
# --- !Downs

DROP TABLE "student";
DROP TABLE "university";
