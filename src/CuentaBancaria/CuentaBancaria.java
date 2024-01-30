package CuentaBancaria;

public class CuentaBancaria {
    private double saldo;

    public CuentaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public synchronized void ingreso(double cantidad) {
        saldo += cantidad;
        System.out.println("Cerrado hilo ingresar");
    }

    public synchronized void retirar(double cantidad) {
        if(cantidad > saldo){
            System.out.println("No se ha podido retirar una cantidad mayor que el saldo");
        } else {
            saldo -= cantidad;
            System.out.println("Cerrado hilo retirar");
        }
    }

    public synchronized double consultaSaldo() {
        return saldo;
    }
}

class Incrementador extends Thread {
    private CuentaBancaria cuenta;

    public Incrementador(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            cuenta.ingreso(100);
        }
    }
}

class Decrementador extends Thread {
    private CuentaBancaria cuenta;

    public Decrementador(CuentaBancaria cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            cuenta.retirar(100);
        }
    }
}

class ProgramaPrincipal {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria(0);

        Incrementador incrementador = new Incrementador(cuenta);
        Decrementador decrementador = new Decrementador(cuenta);

        incrementador.start();
        decrementador.start();
        try {
            incrementador.join();
            decrementador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Saldo final: " + cuenta.consultaSaldo());
        }
}
