import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class BandejaAlertas implements Iterable<Alerta> {
    private ArrayList<Alerta> listaAlertas;

    public BandejaAlertas() {
        this.listaAlertas = new ArrayList<Alerta>();
    }

    void agregar(Alerta alerta) {
        this.listaAlertas.add(alerta);
        // sort llama al compareTo de Alerta para decidir el orden
        Collections.sort(this.listaAlertas);
    }

    public int cantidad() {
        return this.listaAlertas.size();
    }

    Alerta retirarPrimera() {
        if (this.listaAlertas.isEmpty()) {
            throw new IllegalArgumentException("No hay alertas pendientes.");
        }
        return this.listaAlertas.remove(0);
    }

    @Override
    public Iterator<Alerta> iterator() {
        return Collections.unmodifiableList(this.listaAlertas).iterator();
    }
}
