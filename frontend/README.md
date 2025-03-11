#Hướng dẫn cài đặt

    - B1: cài đặt nodejs https://nodejs.org/en/download (rcm v18.20.7 (LST) ) ;
    
    - B2: clone project về: git clone + url ;
    
    - B3: Mở project bằng vscode ;
    
    - B4: cài đặt angular CLI: mở terminal trong vscode chạy:  npm install -g @angular/cli@17 ;
    
    - B5: tải node module, chạy lệnh  npm i ;
    
    - B6: chạy lệnh: ng s để chạy project, nếu chạy thành công, truy cập localhost:4200 ;
    
    - Nếu sử dụng server hiện tại đang chạy trên aws thì không cần chỉnh sửa gì, nếu muốn sử dụng server chạy trên máy cá nhân
      chỉnh sửa file cấu hình: src/environments/environments.ts, sửa biến  backendApiUrl: 'http://13.239.169.8:8080' thành  backendApiUrl: 'http://localhost:8080'

![image](https://github.com/user-attachments/assets/478b86bf-296a-4c54-8ba8-5a40e51d516d)


Home
	Danh sach san pham
	Xem chi tiet san pham
	Them san pham vao gio hang
	Tang/giam so luong
	Dang nhap
	Dang xuat
	404
	Dat hang
	Xem lich su mua hang
	Huy don
	Xac minh tai khoan
	Dang ky
	Quen mat khau

Staff
	Quan li thuc don
		Them, sua, xoa mon an
		Cap nhat gia
		An mon an
	Quan li don hang
		Xem danh sach
		Xac nhan don hang & cap nhat trang thai
		Thong bao gui mail
	Quan li tai khoan kh
		Xem danh sach khach hang
		Khoa/ mo tai khoan
	Thong ke doanh thu
		Theo ngay
		Theo thang
	
Admin	
	Quan ly nhan vien
		Them sua xoa tk nhan vien
		Phan quyen
	Quan ly he thong
		Xem lich su hoat dong cua nhan vien
	
	
