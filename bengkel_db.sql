-- ================================================================
-- DATABASE: db_bengkel
-- Aplikasi Manajemen Bengkel
-- Arsitektur: Java Swing MVC + DAO + MySQL
-- ================================================================

CREATE DATABASE IF NOT EXISTS db_bengkel
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE db_bengkel;

-- ----------------------------------------------------------------
-- USER APLIKASI (jalankan sebagai root)
-- ----------------------------------------------------------------
CREATE USER IF NOT EXISTS 'bengkel_user'@'localhost' IDENTIFIED BY 'bengkel123';
GRANT ALL PRIVILEGES ON db_bengkel.* TO 'bengkel_user'@'localhost';
FLUSH PRIVILEGES;

-- ================================================================
-- TABEL 1: sparepart
-- Menyimpan data stok sparepart bengkel
-- ================================================================
CREATE TABLE IF NOT EXISTS sparepart (
    id      INT           PRIMARY KEY AUTO_INCREMENT,
    nama    VARCHAR(100)  NOT NULL,
    merk    VARCHAR(50),
    harga   DECIMAL(12,2) NOT NULL DEFAULT 0,
    stok    INT           NOT NULL DEFAULT 0,
    CONSTRAINT chk_harga_sp CHECK (harga >= 0),
    CONSTRAINT chk_stok_sp  CHECK (stok  >= 0)
) ENGINE=InnoDB COMMENT='Inventaris sparepart bengkel';

-- ================================================================
-- TABEL 2: pelanggan
-- Menyimpan data identitas pelanggan
-- ================================================================
CREATE TABLE IF NOT EXISTS pelanggan (
    id       INT           PRIMARY KEY AUTO_INCREMENT,
    nama     VARCHAR(100)  NOT NULL,
    telepon  VARCHAR(20),
    alamat   TEXT
) ENGINE=InnoDB COMMENT='Data pelanggan bengkel';

-- ================================================================
-- TABEL 3: servis
-- Header transaksi servis (satu baris = satu kunjungan servis)
-- ================================================================
CREATE TABLE IF NOT EXISTS servis (
    id              INT           PRIMARY KEY AUTO_INCREMENT,
    id_pelanggan    INT           NOT NULL,
    plat_kendaraan  VARCHAR(20)   NOT NULL,
    merk_kendaraan  VARCHAR(50),
    tipe_kendaraan  VARCHAR(50),
    jenis_servis    VARCHAR(50)   NOT NULL,
    biaya_jasa      DECIMAL(12,2) NOT NULL DEFAULT 0,
    total_biaya     DECIMAL(12,2) NOT NULL DEFAULT 0,
    tanggal_servis  DATE          NOT NULL,
    keterangan      TEXT,
    CONSTRAINT fk_servis_pelanggan
        FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='Transaksi servis kendaraan';

-- ================================================================
-- TABEL 4: detail_servis
-- Sparepart yang dipakai dalam satu transaksi servis
-- Relasi: detail_servis banyak ke satu servis,
--         detail_servis banyak ke satu sparepart
-- ================================================================
CREATE TABLE IF NOT EXISTS detail_servis (
    id            INT           PRIMARY KEY AUTO_INCREMENT,
    id_servis     INT           NOT NULL,
    id_sparepart  INT           NOT NULL,
    jumlah        INT           NOT NULL DEFAULT 1,
    harga_satuan  DECIMAL(12,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_detail_servis
        FOREIGN KEY (id_servis)    REFERENCES servis(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_detail_sparepart
        FOREIGN KEY (id_sparepart) REFERENCES sparepart(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='Detail sparepart per transaksi servis';

-- ================================================================
-- DATA CONTOH: sparepart (8 item)
-- ================================================================
INSERT INTO sparepart (nama, merk, harga, stok) VALUES
    ('Oli Mesin',    'Castrol', 75000,  20),
    ('Filter Oli',   'Denso',   35000,  15),
    ('Kampas Rem',   'TDR',     80000,  10),
    ('Busi',         'NGK',     45000,  25),
    ('Air Aki',      'GS',      25000,  30),
    ('V-Belt',       'Bando',   55000,  12),
    ('Lampu Depan',  'Osram',   40000,  18),
    ('Filter Udara', 'Honda',   30000,  20);

-- ================================================================
-- DATA CONTOH: pelanggan (5 orang)
-- ================================================================
INSERT INTO pelanggan (nama, telepon, alamat) VALUES
    ('Budi Santoso',  '08123456789', 'Jl. Mawar No.1, Yogyakarta'),
    ('Siti Rahayu',   '08987654321', 'Jl. Melati No.5, Yogyakarta'),
    ('Ahmad Fauzi',   '08111222333', 'Jl. Flamboyan No.12, Sleman'),
    ('Dewi Lestari',  '08222333444', 'Jl. Anggrek No.3, Bantul'),
    ('Riko Saputra',  '08333444555', 'Jl. Kenanga No.7, Kulon Progo');

-- ================================================================
-- DATA CONTOH: servis (3 transaksi)
-- ================================================================
INSERT INTO servis (id_pelanggan, plat_kendaraan, merk_kendaraan, tipe_kendaraan,
    jenis_servis, biaya_jasa, total_biaya, tanggal_servis, keterangan)
VALUES
    (1, 'AB 1234 CD', 'Honda',  'BeAT',      'Ganti Oli',    50000, 160000, '2024-05-10', 'Ganti oli rutin 3000km'),
    (2, 'AB 5678 EF', 'Yamaha', 'NMAX',      'Tune Up',      75000, 200000, '2024-05-12', 'Tune up + ganti busi'),
    (3, 'AB 9999 GH', 'Honda',  'Vario 150', 'Servis Ringan',60000, 195000, '2024-05-15', 'Servis berkala');

-- ================================================================
-- DATA CONTOH: detail_servis
-- ================================================================
INSERT INTO detail_servis (id_servis, id_sparepart, jumlah, harga_satuan) VALUES
    -- Servis 1: Ganti Oli Mesin + Filter Oli
    (1, 1, 1, 75000),
    (1, 2, 1, 35000),
    -- Servis 2: Busi x2 + Filter Udara
    (2, 4, 2, 45000),
    (2, 8, 1, 30000),
    -- Servis 3: Oli Mesin + Kampas Rem
    (3, 1, 1, 75000),
    (3, 3, 1, 80000);

-- Update stok setelah servis
UPDATE sparepart SET stok = stok - 1 WHERE id = 1; -- Oli Mesin (servis 1)
UPDATE sparepart SET stok = stok - 1 WHERE id = 2; -- Filter Oli (servis 1)
UPDATE sparepart SET stok = stok - 2 WHERE id = 4; -- Busi x2 (servis 2)
UPDATE sparepart SET stok = stok - 1 WHERE id = 8; -- Filter Udara (servis 2)
UPDATE sparepart SET stok = stok - 1 WHERE id = 1; -- Oli Mesin (servis 3)
UPDATE sparepart SET stok = stok - 1 WHERE id = 3; -- Kampas Rem (servis 3)

-- ================================================================
-- VERIFIKASI AKHIR
-- ================================================================
SELECT 'Sparepart:' AS Info, COUNT(*) AS Jumlah FROM sparepart
UNION ALL
SELECT 'Pelanggan:', COUNT(*) FROM pelanggan
UNION ALL
SELECT 'Servis:', COUNT(*) FROM servis
UNION ALL
SELECT 'Detail Servis:', COUNT(*) FROM detail_servis;

