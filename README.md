# NoteHub

**NoteHub** là một nền tảng hỗ trợ làm việc nhóm hiệu quả, giúp các thành viên trong team cộng tác và quản lý công việc một cách dễ dàng. Dự án được xây dựng với kiến trúc hiện đại, đảm bảo khả năng mở rộng và hiệu năng cao.

---

## 🚀 Giới thiệu (Project Overview)

NoteHub cung cấp các công cụ cần thiết để tối ưu hóa quy trình làm việc nhóm.
**Các tính năng chính:**
*   Quản lý người dùng và xác thực, phân quyền bảo mật (SSO với Keycloak).
*   Giao diện tương tác mượt mà, thân thiện với người dùng.
*   Hệ thống Backend mạnh mẽ dựa trên kiến trúc Spring Modulith.
*   Tích hợp Cloudinary để quản lý tài nguyên media.

---

## 🛠 Công nghệ sử dụng (Tech Stack)

Dự án sử dụng các công nghệ tiên tiến nhất hiện nay:

### Backend
*   **Ngôn ngữ:** Java 21
*   **Framework:** Spring Boot 3.5.9, Spring Modulith 1.4.6
*   **Database:** MySQL 8.0 (JPA/Hibernate)
*   **Security:** Spring Security (OAuth2 Resource Server), Keycloak
*   **Tools:** Flyway (Migration), MapStruct, SpringDoc OpenAPI (Swagger)

### Frontend
*   **Framework:** React 19
*   **Build Tool:** Vite
*   **Language:** TypeScript
*   **Styling:** TailwindCSS, Mantine UI
*   **State/Data Fetching:** Axios, React Query (Orval generated)

### Infrastructure
*   **Containerization:** Docker, Docker Compose
*   **Identity Provider:** Keycloak 26.5

---

## 📂 Cấu trúc dự án (Project Structure)

```
NodeHub/
├── src/
│   ├── main/
│   │   ├── java/         # Mã nguồn Java chính (Domain, API, Logic)
│   │   └── resources/    # Cấu hình (application.yml), DB Migration (Flyway)
│   └── test/             # Testing
│       └── java/         # Unit & Integration Tests (Testcontainers, Modulith)
├── frontend/             # Source code Frontend (ReactJS)
│   ├── src/              # Components, Pages, Hooks
│   ├── public/           # Static assets
│   └── package.json      # Dependencies frontend
├── docker-data/          # Dữ liệu persistent cho Docker (MySQL, Keycloak)
├── keycloak-config/      # Cấu hình import cho Keycloak
├── compose.yaml          # File cấu hình Docker Compose
└── build.gradle          # Quản lý dependencies Backend
```

---

## ✅ Yêu cầu tiên quyết (Prerequisites)

Trước khi bắt đầu, hãy đảm bảo máy của bạn đã cài đặt:

*   **Docker & Docker Compose:** Bắt buộc (để chạy Backend và Database).
*   **Node.js:** Phiên bản 20 trở lên (khuyến nghị dùng Bun hoặc npm) - Dành cho Frontend Developer.
*   **Java Development Kit (JDK):** Phiên bản 21 - Chỉ cần thiết nếu bạn muốn phát triển Backend.
*   **Git:** Để quản lý mã nguồn.

---

## ⚙️ Cài đặt & Cấu hình (Installation & Configuration)

### 1. Clone dự án
```bash
git clone <repository-url>
cd NodeHub
```

### 2. Cấu hình biến môi trường
Tạo file `.env` tại thư mục gốc của dự án. Bạn có thể copy từ file mẫu (nếu có) hoặc cấu hình các biến quan trọng sau (tham khảo `compose.yaml`):

```env
# Docker Hub (Để pull image Backend)
DOCKER_HUB_USERNAME=your_docker_username
DOCKER_HUB_REPO=note-backend

# Database
MYSQL_DATABASE=notehub_db
MYSQL_USER=admin
MYSQL_PASSWORD=secret
MYSQL_ROOT_PASSWORD=root_secret
MYSQL_LOCAL_PORT=3306
MYSQL_DOCKER_PORT=3306

# Keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_PORT=8180
KEYCLOAK_CLIENT_SECRET=your_client_secret

# Google OAuth (Optional)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# App
APP_BASE_URL=http://localhost:8180
BACKEND_PORT=8080
```

### 3. Cài đặt Dependencies

**Frontend (Bắt buộc cho FE Dev):**
```bash
cd frontend
npm install
# Hoặc nếu dùng bun
bun install
```

**Backend (Chỉ dành cho Backend Dev):**
Nếu bạn chỉ làm Frontend, bạn có thể bỏ qua bước này vì Backend sẽ chạy qua Docker Image.
```bash
./gradlew build -x test
```

---

## ▶️ Hướng dẫn chạy (How to Run)

### Cách 1: Chạy toàn bộ Backend bằng Docker (Dành cho Frontend Team)
Backend đã được đóng gói sẵn trên Docker Hub. Bạn chỉ cần chạy lệnh sau để khởi động toàn bộ hệ thống (MySQL, Keycloak, Backend App).

**1. Khởi động Backend & Services:**
```bash
docker-compose up -d
```
*   Lệnh này sẽ tự động pull image mới nhất của Backend về máy.
*   Backend API: `http://localhost:8080`
*   Keycloak: `http://localhost:8180`

**2. Chạy Frontend:**
Mở một terminal mới:
```bash
cd frontend
npm run dev
```
Truy cập Frontend tại: `http://localhost:5173`

### Cách 2: Chạy môi trường Development (Dành cho Backend Team)
Nếu bạn cần sửa code Backend, hãy chạy theo cách này.

**1. Khởi động Infrastructure (MySQL, Keycloak):**
```bash
docker-compose up -d mysql keycloak
```

**2. Chạy Backend (Local):**
```bash
./gradlew bootRun
```

**3. Chạy Frontend:**
```bash
cd frontend
npm run dev
```

---

## ❓ Troubleshooting (Các lỗi thường gặp)

1.  **Lỗi không pull được image:**
    *   Kiểm tra biến `DOCKER_HUB_USERNAME` và `DOCKER_HUB_REPO` trong file `.env` đã chính xác chưa.

2.  **Lỗi cổng (Port Conflict):**
    *   Nếu cổng `3306` (MySQL) hoặc `8080`/`8180` (Keycloak) đã bị chiếm dụng, hãy đổi cổng trong file `.env` hoặc tắt service đang chạy.

---
**Happy Coding! 🚀**
