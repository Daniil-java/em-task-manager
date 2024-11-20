--liquibase formatted sql

--changeset DanielK:2

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');