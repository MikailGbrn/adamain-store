create table if not exists app_user (
    user_id         uuid primary key not null,
    first_name      varchar(100) not null,
    last_name       varchar(100) not null,
    username        varchar(100) not null unique,
    password        varchar(100) not null,
    created_at      timestamptz default current_timestamp,
    created_by      text,
    updated_at      timestamptz,
    updated_by      text,
    deleted         boolean default false,
    deleted_at      timestamptz
);