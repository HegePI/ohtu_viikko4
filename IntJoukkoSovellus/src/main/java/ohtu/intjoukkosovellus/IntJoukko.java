
package ohtu.intjoukkosovellus;

import java.io.EOFException;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
            OLETUSKASVATUS = 5; // luotava uusi taulukko on näin paljon isompi kuin vanha
    private int kasvatuskoko; // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] lukuTaulukko; // Joukon luvut säilytetään taulukon alkupäässä.
    private int alkioidenMaara; // Tyhjässä joukossa alkioiden_määrä on nolla.

    public IntJoukko() {
        this.kasvatuskoko = OLETUSKASVATUS;
        lukuTaulukko = new int[KAPASITEETTI];
        for (int i = 0; i < lukuTaulukko.length; i++) {
            lukuTaulukko[i] = 0;
        }
        alkioidenMaara = 0;
    }

    public IntJoukko(int kapasiteetti) {
        this.kasvatuskoko = OLETUSKASVATUS;
        if (kapasiteetti < 0) {
            lukuTaulukko = new int[KAPASITEETTI];
        } else {
            lukuTaulukko = new int[kapasiteetti];
        }
        for (int i = 0; i < lukuTaulukko.length; i++) {
            lukuTaulukko[i] = 0;
        }
        alkioidenMaara = 0;
    }

    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) {
            lukuTaulukko = new int[KAPASITEETTI];
        } else {
            lukuTaulukko = new int[kapasiteetti];
        }
        if (kasvatuskoko < 0) {
            this.kasvatuskoko = OLETUSKASVATUS;
        } else {
            this.kasvatuskoko = kasvatuskoko;
        }
        for (int i = 0; i < lukuTaulukko.length; i++) {
            lukuTaulukko[i] = 0;
        }
        alkioidenMaara = 0;
    }

    public boolean lisaa(int luku) {
        if (alkioidenMaara == 0) {
            lukuTaulukko[0] = luku;
            alkioidenMaara++;
            return true;
        } else if (!kuuluu(luku)) {
            lukuTaulukko[alkioidenMaara] = luku;
            alkioidenMaara++;
            tarkastaKoko();
            return true;
        }
        return false;
    }

    public boolean kuuluu(int luku) {
        for (int i = 0; i < alkioidenMaara; i++) {
            if (luku == lukuTaulukko[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean poista(int luku) {
        for (int i = 0; i < alkioidenMaara; i++) {
            if (luku == lukuTaulukko[i]) {
                alkioidenMaara--;
                siirraLukuja(i);
                return true;
            }
        }
        return false;
    }

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        IntJoukko yhdisteJoukko = new IntJoukko();
        yhdisteJoukko = a;
        for (int luku : b.toIntArray()) {
            yhdisteJoukko.lisaa(luku);
        }
        return yhdisteJoukko;
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko leikkausJoukko = new IntJoukko();
        for (int luku : a.toIntArray()) {
            if (b.kuuluu(luku)) {
                leikkausJoukko.lisaa(luku);
            }
        }
        return leikkausJoukko;
    }

    public static IntJoukko erotus(IntJoukko a, IntJoukko b) {
        IntJoukko erotusJoukko = new IntJoukko();
        for (int luku : a.toIntArray()) {
            if (!b.kuuluu(luku)) {
                erotusJoukko.lisaa(luku);
            }
        }
        return erotusJoukko;
    }

    public int mahtavuus() {
        return alkioidenMaara;
    }

    @Override
    public String toString() {
        if (alkioidenMaara == 0) {
            return "{}";
        } else {
            String lukuJoukko = "{";
            for (int i = 0; i < alkioidenMaara - 1; i++) {
                lukuJoukko += lukuTaulukko[i] + ", ";
            }
            lukuJoukko += lukuTaulukko[alkioidenMaara - 1] + "}";
            return lukuJoukko;
        }
    }

    public int[] toIntArray() {
        int[] taulu = new int[alkioidenMaara];
        for (int i = 0; i < taulu.length; i++) {
            taulu[i] = lukuTaulukko[i];
        }
        return taulu;
    }

    private void kopioiTaulukko(int[] vanha, int[] uusi) {
        for (int i = 0; i < vanha.length; i++) {
            uusi[i] = vanha[i];
        }
    }

    private void siirraLukuja(int kohta) {
        for (int j = kohta; j < alkioidenMaara; j++) {
            lukuTaulukko[j] = lukuTaulukko[j + 1];
        }
        lukuTaulukko[alkioidenMaara] = 0;
    }

    private void tarkastaKoko() {
        if (alkioidenMaara % lukuTaulukko.length == 0) {
            int[] uusiTaulukko = new int[alkioidenMaara + kasvatuskoko];
            kopioiTaulukko(lukuTaulukko, uusiTaulukko);
            lukuTaulukko = uusiTaulukko;
        }
    }
}
