DROP TABLE IF EXISTS ROUTE_INFO;

--라우트 정보
CREATE TABLE ROUTE_INFO (
    ROUTE_ID VARCHAR(100) NOT NULL PRIMARY KEY, --관리자 번호
    PATH VARCHAR(100) NOT NULL, --관리자 아이디
    URI VARCHAR(100) NOT NULL --관리자 이름
);

INSERT INTO ROUTE_INFO (
    ROUTE_ID,
    PATH,
    URI
) VALUES (
    'server-template',
    '/template/**',
    'http://localhost:8080/template'
);