CREATE TABLE IF NOT EXISTS CREDIT_CARD_DETAILS(
  ID NUMBER(19,0) NOT NULL,
  CUSTOMER_NAME VARCHAR2(200) NOT NULL,
  CARD_NUMBER VARCHAR2(19) NOT NULL,
  CREDIT_LIMIT NUMBER(19,0) NOT NULL,
  BALANCE NUMBER(19,2) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT UC_CUSTOMER_NAME UNIQUE (CUSTOMER_NAME)
);