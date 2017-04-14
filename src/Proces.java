import java.util.ArrayList;
import java.util.Random;

public class Proces {

    int pamiecWirtualna;
    int pamiecFizyczna;
    int odwolania;
    ArrayList<Integer> listaOdwolan = new ArrayList<Integer>();
    Random r = new Random();
    
    Proces(int wirtualna, int fizyczna, int odwolania, ArrayList<Integer> listaOdwolan) {
	this.listaOdwolan = listaOdwolan;
	pamiecWirtualna = wirtualna;
	pamiecFizyczna = fizyczna;
	this.odwolania = odwolania;
	generujOdwolania(this.listaOdwolan);
    }

    void generujOdwolania(ArrayList<Integer> listaOdwolan) {
	int odwolanie = r.nextInt(pamiecWirtualna);
	listaOdwolan.add(odwolanie);
	int poprzednieOdwolanie = listaOdwolan.get(0);
	int noweOdwolanie = r.nextInt(pamiecWirtualna);
	for (int i = 0; i < odwolania - 1; i++) {
	    while (Math.abs(poprzednieOdwolanie - noweOdwolanie) > Algorytmy.lokalnosc)
		noweOdwolanie = r.nextInt(pamiecWirtualna);
	    listaOdwolan.add(noweOdwolanie);
	    poprzednieOdwolanie = noweOdwolanie;
	    noweOdwolanie = r.nextInt(pamiecWirtualna);
	}
    }
    
    public String toString() {
	return listaOdwolan.toString();
    }
}