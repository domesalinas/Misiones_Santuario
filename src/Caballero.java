
public class Caballero {

    // Atributos
    private String id;
    private String nombre;
    private String rango;
    private String constelacion;
    private int nivelPoder;
    private String mision;
    private int dificultad;
    private double recompensa;

    // Constructor
    public Caballero(String id, String nombre, String rango,
                     String constelacion, int nivelPoder,
                     String mision, int dificultad,
                     double recompensa) {

        setId(id);
        setNombre(nombre);
        setRango(rango);
        setConstelacion(constelacion);
        setNivelPoder(nivelPoder);
        setMision(mision);
        setDificultad(dificultad);
        setRecompensa(recompensa);
    }

    // Validar campos de texto
    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " no puede estar vacío."
            );
        }
    }

    // Validar ID
    public void setId(String id) {
        validarTexto(id, "ID");
        this.id = id.trim();
    }

    // Validar nombre
    public void setNombre(String nombre) {
        validarTexto(nombre, "nombre");
        this.nombre = nombre.trim();
    }

    // Validar rango
    public void setRango(String rango) {
        validarTexto(rango, "rango");

        String valor = rango.trim();

        if (!valor.equalsIgnoreCase("Bronce") &&
                !valor.equalsIgnoreCase("Plata") &&
                !valor.equalsIgnoreCase("Oro")) {
            throw new IllegalArgumentException(
                    "El rango debe ser Bronce, Plata u Oro."
            );
        }

        this.rango = valor;
    }

    // Validar constelación
    public void setConstelacion(String constelacion) {
        validarTexto(constelacion, "constelación");
        this.constelacion = constelacion.trim();
    }

    // Validar nivel de poder
    public void setNivelPoder(int nivelPoder) {
        if (nivelPoder < 1 || nivelPoder > 10) {
            throw new IllegalArgumentException(
                    "El nivel de poder debe estar entre 1 y 10."
            );
        }

        this.nivelPoder = nivelPoder;
    }

    // Validar misión
    public void setMision(String mision) {
        validarTexto(mision, "misión");
        this.mision = mision.trim();
    }

    // Validar dificultad
    public void setDificultad(int dificultad) {
        if (dificultad < 1 || dificultad > 5) {
            throw new IllegalArgumentException(
                    "La dificultad debe estar entre 1 y 5."
            );
        }

        this.dificultad = dificultad;
    }

    // Validar recompensa
    public void setRecompensa(double recompensa) {
        if (!Double.isFinite(recompensa) || recompensa < 0) {
            throw new IllegalArgumentException(
                    "La recompensa debe ser un valor positivo o cero."
            );
        }

        this.recompensa = recompensa;
    }

    // Calcular aporte al Fondo del Santuario
    public double calcularAporteSantuario() {
        return recompensa * 0.10;
    }

    // Calcular impuesto del Reino de Atena
    public double calcularImpuestoAtena() {

        if (recompensa <= 100000) {
            return 0;
        } else if (recompensa <= 200000) {
            return (recompensa - 100000) * 0.12;
        } else if (recompensa <= 400000) {
            return (recompensa - 200000) * 0.25;
        } else {
            return (recompensa - 400000) * 0.35;
        }
    }

    // Calcular recompensa neta
    public double calcularRecompensaNeta() {
        return recompensa
                - calcularAporteSantuario()
                - calcularImpuestoAtena();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRango() {
        return rango;
    }

    public String getConstelacion() {
        return constelacion;
    }

    public int getNivelPoder() {
        return nivelPoder;
    }

    public String getMision() {
        return mision;
    }

    public int getDificultad() {
        return dificultad;
    }

    public double getRecompensa() {
        return recompensa;
    }

    // Mostrar información del caballero
    @Override
    public String toString() {
        return "Caballero{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rango='" + rango + '\'' +
                ", constelacion='" + constelacion + '\'' +
                ", nivelPoder=" + nivelPoder +
                ", mision='" + mision + '\'' +
                ", dificultad=" + dificultad +
                ", recompensa=" + recompensa +
                '}';
    }
}
