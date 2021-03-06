CREATE TABLE "QUALITY_GATE_CONDITIONS"(
    "ID" INTEGER NOT NULL AUTO_INCREMENT (1,1),
    "UUID" VARCHAR(40) NOT NULL,
    "QGATE_ID" INTEGER,
    "METRIC_ID" INTEGER,
    "PERIOD" INTEGER,
    "OPERATOR" VARCHAR(3),
    "VALUE_ERROR" VARCHAR(64),
    "VALUE_WARNING" VARCHAR(64),
    "CREATED_AT" TIMESTAMP,
    "UPDATED_AT" TIMESTAMP
);
