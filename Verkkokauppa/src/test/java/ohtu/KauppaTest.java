package ohtu;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import ohtu.verkkokauppa.*;

public class KauppaTest {

    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;

    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(42);
        varasto = mock(Varasto.class);
    }

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {

        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // sitten testattava kauppa
        Kauppa k = new Kauppa(varasto, pankki, viite);

        // tehdään ostokset
        k.aloitaAsiointi();
        k.lisaaKoriin(1); // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), anyInt());
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }

    @Test
    public void ostetaanVarastostaLoytyvaTuote() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "karjalanpiirakka", 2));

        Kauppa k = new Kauppa(varasto, pankki, viite);

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("Heikki", "9876");

        verify(pankki).tilisiirto("Heikki", 42, "9876", "33333-44455", 2);
    }

    @Test
    public void ostetaanKaksiVarastostaLoytyvaaTuotetta() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "mysli", 6));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "jugurtti", 4));

        Kauppa k = new Kauppa(varasto, pankki, viite);

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("Heikki", "9876");

        verify(pankki).tilisiirto("Heikki", 42, "9876", "33333-44455", 10);
    }

    @Test
    public void ostetaanKaksiVarastostaLoytyvaaSamaaTuotetta() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "patteri", 10));

        Kauppa k = new Kauppa(varasto, pankki, viite);

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(1);
        k.tilimaksu("Heikki", "9876");

        verify(pankki).tilisiirto("Heikki", 42, "9876", "33333-44455", 20);
    }

    @Test
    public void ostetaanVarastostaLoytyvaTuoteJaVarastostaLoppunutTuote() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(0);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juusto", 3));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "voi", 5));

        Kauppa k = new Kauppa(varasto, pankki, viite);

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("Heikki", "9876");

        verify(pankki).tilisiirto("Heikki", 42, "9876", "33333-44455", 3);
    }

    @Test
    public void lisataanTuoteOstoskoriinJaPoistetaanSeSielta() {
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juusto", 3));

        Kauppa k = new Kauppa(varasto, pankki, viite);

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("Heikki", "9876");

        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.poistaKorista(1);
        k.tilimaksu("Heikki", "9876");

        verify(viite, times(2)).uusi();
        verify(pankki).tilisiirto("Heikki", 42, "9876", "33333-44455", 0);

    }
}