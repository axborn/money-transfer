create table ACCOUNTS(
  ID 		int(10) auto_increment,
  STATUS  	varchar(10),
  EMAIL    	varchar(320),
  PHONE		varchar(20),
  IBAN		varchar(34),
  CURRENCY	varchar(3),
  BALANCE	varchar(10),
  constraint PK_ACCOUNT primary key ( ID )
);

  
insert into ACCOUNTS values(default, 'PENDING', 'drakoniukas@email.lt', '+3706666666', 'LT123', 'EUR', '0' );
insert into ACCOUNTS values(default, 'APPROVED', 'kirmelyte@email.lt', '86868686868', 'LT234', 'EUR', '1000' );
insert into ACCOUNTS values(default, 'CREATED', 'oziukas@email.lt', '4242424242', 'LT345', 'EUR', '0.35' );