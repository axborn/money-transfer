create table ACCOUNTS(
  ID 		int(10) auto_increment NOT NULL,
  STATUS  	varchar(10) NOT NULL,
  EMAIL    	varchar(320) NOT NULL,
  PHONE		varchar(20) NOT NULL,
  IBAN		varchar(34) NOT NULL,
  CURRENCY	varchar(3) NOT NULL,
  BALANCE	decimal(10) NOT NULL,
  constraint PK_ACCOUNT primary key ( ID )
);

create table TRANSACTIONS(
  ID 		int(10) auto_increment NOT NULL,
  FROM_ACCOUNT 		int(10) NOT NULL,
  TO_ACCOUNT 		int(10) NOT NULL,
  CURRENCY	varchar(3) NOT NULL,
  AMOUNT	decimal(10) NOT NULL,
  STATUS  	varchar(10) NOT NULL,
  constraint PK_TRANSACTION primary key ( ID )
);

  
insert into ACCOUNTS values(default, 'PENDING', 'drakoniukas@email.lt', '+3706666666', 'LT123', 'EUR', '-400' );
insert into ACCOUNTS values(default, 'APPROVED', 'kirmelyte@email.lt', '86868686868', 'LT234', 'EUR', 600 );
insert into ACCOUNTS values(default, 'CREATED', 'oziukas@email.lt', '4242424242', 'LT345', 'EUR', -200 );
insert into ACCOUNTS values(default, 'SUSPENDED', 'asilas@email.lt', '6969696969', 'LT456', 'EUR', 0 );


insert into TRANSACTIONS values(default, 2, 1, 'EUR', 100, 'PENDING');
insert into TRANSACTIONS values(default, 1, 2, 'EUR', 500, 'CLEARED');
insert into TRANSACTIONS values(default, 3, 2, 'EUR', 200, 'CLEARED');