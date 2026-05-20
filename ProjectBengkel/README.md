# 🔧 Aplikasi Manajemen Bengkel
**Java Swing | MVC + DAO | MySQL (db_bengkel)**

---

## 📁 Struktur Project
```
BengkelApp/
├── src/
│   ├── Main.java                        ← Entry point
│   ├── database/
│   │   └── DatabaseConnection.java      ← Koneksi MySQL (Singleton)
│   ├── model/
│   │   ├── Sparepart.java
│   │   ├── Pelanggan.java
│   │   ├── Servis.java
│   │   └── DetailServis.java
│   ├── dao/
│   │   ├── SparepartDAO.java            ← CRUD tabel sparepart
│   │   ├── PelangganDAO.java            ← CRUD tabel pelanggan
│   │   └── ServisDAO.java               ← Transaksi servis + detail
│   ├── controller/
│   │   ├── SparepartController.java     ← Validasi & koordinasi View↔DAO
│   │   ├── PelangganController.java
│   │   └── ServisController.java
│   ├── view/
│   │   ├── MainFrame.java               ← Jendela utama (JTabbedPane)
│   │   ├── SparepartPanel.java          ← Tab Inventaris Sparepart
│   │   ├── PelangganPanel.java          ← Tab Data Pelanggan
│   │   ├── ServisPanel.java             ← Tab Transaksi Servis
│   │   └── RiwayatPanel.java            ← Tab Riwayat Servis
│   └── util/
│       └── FormatUtil.java              ← Helper format Rupiah
└── lib/
    └── mysql-connector-j-*.jar          ← Driver MySQL (download sendiri)
```

---

## ⚙️ Persiapan Database

1. Jalankan MySQL
2. Import file SQL:
```sql
mysql -u root -p < bengkel_db.sql
```
Atau buka di MySQL Workbench dan jalankan seluruh isi `bengkel_db.sql`

Database akan otomatis membuat:
- Database `db_bengkel`
- User `bengkel_user` / password `bengkel123`
- Tabel: `sparepart`, `pelanggan`, `servis`, `detail_servis`
- Data contoh siap pakai

---

## 🔌 Download Driver MySQL

Download **MySQL Connector/J** dari:
https://dev.mysql.com/downloads/connector/j/

Pilih versi JAR → taruh di folder `lib/`

---

## 🚀 Cara Compile & Run

### Windows (CMD)
```cmd
mkdir bin
javac -cp lib\mysql-connector-j-*.jar -d bin src\database\*.java src\model\*.java src\dao\*.java src\controller\*.java src\view\*.java src\util\*.java src\Main.java
java -cp bin;lib\mysql-connector-j-*.jar Main
```

### Linux / Mac (Terminal)
```bash
mkdir bin
javac -cp "lib/mysql-connector-j-*.jar" -d bin src/database/*.java src/model/*.java src/dao/*.java src/controller/*.java src/view/*.java src/util/*.java src/Main.java
java -cp "bin:lib/mysql-connector-j-*.jar" Main
```

---

## 🏗️ Arsitektur MVC + DAO

```
VIEW  ──→  CONTROLLER  ──→  DAO  ──→  DATABASE (MySQL)
 ↑              ↓             ↓
 └──── data ────┴─── Model ───┘
```

| Layer      | Tanggung Jawab                              |
|------------|---------------------------------------------|
| Model      | Representasi data (POJO/JavaBean)           |
| DAO        | Query SQL langsung ke database              |
| Controller | Validasi input, koordinasi View ↔ DAO       |
| View       | Tampilan GUI (Swing), event handler tombol  |

---

## ✅ Fitur Aplikasi

| Modul       | Fitur                                              |
|-------------|----------------------------------------------------|
| Sparepart   | Tambah, Update, Hapus, Lihat daftar + stok         |
| Pelanggan   | Tambah, Update, Hapus, Lihat daftar                |
| Servis      | Input servis baru, pilih sparepart, hitung total   |
| Riwayat     | Lihat history servis + detail sparepart per servis |

### Logika Inti (ServisDAO - Transaction):
1. Cek stok semua sparepart → jika tidak cukup: **TOLAK**
2. INSERT ke tabel `servis` → dapatkan `id` baru
3. INSERT ke `detail_servis` untuk setiap sparepart
4. UPDATE stok sparepart (kurangi)
5. Jika ada yang gagal → **ROLLBACK** semua perubahan
