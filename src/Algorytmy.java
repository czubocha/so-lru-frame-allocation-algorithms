import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Algorytmy {
    static int lokalnosc;
    private int iloscProcesow;
    static ArrayList<Proces> listaProcesow = new ArrayList<Proces>();
    Random r = new Random();
    int sumaBledow;
    int maxWirtualna;
    int maxOdwolania;
    int pamiecFizyczna;
    boolean czyDodacRamke;
    boolean czyUsunacRamke;
    int z;
    int roznicaRamek;

    public Algorytmy() {
	Scanner scanner = new Scanner(System.in);

	System.out.println("Podaj max pamiêæ wirtualn¹ :");
	maxWirtualna = scanner.nextInt();
	System.out.println("Podaj pamiêæ fizyczn¹ :");
	pamiecFizyczna = scanner.nextInt();
	System.out.println("Podaj max iloœæ odwo³añ :");
	maxOdwolania = scanner.nextInt();
	System.out.println("Podaj przedzia³ lokalnoœci :");
	lokalnosc = scanner.nextInt();
	System.out.println("Podaj iloœæ procesów :");
	iloscProcesow = scanner.nextInt();

	scanner.close();

	generujProcesy();
    }

    void generujProcesy() {
	for (int i = 0; i < iloscProcesow; i++) {
	    int wirtualna = r.nextInt(maxWirtualna) + 1;
	    int odwolania = r.nextInt(maxOdwolania) + 1;
	    listaProcesow.add(new Proces(wirtualna, 0, odwolania, new ArrayList<Integer>()));
	}
    }

    void wyswietlProcesy() {
	for (Proces x : listaProcesow)
	    System.out.println(x);
    }

    public void wypelnijRamki(ArrayList<Integer> listaRamek, Proces p) {
	for (int i = 0; (listaRamek.size() < p.pamiecFizyczna) && (i < p.listaOdwolan.size()); i++) {
	    if (!listaRamek.contains(p.listaOdwolan.get(i)))
		listaRamek.add(p.listaOdwolan.get(i));
	}
    }

    public int LRU(Proces p) {

	int bledy = 0;
	ArrayList<Integer> listaRamek = new ArrayList<Integer>();
	wypelnijRamki(listaRamek, p);

	for (int i = 0; i < p.listaOdwolan.size(); i++) {
	    // System.out.print("Ramki: " + listaRamek);
	    // System.out.print(" Nastêpne odwo³anie: " + listaOdwolan.get(i));
	    if (!listaRamek.contains(p.listaOdwolan.get(i))) {
		bledy++;
		// System.out.print(" Usuniêto: " + listaRamek.get(0));
		listaRamek.remove(0);
	    } else {
		// System.out.print(" Usuwamy bez b³êdu: " + listaOdwolan.get(i));
		listaRamek.remove(p.listaOdwolan.get(i));
	    }
	    listaRamek.add(p.listaOdwolan.get(i));
	    // System.out.println();
	}
	// System.out.print("Ramki : " + listaRamek + "\n");
	// System.out.println("\nB³êdy :" + bledy);
	sumaBledow += bledy;
	return bledy;
    }

    int LRUrowny() {
	sumaBledow = 0;
	int ramki = pamiecFizyczna / iloscProcesow;
	int reszta = pamiecFizyczna % iloscProcesow;

	for (int i = 0; i < iloscProcesow; i++) {
	    if (reszta == 0) {
		listaProcesow.get(i).pamiecFizyczna = ramki;
	    } else {
		listaProcesow.get(i).pamiecFizyczna = ramki + 1;
		reszta--;
	    }
	}

	for (int i = 0; i < iloscProcesow; i++) {
	    // System.out.println("Proces " + (i + 1));
	    int bledy = LRU(listaProcesow.get(i));
	    // System.out.println("Iloœæ ramek procesu: " + listaProcesow.get(i).pamiecFizyczna);
	    // System.out.println("Iloœæ stron procesu: " + listaProcesow.get(i).pamiecWirtualna);
	    // System.out.println("Iloœæ odwo³añ procesu: " + listaProcesow.get(i).odwolania);
	    // System.out.println("Odwo³ania procesu: " + listaProcesow.get(i).listaOdwolan);
	    // System.out.println("B³êdy: " + bledy + "\n");
	}
	System.out.println("\nSuma b³êdów rozk³adu równego :" + sumaBledow);
	return sumaBledow;
    }

    int LRUproporcjonalny() {
	sumaBledow = 0;
	int wszystkieOdwolania = 0;
	int ileLacznieRamek = 0;

	for (int j = 0; j < iloscProcesow; j++) {
	    wszystkieOdwolania += listaProcesow.get(j).odwolania;
	}

	for (int i = 0; i < iloscProcesow; i++) {
	    int odwolania = listaProcesow.get(i).odwolania;
	    double pomocnicza = (double) odwolania / wszystkieOdwolania;
	    double ramkii = pamiecFizyczna * pomocnicza;
	    int ramki = (int) Math.round(ramkii);
	    listaProcesow.get(i).pamiecFizyczna = (ramki == 0) ? 1 : ramki;
	    ileLacznieRamek += ramki;
	}

	while (ileLacznieRamek > pamiecFizyczna) {
	    Proces pomocniczy = listaProcesow.get(0);
	    for (int k = 1; k < iloscProcesow; k++)
		if (listaProcesow.get(k).pamiecFizyczna > listaProcesow.get(k - 1).pamiecFizyczna)
		    pomocniczy = listaProcesow.get(k);
	    pomocniczy.pamiecFizyczna--;
	    ileLacznieRamek--;
	}
	while (ileLacznieRamek < pamiecFizyczna) {
	    Proces pomocniczy = listaProcesow.get(0);
	    for (int k = 1; k < iloscProcesow; k++)
		if (listaProcesow.get(k).pamiecFizyczna < listaProcesow.get(k - 1).pamiecFizyczna)
		    pomocniczy = listaProcesow.get(k);
	    pomocniczy.pamiecFizyczna++;
	    ileLacznieRamek++;
	}

	for (int i = 0; i < iloscProcesow; i++) {
	    LRU(listaProcesow.get(i));
	    // System.out.println("Iloœæ ramek procesu:" + (i + 1) + ",   " + listaProcesow.get(i).pamiecFizyczna);
	    // System.out.println("Iloœæ stron procesu:" + (i + 1) + ",   " + listaProcesow.get(i).pamiecWirtualna);
	    // System.out.println("Iloœæ odwo³añ procesu:" + (i + 1) + ",   " + listaProcesow.get(i).odwolania);
	    // System.out.println("Odwo³ania procesu:" + (i + 1) + ",   " + listaProcesow.get(i).listaOdwolan);
	    // System.out.println();
	}
	System.out.println("Suma b³êdów rozk³adu proporcjonalnego :" + sumaBledow);
	return sumaBledow;
    }

    public int LRU2(Proces p) {
	int bledy = 0;
	roznicaRamek = 0;
	ArrayList<Integer> listaRamek = new ArrayList<Integer>();
	wypelnijRamki(listaRamek, p);
	for (int i = 0; i < p.listaOdwolan.size(); i++) {
	    if (!listaRamek.contains(p.listaOdwolan.get(i))) {
		if ((z != iloscProcesow - 1) && (bledy >= i / 2)
			&& (listaProcesow.get(z + 1).pamiecFizyczna - roznicaRamek > 1 && i > p.pamiecFizyczna)) {
		    p.pamiecFizyczna++;
		    roznicaRamek++;
		    // System.out.println("zwiekszam ilosc ramek procesowi " + z + " (przekroczenie progu)");
		} else {
		    bledy++;
		    listaRamek.remove(0);
		}
	    } else {
		if ((z != iloscProcesow - 1) && (bledy < i / 2) && listaRamek.size() > 1 && i > p.pamiecFizyczna) {
		    p.pamiecFizyczna--;
		    roznicaRamek--;
		    // System.out.println("zmniejszam ilosc ramek procesowi " + z + " (nie osiagniecie progu)");
		    listaRamek.remove(p.listaOdwolan.get(i));
		    listaRamek.remove(0);
		} else
		    listaRamek.remove(p.listaOdwolan.get(i));
	    }
	    listaRamek.add(p.listaOdwolan.get(i));
	}
	sumaBledow += bledy;
	return bledy;
    }

    int LRUsterCzestBledow() {

	sumaBledow = 0;
	int ramki = pamiecFizyczna / iloscProcesow;
	int reszta = pamiecFizyczna % iloscProcesow;

	for (int i = 0; i < iloscProcesow; i++) {
	    if (reszta == 0) {
		listaProcesow.get(i).pamiecFizyczna = ramki;
	    } else {
		listaProcesow.get(i).pamiecFizyczna = ramki + 1;
		reszta--;
	    }
	}

	for (z = 0; z < iloscProcesow; z++) {
	    listaProcesow.get(z).pamiecFizyczna -= roznicaRamek;
	    int bledy = LRU2(listaProcesow.get(z));
//	    System.out.println("Iloœæ ramek procesu: " + listaProcesow.get(z).pamiecFizyczna);
//	    System.out.println("Iloœæ stron procesu: " + listaProcesow.get(z).pamiecWirtualna);
//	    System.out.println("Iloœæ odwo³añ procesu: " + listaProcesow.get(z).odwolania);
//	    System.out.println("Odwo³ania procesu: " + listaProcesow.get(z).listaOdwolan);
//	    System.out.println("B³êdy: " + bledy + "\n");
	}

	System.out.println("Suma b³êdów przy przydziale sterowaniem czêstoœci¹ b³êdu strony :" + sumaBledow);
	return sumaBledow;
    }

    public static void main(String[] args) {

	Algorytmy test = new Algorytmy();
	// test.wyswietlProcesy();
	test.LRUrowny();
	test.LRUproporcjonalny();
	test.LRUsterCzestBledow();
    }
}
