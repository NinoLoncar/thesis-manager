CREATE TABLE role
(
	id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	CONSTRAINT pk_role PRIMARY KEY (id),
	CONSTRAINT uq_role_name UNIQUE (name)
);

CREATE TABLE permission
(
	id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	CONSTRAINT pk_permission PRIMARY KEY (id),
	CONSTRAINT uq_permission_name UNIQUE (name)
);

CREATE TABLE users
(
	id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
	email      VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name  VARCHAR(255) NOT NULL,
	password   VARCHAR(255) NOT NULL,
	created_at DATETIME     NOT NULL,
	created_by VARCHAR(255) NULL,
	updated_at DATETIME NULL,
	updated_by VARCHAR(255) NULL,
	CONSTRAINT pk_users PRIMARY KEY (id),
	CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE role_permission
(
	role_id       INT UNSIGNED  NOT NULL,
	permission_id INT UNSIGNED  NOT NULL,
	CONSTRAINT pk_role_permission PRIMARY KEY (role_id, permission_id),
	CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES role (id),
	CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permission (id)
);

CREATE TABLE user_role
(
	user_id INT UNSIGNED  NOT NULL,
	role_id INT UNSIGNED  NOT NULL,
	CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users (id),
	CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE thesis
(
	id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
	title       VARCHAR(255) NOT NULL,
	abstract    TEXT         NULL,
	type        ENUM ('BACHELOR', 'MASTER') NOT NULL,
	status      ENUM ('PROPOSED', 'IN_PROGRESS', 'SUBMITTED') NOT NULL,
	student_id  INT UNSIGNED NULL,
	mentor_id   INT UNSIGNED NOT NULL,
	created_at  DATETIME     NOT NULL,
	created_by  VARCHAR(255) NULL,
	updated_at  DATETIME     NULL,
	updated_by  VARCHAR(255) NULL,
	submitted_at DATETIME    NULL,
	reserved_at DATETIME     NULL,
	CONSTRAINT pk_thesis PRIMARY KEY (id),
	CONSTRAINT fk_thesis_student FOREIGN KEY (student_id) REFERENCES users (id),
	CONSTRAINT fk_thesis_mentor FOREIGN KEY (mentor_id) REFERENCES users (id)
);

CREATE TABLE thesis_reservation
(
	id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
	student_id INT UNSIGNED NOT NULL,
	thesis_id  INT UNSIGNED NOT NULL,
	status     ENUM ('PENDING', 'APPROVED', 'DENIED', 'CANCELED') NOT NULL,
	created_at DATETIME     NOT NULL,
	created_by VARCHAR(255) NULL,
	updated_at DATETIME     NULL,
	updated_by VARCHAR(255) NULL,
	CONSTRAINT pk_thesis_reservation PRIMARY KEY (id),
	CONSTRAINT fk_thesis_reservation_student FOREIGN KEY (student_id) REFERENCES users (id),
	CONSTRAINT fk_thesis_reservation_thesis FOREIGN KEY (thesis_id) REFERENCES thesis (id)
);

CREATE TABLE thesis_submission
(
	id             INT UNSIGNED NOT NULL AUTO_INCREMENT,
	thesis_id      INT UNSIGNED NOT NULL,
	student_id     INT UNSIGNED NOT NULL,
	version        INT UNSIGNED NOT NULL,
	file_path      VARCHAR(500) NOT NULL,
	file_name      VARCHAR(255) NOT NULL,
	description    TEXT         NULL,
	status         ENUM ('UNDER_REVIEW', 'CHANGES_REQUESTED', 'ACCEPTED') NOT NULL,
	reviewed_by    INT UNSIGNED NULL,
	reviewed_at    DATETIME     NULL,
	created_at     DATETIME     NOT NULL,
	CONSTRAINT pk_thesis_submission PRIMARY KEY (id),
	CONSTRAINT uq_thesis_submission_version UNIQUE (thesis_id, version),
	CONSTRAINT fk_thesis_submission_thesis FOREIGN KEY (thesis_id) REFERENCES thesis (id),
	CONSTRAINT fk_thesis_submission_student FOREIGN KEY (student_id) REFERENCES users (id),
	CONSTRAINT fk_thesis_submission_reviewer FOREIGN KEY (reviewed_by) REFERENCES users (id)
);

CREATE TABLE thesis_submission_comment
(
	id            INT UNSIGNED NOT NULL AUTO_INCREMENT,
	submission_id INT UNSIGNED NOT NULL,
	author_id     INT UNSIGNED NOT NULL,
	content       TEXT         NOT NULL,
	created_at    DATETIME     NOT NULL,
	CONSTRAINT pk_thesis_submission_comment PRIMARY KEY (id),
	CONSTRAINT fk_thesis_submission_comment_submission FOREIGN KEY (submission_id) REFERENCES thesis_submission (id),
	CONSTRAINT fk_thesis_submission_comment_author FOREIGN KEY (author_id) REFERENCES users (id)
);

INSERT INTO role (id, name)
VALUES (1, 'ADMIN'),
       (2, 'STUDENT'),
       (3, 'PROFESSOR');

INSERT INTO permission (id, name)
VALUES (1, 'USERS_READ'),
       (2, 'ROLES_READ'),
       (3, 'USER_CREATE'),
       (4, 'USER_EDIT'),
       (5, 'THESIS_CREATE'),
       (6, 'THESES_READ'),
       (7, 'THESIS_DELETE'),
       (8, 'THESIS_EDIT'),
       (9, 'USER_DELETE'),
       (10, 'RESERVATION_CREATE'),
       (11, 'RESERVATION_MANAGE'),
       (12, 'SUBMISSIONS_READ'),
       (13, 'SUBMISSION_CREATE'),
       (14, 'COMMENT_CREATE')
       (15, 'SUBMISSION_REVIEW'),;

INSERT INTO role_permission (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (2, 6),
       (2, 10),
       (2, 12),
       (2, 13),
       (2, 14),
       (3, 5),
       (3, 6),
       (3, 7),
       (3, 8),
       (3, 11),
       (3, 12),
       (3, 14),
       (3, 15);

INSERT INTO users (id, email, first_name, last_name, password, created_at, created_by, updated_at, updated_by)
VALUES (1, 'admin', 'admin', 'admin', '$2a$10$HxZeiqys/KLGpWcn2cSiz.lBA.TB1.i.fptVnN0V4md.RNf.ULNme', NOW(), 'SYSTEM', NOW(), 'SYSTEM');

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);
