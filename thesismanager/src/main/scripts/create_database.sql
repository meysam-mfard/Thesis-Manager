
# JUST FOR DEVELOPMENT PHASE
DROP DATABASE IF EXISTS thesis_manager;

CREATE DATABASE thesis_manager;

USE thesis_manage;

create table document (dtype varchar(31) not null, id bigint not null auto_increment, comment longtext, file longblob, file_name varchar(100), file_type varchar(50), submission_time datetime, author_role integer, author_id bigint, primary key (id)) engine=InnoDB;
create table semester (id bigint not null auto_increment, active bit not null, final_report_deadline datetime, project_description_deadline datetime, project_plan_deadline datetime, report_deadline datetime, semester_period integer, year tinyblob, primary key (id)) engine=InnoDB;
create table submission (id bigint not null auto_increment, grade float, type varchar(50), submitted_document_id bigint, thesis_id bigint, primary key (id)) engine=InnoDB;
create table submission_feedbacks (submission_id bigint not null, feedbacks_id bigint not null) engine=InnoDB;
create table thesis (id bigint not null auto_increment, final_grade float, supervisor_request_status integer, coordinator_id bigint, opponent_id bigint, semester_id bigint, student_id bigint, supervisor_id bigint, primary key (id)) engine=InnoDB;
create table thesis_bidders (thesis_id bigint not null, bidders_id bigint not null, primary key (thesis_id, bidders_id)) engine=InnoDB;
create table thesis_readers (thesis_id bigint not null, readers_id bigint not null, primary key (thesis_id, readers_id)) engine=InnoDB;
create table user (id bigint not null auto_increment, account_is_active bit, first_name varchar(50), last_name varchar(50), password varchar(255), username varchar(50), primary key (id)) engine=InnoDB;
create table user_roles (user_id bigint not null, roles varchar(50)) engine=InnoDB;
alter table submission_feedbacks add constraint UK_y09lpv0s5k0m36khyq80pmip unique (feedbacks_id);
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);
alter table document add constraint FKegc4plit7eewymjwtrr02r89o foreign key (author_id) references user (id);
alter table submission add constraint FKiu70a8toh3mb32ndg2mxb41pc foreign key (submitted_document_id) references document (id);
alter table submission add constraint FK8oga1ynb0pgulioyvhx4tfr8u foreign key (thesis_id) references thesis (id);
alter table submission_feedbacks add constraint FKdcngvah11nh7nj9p3hk00slji foreign key (feedbacks_id) references document (id);
alter table submission_feedbacks add constraint FK6k22k0f64nysvq4jpvdxe4dph foreign key (submission_id) references submission (id);
alter table thesis add constraint FK1m3nwp9a8i8pkjt8xrttmtkls foreign key (coordinator_id) references user (id);
alter table thesis add constraint FKx286ecpvk624ug292ua5s2ap foreign key (opponent_id) references user (id);
alter table thesis add constraint FKf4lac98vqs906kbxaxmp34ms7 foreign key (semester_id) references semester (id);
alter table thesis add constraint FKe4wjivc09sfclqkoyphg7iqdp foreign key (student_id) references user (id);
alter table thesis add constraint FKej6y1qfyfnk9447n0s68umgsk foreign key (supervisor_id) references user (id);
alter table thesis_bidders add constraint FKsi1n2j33tcqs9nu21n3hy02c3 foreign key (bidders_id) references user (id);
alter table thesis_bidders add constraint FK9xo5hll8r2bgoy0faegpjhw4e foreign key (thesis_id) references thesis (id);
alter table thesis_readers add constraint FK80pdcsh51w0tar86wftmi6wsc foreign key (readers_id) references user (id);
alter table thesis_readers add constraint FKapxuqqe05kj74nnydyny7ouh7 foreign key (thesis_id) references thesis (id);
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);
