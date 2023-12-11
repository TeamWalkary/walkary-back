# Walkary API

# 회원 API

## 회원가입 API

| TYPE |                  URL                  | TOKEN |
| :--: | :-----------------------------------: | :---: |
| POST  | /apis/signup-----------------------: |   X   |

> DB 저장 x

### Request

```json
{
    "userId": "아이디",
    "password": "비번",
    "username": "유저이름"
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
