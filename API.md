# Walkary API
백엔드 도메인 walkary.kro.kr

# 회원 API

## 회원가입 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| POST  | /apis/signup-----------------------: |   X   |

### Request

```json
{
    "userId": "아이디",
    "password": "패스워드",
    "username": "닉네임",
    "email":"이메일",
    "phoneNumber":"전화번호"
}
```

> params

### Response

- 회원가입 성공 [200 OK]

```json
{
    "message": "회원가입이 완료되었습니다."
}
```

- 인증 실패 (중복 회원) [400 Bad Request]

```json
{
    "message": "이미 존재하는 아이디입니다."
}
```

유효성 검사
아이디·비밀번호·메일주소·얀락처 누락 시
```json
{
    "message": "XXX가 누락되었습니다"
}
```
userId
"아이디는 4자 이상 20자 이하로 입력해주세요."
"아이디는 영문과 숫자만 입력 가능합니다."

password
"비밀번호는 8글자에서 16글자 사이여야 합니다."
"비밀번호는 최소 한 개의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다."

username
"이름은 2글자에서 10글자 사이여야 합니다."

email
"올바른 이메일 형식이 아닙니다."
"이메일 주소는 '@' 다음에 '.'이 와야 합니다."

phoneNumber
"전화번호는 숫자만 포함해야 합니다."

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />

## 로그인 API

| TYPE |                  URL                   | TOKEN |
| :--: | :------------------------------------: | :---: |
| PUT  | /api/login --------------------------: |   X   |

### Request

```json
{
    "userId": "아이디",
    "password": "비밀번호"
}
```

### Response

- 로그인 성공(토큰 발급) [200 OK]

```json
{
    "token": "Bearer XXXXX"
}
```

- 로그인 실패(잘못된 id 혹은 패스워드) [400 Bad Request]

```json
{
    "message": "잘못된 id 혹은 password 입니다."
}
```

- 로그인 실패 [500 Internal Server Error]
서버 내부 오류

# 일기 API

## 일기 작성

| TYPE |    URL     | TOKEN |
| :--: | :--------: | :---: |
| POST | /apis/diary |   O   |

### Request

```json 
{
  "title":"타이틀명",
  "content":"콘텐츠",
  "image":"data:image/png;base64,[바이너리 데이터]"
}
```

> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함


### Response

- 일기 작성 성공 [200 OK]

```json
{
    "message": "일기가 작성되었습니다."
}
```

- 일기 작성 실패(토큰 만료) [향후 수정 필요]

```json
{
}
```
403 Forbidden

- 일기 작성 실패 [500 Internal Server Error]
서버 내부 오류

## 오늘의 일기 조회하기
| TYPE |       URL       | TOKEN |
| :--: | :-------------: | :---: |
| GET | /apis/main/diary?date=yyyyMMdd |   O   |

### Request

```
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 예시 : /apis/main/diary?date=20231127
> 해당하는 날짜 필요
```

### Response

- 조회 성공 [200 OK]
``` json
{
    "id": "diary 아이디",
    "date": null,
    "title": "타이틀명",
    "content": "콘텐츠",
    "image": "data:image/png;base64,[바이너리 데이터]"
}
```

- 조회 실패(일기 부재 시) [200 OK]
``` json
{
    "message": "일기를 작성해주세요"
}
```

- 조회 실패(잘못된 토큰 혹은 토큰 만료) [수정 필요]
403 Forbidden

- 조회 실패 [500 Internal Server Error]
서버 내부 오류


## 일기 모아보기
| TYPE |         URL        | TOKEN |
| :--: | :----------------: | :---: |
| GET | /apis/collect/diary |   O   |

### Request

```
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 예시 : /apis/collect/diary?limit=5&offset=0&sortBy=latest
> limit 조회 사이즈
> offset 시작 페이지
> sortBy=latest 최신순, 없으면 작성 순서대로 (기준값은 diaryId)
```

### Response
- 조회 성공 [200 OK]
``` json
[
    {
        "id": 471,
        "title": "11.27 new Test222",
        "content": "테스트222",
        "image": "data:image/png;base64,[바이너리 데이터]",
        "date": "2023-11-27"
    },
    {
        "id": 442,
        "title": "11.06 new Test222",
        "content": "테스트222",
        "image": "data:image/png;base64,[바이너리 데이터]",
        "date": "2023-11-06"
    },
    {
        "id": 440,
        "title": "11.06 new Test",
        "content": "테스트",
        "image": "data:image/png;base64,[바이너리 데이터]"
        "date": "2023-11-06"
    }
]
```
- 조회 실패 [500 Internal Server Error]
서버 내부 오류

## 일기 수정
| TYPE |         URL            | TOKEN |
| :--: | :--------------------: | :---: |
| PATCH | /apis/diary/{diaryId} |   O   |

### Request

> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 예시 : /apis/diary/471
> diaryId에 수정해야 할 해당 diaryId를 넣어야 함.
``` json
{    
    "title": "수정된 타이틀",
    "content": "수정된 콘텐츠",
    "image": "이미지 파일"
}
```

### Response
- 수정 성공 [200 OK]
``` json
{
    "message": "수정되었습니다."
}  
```

- 수정 실패(잘못된 토큰)
HTTP Status 500 – Internal Server Error [페이지]


## 일기 삭제 (수정 필요) - 삭제 전 일기를 쓴 아이디가 맞는지 확인하는 작업이 필요할 듯
| TYPE |            URL            | TOKEN |
| :--: | :-----------------------: | :---: |
| GET | /apis/main/diary/{diaryId} |   O   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 삭제할 diaryId를 URL에 기입해야 함

### Response
- 삭제 성공 [200 OK]
``` json
{
    "message": "일기가 삭제되었습니다."
}
```

- 삭제 실패 (해당하는 일기가 존재하지 않을 때) [400 Bad Request]
``` json
{
    "message": "일기 삭제하기에 실패하였습니다."

}
```

- 삭제 실패 [500 Internal Server Error]
  서버 내부 오류

# 핀 API

## 핀 생성 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| POST  | /apis/pin-----------------------: |   X   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
>   
```json
{
    "contents": "장소",
    "latitude": 123.1233,
    "longitude": 23.22232
}

```
장소, 위도, 경도

### Response

- 핀 생성 성공 [200 OK]

```json
{
    "message": "핀이 생성되었습니다."
}
```

- 실패 
403 Forbidden : 토큰 만료 혹은 부재

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />

## 핀 메인화면 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| POST  | /apis/main/maps-pin?date=yyyyMMdd&sortBy=LATEST-----------------------: |   X   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 예시 : /apis/main/maps-pin?date=20231023&sortBy=LATEST
> date 부재 시 default값은 오늘.
> LATEST 핀 id순 OLDEST는 핀id 역순 default값은 역순


### Response

- 핀 메인화면 성공 [200 OK]

```json
{
    "pins": [
        {
            "id": 핀id,
            "contents": "콘텐츠",
            "latitude": 위도,
            "longitude": 경도,
            "stampTime": "핀 작성 시간"
        }
    ]
}
```

- 핀 메인화면 성공[200 OK]
값 없을 때
```json
{
    "pins": {}
}
```

- 실패 
403 Forbidden : 토큰 만료 혹은 부재

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />

## 핀 수정 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| PATCH  | /apis/pin-----------------------: |   X   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
>   
```json
{
    "id": 619,
    "contents": "수정된 컨텐츠",
    "latitude": 123.1233,
    "longitude": 23.22232
}

```
핀ID, 수정된 컨텐츠, 위도, 경도

### Response

- 핀 생성 성공 [200 OK]

```json
{
    "message": "핀이 수정되었습니다."
}
```

- 실패 잘못된 핀ID 입력
```json
{
    "message": "존재하지 않는 핀입니다"
}
```

- 실패 403 Forbidden
- 토큰 만료 혹은 부재

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />

## 핀 삭제 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| DELETE  | /apis/pin/{pinId}-----------------------: |   X   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 삭제할 pinId를 URL에 기입해야 함

### Response

- 핀 삭제 성공 [200 OK]

```json
{
    "message": "핀이 삭제되었습니다."
}
```

- 실패 잘못된 핀ID 입력
```json
{
    "message": "핀 삭제하기 실패"
}
```

- 실패 사용자가 핀 생성한 사람이 아닐 때
```json
{
    "message": "핀 작성자와 현 사용자의 아이디가 동일하지 않습니다"
}
```

- 실패 403 Forbidden
- 토큰 만료 혹은 부재

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />

## 핀 모아보기 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| GET  | /apis/pin-----------------------: |   X   |

### Request
> 필수 요건 : Header의 Authorization에 Bearer XXX 토큰 넣어야 함
> 예시 : /apis/pin?sortBy=LATEST
> 날짜 최신순은 LATEST, 오래된 순은 OLDEST로 지정
> 디폴트 값은 OLDEST

### Response

- 핀 모아보기 성공 [200 OK]

```json
{
    "pins": {
        "2023-12-18": [
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            },
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            }
        ],
        "2023-12-12": [
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            },
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            },
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            },
            {
                "id": 핀ID,
                "contents": "콘텐츠",
                "latitude": 위도,
                "longitude": 경도,
                "date": "날짜",
                "stampTime": "시간"
            }
        ],
```

- 핀 모아보기 조회 성공[200OK]
값 없을 때
```json
{
    "pins": {}
}
```

- 실패 403 Forbidden
- 토큰 만료 혹은 부재

- 실패 [500 Internal Sever Error]
  서버 내부 오류

<br />
