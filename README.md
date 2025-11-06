# 덕후감

[![codecov](https://codecov.io/gh/sb5-part3-team6/sb5-deokhugam-team6/branch/dev/graph/badge.svg?token=77AGWST30O)](https://codecov.io/gh/sb5-part3-team6/sb5-deokhugam-team6)

## 팀 문서

- [팀 노션](https://www.notion.so/_6-_5-2797bb3b3a0f810a8680ed5279a3de3e?source=copy_link)

## 팀원 구성
- 김동규 : [redmatoda](https://github.com/redmatoda)
- 박문아 : [ayanemoona](https://github.com/ayanemoona)
- 박주환 : [parkjoohwan](https://github.com/parkjoohwan)
- 이소연 : [isylsy166](https://github.com/isylsy166)
- 이유호 : [yuhoyuho](https://github.com/yuhoyuho)

## 프로젝트 소개
```aiignore
덕후감은 책을 사랑하는 모든 독자를 위한 독서 커뮤니티 서비스입니다. 
Naver API 및 OCR 기술을 통해 이미지 속 ISBN을 인식해 책 정보를 등록합니다. 
Batch 처리로 인기 도서·리뷰·사용자 순위를 계산하고, 독자들은  리뷰와 감상을 자유롭게 공유할 수 있습니다.
```

프로젝트 기간: 2025.10.20 ~ 2025.11.07

## 기술 스택

### 프론트
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white" alt="">
<img src="https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white" alt="">

---
### 백엔드
<img src="https://img.shields.io/badge/Java 17-007396?style=for-the-badge&logo=openjdk&logoColor=white" alt="">
<img src="https://img.shields.io/badge/SpringBoot 3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="">
<img src="https://img.shields.io/badge/PostgreSQL 17.6-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="">

---
### 협업 도구
<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=github&logoColor=white" alt="">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white" alt="">
<img src="https://img.shields.io/badge/DISCORD-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="">

### 아키텍처

<img width="1000" height="550" alt="Image" src="https://github.com/user-attachments/assets/994e5a2b-6183-45de-b0cd-3ca388d897a9" />

## 구현 기능 상세

### 도서

- 도서를 등록, 수정, 삭제 할 수 있습니다. 
- 도서의 정보는 isbn으로 불러올 수 있고, 이미지에서 isbn을 추출할 수 있습니다.
### 유저

- 회원가입, 로그인, 회원탈퇴가 가능합니다.
### 리뷰

- 도서에 대한 리뷰를 등록, 수정, 삭제 할 수 있습니다.  
- 리뷰에 대한 좋아요를 할 수 있습니다.
### 댓글

- 리뷰에 대한 댓글를 등록, 수정, 삭제 할 수 있습니다.
### 알림

- 유저의 작성 리뷰에 대한 알림을 받을 수 있습니다. (댓글, 좋아요, 랭킹)
### 대시보드

- 인기 도서, 인기 리뷰, 인기 유저를 모아볼 수 있습니다. (일간, 주간, 월간, 역대)
## docker

- 아래 docker container 환경에선 isbn 이미지로 가져오기(OCR), 정보 불러오기(NAVER API) 기능이 동작하지 않습니다.
- 이미지는 /app/uploads 경로에 저장됩니다.
- 실행 전 local postgres에 schema.sql를 실행하거나, docker-compose.yml을 아래처럼 수정해주세요.

### 실행을 위한 .env
```aiignore
SPRING_PROFILES_ACTIVE=dev
# =========== DB ==========
POSTGRES_PORT=5432
POSTGRES_DB=deokhugam
POSTGRES_HOST=localhost
POSTGRES_USER=deokhugam_user
POSTGRES_PASSWORD=user1234

# ========== Spring DATASOURCE ==========
SPRING_DATASOURCE_URL=jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
# ========= NAVER API=======
NAVER_CLIENT_ID=
NAVER_CLIENT_SECRET=
# ========= OCR API=======
NCP_INVOKE_URL=
NCP_CLIENT_SECRET=
# ========== 파일 업로드 ==========
FILE_UPLOAD_DIR=/app/uploads
AWS_S3_REGION=ap-northeast-2
AWS_S3_BUCKET=
AWS_S3_ACCESS_KEY=
AWS_S3_SECRET_KEY=
CDN_URL=
# ========== 타임존 ==========
TZ=Asia/Seoul
```

## if you want 
docker 실행을 원하시는 경우, 아래 파일을 교체해주세요.
```aiignore
# docker-compose.yml
version: "3.9"

services:
  app:
    image: deokhugam:local
    build:
      context: .
      dockerfile: Dockerfile
    container_name: deokhugam-app
    env_file:
      - .env
    environment:
      TZ: ${TZ}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL}
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      AWS_S3_ACCESS_KEY: ${AWS_S3_ACCESS_KEY}
      AWS_S3_SECRET_KEY: ${AWS_S3_SECRET_KEY}
      AWS_S3_REGION: ${AWS_S3_REGION}
      AWS_S3_BUCKET: ${AWS_S3_BUCKET}
      CDN_URL: ${CDN_URL}
      NAVER_CLIENT_ID: ${NAVER_CLIENT_ID}
      NAVER_CLIENT_SECRET: ${NAVER_CLIENT_SECRET}
      NCP_INVOKE_URL: ${NCP_INVOKE_URL}
      NCP_CLIENT_SECRET: ${NCP_CLIENT_SECRET}
      FILE_UPLOAD_DIR: ${FILE_UPLOAD_DIR}
    ports:
      - "80:80"
    depends_on:
      - db
    volumes:
      - binary-content-storage:${FILE_UPLOAD_DIR}
    networks:
      - deokhugam-network

  db:
    image: postgres:17-alpine
    container_name: deokhugam-db
    environment:
      - POSTGRES_DB=${POSTGRES_DB}
      - POSTGRES_USER=${POSTGRES_USER}
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./src/main/resources/schema.sql:/docker-entrypoint-initdb.d/schema.sql
    networks:
      - deokhugam-network
      
volumes:
  postgres-data:
  binary-content-storage:

networks:
  deokhugam-network:
    driver: bridge 
```

## docker 실행

```aiignore
docker-compose up
```

## 구현 홈페이지
- [덕후감](http://54.180.228.190/) (2025-11-15 까지 유지)

## 디렉토리 구조
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── codeit
│   │           └── project
│   │               └── deokhugam
│   │                   ├── DeokhugamApplication.java
│   │                   ├── batch
│   │                   │   ├── job
│   │                   │   │   └── RankBatchConfig.java
│   │                   │   ├── processor
│   │                   │   │   └── RankProcessor.java
│   │                   │   ├── reader
│   │                   │   │   └── RankReader.java
│   │                   │   ├── scheduler
│   │                   │   │   ├── CleanBatchScheduler.java
│   │                   │   │   └── RankBatchScheduler.java
│   │                   │   └── writer
│   │                   │       └── RankWriter.java
│   │                   ├── domain
│   │                   │   ├── book
│   │                   │   │   ├── controller
│   │                   │   │   │   ├── BookApi.java
│   │                   │   │   │   └── BookController.java
│   │                   │   │   ├── dto
│   │                   │   │   │   ├── BookStatDto.java
│   │                   │   │   │   ├── request
│   │                   │   │   │   │   ├── BookCreateRequest.java
│   │                   │   │   │   │   ├── BookPopularRequest.java
│   │                   │   │   │   │   ├── BookSearchRequest.java
│   │                   │   │   │   │   └── BookUpdateRequest.java
│   │                   │   │   │   └── response
│   │                   │   │   │       ├── BookDto.java
│   │                   │   │   │       ├── BookResponse.java
│   │                   │   │   │       ├── CursorPageResponseBookDto.java
│   │                   │   │   │       └── PopularBookDto.java
│   │                   │   │   ├── entity
│   │                   │   │   │   └── Book.java
│   │                   │   │   ├── exception
│   │                   │   │   │   ├── BoockExceptionHandler.java
│   │                   │   │   │   ├── BookErrorCode.java
│   │                   │   │   │   ├── BookException.java
│   │                   │   │   │   └── detail
│   │                   │   │   │       └── BookNotFoundException.java
│   │                   │   │   ├── mapper
│   │                   │   │   │   └── BookMapper.java
│   │                   │   │   ├── repository
│   │                   │   │   │   ├── BookRepository.java
│   │                   │   │   │   ├── BookRepositoryCustom.java
│   │                   │   │   │   └── BookRepositoryCustomImpl.java
│   │                   │   │   └── service
│   │                   │   │       ├── BookService.java
│   │                   │   │       └── BookServiceImpl.java
│   │                   │   ├── comment
│   │                   │   │   ├── controller
│   │                   │   │   │   ├── CommentAPI.java
│   │                   │   │   │   └── CommentController.java
│   │                   │   │   ├── dto
│   │                   │   │   │   ├── event
│   │                   │   │   │   │   ├── CommentDeleteEvent.java
│   │                   │   │   │   │   ├── CommentEvent.java
│   │                   │   │   │   │   └── CommentUpdateEvent.java
│   │                   │   │   │   ├── request
│   │                   │   │   │   │   ├── CommentCreateRequest.java
│   │                   │   │   │   │   └── CommentUpdateRequest.java
│   │                   │   │   │   └── response
│   │                   │   │   │       └── CommentDto.java
│   │                   │   │   ├── entity
│   │                   │   │   │   └── Comment.java
│   │                   │   │   ├── exception
│   │                   │   │   ├── mapper
│   │                   │   │   │   └── CommentMapper.java
│   │                   │   │   ├── repository
│   │                   │   │   │   ├── CommentRepository.java
│   │                   │   │   │   ├── CommentRepositoryCustom.java
│   │                   │   │   │   └── CommentRepositoryCustomImpl.java
│   │                   │   │   └── service
│   │                   │   │       ├── CommentService.java
│   │                   │   │       └── CommentServiceImpl.java
│   │                   │   ├── notification
│   │                   │   │   ├── controller
│   │                   │   │   │   ├── NotificationApi.java
│   │                   │   │   │   └── NotificationController.java
│   │                   │   │   ├── dto
│   │                   │   │   │   ├── command
│   │                   │   │   │   │   ├── NotificationCreateCommand.java
│   │                   │   │   │   │   ├── NotificationDeleteCommand.java
│   │                   │   │   │   │   └── NotificationUpdateCommand.java
│   │                   │   │   │   ├── request
│   │                   │   │   │   │   └── NotificationUpdateRequest.java
│   │                   │   │   │   └── response
│   │                   │   │   │       └── NotificationDto.java
│   │                   │   │   ├── entity
│   │                   │   │   │   ├── Notification.java
│   │                   │   │   │   └── NotificationType.java
│   │                   │   │   ├── event
│   │                   │   │   │   └── NotificationEventHandler.java
│   │                   │   │   ├── exception
│   │                   │   │   │   ├── NotificationErrorCode.java
│   │                   │   │   │   ├── NotificationException.java
│   │                   │   │   │   ├── NotificationExceptionHandler.java
│   │                   │   │   │   └── detail
│   │                   │   │   │       ├── NotificationInvalidUserException.java
│   │                   │   │   │       └── NotificationNotFoundException.java
│   │                   │   │   ├── mapper
│   │                   │   │   │   └── NotificationMapper.java
│   │                   │   │   ├── repository
│   │                   │   │   │   ├── NotificationRepository.java
│   │                   │   │   │   ├── NotificationRepositoryCustom.java
│   │                   │   │   │   └── NotificationRepositoryCustomImpl.java
│   │                   │   │   └── service
│   │                   │   │       ├── NotificationBatchService.java
│   │                   │   │       ├── NotificationBatchServiceImpl.java
│   │                   │   │       ├── NotificationService.java
│   │                   │   │       └── NotificationServiceImpl.java
│   │                   │   ├── rank
│   │                   │   │   ├── entity
│   │                   │   │   │   ├── Rank.java
│   │                   │   │   │   ├── RankTarget.java
│   │                   │   │   │   └── RankType.java
│   │                   │   │   └── repository
│   │                   │   │       ├── RankRepository.java
│   │                   │   │       ├── RankRepositoryCustom.java
│   │                   │   │       └── RankRepositoryCustomImpl.java
│   │                   │   ├── review
│   │                   │   │   ├── controller
│   │                   │   │   │   ├── ReviewApi.java
│   │                   │   │   │   └── ReviewController.java
│   │                   │   │   ├── dto
│   │                   │   │   │   ├── ReviewStatDto.java
│   │                   │   │   │   ├── event
│   │                   │   │   │   │   ├── ReviewLikedDeleteEvent.java
│   │                   │   │   │   │   ├── ReviewLikedEvent.java
│   │                   │   │   │   │   ├── ReviewRankedDeleteEvent.java
│   │                   │   │   │   │   └── ReviewRankedEvent.java
│   │                   │   │   │   ├── request
│   │                   │   │   │   │   ├── ReviewCreateRequest.java
│   │                   │   │   │   │   ├── ReviewPopularQueryParams.java
│   │                   │   │   │   │   ├── ReviewQueryParams.java
│   │                   │   │   │   │   └── ReviewUpdateRequest.java
│   │                   │   │   │   └── response
│   │                   │   │   │       ├── PopularReviewDto.java
│   │                   │   │   │       ├── ReviewDto.java
│   │                   │   │   │       └── ReviewLikeDto.java
│   │                   │   │   ├── entity
│   │                   │   │   │   ├── Review.java
│   │                   │   │   │   └── ReviewLike.java
│   │                   │   │   ├── exception
│   │                   │   │   │   ├── ReviewErrorCode.java
│   │                   │   │   │   ├── ReviewException.java
│   │                   │   │   │   ├── ReviewExceptionHandler.java
│   │                   │   │   │   └── detail
│   │                   │   │   │       ├── ReviewAlreadyExistsException.java
│   │                   │   │   │       └── ReviewNotFoundException.java
│   │                   │   │   ├── mapper
│   │                   │   │   │   └── ReviewMapper.java
│   │                   │   │   ├── repository
│   │                   │   │   │   ├── ReviewLikeRepository.java
│   │                   │   │   │   ├── ReviewRepository.java
│   │                   │   │   │   ├── ReviewRepositoryCustom.java
│   │                   │   │   │   └── ReviewRepositoryCustomImpl.java
│   │                   │   │   └── service
│   │                   │   │       ├── ReviewService.java
│   │                   │   │       └── ReviewServiceImpl.java
│   │                   │   └── user
│   │                   │       ├── controller
│   │                   │       │   ├── UserApi.java
│   │                   │       │   └── UserController.java
│   │                   │       ├── dto
│   │                   │       │   ├── UserStatDto.java
│   │                   │       │   ├── request
│   │                   │       │   │   ├── PowerUserQueryParams.java
│   │                   │       │   │   ├── UserLoginRequest.java
│   │                   │       │   │   ├── UserRegisterRequest.java
│   │                   │       │   │   └── UserUpdateRequest.java
│   │                   │       │   └── response
│   │                   │       │       ├── PowerUserDto.java
│   │                   │       │       └── UserDto.java
│   │                   │       ├── entity
│   │                   │       │   └── User.java
│   │                   │       ├── exception
│   │                   │       │   ├── UserErrorCode.java
│   │                   │       │   ├── UserException.java
│   │                   │       │   ├── UserExceptionHandler.java
│   │                   │       │   └── detail
│   │                   │       │       ├── DeleteNotAllowedException.java
│   │                   │       │       ├── EmailDuplicationException.java
│   │                   │       │       ├── LoginInputInvalidException.java
│   │                   │       │       ├── NicknameDuplicationException.java
│   │                   │       │       ├── UserAlreadyDeletedException.java
│   │                   │       │       └── UserNotFoundException.java
│   │                   │       ├── repository
│   │                   │       │   ├── UserRepository.java
│   │                   │       │   ├── UserRepositoryCustom.java
│   │                   │       │   └── UserRepositoryCustomImpl.java
│   │                   │       ├── service
│   │                   │       │   ├── UserBatchService.java
│   │                   │       │   ├── UserBatchServiceImpl.java
│   │                   │       │   ├── UserService.java
│   │                   │       │   └── UserServiceImpl.java
│   │                   │       └── util
│   │                   │           ├── LoginUser.java
│   │                   │           ├── LoginUserArgumentResolver.java
│   │                   │           └── PasswordUtil.java
│   │                   ├── external
│   │                   │   └── client
│   │                   │       ├── NaverBookApiClient.java
│   │                   │       ├── NaverCloudOcrApiClient.java
│   │                   │       └── dto
│   │                   │           ├── ClovaOcrRequest.java
│   │                   │           ├── ClovaOcrResponse.java
│   │                   │           ├── Item.java
│   │                   │           └── NaverBookRss.java
│   │                   └── global
│   │                       ├── common
│   │                       │   ├── dto
│   │                       │   │   ├── ErrorResponse.java
│   │                       │   │   └── PageResponse.java
│   │                       │   └── entity
│   │                       │       ├── BaseDeletableEntity.java
│   │                       │       └── BaseEntity.java
│   │                       ├── config
│   │                       │   ├── FileConfig.java
│   │                       │   ├── QuerydslConfig.java
│   │                       │   ├── S3Config.java
│   │                       │   ├── WebClientConfig.java
│   │                       │   ├── WebMvcConfig.java
│   │                       │   └── impl
│   │                       │       ├── FileConfigDev.java
│   │                       │       └── FileConfigProd.java
│   │                       ├── exception
│   │                       │   ├── DeokhugamException.java
│   │                       │   ├── ErrorCode.java
│   │                       │   ├── GlobalErrorCode.java
│   │                       │   └── GlobalExceptionHandler.java
│   │                       ├── log
│   │                       │   └── MDCLoggingInterceptor.java
│   │                       └── storage
│   │                           ├── FileStorage.java
│   │                           └── impl
│   │                               ├── FileStorageDev.java
│   │                               └── FileStorageS3.java
│   └── resources
│       ├── application-dev.yaml
│       ├── application-prod.yaml
│       ├── application.yaml
│       ├── logback-spring.xml
│       ├── schema.sql
│       ├── static
│       │   ├── assets
│       │   │   ├── index-B4R5l8Tc.js
│       │   │   └── index-B5v_jO85.css
│       │   ├── favicon.ico
│       │   ├── images
│       │   │   ├── app
│       │   │   │   └── favicon.ico
│       │   │   ├── books
│       │   │   │   └── imgError.png
│       │   │   ├── common
│       │   │   │   ├── buttonLoader.gif
│       │   │   │   ├── dataLoader.gif
│       │   │   │   ├── empty_background_pattern.png
│       │   │   │   ├── notFound.png
│       │   │   │   └── pageLoader.gif
│       │   │   ├── icon
│       │   │   │   ├── ic_award.svg
│       │   │   │   ├── ic_bell.svg
│       │   │   │   ├── ic_book2.svg
│       │   │   │   ├── ic_calendar.svg
│       │   │   │   ├── ic_check.svg
│       │   │   │   ├── ic_chevron-down.svg
│       │   │   │   ├── ic_chevron-right.svg
│       │   │   │   ├── ic_close.png
│       │   │   │   ├── ic_comment-filled.svg
│       │   │   │   ├── ic_comment.svg
│       │   │   │   ├── ic_edit.svg
│       │   │   │   ├── ic_empty_search.svg
│       │   │   │   ├── ic_exclamation-circle.svg
│       │   │   │   ├── ic_eye_close.svg
│       │   │   │   ├── ic_eye_open.svg
│       │   │   │   ├── ic_heart.svg
│       │   │   │   ├── ic_heart_black.svg
│       │   │   │   ├── ic_heart_red.svg
│       │   │   │   ├── ic_more.svg
│       │   │   │   ├── ic_photo.svg
│       │   │   │   ├── ic_photo_plus.svg
│       │   │   │   ├── ic_plus.svg
│       │   │   │   ├── ic_ranking.svg
│       │   │   │   ├── ic_search.svg
│       │   │   │   ├── ic_star.svg
│       │   │   │   ├── ic_star_failled.svg
│       │   │   │   ├── ic_star_half.svg
│       │   │   │   ├── ic_trash.svg
│       │   │   │   ├── ic_xbox.svg
│       │   │   │   ├── u_angle-left-b.svg
│       │   │   │   └── u_angle-right-b.svg
│       │   │   ├── logo
│       │   │   │   └── logo_symbol.png
│       │   │   ├── nav
│       │   │   │   ├── arrow_down.svg
│       │   │   │   ├── deokhugam.svg
│       │   │   │   └── notification.svg
│       │   │   └── notification
│       │   │       ├── decorative_Background_pattern.png
│       │   │       └── scroll_Loading.gif
│       │   ├── index.html
│       │   └── static
│       │       └── images
│       │           └── favicon.ico
│       └── templates
└── test
    ├── java
    │   └── com
    │       └── codeit
    │           └── project
    │               └── deokhugam
    │                   ├── batch
    │                   │   └── rank
    │                   │       ├── RankProcessorTest.java
    │                   │       ├── RankReaderTest.java
    │                   │       └── RankWriterTest.java
    │                   └── domain
    │                       ├── notification
    │                       │   ├── controller
    │                       │   │   └── NotificationControllerTest.java
    │                       │   ├── event
    │                       │   │   └── NotificationEventHandlerTest.java
    │                       │   ├── repository
    │                       │   │   └── NotificationRepositoryCustomTest.java
    │                       │   └── service
    │                       │       └── NotificationServiceTest.java
    │                       ├── rank
    │                       │   └── repository
    │                       │       └── RankRepositoryCustomTest.java
    │                       └── user
    │                           ├── controller
    │                           │   └── UserControllerTest.java
    │                           └── service
    │                               └── UserServiceTest.java
    └── resources
        └── application-test.yaml
```
