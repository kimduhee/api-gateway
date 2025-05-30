
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
### 대응답 모드(예정)
### 응답값의 field conversion
jackson lib 의 PropertyNamingStrategies.class를 customizing
+ 처리방법론 고민
><b>BeanUtils.copyproperties(Reflection) 커스터마이징을 통한 방법이 있지 않을까?</b>
>>root에 대한 접근이며 하위 필드접근에 대한 어려움으로 제외

><b>이미 기능을 제공하고 있는 @JsonNaming을 커스터마이징 해서 처리</b>
>>모든 Dto에 @JsonNaming(custom class)을 포함시켜야 하는 번거로움 및 위험성

><b>(확정)신규 필드명 변경로직을 PropertyNamingStrategies에 추가후 ObjectMapper의 
setPropertyNamingStrategy를 활용하여 필드명 변경</b>

<br>

+ 관리자 페이지에서 field mapping rule 관련 셋팅
<pre><code>DB구조                        필드명     변경필드명
com.framework.dto.Category    cateId    카테고리ID
com.framework.dto.Category    cateNm    카테고리명
com.framework.dto.Goods       goodsId   상품ID
com.framework.dto.Goods       goodsNm   상품명
......
</code></pre>
<br>

+ 처리과정
>- Dto는 공유 lib로 처리
>- json lib의 PropertyNamingStrategies class를 동일한 패키지생성 및 복사하여 DB 또는 redis 조회하여 필드명 변경하는 로직 구현 
>- 서비스 서버에서 header값에 response dto(패키지 + 클래스명) 정보를 전달<br>
>- api gateway 서버에서 modifyResponseBody로 response 데이터 및 header값 추출<br>
>- ObjectMapper의 setPropertyNamingStrategy(new PropertyNamingStrategies.ConvertCaseStrategy()); 설정
>- 서비스 서버로 부터 전달받은 dto 클래스로 objectMapper.readValue를 사용하여 데이터 Deserialize 처리
>- objectMapper.writeValueAsString를 사용하여 Serialize하여 response 응답


### 컨텍스트별/api별 통계(예정)
### 무중단 route 반영(예정_고민....)


