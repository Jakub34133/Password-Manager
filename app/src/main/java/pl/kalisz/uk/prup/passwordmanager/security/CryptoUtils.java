package pl.kalisz.uk.prup.passwordmanager.security;

import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptoUtils {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "MySecretAppKeyAlias";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // Standardowa długość IV dla GCM (12 bajtów)
    private static final int GCM_TAG_LENGTH = 128; // Długość tagu uwierzytelniającego w bitach

    // Metoda pomocnicza: Pobiera istniejący klucz lub tworzy nowy w Android Keystore
    private static SecretKey getOrCreateSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);

        // Jeśli klucz już istnieje, zwróć go
        if (keyStore.containsAlias(KEY_ALIAS)) {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            return secretKeyEntry.getSecretKey();
        }

        // Jeśli nie istnieje, wygeneruj nowy bezpieczny klucz AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        android.security.keystore.KeyGenParameterSpec keyGenParameterSpec = new android.security.keystore.KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        return keyGenerator.generateKey();
    }

    /**
     * SZYFROWANIE HASŁA
     * @param plainText Oryginalne hasło w czystym tekście
     * @return Zaszyfrowany String (zawiera IV oraz CipherText połączone dwukropkiem i zakodowane w Base64)
     */
    public static String encrypt(String plainText) {
        try {
            SecretKey secretKey = getOrCreateSecretKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] iv = cipher.getIV();
            byte[] encryptionBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Kodujemy IV oraz zaszyfrowany tekst do Base64, aby były bezpiecznymi Stringami
            String strIv = Base64.encodeToString(iv, Base64.DEFAULT);
            String strCipherText = Base64.encodeToString(encryptionBytes, Base64.DEFAULT);

            // Łączymy je w jeden String za pomocą separatora ":"
            return strIv + ":" + strCipherText;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // W produkcyjnej aplikacji warto rzucić własny wyjątek lub obsłużyć błąd
        }
    }

    /**
     * ODSZYFROWYWANIE HASŁA
     * @param encryptedData String zwrócony wcześniej przez metodę encrypt()
     * @return Oryginalne hasło w czystym tekście
     */
    public static String decrypt(String encryptedData) {
        try {
            if (encryptedData == null || !encryptedData.contains(":")) {
                return null;
            }

            // Rozdzielamy String na część zawierającą IV oraz część z zaszyfrowanym tekstem
            String[] parts = encryptedData.split(":");
            byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
            byte[] cipherText = Base64.decode(parts[1], Base64.DEFAULT);

            SecretKey secretKey = getOrCreateSecretKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // Do odszyfrowania w trybie GCM musimy przekazać ten sam IV, który posłużył do zaszyfrowania
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decryptedBytes = cipher.doFinal(cipherText);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

/*
// 1. Szyfrowanie
String mojeHaslo = "SuperTajneHaslo123!";
String zaszyfrowanyString = CryptoUtils.encrypt(mojeHaslo);

// Zmienna 'zaszyfrowanyString' będzie wyglądać mniej więcej tak:
// "dBjftJeZf...==:dXg5Z3M...==" (dwa Stringi Base64 rozdzielone dwukropkiem)
Log.d("Crypto", "Zaszyfrowany: " + zaszyfrowanyString);

// 2. Odszyfrowanie
String odszyfrowaneHaslo = CryptoUtils.decrypt(zaszyfrowanyString);
Log.d("Crypto", "Odszyfrowany: " + odszyfrowaneHaslo); // Wypisze: "SuperTajneHaslo123!"
*/

/*
AES/GCM/NoPadding: To algorytm szyfrowania symetrycznego rekomendowany przez Google dla systemu Android. Zapewnia nie tylko poufność, ale i integralność danych (nikt nie zmodyfikuje zaszyfrowanej paczki bez uszkodzenia tagu GCM).

Android Keystore: Klucz szyfrujący (MySecretAppKeyAlias) rodzi się w bezpiecznej, odizolowanej przestrzeni systemu Android. Twój kod aplikacji nigdy nie widzi samego klucza jako tablicy bajtów – system daje Ci jedynie "referencję" do niego, a operacje kryptograficzne są delegowane do bezpiecznego kontenera systemu operacyjnego.
 */