-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 30 Bulan Mei 2026 pada 23.46
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_bengkel`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `detail_servis`
--

CREATE TABLE `detail_servis` (
  `id` int(11) NOT NULL,
  `id_servis` int(11) NOT NULL,
  `id_sparepart` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL DEFAULT 1,
  `harga_satuan` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Detail sparepart per transaksi servis';

--
-- Dumping data untuk tabel `detail_servis`
--

INSERT INTO `detail_servis` (`id`, `id_servis`, `id_sparepart`, `jumlah`, `harga_satuan`) VALUES
(17, 9, 23, 1, 250000),
(18, 10, 20, 1, 25000),
(19, 10, 23, 1, 250000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `telepon` varchar(20) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Data pelanggan bengkel';

--
-- Dumping data untuk tabel `pelanggan`
--

INSERT INTO `pelanggan` (`id`, `nama`, `telepon`, `alamat`) VALUES
(1, 'Budi Santoso', '08123456789', 'Jl. Mawar No.1, Yogyakarta'),
(2, 'Siti Rahayu', '08987654321', 'Jl. Melati No.5, Yogyakarta'),
(3, 'Ahmad Fauziah', '08111222333', 'Jl. Flamboyan No.12, Sleman'),
(4, 'Dewi Lestari', '08222333444', 'Jl. Anggrek No.3, Bantul'),
(5, 'Riko Saputra', '08333444555', 'Jl. Kenanga No.7, Kulon Progo'),
(12, 'Buahlil', '081908290344', 'Jl. Kenongo No. 10, Yogyakarta');

-- --------------------------------------------------------

--
-- Struktur dari tabel `servis`
--

CREATE TABLE `servis` (
  `id` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `plat_kendaraan` varchar(20) NOT NULL,
  `merk_kendaraan` varchar(50) DEFAULT NULL,
  `tipe_kendaraan` varchar(50) DEFAULT NULL,
  `jenis_servis` varchar(50) NOT NULL,
  `biaya_jasa` int(11) NOT NULL DEFAULT 0,
  `total_biaya` int(11) NOT NULL DEFAULT 0,
  `tanggal_servis` date NOT NULL,
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Transaksi servis kendaraan';

--
-- Dumping data untuk tabel `servis`
--

INSERT INTO `servis` (`id`, `id_pelanggan`, `plat_kendaraan`, `merk_kendaraan`, `tipe_kendaraan`, `jenis_servis`, `biaya_jasa`, `total_biaya`, `tanggal_servis`, `keterangan`) VALUES
(9, 12, 'BK 2340 FK', 'Beat', 'Honda', 'Tune Up', 50000, 300000, '2026-05-31', ''),
(10, 3, 'BK 2340 FK', 'Beat', 'Honda', 'Servis Ringan', 20000, 295000, '2026-05-31', '');

-- --------------------------------------------------------

--
-- Struktur dari tabel `sparepart`
--

CREATE TABLE `sparepart` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `merk` varchar(50) DEFAULT NULL,
  `harga` int(11) NOT NULL DEFAULT 0,
  `stok` int(11) NOT NULL DEFAULT 0
) ;

--
-- Dumping data untuk tabel `sparepart`
--

INSERT INTO `sparepart` (`id`, `nama`, `merk`, `harga`, `stok`) VALUES
(19, 'Oli Mesin', 'Yamalube', 65000, 50),
(20, 'Busi', 'NGK', 25000, 99),
(21, 'Kampas Rem', 'Federal', 85000, 30),
(22, 'Filter Udara', 'Honda', 45000, 25),
(23, 'Aki Motor', 'GS Astra', 250000, 13),
(24, 'Ban Depan', 'IRC', 180000, 20),
(25, 'Ban Belakang', 'IRC', 220000, 20),
(26, 'Lampu LED', 'Philips', 75000, 40),
(27, 'Rantai Motor', 'SSS', 150000, 18),
(28, 'Oli Gardan', 'Yamalube', 30000, 35);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `detail_servis`
--
ALTER TABLE `detail_servis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_detail_servis` (`id_servis`),
  ADD KEY `fk_detail_sparepart` (`id_sparepart`);

--
-- Indeks untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `servis`
--
ALTER TABLE `servis`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_servis_pelanggan` (`id_pelanggan`);

--
-- Indeks untuk tabel `sparepart`
--
ALTER TABLE `sparepart`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `detail_servis`
--
ALTER TABLE `detail_servis`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT untuk tabel `servis`
--
ALTER TABLE `servis`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `sparepart`
--
ALTER TABLE `sparepart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detail_servis`
--
ALTER TABLE `detail_servis`
  ADD CONSTRAINT `fk_detail_servis` FOREIGN KEY (`id_servis`) REFERENCES `servis` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_detail_sparepart` FOREIGN KEY (`id_sparepart`) REFERENCES `sparepart` (`id`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `servis`
--
ALTER TABLE `servis`
  ADD CONSTRAINT `fk_servis_pelanggan` FOREIGN KEY (`id_pelanggan`) REFERENCES `pelanggan` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
