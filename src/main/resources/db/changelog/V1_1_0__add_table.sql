-- description
create table user_registration_details (
urd_id int8 not null,
created_at timestamp not null,
updated_at timestamp not null,
urd_pass_doc bytea,
urd_nid_doc bytea,
urd_vt_doc bytea,
urd_bc_doc bytea,
urd_hc_doc bytea,
urd_oc_doc bytea,
urd_ur_id int8 not null,
primary key (urd_id)
);

alter table if exists user_registration_details
add constraint fk_user_details_registration foreign key (urd_ur_id) references user_registration;

