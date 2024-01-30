package Semaphores;

import java.util.concurrent.Semaphore;

public class Almacen {
    private int capacidad;
    private Producto[] almacenado;
    private int nDatos;
    private int aInsertar;
    private int aExtraer;

    private Semaphore almac; // Semáforo para gestionar el espacio disponible en el almacén
    private Semaphore mutex; // Semáforo para garantizar la exclusión mutua

    public Almacen(int capacidad) {
        this.capacidad = capacidad;
        this.almacenado = new Producto[capacidad];
        this.almac = new Semaphore(0); // Inicializado con 0, bloqueando extracciones si el almacén está vacío
        this.mutex = new Semaphore(1); // Inicializado con 1, indicando exclusión mutua
    }

    public void almacenar(Producto producto) throws InterruptedException {
        almac.release(); // Incrementamos el semáforo antes de almacenar para indicar espacio disponible
        mutex.acquire(); // Esperamos a ser los únicos tocando la sección crítica

        // Sección crítica
        almacenado[aInsertar] = producto;
        nDatos++;
        aInsertar = (aInsertar + 1) % capacidad;

        mutex.release(); // Enviamos una señal de que hemos terminado con la sección crítica
    }

    public Producto extraer() throws InterruptedException {
        almac.acquire(); // Esperamos a que haya al menos un elemento en el almacén
        mutex.acquire(); // Esperamos a ser los únicos tocando la sección crítica

        // Sección crítica
        Producto result = almacenado[aExtraer];
        almacenado[aExtraer] = null;
        nDatos--;
        aExtraer = (aExtraer + 1) % capacidad;

        mutex.release(); // Enviamos una señal de que hemos terminado con la sección crítica

        return result;
    }
}