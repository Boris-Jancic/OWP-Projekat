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

CREATE TABLE wishLists
(	
	username VARCHAR(30) NOT NULL,
	isbn VARCHAR(13) NOT NULL
);

CREATE TABLE boughtBooks
(	
	id VARCHAR(10) NOT NULL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
	book VARCHAR(13) NOT NULL,
    numOfCopies INT NOT NULL,
    price FLOAT NOT NULL,
	idReceipt VARCHAR(10) NOT NULL
);

CREATE TABLE receipts
(	
	id VARCHAR(10) NOT NULL PRIMARY KEY,
    dateOfPurchase VARCHAR(30) NOT NULL,
	name VARCHAR(30) NOT NULL,
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
	active BIT NOT NULL,
    status VARCHAR(30) NOT NULL,
	points int NOT NULL
);

CREATE TABLE comments
(	
	id VARCHAR(10) NOT NULL PRIMARY KEY,
	author VARCHAR(30) NOT NULL,
	text VARCHAR(300) NOT NULL,
	book VARCHAR(13) NOT NULL,
	grade int NOT NULL,
	dateOfComment DATE NOT NULL,
    status VARCHAR(30)
);

CREATE TABLE discounts
(
    id VARCHAR(10) NOT NULL PRIMARY KEY,
    discount int NOT NULL,
    startDisc DATE NOT NULL,
    endDisc DATE NOT NULL,
    isbn VARCHAR(13) NOT NULL
);