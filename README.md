<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.

<br>

------
##기능 목록
###Step 1. 인수 테스트 기반 리팩터링
- [X] LineService 리팩터링
- [X] LineSectionAcceptanceTest 리팩터링

###Step 2. 경로 조회 기능
- [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 정상 시나리오 : 조회 성공
  - [X] 예외 시나리오
    - [X] 출발역과 도착역이 같은 경우 조회 불가
    - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우 조회 불가
    - [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우 조회 불가
- [X] 최단 경로 조회 기능 구현하기
  - [X] jgrapht 라이브러리 사용하여 최단 경로 조회
  - [X] 최단 경로 지하철역 리스트 조회
  - [X] 최단 경로 거리 조회