CREATE TABLE "PERMISSION_TEMPLATES"(
    "ID" INTEGER NOT NULL AUTO_INCREMENT (1,1),
    "UUID" VARCHAR(40),
    "ORGANIZATION_UUID" VARCHAR(40) NOT NULL,
    "NAME" VARCHAR(100) NOT NULL,
    "KEE" VARCHAR(100) NOT NULL,
    "DESCRIPTION" VARCHAR(4000),
    "CREATED_AT" TIMESTAMP,
    "UPDATED_AT" TIMESTAMP,
    "KEY_PATTERN" VARCHAR(500)
);
ALTER TABLE "PERMISSION_TEMPLATES" ADD CONSTRAINT "PK_PERMISSION_TEMPLATES" PRIMARY KEY("ID");
