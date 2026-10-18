# Hệ thống quản lý thương mại điện tử

Ứng dụng console viết bằng Java, phục vụ bài tập lập trình hướng đối tượng. Chương trình hỗ trợ quản lý người dùng, sản phẩm, giỏ hàng, đơn hàng và báo cáo kinh doanh; dữ liệu được lưu trong các file CSV.

## 1. Yêu cầu môi trường

- Cài **JDK 11 trở lên**, bao gồm cả `java` và `javac`.
- Thêm thư mục `bin` của JDK vào biến môi trường `PATH`.
- Các lệnh dưới đây sử dụng **PowerShell trên Windows**.
- Không cần cài thư viện ngoài, Maven hoặc Gradle.

Kiểm tra môi trường:

```powershell
java -version
javac -version
```

Cả hai lệnh phải hiển thị phiên bản Java. Nên sử dụng `java` và `javac` thuộc cùng một JDK.

## 2. Cấu trúc dự án

```text
ecommerce/
├── src/
│   ├── Main.java          # Điểm bắt đầu chương trình và menu console
│   ├── model/             # Người dùng, sản phẩm, giỏ hàng, đơn hàng
│   ├── manager/           # Quản lý dữ liệu và xử lý nghiệp vụ
│   ├── factory/           # Khởi tạo sản phẩm
│   ├── strategy/          # Các lớp mô hình hóa phương thức thanh toán
│   ├── exception/         # Ngoại lệ tự định nghĩa
│   └── util/              # Đọc và ghi dữ liệu
├── data/
│   ├── users.csv
│   ├── products.csv
│   ├── orders.csv
│   ├── order_items.csv
│   └── cart_items.csv
└── README.md
```

Thư mục `out` sẽ được tạo khi build để chứa các file `.class`.

## 3. Build chương trình

Mở PowerShell tại thư mục gốc của dự án, là thư mục chứa `src` và `data`. Nếu cần chuyển thư mục, dùng đường dẫn thực tế trên máy, ví dụ:

```powershell
cd "C:\Code\Lap trinh huong doi tuong\ecommerce"
```

Biên dịch toàn bộ mã nguồn:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$sources = @(Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object FullName)
javac -encoding UTF-8 --release 11 -d out @sources
```

Nếu không xuất hiện thông báo lỗi, quá trình build đã thành công. Tùy chọn `-encoding UTF-8` giúp biên dịch đúng các chuỗi tiếng Việt; `--release 11` tạo mã tương thích Java 11 trở lên.

## 4. Chạy chương trình

Giữ PowerShell tại thư mục gốc dự án và chạy:

```powershell
java -cp out Main
```

Menu chính:

| Lựa chọn | Chức năng            |
| -------- | -------------------- |
| 1        | Quản lý sản phẩm     |
| 2        | Xem tất cả sản phẩm  |
| 3        | Tìm kiếm sản phẩm    |
| 4        | Thống kê sản phẩm    |
| 5        | Báo cáo kinh doanh   |
| 6        | Quản lý người dùng   |
| 7        | Quản lý giỏ hàng     |
| 8        | Quản lý đơn hàng     |
| 0        | Lưu dữ liệu và thoát |

Nhập số tương ứng rồi nhấn **Enter**. Trong các menu con, chọn **0** để quay lại.

Sau mỗi lần chỉnh sửa mã nguồn, thực hiện lại bước build trước khi chạy.

## 5. Luồng thao tác mẫu

1. Chọn **6**, thêm người dùng thuộc loại **Buyer**, ghi nhớ mã Buyer.
2. Chọn **1**, thêm sản phẩm với giá và số lượng tồn kho hợp lệ, hoặc sử dụng sản phẩm có sẵn.
3. Chọn **7**, nhập mã Buyer, thêm sản phẩm vào giỏ bằng mã sản phẩm.
4. Trong menu giỏ hàng, chọn **6** để tạo đơn; nhập mã đơn mới và xác nhận bằng **Y**.
5. Chọn **8** ở menu chính để xem, xác nhận, hoàn tất hoặc hủy đơn.
6. Chọn **5** để xem báo cáo kinh doanh.
7. Chọn **0** ở menu chính để lưu và thoát chương trình.

Tạo đơn thành công sẽ trừ tồn kho và làm trống giỏ. Đơn hàng giữ đơn giá tại thời điểm mua. Đơn `PENDING` có thể được xác nhận thành `CONFIRMED`, sau đó hoàn tất thành `COMPLETED`. Có thể hủy đơn `PENDING` hoặc `CONFIRMED` để hoàn kho đúng một lần. Doanh thu và số lượng đã bán chỉ tính đơn `COMPLETED`.

## 6. Lưu và đọc dữ liệu

| File              | Nội dung                                           |
| ----------------- | -------------------------------------------------- |
| `users.csv`       | Thông tin người dùng và vai trò Buyer/Seller/Admin |
| `products.csv`    | Sản phẩm, giá hiện tại và tồn kho                  |
| `orders.csv`      | Mã đơn, mã Buyer, ngày tạo và trạng thái           |
| `order_items.csv` | Sản phẩm trong đơn, số lượng và đơn giá lúc mua    |
| `cart_items.csv`  | Sản phẩm và số lượng trong giỏ của từng Buyer      |

Chương trình đọc dữ liệu khi khởi động, lưu sau các thao tác quản lý và khi chọn thoát. Cả 5 file CSV nằm trong thư mục **`data`**. Đường dẫn `data` được tính từ **thư mục đang chạy chương trình**, vì vậy cần chạy từ thư mục gốc dự án, không chuyển vào `src`, `out` hoặc `data`. Khi lưu, chương trình tự tạo thư mục `data` nếu chưa có.

Nếu bắt đầu khi chưa có file dữ liệu, chương trình tạo các file khi lưu. Khi đã có bộ dữ liệu, hãy giữ đủ 5 file cùng nhau để bảo toàn các liên kết. Nếu file bị lỗi hoặc thiếu dữ liệu liên kết, chương trình báo lỗi và dừng lúc khởi động để tránh ghi đè.

## 7. Một số lỗi thường gặp

- **Không nhận diện `java` hoặc `javac`:** kiểm tra đã cài JDK và cấu hình `PATH`, sau đó mở lại PowerShell.
- **`Could not find or load main class Main`:** kiểm tra build thành công, có file `out/Main.class` và đang chạy từ thư mục gốc bằng `java -cp out Main`.
- **`UnsupportedClassVersionError`:** kiểm tra phiên bản `java` đang chạy; dùng Java 11 trở lên và build lại bằng lệnh ở mục 3.
- **Không thấy dữ liệu CSV:** kiểm tra thư mục làm việc hiện tại bằng `Get-Location`; chuyển về thư mục gốc dự án.
- **Tiếng Việt hiển thị sai:** sử dụng terminal hỗ trợ UTF-8 và font có tiếng Việt; có thể chuyển mã trang console bằng `chcp 65001` rồi chạy lại chương trình.

## 7. Link Github

- https://github.com/nguyentheanh24lc-design/ecommerce.git
