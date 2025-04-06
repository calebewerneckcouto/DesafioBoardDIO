--liquibase formatted sql
--changeset calebe:202408191938
--comment: set unblock_reason nullable

ALTER TABLE BLOCKS ALTER COLUMN unblock_reason DROP NOT NULL;

--rollback ALTER TABLE BLOCKS ALTER COLUMN unblock_reason SET NOT NULL;
