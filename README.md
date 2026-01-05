Truy cập localhost:8180 để tạo 2 user mẫu là kenshirn và admin. T sẽ quay clip hướng dẫn.

🚀 Quy trình làm việc (Git Workflow) - Terminal & IntelliJ

Tài liệu này hướng dẫn quy trình Feature Branch Workflow chuẩn cho team. Bạn có thể chọn dùng dòng lệnh (Terminal) hoặc giao diện IntelliJ IDEA tùy thích.

🛑 Nguyên tắc vàng (Rules)

KHÔNG push thẳng lên main.

Code phải đi qua Pull Request (PR) và được Review.

Chỉ Merge khi Build & Test báo xanh (Pass).

🛠 Hướng dẫn từng bước (Step-by-step)

Bước 1: Đồng bộ code mới nhất (Sync Main)

Trước khi làm task mới, phải đảm bảo code dưới máy bạn là mới nhất từ server.

💻 Cách 1: Dùng Terminal

git checkout main
git pull origin main


🧠 Cách 2: Dùng IntelliJ IDEA

Nhìn góc trên cùng bên phải (hoặc dưới cùng phải), bấm vào tên nhánh hiện tại.

Chọn Local > main -> Chọn Checkout.

Bấm nút Update Project (Mũi tên màu xanh dương ⬇️ ở thanh công cụ phía trên hoặc phím tắt Ctrl + T).

Chọn OK.

Bước 2: Tạo nhánh mới (Create Branch)

Tạo "vùng đất riêng" để code, không ảnh hưởng đến ai.

Feature: feature/ten-tinh-nang (VD: feature/login-page)

Fix bug: fix/ten-loi (VD: fix/nav-bar-color)

💻 Cách 1: Dùng Terminal

git checkout -b feature/ten-tinh-nang


🧠 Cách 2: Dùng IntelliJ IDEA

Bấm vào tên nhánh (main) ở widget nhánh (góc phải).

Chọn + New Branch.

Nhập tên: feature/ten-tinh-nang.

Đảm bảo ô "Checkout branch" được tích.

Bấm Create.

Bước 3: Code, Commit và Push

Sau khi code xong, hãy lưu lại và đẩy lên server.

💻 Cách 1: Dùng Terminal

git add .
git commit -m "Add login form UI"
git push origin feature/ten-tinh-nang


🧠 Cách 2: Dùng IntelliJ IDEA (Siêu nhanh)

Bấm phím tắt Ctrl + K (hoặc Cmd + K trên Mac) để mở cửa sổ Commit.

Tích chọn các file muốn lưu.

Viết mô tả vào ô Commit Message.

Bấm vào mũi tên nhỏ bên cạnh nút Commit, chọn Commit and Push...

Bấm Push ở cửa sổ xác nhận hiện ra sau đó.

Bước 4: Tạo Pull Request (PR)

Bước này thực hiện trên Web GitHub.

Vào Repository trên GitHub.

Bạn sẽ thấy thông báo "Compare & pull request" màu vàng hiện lên. Bấm vào đó.

Review lại tiêu đề và mô tả những gì bạn đã làm.

Bấm Create Pull Request.

Mẹo: Trong IntelliJ, nếu bạn cài plugin GitHub, bạn có thể tạo PR ngay trong IDE tại tab Pull Requests bên trái.

Bước 5: Chờ kiểm tra tự động (Automated Checks)

Hệ thống CI (GitHub Actions) sẽ tự chạy để kiểm tra code của bạn (file pr-validation.yml).

🟡 Vàng: Đang chạy... (Đi uống nước chờ xíu).

🔴 Đỏ (Fail): Code lỗi hoặc Test sai.

Xử lý: Xem log lỗi trên GitHub, sửa code ở máy local (IntelliJ), sau đó Commit & Push lại (lặp lại Bước 3). PR sẽ tự cập nhật.

✅ Xanh (Pass): Code ngon, sẵn sàng để Review.

Bước 6: Review và Merge

Gửi link PR vào nhóm chat team: "Ae review hộ cái PR login nhé".

Đồng đội vào xem code, comment góp ý hoặc bấm Approve.

Khi đủ 2 điều kiện: Đèn Xanh (✅) VÀ Được Approve, nút Merge sẽ sáng lên.

Bấm Merge để gộp code vào main.

🆘 Xử lý sự cố thường gặp (Troubleshooting)

Q: Đang push thì IntelliJ báo "Push Rejected" (Conflict)?

A: Bạn cần lấy code mới nhất từ main về gộp vào nhánh của bạn.

Tại IntelliJ: Click vào nhánh main (Local) -> Chọn Update.

Click vào nhánh main lần nữa -> Chọn Merge 'main' into 'feature/...'.

Giải quyết conflict (nếu có) bằng giao diện 3 cửa sổ của IntelliJ.

Commit và Push lại.

Q: Lỡ code trên main mà quên tạo nhánh?

A: Đừng lo.

Tại IntelliJ: Vào menu Git -> Uncommitted Changes -> Stash Changes (Cất tạm code đi).

Tạo nhánh mới (feature/xyz).

Vào menu Git -> Uncommitted Changes -> Unstash Changes (Lôi code ra lại).
