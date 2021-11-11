/*
description
*/
create table user_registration (
ur_id int8 not null,
created_at timestamp not null,
updated_at timestamp not null,
ur_address_id int8,
ur_mail varchar(255),
ur_mobile_nr varchar(255),
ur_phone_nr varchar(15),
ur_first_name varchar(255),
ur_full_name varchar(255),
ur_last_name varchar(255),
ur_middle_name varchar(255),
ur_login_name varchar(255),
ur_login_pass varchar(255),
ur_web_www varchar(255),
primary key (ur_id)
);
/*
description
*/
create table user_registration_profile (
urp_id int8 not null,
urp_city varchar(255),
urp_country varchar(255),
urp_dob date,
urp_house_nr varchar(11),
urp_state varchar(255),
urp_street varchar(255),
urp_zip_code varchar(32),
urp_ur_id int8 not null,
primary key (urp_id)
);

/*
description
*/
create table user_registration_role (
urr_id int8 not null,
created_at timestamp not null,
updated_at timestamp not null,
urr_ref_nr_id int8,
urr_cr_date timestamp,
urr_mo_date timestamp,
urr_name varchar(255),
urr_type varchar(255),
urr_ur_id int8 not null,
primary key (urr_id)
);

alter table if exists user_registration_profile
add constraint UK_e11616iflgryv1ms3mqa1mbry unique (urp_ur_id);
alter table if exists user_registration_profile
add constraint fk_user_profile_registration foreign key (urp_ur_id) references user_registration;
alter table if exists user_registration_role
add constraint fk_user_role_registration foreign key (urr_ur_id) references user_registration;

