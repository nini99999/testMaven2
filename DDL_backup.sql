CREATE TABLE E_CLASS
(
    ID VARCHAR2(64) NOT NULL,
    CLASSNO VARCHAR2(20) NOT NULL,
    CLASSNAME VARCHAR2(20) NOT NULL,
    GRADENO VARCHAR2(20) DEFAULT NULL NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    SCHOOLNO VARCHAR2(20) DEFAULT NULL NOT NULL,
    CONSTRAINT E_CLASS_PK PRIMARY KEY (ID, CLASSNO),
    CONSTRAINT E_CLASS_FK1 FOREIGN KEY (GRADENO) REFERENCES E_GRADE (GRADENO),
    CONSTRAINT E_CLASS_FK FOREIGN KEY (SCHOOLNO) REFERENCES E_SCHOOL (SCHOOLNO)
);
COMMENT ON COLUMN E_CLASS.CLASSNO IS '班级编号';
COMMENT ON COLUMN E_CLASS.CLASSNAME IS '班级ming'chengg';
CREATE UNIQUE INDEX E_CLASS_ECLASSNO_UINDEX ON E_CLASS (CLASSNO);
CREATE TABLE E_GRADE
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    GRADENAME VARCHAR2(20) NOT NULL,
    GRADENO VARCHAR2(20) DEFAULT NULL NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate,
    SCHOOLNO VARCHAR2(20) DEFAULT NULL NOT NULL,
    CONSTRAINT E_GRADE_FK FOREIGN KEY (SCHOOLNO) REFERENCES E_SCHOOL (SCHOOLNO)
);
COMMENT ON COLUMN E_GRADE.GRADENAME IS '年级名称';
COMMENT ON COLUMN E_GRADE.GRADENO IS '年级编号';
CREATE UNIQUE INDEX E_GRADE_GRADENO_UINDEX ON E_GRADE (GRADENO);
CREATE TABLE E_PAPER_Q_TYPE
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    TPNO VARCHAR2(32) DEFAULT NULL NOT NULL,
    QUESTIONTYPE VARCHAR2(20) NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate,
    MARK NUMBER(6) DEFAULT 0,
    QUESTIONNUM NUMBER(6) DEFAULT 0,
    DESCRIPTION VARCHAR2(64)
);
COMMENT ON COLUMN E_PAPER_Q_TYPE.TPNO IS '试卷编号';
COMMENT ON COLUMN E_PAPER_Q_TYPE.QUESTIONTYPE IS '题型编号';
CREATE UNIQUE INDEX TPNO_QUESTIONTYPE_UINDEX ON E_PAPER_Q_TYPE (TPNO, QUESTIONTYPE);
CREATE TABLE E_SCHOOL
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    SCHOOLNO VARCHAR2(20) NOT NULL,
    SCHOOLNAME VARCHAR2(64) NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    SCHOOLTYPE VARCHAR2(4) DEFAULT '01'
);
COMMENT ON COLUMN E_SCHOOL.SCHOOLNO IS '学校编码';
COMMENT ON COLUMN E_SCHOOL.SCHOOLNAME IS '学校名称';
COMMENT ON COLUMN E_SCHOOL.SCHOOLTYPE IS ''01'--高中,'02'--初中，'03'--小学';
CREATE UNIQUE INDEX E_SCHOOL_SCHOOLNO_UINDEX ON E_SCHOOL (SCHOOLNO);
CREATE TABLE E_STUDENT
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    CHINAID VARCHAR2(30),
    STUDENTNAME VARCHAR2(20) NOT NULL,
    LOCALID VARCHAR2(30),
    SCHOOLNO VARCHAR2(20) DEFAULT NULL NOT NULL,
    COUNTRYID VARCHAR2(40),
    GRADENO VARCHAR2(20) DEFAULT NULL NOT NULL,
    CLASSNO VARCHAR2(20) DEFAULT NULL NOT NULL,
    NATION VARCHAR2(20),
    BIRTHDAY DATE,
    ADMISSIONDATE DATE,
    SCHOOLSTATE VARCHAR2(4) DEFAULT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE,
    STUDYSTATE VARCHAR2(4),
    CONSTRAINT E_STUDENT_E_SCHOOL_SCHOOLNO_FK FOREIGN KEY (SCHOOLNO) REFERENCES E_SCHOOL (SCHOOLNO),
    CONSTRAINT E_STUDENT_E_GRADE_GRADENO_FK FOREIGN KEY (GRADENO) REFERENCES E_GRADE (GRADENO),
    CONSTRAINT E_STUDENT_E_CLASS_CLASSNO_FK FOREIGN KEY (CLASSNO) REFERENCES E_CLASS (CLASSNO)
);
COMMENT ON COLUMN E_STUDENT.CHINAID IS '身份证号';
COMMENT ON COLUMN E_STUDENT.LOCALID IS '学籍辅号';
COMMENT ON COLUMN E_STUDENT.SCHOOLNO IS '学校编码';
COMMENT ON COLUMN E_STUDENT.COUNTRYID IS '全国学籍号';
COMMENT ON COLUMN E_STUDENT.NATION IS '民族';
COMMENT ON COLUMN E_STUDENT.BIRTHDAY IS '生日';
COMMENT ON COLUMN E_STUDENT.ADMISSIONDATE IS '入学时间';
COMMENT ON COLUMN E_STUDENT.SCHOOLSTATE IS '学籍状态：0-正常；1-转入；2-休学';
COMMENT ON COLUMN E_STUDENT.CREATOR IS '创建人';
COMMENT ON COLUMN E_STUDENT.CREATEDATE IS '创建日期';
COMMENT ON COLUMN E_STUDENT.STUDYSTATE IS '学习状态：0在校/1毕业/2离校';
CREATE UNIQUE INDEX E_STUDENT_CHINAID_UINDEX ON E_STUDENT (CHINAID);
CREATE TABLE E_STUDENT_MARK
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    STUDENTNO VARCHAR2(20),
    TESTPAPERNO VARCHAR2(20),
    MARK NUMBER(6,2),
    TESTDATE DATE,
    MARKONE NUMBER(6,2),
    MARKTWO NUMBER(6,2),
    CREATOR VARCHAR2(20),
    CREATEDATE DATE
);
COMMENT ON COLUMN E_STUDENT_MARK.MARKONE IS '客观题分数';
COMMENT ON COLUMN E_STUDENT_MARK.MARKTWO IS '主观题';
CREATE TABLE E_SUBJECT_Q_TYPE
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    SUBJECTNO VARCHAR2(20) DEFAULT NULL,
    QUESTIONTYPE VARCHAR2(20) DEFAULT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE,
    QUESTIONTYPENAME VARCHAR2(30) DEFAULT NULL,
    CONSTRAINT E_SUBJECT_Q_TYPE_FK FOREIGN KEY (SUBJECTNO) REFERENCES E_SUBJECTS (SUBJECTNO)
);
COMMENT ON COLUMN E_SUBJECT_Q_TYPE.SUBJECTNO IS '学科编码';
COMMENT ON COLUMN E_SUBJECT_Q_TYPE.QUESTIONTYPE IS '试题分类编码';
COMMENT ON COLUMN E_SUBJECT_Q_TYPE.QUESTIONTYPENAME IS '试题类型名称';
CREATE UNIQUE INDEX E_QUESTIONTYPE_UINDEX ON E_SUBJECT_Q_TYPE (QUESTIONTYPE);
CREATE TABLE E_SUBJECTS
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    SUBJECTNAME VARCHAR2(20) NOT NULL,
    SUBJECTNO VARCHAR2(20) DEFAULT NULL  NOT NULL,
    TOTALSCORE NUMBER(4)
);
COMMENT ON COLUMN E_SUBJECTS.SUBJECTNAME IS '学科名称';
COMMENT ON COLUMN E_SUBJECTS.SUBJECTNO IS '学科编码';
COMMENT ON COLUMN E_SUBJECTS.TOTALSCORE IS '满分';
CREATE UNIQUE INDEX E_SUBJECTS_SUBJECTNO_UINDEX ON E_SUBJECTS (SUBJECTNO);
CREATE TABLE E_TEACHER
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    TEACHERID VARCHAR2(20),
    TEACHERNAME VARCHAR2(20),
    SUBJECTNO VARCHAR2(20),
    SCHOOLNO VARCHAR2(20),
    TEL NUMBER(11) DEFAULT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    CONSTRAINT E_TEACHER_FK FOREIGN KEY (SUBJECTNO) REFERENCES E_SUBJECTS (SUBJECTNO),
    CONSTRAINT E_TEACHER_FK1 FOREIGN KEY (SCHOOLNO) REFERENCES E_SCHOOL (SCHOOLNO)
);
COMMENT ON COLUMN E_TEACHER.SUBJECTNO IS '所教学科编码';
CREATE UNIQUE INDEX "e_teacher_teacherid_uindex" ON E_TEACHER (TEACHERID);
CREATE TABLE E_TEACHER_CLASS
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    TEACHERNO VARCHAR2(20),
    CLASSNO VARCHAR2(20),
    CREATOR VARCHAR2(20),
    CREATEDATE DATE,
    CONSTRAINT E_TEACHER_CLASS_FK FOREIGN KEY (TEACHERNO) REFERENCES E_TEACHER (TEACHERID),
    CONSTRAINT E_TEACHER_CLASS_FK1 FOREIGN KEY (CLASSNO) REFERENCES E_CLASS (CLASSNO)
);
CREATE TABLE E_TESTPAPER
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    TPNAME VARCHAR2(128) NOT NULL,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate ,
    SUBJECTNO VARCHAR2(20),
    GRADENO VARCHAR2(20),
    SCHOOLNO VARCHAR2(20),
    TERM VARCHAR2(4),
    TPNO VARCHAR2(32) DEFAULT NULL NOT NULL,
    EXAMTYPE VARCHAR2(1) DEFAULT '0'
);
COMMENT ON COLUMN E_TESTPAPER.ID IS '试卷id';
COMMENT ON COLUMN E_TESTPAPER.TPNAME IS '试卷名称 ';
COMMENT ON COLUMN E_TESTPAPER.TERM IS '学期：0-上学期,1-下学期';
COMMENT ON COLUMN E_TESTPAPER.TPNO IS '试卷编号';
COMMENT ON COLUMN E_TESTPAPER.EXAMTYPE IS '考试类型';
CREATE UNIQUE INDEX E_TESTPAPER_TPNO_UINDEX ON E_TESTPAPER (TPNO);
CREATE TABLE R_ABOVESPECIFIEDMARK
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    CLASSNO VARCHAR2(20),
    ABOVEMARK NUMBER(6) DEFAULT 0,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate

);
CREATE UNIQUE INDEX R_ABOVEMARK_CLASSNO_UINDEX ON R_ABOVESPECIFIEDMARK (CLASSNO);
CREATE TABLE R_CLASSMARK
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    SCHOOLNO VARCHAR2(20),
    GRADENO VARCHAR2(20),
    CLASSNO VARCHAR2(20),
    TPNO VARCHAR2(20),
    SUBJECTNO VARCHAR2(20),
    TESTDATE DATE,
    MARK NUMBER(6,2) DEFAULT 0,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE
);
COMMENT ON COLUMN R_CLASSMARK.TESTDATE IS '考试日期';
COMMENT ON COLUMN R_CLASSMARK.MARK IS '班级平均分';
CREATE TABLE R_MARKAREA
(
    ID VARCHAR2(64),
    GRADENO VARCHAR2(20),
    SCHOOLNO VARCHAR2(20),
    CLASSNO VARCHAR2(20),
    MARKAREA VARCHAR2(10) DEFAULT 0,
    MARKAREANUM NUMBER(4) DEFAULT 0,
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    SUBJECTNO VARCHAR2(10) DEFAULT 'noselected'
);
COMMENT ON COLUMN R_MARKAREA.MARKAREA IS '成绩排序号';
COMMENT ON COLUMN R_MARKAREA.MARKAREANUM IS '成绩在该区间的学生分布数量';
COMMENT ON COLUMN R_MARKAREA.SUBJECTNO IS 'noselected-总分；其它存学科编码';
CREATE UNIQUE INDEX "R_MARKAERA_ID_pk" ON R_MARKAREA (ID);
CREATE UNIQUE INDEX R_MARKAERA_UNIQUE_INDEX ON R_MARKAREA (SCHOOLNO, GRADENO, CLASSNO, MARKAREA, CREATOR);
CREATE TABLE R_WRONGQUESTION
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    TPNO VARCHAR2(60) DEFAULT NULL,
    QUESTIONNO VARCHAR2(20),
    WRONGNUMS NUMBER(10),
    TESTNUMS NUMBER(10),
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate
,
    TESTPOINT VARCHAR2(100)
);
COMMENT ON COLUMN R_WRONGQUESTION.TPNO IS '试卷编码';
COMMENT ON COLUMN R_WRONGQUESTION.QUESTIONNO IS '题号';
COMMENT ON COLUMN R_WRONGQUESTION.WRONGNUMS IS '错误人数';
COMMENT ON COLUMN R_WRONGQUESTION.TESTNUMS IS '考试人数';
COMMENT ON COLUMN R_WRONGQUESTION.TESTPOINT IS '考点';
CREATE TABLE R_YEARMARK
(
    ID VARCHAR2(64) PRIMARY KEY NOT NULL,
    YEAR NUMBER(*) DEFAULT NULL,
    CLASSNO VARCHAR2(20),
    TEACHERNAME VARCHAR2(20),
    MARKONE NUMBER(6,2),
    MARKTWO NUMBER(6,2),
    MARKTHREE NUMBER(6,2),
    MARKFOUR NUMBER(6,2),
    MARKFIVE NUMBER(6,2),
    MARKSIX NUMBER(6,2),
    MARKSEVEN NUMBER(6,2),
    MARKEIGHT NUMBER(6,2),
    MARKNINE NUMBER(6,2),
    MARKTEN NUMBER(6,2),
    MARKELEVEN NUMBER(6,2),
    MARKTWEVLE NUMBER(6,2),
    MARKMIDTERM NUMBER(6,2),
    MARKFINAL NUMBER(6,2),
    AVEMARK NUMBER(6,2),
    AVEPOSITON NUMBER(4,2),
    CREATOR VARCHAR2(20),
    CREATEDATE DATE DEFAULT sysdate

);
COMMENT ON COLUMN R_YEARMARK.MARKONE IS '1月平均分';
COMMENT ON COLUMN R_YEARMARK.MARKTWO IS '2月平均分';
COMMENT ON COLUMN R_YEARMARK.MARKMIDTERM IS '期中成绩';
COMMENT ON COLUMN R_YEARMARK.AVEMARK IS '年度平均分';
COMMENT ON COLUMN R_YEARMARK.AVEPOSITON IS '平均排名名次';
CREATE TABLE TB_MENU
(
    MENU_ID VARCHAR2(200) PRIMARY KEY NOT NULL,
    PARENT_MENU_ID VARCHAR2(200),
    MENU_NO VARCHAR2(200),
    MENU_TITLE VARCHAR2(200),
    MENU_DESCRIPTION VARCHAR2(1000),
    OPEN_FLAG VARCHAR2(200),
    MENU_URL VARCHAR2(500),
    SORT_NO VARCHAR2(200),
    DELETE_FLAG VARCHAR2(200)
);
CREATE TABLE TB_ROLE
(
    ROLE_ID VARCHAR2(200) PRIMARY KEY NOT NULL,
    ROLE_NAME VARCHAR2(200),
    DESCRIPTION VARCHAR2(2000),
    OPEN_FLAG VARCHAR2(200),
    ENTERPRISE_ID VARCHAR2(200)
);
CREATE TABLE TB_ROLE_MENU
(
    ROLE_ID VARCHAR2(200) NOT NULL,
    MENU_ID VARCHAR2(200) NOT NULL,
    CONSTRAINT TB_ROLE_MENU_PK PRIMARY KEY (ROLE_ID, MENU_ID)
);
CREATE TABLE TB_USER
(
    USER_ID VARCHAR2(200) PRIMARY KEY NOT NULL,
    USER_NAME VARCHAR2(200),
    USER_PASSWORD VARCHAR2(200),
    USER_STATE VARCHAR2(200),
    LOCKED_STATE VARCHAR2(200),
    EMAIL VARCHAR2(200),
    PHONE VARCHAR2(200),
    CREATE_TIME TIMESTAMP(6),
    USER_REAL_NAME VARCHAR2(200),
    USER_DESCRIPTION VARCHAR2(2000),
    USER_GRADE VARCHAR2(200),
    USER_ROLE_IDS VARCHAR2(200),
    USER_ROLE_NAMES VARCHAR2(200),
    USER_SCHOOL VARCHAR2(200)
);
CREATE INDEX TB_USER_USER_NAME_INDEX ON TB_USER (USER_NAME);
CREATE TABLE TB_USER_ROLE
(
    USER_ID VARCHAR2(200) NOT NULL,
    ROLE_ID VARCHAR2(200) NOT NULL,
    CONSTRAINT TB_USER_ROLE_PK PRIMARY KEY (USER_ID, ROLE_ID)
);