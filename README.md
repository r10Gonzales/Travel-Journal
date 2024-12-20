---

# Travel-Journal

---

**Aplikasi Travel Journal**

Aplikasi Travel Journal ini memungkinkan pengguna untuk mencatat perjalanan mereka, menyimpan catatan dengan informasi seperti judul, deskripsi, lokasi, dan tanggal perjalanan. Pengguna dapat menambahkan, mengedit, dan menghapus catatan perjalanan, serta melihat daftar catatan perjalanan mereka dalam tampilan yang sederhana dan intuitif.

### Fitur Utama:
- **Menambah Catatan Perjalanan**: Pengguna dapat membuat catatan perjalanan baru dengan menambahkan judul, deskripsi, lokasi, dan tanggal.
- **Melihat Daftar Catatan**: Semua catatan perjalanan yang disimpan dapat dilihat dalam daftar dengan informasi judul, lokasi, dan tanggal.
- **Mengedit dan Menghapus Catatan**: Pengguna dapat memperbarui atau menghapus catatan perjalanan yang sudah ada.
- **Notifikasi Pengingat**: Pengguna dapat mengatur pengingat untuk menambahkan catatan perjalanan sesuai kebutuhan.
- **Penyimpanan di Firebase**: Semua catatan perjalanan disimpan secara online menggunakan Firebase Realtime Database, memungkinkan data untuk diakses di berbagai perangkat.

### Teknologi yang Digunakan:
- **Kotlin**: Bahasa pemrograman utama untuk pengembangan aplikasi Android.
- **Firebase**: Digunakan untuk penyimpanan data real-time dan autentikasi pengguna.
- **Material Design**: Desain antarmuka pengguna yang mengikuti pedoman Material Design untuk memberikan pengalaman pengguna yang modern dan responsif.

---

### Getting Started:
1. **Buka Terminal atau Command Prompt** di komputer Anda.
2. Jalankan perintah berikut untuk meng-clone repositori:
   ```bash
   git clone https://github.com/username/Travel-Journal.git
   ```
   Gantilah `username` dengan nama pengguna GitHub Anda.

3. Masuk ke direktori project:
   ```bash
   cd Travel-Journal
   ```

4. **Buka project menggunakan Android Studio**:
   - Pilih **"Open an existing project"** dan pilih folder **Travel-Journal**.

5. **Sinkronkan dengan Firebase**:
   - Jika Anda belum melakukannya, ikuti langkah-langkah di dokumentasi Firebase untuk menambahkan aplikasi Android Anda ke Firebase dan mendapatkan **`google-services.json`**.
   - Letakkan file **`google-services.json`** ke dalam folder **app/** di project Anda.

6. **Jalankan Aplikasi**:
   - Setelah Firebase terkonfigurasi, Anda dapat menjalankan aplikasi di perangkat Android atau emulator.

---

### Getting Changes If You Have Cloned:
Jika Anda sudah pernah meng-clone project sebelumnya dan ingin mendapatkan perubahan terbaru dari repositori, ikuti langkah-langkah berikut:

1. **Buka Terminal atau Command Prompt** dan arahkan ke direktori project:
   ```bash
   cd Travel-Journal
   ```

2. **Periksa status branch** untuk memastikan Anda berada di branch yang benar (misalnya `main`):
   ```bash
   git status
   ```

3. **Tarik perubahan terbaru** dari repositori dengan perintah `git pull`:
   ```bash
   git pull origin main
   ```
   Perintah ini akan mengunduh perubahan terbaru dari branch `main` di GitHub dan menggabungkannya dengan branch lokal Anda.

4. Jika ada konflik, Anda akan diminta untuk menyelesaikan konflik tersebut. Setelah konflik diselesaikan, lakukan commit dan push jika diperlukan.

5. **Sinkronkan Firebase (Jika Diperlukan)**:
   Jika ada pembaruan terkait konfigurasi Firebase (misalnya perubahan pada **`google-services.json`**), pastikan untuk memperbarui file tersebut dengan versi terbaru yang diberikan oleh pengelola repositori.

6. **Jalankan Aplikasi**:
   Setelah menarik perubahan terbaru, Anda dapat menjalankan kembali aplikasi untuk melihat perubahan yang telah diterapkan.

---

### Membuat Branch Baru untuk Perubahan:
Jika Anda ingin membuat perubahan pada project ini, disarankan untuk membuat branch baru terlebih dahulu. Ini akan membantu menjaga branch `main` tetap stabil dan memungkinkan Anda untuk bekerja pada fitur atau perbaikan tertentu secara terpisah.

1. **Buka Terminal atau Command Prompt** dan arahkan ke direktori project:
   ```bash
   cd Travel-Journal
   ```

2. **Periksa status branch** untuk memastikan Anda berada di branch yang benar (misalnya `main`):
   ```bash
   git status
   ```

3. **Pastikan branch `main` sudah terupdate** dengan menarik perubahan terbaru dari repositori:
   ```bash
   git pull origin main
   ```

4. **Buat branch baru** berdasarkan `main` untuk perubahan yang ingin Anda lakukan. Gunakan format nama branch yang jelas dan deskriptif, seperti:
   ```bash
   git checkout -b feature-nama-fitur-atau-perubahan
   ```
   Contoh:
   ```bash
   git checkout -b feature-add-user-specific-notes
   ```

5. **Lakukan perubahan yang diperlukan** di branch baru tersebut. Setelah selesai melakukan perubahan, tambahkan file yang telah diubah ke staging area:
   ```bash
   git add .
   ```

6. **Lakukan commit** dengan pesan yang jelas menjelaskan perubahan yang dilakukan:
   ```bash
   git commit -m "Menambahkan fitur catatan khusus pengguna"
   ```

7. **Push branch baru** ke repositori GitHub:
   ```bash
   git push origin feature-nama-fitur-atau-perubahan
   ```

8. **Buat Pull Request**:
   - Setelah branch baru dipush ke GitHub, buka repositori di GitHub.
   - Pilih branch baru yang Anda buat dan klik tombol **"Compare & pull request"**.
   - Berikan deskripsi yang jelas tentang perubahan yang Anda buat dan kirimkan pull request untuk direview.

Menggunakan branch baru, untuk memastikan bahwa perubahan tidak langsung memengaruhi branch `main`.

---
