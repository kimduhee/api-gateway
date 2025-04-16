
# 개발환경
+ JDK 17(17.0.14)
+ IDE IntelliJ
+ memory 8G >.<
  <br/><br/>
- - -
# Spring Cloud Gateway(SCG) 주요 3요소
### Route
+ Route는 id, 목적지URI, Predicate,filters가 있으며 
Route를 통한 요청된 URI조건이 predicate를 통과하여 
규칙에 만족하는 경우 매핑된 해당 경로로 매칭된다
### Predicate
+ Predicate는 요청이 주어진 조건을 만족하는지 확인하는 구성요소이며,
하나 이상의 조전자를 정할 수 있다.
Predicate에 매칭되지 않을 경우 HTTP 404로 응답한다.
### Filter
+ 요청이나 응답의 전처리 및 후처리를 하며, 
Proxy Filter는 프록시 요청에 처리될 때 수행되는 필터이다.
- - -
# 설정방법
### java 설정(해당방법으로 진행)
- - -
# 기능정의
### Route 설정 DB 관리(진행중)
### 서비스(api) 제한 시간 체크(예정)
### JWT 유효성 체크
### 대응답 모드(하드코딩응답)(예정)
### 컨텍스트별/api별 통계(예정)
### 무중단 route 반영(예정_고민....)


