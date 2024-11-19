--liquibase formatted sql

--changeset DanielK:2

alter table users
    rename column password to password_hash;

alter table users
    add username text not null;

alter table roles
    rename column authority to name;

alter table tasks
    rename column executor_id to assignee_id;

alter table tasks
drop constraint tasks_executor_id_fkey;

alter table tasks
    add foreign key (assignee_id) references users;