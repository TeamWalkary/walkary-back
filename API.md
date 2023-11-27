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

- 회원가입 성공

```json
{
    "message": "회원가입이 완료되었습니다."
}
```

- 인증 실패 (중복 회원)

```json
{
    "message": "이미 존재하는 아이디입니다."
}
```

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

- 로그인 성공(토큰 발급)

```json
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6enoiLCJhdXRoIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzAxMDg5NzAyLCJleHAiOjE3MDEwOTMzMDJ9.3wmiNnetrjyCbnUuJIBxuFxU2C1LwuZjvV5dLNXXhK0"
}
```

- 로그인 실패(잘못된 id 혹은 패스워드)

```json
{
    "message": "잘못된 id 혹은 password 입니다."
}
```

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

- 일기 작성 성공

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

## 오늘의 일기 조회하기
| TYPE |       URL       | TOKEN |
| :--: | :-------------: | :---: |
| GET | /apis/main/diary |   O   |

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

- 조회 성공
{
    "id": diary 아이디,
    "date": null,
    "title": "타이틀명",
    "content": "콘텐츠",
    "image": "data:image/png;base64,[바이너리 데이터]"
}


- 조회 실패(잘못된 토큰) [수정 필요]
403 Forbidden

- 조회 실패(토큰 만료)


## 일기 모아보기
| TYPE |         URL        | TOKEN |
| :--: | :----------------: | :---: |
| GET | /apis/collect/diary |   O   |

## 일기 수정
| TYPE |         URL            | TOKEN |
| :--: | :--------------------: | :---: |
| PATCH | /apis/diary/{diaryId} |   O   |


## 일기 삭제
| TYPE |            URL            | TOKEN |
| :--: | :-----------------------: | :---: |
| GET | /apis/main/diary/{diaryId} |   O   |
