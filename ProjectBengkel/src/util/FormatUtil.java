package util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * FormatUtil - Utility untuk format tampilan angka dan konstanta pilihan
 */
public class FormatUtil {

    private static final NumberFormat RUPIAH =
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public static String formatRupiah(double nominal) {
        return RUPIAH.format(nominal);
    }

    public static final String[] JENIS_SERVIS = {
        "Ganti Oli", "Tune Up", "Servis Ringan", "Servis Berat",
        "Ganti Ban", "Servis Rem", "Ganti Aki", "Lainnya"
    };
}
