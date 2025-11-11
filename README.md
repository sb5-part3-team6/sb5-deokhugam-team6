# 📚 덕후감 (Deokhugam)

[![codecov](https://codecov.io/gh/sb5-part3-team6/sb5-deokhugam-team6/branch/dev/graph/badge.svg?token=77AGWST30O)](https://codecov.io/gh/sb5-part3-team6/sb5-deokhugam-team6)

> 책을 사랑하는 모든 독자를 위한 **독서 커뮤니티 서비스**  
> 이미지 속 ISBN을 인식하고, 리뷰와 감상을 공유하며, 함께 성장하는 독서 경험을 제공합니다.

---

## 🖼 서비스 미리보기

<details>
<summary>스크린샷 보기</summary>
<div>
<img width="1500" src="https://github.com/user-attachments/assets/4deeba61-c3c9-4bde-916f-78266d3e5c0a" />
<img width="1500" src="https://github.com/user-attachments/assets/2861bf50-75aa-4304-a92c-174a2893f7ea" />
<img width="1500" src="https://github.com/user-attachments/assets/b1c3ff16-3fc4-426f-b189-09d899d4378d" />
<img width="1500" src="https://github.com/user-attachments/assets/e49d8d60-0c4d-4952-8ee4-b56934414491" />
</div>
</details>

---

## 🧭 팀 문서
- 📄 [팀 노션 바로가기](https://www.notion.so/_6-_5-2797bb3b3a0f810a8680ed5279a3de3e?source=copy_link)

---

## 💡 프로젝트 소개

### ✨ 주요 기능
- 📖 **ISBN 인식 등록** — Naver API 및 **OCR 기술**을 통해 이미지 속 ISBN을 자동 인식하여 책 정보를 손쉽게 등록
- 🧮 **Batch 처리 기반 통계** — 인기 **도서·리뷰·사용자 순위**를 주기적으로 계산하여 최신 트렌드 제공
- 💬 **리뷰 & 감상 공유** — 독자들이 자유롭게 감상을 나누며 소통할 수 있는 커뮤니티 공간

---

## 👥 팀원 구성

| 이름 | GitHub |
|------|---------|
| 김동규 | [@redmatoda](https://github.com/redmatoda) |
| 박문아 | [@ayanemoona](https://github.com/ayanemoona) |
| 박주환 | [@parkjoohwan](https://github.com/parkjoohwan) |
| 이소연 | [@isylsy166](https://github.com/isylsy166) |
| 이유호 | [@yuhoyuho](https://github.com/yuhoyuho) |

---

## 🗓 프로젝트 기간
**2025.10.20 ~ 2025.11.07**

---

## ⚙️ 기술 스택

### 🖥 프론트엔드
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white" alt=""/> <img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white" alt=""/>

### 💾 백엔드
<img src="https://img.shields.io/badge/Java 17-007396?style=for-the-badge&logo=openjdk&logoColor=white" alt=""/> <img src="https://img.shields.io/badge/SpringBoot 3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt=""/> <img src="https://img.shields.io/badge/PostgreSQL 17.6-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt=""/>

### 🤝 협업 도구
<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=github&logoColor=white" alt=""/> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white" alt=""/> <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt=""/>

---

## 🏗 아키텍처
<img width="1000" src="https://github.com/user-attachments/assets/994e5a2b-6183-45de-b0cd-3ca388d897a9" />

---

## 🔍 구현 기능 상세

### 📚 도서
- 도서를 등록, 수정, 삭제할 수 있습니다.
- ISBN 또는 이미지(OCR)를 이용해 도서 정보를 불러올 수 있습니다.

### 👤 유저
- 회원가입, 로그인, 회원탈퇴 기능을 제공합니다.

### 📝 리뷰
- 도서에 대한 리뷰 등록·수정·삭제 기능
- 리뷰 좋아요 기능 지원

### 💬 댓글
- 리뷰에 대한 댓글 등록·수정·삭제 기능

### 🔔 알림
- 리뷰에 대한 댓글, 좋아요, 랭킹 관련 알림을 제공합니다.

### 📊 대시보드
- 인기 도서·리뷰·유저 순위를 일간/주간/월간/역대 기준으로 제공합니다.

---

## 🐳 Docker 환경

> ⚠️ Local 환경에서는 OCR 및 Naver API 기능이 제한됩니다.

- 업로드된 이미지는 `/app/uploads` 경로에 저장됩니다.
- 실행 전 local postgres에 `schema.sql`을 적용하거나 `docker-compose-local.yml`을 사용하세요.

### 🧾 .env 예시 (local)
```bash
SPRING_PROFILES_ACTIVE=dev

# =========== DB ==========
POSTGRES_PORT=5432
POSTGRES_DB=deokhugam
POSTGRES_HOST=db
POSTGRES_USER=deokhugam_user
POSTGRES_PASSWORD=user1234

# ========== Spring DATASOURCE ==========
SPRING_DATASOURCE_URL=jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}
SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}

# ========= NAVER API =======
NAVER_CLIENT_ID=
NAVER_CLIENT_SECRET=

# ========= OCR API =======
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

### 🚀 실행 명령어
```bash
docker compose -f docker-compose-local.yml up
```

---

## 🌐 구현 홈페이지
🔗 [덕후감 서비스 (2025-11-15까지 유지)](http://54.180.228.190/)

---

## 📁 디렉토리 구조

```plaintext
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
│   │                   │   │   └── ...
│   │                   │   ├── processor
│   │                   │   │   └── ...
│   │                   │   ├── reader
│   │                   │   │   └── ...
│   │                   │   ├── scheduler
│   │                   │   │   ├── ...
│   │                   │   └── writer
│   │                   │       └── ...
│   │                   ├── domain
│   │                   │   ├── book
│   │                   │   │   ├── controller
│   │                   │   │   │   └── ...
│   │                   │   │   ├── dto
│   │                   │   │   │   ├── command
│   │                   │   │   │   │   ├── ...
│   │                   │   │   │   ├── request
│   │                   │   │   │   │   └── ...
│   │                   │   │   │   └── response
│   │                   │   │   │       └── ...
│   │                   │   │   ├── entity
│   │                   │   │   │   └── ...
│   │                   │   │   ├── event
│   │                   │   │   │   └── ...
│   │                   │   │   ├── exception
│   │                   │   │   │   ├── ....
│   │                   │   │   │   └── detail
│   │                   │   │   │       └── ...
│   │                   │   │   ├── mapper
│   │                   │   │   │   └── ...
│   │                   │   │   ├── repository
│   │                   │   │   │   └── ...
│   │                   │   │   └── service
│   │                   │   │       └── ...
│   │                   │   └── other domains...
│   │                   ├── external
│   │                   │   └── client
│   │                   │       ├── ...
│   │                   │       └── dto
│   │                   │           └── ...
│   │                   └── global
│   │                       ├── common
│   │                       │   ├── dto
│   │                       │   │   ├── ...
│   │                       │   └── entity
│   │                       │       ├── ...
│   │                       ├── config
│   │                       │   ├── ...
│   │                       │   └── impl
│   │                       │       ├── ...
│   │                       ├── exception
│   │                       │   ├── ...
│   │                       ├── log
│   │                       │   └── ...
│   │                       └── storage
│   │                           ├── ...
│   │                           └── impl
│   │                               └── ...
│   └── resources
│       ├── ...
│       ├── static
│       │   ├── ...
│       └── templates
└── test
    ├── java
    │   └── com
    │       └── codeit
    │           └── project
    │               └── deokhugam
    │                   ├── batch
    │                   │   └── rank
    │                   │       └── ...
    │                   └── domain
    │                       ├── notification
    │                       │   ├── controller
    │                       │   │   └── ...
    │                       │   ├── event
    │                       │   │   └── ...
    │                       │   ├── repository
    │                       │   │   └── ...
    │                       │   └── service
    │                       │       └── ...
    │                       └─── ...
    └── resources
        └── ...
```

---

> 💬 **덕후감**은 단순한 독서 기록을 넘어, 독자들이 서로의 생각을 나누고 성장하는 커뮤니티를 지향합니다.