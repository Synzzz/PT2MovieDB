DROP DATABASE IF EXISTS moviedb;
CREATE DATABASE moviedb;

USE moviedb;

DROP TABLE IF EXISTS moviedb.movies;

CREATE TABLE movies(
	title VARCHAR(100) NOT NULL,
	director VARCHAR(50) NOT NULL,
    PRIMARY KEY(title,director),
	prodYear INTEGER NOT NULL,
	lent BOOLEAN DEFAULT FALSE NOT NULL,
	lentCount INTEGER DEFAULT 0 NOT NULL,
	original BOOLEAN NOT NULL,
	storageType VARCHAR(3) NOT NULL,
	movieLength INTEGER NOT NULL,
	mainCast VARCHAR(255) NOT NULL,
	img LONGBLOB 	
	);

INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('The Godfather','Francis Ford Coppola',1972,1,'VHS',175,'Marlon Brando,Al Pacino');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('The Dark Knight','Christopher Nolan',2008,0,'DVD',152,'Christian Bale,Heath Ledger');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Pulp Fiction','Quentin Tarantino',1994,1,'VHS',154,'Samuel L. Jackson,John Travolta');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('The Empire Strikes Back','Irvin Kershner',1980,1,'DVD',124,'Mark Hamill,Harrison Ford,Carrie Fisher');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Spirited away',' Hayao Miyazaki',2001,0,'DVD',125,'');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Interstellar','Christopher Nolan',2014,0,'DVD',169,'Matthew McConaughey');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Back to the Future','Robert Zemeckis',1985,1,'VHS',116,'Michael J. Fox,Christopher Lloyd');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Django Unchained','Quentin Tarantino',2012,0,'DVD',165,'Jamie Foxx,Leonardo DiCaprio,Samuel L. Jackson');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('Your name','Makoto Shinkai',2016,1,'DVD',106,'');
INSERT INTO movies (title,director,prodYear,original,storageType,movieLength,mainCast) 
	VALUES('The Force Awakens','J.J. Abrams',2015,0,'DVD',136,'Harrison Ford,Mark Hamill,Carrie Fisher,Adam Driver,Daisy Ridley,John Boyega');

DROP TABLE IF EXISTS moviedb.lendings;

CREATE TABLE lendings(
	name VARCHAR(50),
	startDate DATE,
	endDate DATE,
	title VARCHAR(100),
    director VARCHAR(50)
);




