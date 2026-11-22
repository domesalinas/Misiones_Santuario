import java.util.ArrayList;

public class GestorCaballeros {

    private ArrayList<Caballero> listaCaballeros;

    public GestorCaballeros() {
        listaCaballeros = new ArrayList<>();
    }

    public boolean registrarCaballero(Caballero nuevo) {
        for (int i = 0; i < listaCaballeros.size(); i++) {
            if (listaCaballeros.get(i).getId().equalsIgnoreCase(nuevo.getId())) {
                return false;
            }
        }
        listaCaballeros.add(nuevo);
        return true;
    }

    public Caballero buscarPorId(String id) {
        for (int i = 0; i < listaCaballeros.size(); i++) {
            if (listaCaballeros.get(i).getId().equalsIgnoreCase(id)) {
                return listaCaballeros.get(i);
            }
        }
        return null;
    }

    public boolean modificarCaballero(String id, String nombre, String rango,
                                      String constelacion, int nivelPoder,
                                      String mision, int dificultad, double recompensa) {
        Caballero c = buscarPorId(id);
        if (c != null) {
            c.setNombre(nombre);
            c.setRango(rango);
            c.setConstelacion(constelacion);
            c.setNivelPoder(nivelPoder);
            c.setMision(mision);
            c.setDificultad(dificultad);
            c.setRecompensa(recompensa);
            return true;
        }
        return false;
    }

    public ArrayList<Caballero> listarCaballeros() {
        return listaCaballeros;
    }
}