DO $$
BEGIN
  CREATE TABLE PRODUCT (
    PRODUCT_ID UUID NOT NULL DEFAULT GEN_RANDOM_UUID(),
    REGISTRATION_DATE TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT PRODUCT_PK PRIMARY KEY (PRODUCT_ID)
  );

  COMMENT ON  TABLE PRODUCT IS 'Table to store Products';
  COMMENT ON COLUMN PRODUCT.PRODUCT_ID IS 'The Id in this table.';
  COMMENT ON COLUMN PRODUCT.REGISTRATION_DATE IS 'The Registration Date in this table.';

  CREATE TABLE PRODUCT_PERIOD_EFFECT (
    PRODUCT_PERIOD_EFFECT_ID UUID NOT NULL DEFAULT GEN_RANDOM_UUID(),
    PERIOD_EFFECT TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    PRODUCT_ID UUID NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    PRICE NUMERIC(15, 2) NOT NULL DEFAULT 0,
    DISCOUNT NUMERIC(5, 2) NOT NULL DEFAULT 0,
    CONSTRAINT PRODUCT_PERIOD_EFFECT_PK PRIMARY KEY (PRODUCT_PERIOD_EFFECT_ID),
    CONSTRAINT PRODUCT_FK FOREIGN KEY (PRODUCT_ID)
      REFERENCES PRODUCT (PRODUCT_ID)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
  );

  COMMENT ON  TABLE PRODUCT_PERIOD_EFFECT IS 'Table to store Products Period Effects';
  COMMENT ON COLUMN PRODUCT_PERIOD_EFFECT.PRODUCT_PERIOD_EFFECT_ID IS 'The Id in this table.';
  COMMENT ON COLUMN PRODUCT_PERIOD_EFFECT.PERIOD_EFFECT IS 'The Period Effect in this table.';
  COMMENT ON COLUMN PRODUCT_PERIOD_EFFECT.PRODUCT_ID IS 'The Product Id in this table.';
  COMMENT ON COLUMN PRODUCT_PERIOD_EFFECT.PRICE IS 'The Price in this table.';
  COMMENT ON COLUMN PRODUCT_PERIOD_EFFECT.DISCOUNT IS 'The Discount in this table.';
END
$$;
