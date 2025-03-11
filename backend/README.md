Hướng dẫn cài đặt server: 

- Cài đặt java JDK 21: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html , bản Windows x64 Installer
- cài đặt Intellij: để mở project
- clone project từ github về
- mở Intellij, chọn File -> Open và chọn thư mục project
- cài đặt JDK để chạy project: chọn File -> Setting -> Build,Excution, Deployment -> Build Tools -> Maven -> Importing: tại mục JDK for Importer: chọn JDK 21
- chọn File -> Project Structure: Tại mục Project SDK chọn 21
- ở góc trên cùng bên phải màn hình, chọn Maven -> Tên project -> Lifecycle: chọn Install để tải các thư viện cần thiết.
- Sau khi tải xong thì có thể chạy project.
- Hiện tại database đang sử dụng là mysql của aws, có thể tự tải mysql (tải mysql work bench và mysql server ) về và chạy database tại máy tính cá nhân.
- nếu thay đổi thì thay đổi file cấu hình src/main/resources/application.properties như sau:
    spring.datasource.url=jdbc:mysql://localhost:3306/ tên database (tên database đặt khi tạo database trong mysql work bench, cổng mặc định là 3306)
    spring.datasource.username=tên đăng nhập database
    spring.datasource.password=mật khẩu đăng nhập database
    (chỉ thay đổi 3 dòng này)
- sau khi thay dổi chạy lại project là được, vào http://localhost:8080/swagger-ui/index.html nếu có hiển thị giao diện các API là thành công, tài khoản mặc định
  sau khi chạy là admin/admin.

  ![image](https://github.com/user-attachments/assets/c2c92586-fa3e-4e91-a519-076c3318f22b)

