package Semaphores;

public class Main {

    public static void main(String[] args) {
        Almacen almacen = new Almacen (1000);

        Thread productor = new Thread(() -> {

            //Almacenamos 300 items de cada tipo
            try {
                for (int i = 0; i < 300; i++) {
                    almacen.almacenar(new Producto(0, "Ratón", 20));
                    System.out.println("Almacenado Ratón");
                    almacen.almacenar(new Producto(1, "Teclado", 20));
                    System.out.println("Almacenado Teclado");
                    almacen.almacenar(new Producto(2, "USB", 20));
                    System.out.println("Almacenado USB");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumidor = new Thread(() -> {

            //Extraemos 1000 items para se bloquee el hilo al queda vacío
            try {
                for (int i = 0; i < 1000; i++) {
                    Producto producto = almacen.extraer();
                    System.out.println("Consumido: " + producto.getNombre());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        productor.start();
        consumidor.start();
    }
}
