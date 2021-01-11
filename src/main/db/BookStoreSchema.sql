CREATE TABLE books 
(
	name VARCHAR(30) NOT NULL,
	isbn VARCHAR(13) NOT NULL PRIMARY KEY,
	publisher VARCHAR(25) NOT NULL,
	author VARCHAR(40) NOT NULL,
	released date NOT NULL,
	description VARCHAR(100) NOT NULL,
	picture VARCHAR(200) NOT NULL,
    pages INT NOT NULL,
    cover VARCHAR(10) NOT NULL,
    letter VARCHAR(10) NOT NULL,
    language VARCHAR(20) NOT NULL,
    price FLOAT NOT NULL,
    remaining INT NOT NULL,
    rating FLOAT,
	active BIT
);

CREATE TABLE bookGenres
(	
	bookISBN VARCHAR(13) NOT NULL,
	genreName VARCHAR(30) NOT NULL
);

CREATE TABLE genres
(	
	name VARCHAR(30) NOT NULL PRIMARY KEY,
	description VARCHAR(100) NOT NULL
);

CREATE TABLE boughtBook
(	
	name VARCHAR(30) NOT NULL,
	id VARCHAR(10) NOT NULL PRIMARY KEY,
	book VARCHAR(10) NOT NULL,
    price FLOAT NOT NULL
);

CREATE TABLE boughtBookReceipt
(	
	idBook VARCHAR(10) NOT NULL,
	idReceipt VARCHAR(10) NOT NULL
);

CREATE TABLE receipt
(	
	name VARCHAR(30) NOT NULL,
	id VARCHAR(10) NOT NULL PRIMARY KEY,
	book VARCHAR(13) NOT NULL,
    user VARCHAR(30) NOT NULL,
    dateOfPurchase DATE NOT NULL,
    numOfBooks INT NOT NULL,
    price FLOAT NOT NULL
);

CREATE TABLE users
(	
	username VARCHAR(30) NOT NULL PRIMARY KEY,
	password VARCHAR(30) NOT NULL,
	email VARCHAR(30) NOT NULL,
	name VARCHAR(30) NOT NULL,
	lastName VARCHAR(30) NOT NULL,
	dateOfBirth date NOT NULL,
    address VARCHAR(30) NOT NULL,
    phone VARCHAR(30) NOT NULL,
	dateOfRegistration VARCHAR(30) NOT NULL,
    userType  VARCHAR(30) NOT NULL,
	active BIT NOT NULL
);

CREATE TABLE comments
(	
name VARCHAR(30) NOT NULL,
	comment_text VARCHAR(300) NOT NULL,
	grade FLOAT NOT NULL,
	dateOfComment DATE NOT NULL,
	author VARCHAR(30) NOT NULL,
	commentedBook VARCHAR(13) NOT NULL,
    status VARCHAR(30)
);