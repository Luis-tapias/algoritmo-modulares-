import java.util.Scanner;

public class TallerAlgoritmosModulares {

    static Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== TALLER DE ALGORITMOS MODULARES =====");
            System.out.println("1. Sistema de inventario de cafeteria");
            System.out.println("2. Sistema de control academico");
            System.out.println("3. Sistema de control de produccion");
            System.out.println("0. Salir");
            opcion = leerEntero("Seleccione una opcion: ", 0, 3);

            switch (opcion) {
                case 1:
                    ejecutarInventario();
                    break;
                case 2:
                    ejecutarControlAcademico();
                    break;
                case 3:
                    ejecutarProduccion();
                    break;
                case 0:
                    System.out.println("Gracias por utilizar el programa.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ==================== FUNCIONES GENERALES ====================

    static int leerEntero(String mensaje, int minimo, int maximo) {
        int valor;
        while (true) {
            System.out.print(mensaje);
            if (entrada.hasNextInt()) {
                valor = entrada.nextInt();
                entrada.nextLine();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: ingrese un numero entre " + minimo + " y " + maximo + ".");
            } else {
                System.out.println("Error: debe ingresar un numero entero.");
                entrada.nextLine();
            }
        }
    }

    static double leerDecimal(String mensaje, double minimo, double maximo) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            if (entrada.hasNextDouble()) {
                valor = entrada.nextDouble();
                entrada.nextLine();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: el valor debe estar entre " + minimo + " y " + maximo + ".");
            } else {
                System.out.println("Error: debe ingresar un numero.");
                entrada.nextLine();
            }
        }
    }

    static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = entrada.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El dato no puede estar vacio.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    // ==================== CASO 2: INVENTARIO ====================

    static void ejecutarInventario() {
        final int MAX_PRODUCTOS = 20;
        String[] codigos = new String[MAX_PRODUCTOS];
        String[] nombres = new String[MAX_PRODUCTOS];
        String[] categorias = new String[MAX_PRODUCTOS];
        double[] precios = new double[MAX_PRODUCTOS];
        int[] cantidades = new int[MAX_PRODUCTOS];
        int totalProductos = 0;
        double totalVendido = 0;
        int opcion;

        do {
            System.out.println("\n--- INVENTARIO DE CAFETERIA ---");
            System.out.println("1. Registrar producto");
            System.out.println("2. Realizar venta");
            System.out.println("3. Actualizar inventario");
            System.out.println("4. Mostrar productos agotados");
            System.out.println("5. Mostrar inventario completo");
            System.out.println("6. Consultar total vendido del dia");
            System.out.println("0. Volver al menu principal");
            opcion = leerEntero("Seleccione: ", 0, 6);

            switch (opcion) {
                case 1:
                    totalProductos = registrarProducto(codigos, nombres, categorias, precios,
                            cantidades, totalProductos, MAX_PRODUCTOS);
                    break;
                case 2:
                    totalVendido = realizarVenta(codigos, nombres, precios, cantidades,
                            totalProductos, totalVendido);
                    break;
                case 3:
                    actualizarInventario(codigos, nombres, cantidades, totalProductos);
                    break;
                case 4:
                    mostrarAgotados(nombres, cantidades, totalProductos);
                    break;
                case 5:
                    mostrarInventario(codigos, nombres, categorias, precios, cantidades, totalProductos);
                    break;
                case 6:
                    System.out.printf("Total vendido durante esta ejecucion: $%.2f%n", totalVendido);
                    break;
                case 0:
                    System.out.println("Regresando al menu principal...");
                    break;
            }
        } while (opcion != 0);
    }

    static int buscarProducto(String[] codigos, int total, String codigo) {
        for (int i = 0; i < total; i++) {
            if (codigos[i].equalsIgnoreCase(codigo)) {
                return i;
            }
        }
        return -1;
    }

    static int registrarProducto(String[] codigos, String[] nombres, String[] categorias,
                                 double[] precios, int[] cantidades, int total, int limite) {
        if (total == limite) {
            System.out.println("No hay cupos disponibles. El limite es de 20 productos.");
            return total;
        }

        String codigo = leerTexto("Codigo: ");
        if (buscarProducto(codigos, total, codigo) != -1) {
            System.out.println("Ya existe un producto con ese codigo.");
            return total;
        }

        codigos[total] = codigo;
        nombres[total] = leerTexto("Nombre: ");
        System.out.println("Categorias: 1. Bebidas  2. Panaderia  3. Snacks");
        int categoria = leerEntero("Seleccione categoria: ", 1, 3);
        categorias[total] = nombreCategoria(categoria);
        precios[total] = leerDecimal("Precio unitario: ", 0.01, 100000000);
        cantidades[total] = leerEntero("Cantidad inicial: ", 0, 1000000);
        System.out.println("Producto registrado correctamente.");
        return total + 1;
    }

    static String nombreCategoria(int opcion) {
        switch (opcion) {
            case 1: return "Bebidas";
            case 2: return "Panaderia";
            default: return "Snacks";
        }
    }

    static double realizarVenta(String[] codigos, String[] nombres, double[] precios,
                                int[] cantidades, int total, double acumulado) {
        if (total == 0) {
            System.out.println("Primero debe registrar productos.");
            return acumulado;
        }
        String codigo = leerTexto("Codigo del producto que desea vender: ");
        int posicion = buscarProducto(codigos, total, codigo);
        if (posicion == -1) {
            System.out.println("Producto no encontrado.");
            return acumulado;
        }
        if (cantidades[posicion] == 0) {
            System.out.println("No se puede vender: el producto esta agotado.");
            return acumulado;
        }
        int unidades = leerEntero("Cantidad a vender: ", 1, 1000000);
        if (unidades > cantidades[posicion]) {
            System.out.println("Venta rechazada. Stock disponible: " + cantidades[posicion]);
            return acumulado;
        }
        double subtotal = unidades * precios[posicion];
        cantidades[posicion] -= unidades;
        acumulado += subtotal;
        System.out.printf("Venta realizada: %d unidad(es) de %s. Total: $%.2f%n",
                unidades, nombres[posicion], subtotal);
        return acumulado;
    }

    static void actualizarInventario(String[] codigos, String[] nombres, int[] cantidades, int total) {
        String codigo = leerTexto("Codigo del producto: ");
        int posicion = buscarProducto(codigos, total, codigo);
        if (posicion == -1) {
            System.out.println("Producto no encontrado.");
            return;
        }
        int ingreso = leerEntero("Unidades que desea agregar: ", 1, 1000000);
        cantidades[posicion] += ingreso;
        System.out.println("Inventario actualizado. Nuevo stock de " + nombres[posicion] + ": "
                + cantidades[posicion]);
    }

    static void mostrarAgotados(String[] nombres, int[] cantidades, int total) {
        boolean hayAgotados = false;
        System.out.println("\nPRODUCTOS AGOTADOS");
        for (int i = 0; i < total; i++) {
            if (cantidades[i] == 0) {
                System.out.println("- " + nombres[i]);
                hayAgotados = true;
            }
        }
        if (!hayAgotados) System.out.println("No hay productos agotados.");
    }

    static void mostrarInventario(String[] codigos, String[] nombres, String[] categorias,
                                  double[] precios, int[] cantidades, int total) {
        if (total == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println("\nCODIGO | NOMBRE | CATEGORIA | PRECIO | STOCK");
        for (int i = 0; i < total; i++) {
            System.out.printf("%s | %s | %s | $%.2f | %d%n",
                    codigos[i], nombres[i], categorias[i], precios[i], cantidades[i]);
        }
    }

    // ==================== CASO 3: CONTROL ACADEMICO ====================

    static void ejecutarControlAcademico() {
        final int MAX_ESTUDIANTES = 25;
        String[] codigos = new String[MAX_ESTUDIANTES];
        String[] nombres = new String[MAX_ESTUDIANTES];
        double[][] notas = new double[MAX_ESTUDIANTES][3];
        double[] asistencias = new double[MAX_ESTUDIANTES];
        int totalEstudiantes = 0;
        int opcion;

        do {
            System.out.println("\n--- CONTROL ACADEMICO ---");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Mostrar resultados de estudiantes");
            System.out.println("3. Consultar promedio general");
            System.out.println("4. Identificar mejor y peor promedio");
            System.out.println("0. Volver al menu principal");
            opcion = leerEntero("Seleccione: ", 0, 4);

            switch (opcion) {
                case 1:
                    totalEstudiantes = registrarEstudiante(codigos, nombres, notas, asistencias,
                            totalEstudiantes, MAX_ESTUDIANTES);
                    break;
                case 2:
                    mostrarResultados(codigos, nombres, notas, asistencias, totalEstudiantes);
                    break;
                case 3:
                    mostrarPromedioGeneral(notas, totalEstudiantes);
                    break;
                case 4:
                    mostrarMejorPeor(codigos, nombres, notas, totalEstudiantes);
                    break;
                case 0:
                    System.out.println("Regresando al menu principal...");
                    break;
            }
        } while (opcion != 0);
    }

    static int registrarEstudiante(String[] codigos, String[] nombres, double[][] notas,
                                   double[] asistencias, int total, int limite) {
        if (total == limite) {
            System.out.println("Se alcanzo el limite de 25 estudiantes.");
            return total;
        }
        String codigo = leerTexto("Codigo del estudiante: ");
        for (int i = 0; i < total; i++) {
            if (codigos[i].equalsIgnoreCase(codigo)) {
                System.out.println("Ese codigo ya esta registrado.");
                return total;
            }
        }
        codigos[total] = codigo;
        nombres[total] = leerTexto("Nombre completo: ");
        for (int j = 0; j < 3; j++) {
            notas[total][j] = leerDecimal("Nota parcial " + (j + 1) + " (0.0 a 5.0): ", 0.0, 5.0);
        }
        asistencias[total] = leerDecimal("Porcentaje de asistencia (0 a 100): ", 0, 100);
        System.out.println("Estudiante registrado correctamente.");
        return total + 1;
    }

    static double calcularDefinitiva(double[][] notas, int posicion) {
        // Se usa promedio simple y se redondea a un decimal.
        // Ejemplo: 2.96 se convierte en 3.0.
        double promedio = (notas[posicion][0] + notas[posicion][1] + notas[posicion][2]) / 3.0;
        return Math.round(promedio * 10.0) / 10.0;
    }

    static String determinarEstado(double definitiva) {
        // Según la escala indicada para el taller:
        // de 0.0 a 2.9 reprueba; 3.0 habilita; de 3.1 a 5.0 aprueba.
        if (definitiva < 3.0) return "Reprueba";
        if (definitiva == 3.0) return "Habilita";
        return "Aprobado";
    }

    static void mostrarResultados(String[] codigos, String[] nombres, double[][] notas,
                                  double[] asistencias, int total) {
        if (total == 0) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        System.out.println("\nCODIGO | ESTUDIANTE | DEFINITIVA | ASISTENCIA | ESTADO");
        for (int i = 0; i < total; i++) {
            double definitiva = calcularDefinitiva(notas, i);
            System.out.printf("%s | %s | %.2f | %.1f%% | %s%n",
                    codigos[i], nombres[i], definitiva, asistencias[i], determinarEstado(definitiva));
        }
    }

    static void mostrarPromedioGeneral(double[][] notas, int total) {
        if (total == 0) {
            System.out.println("No hay datos para calcular el promedio.");
            return;
        }
        double suma = 0;
        for (int i = 0; i < total; i++) suma += calcularDefinitiva(notas, i);
        System.out.printf("Promedio general del grupo: %.2f%n", suma / total);
    }

    static void mostrarMejorPeor(String[] codigos, String[] nombres, double[][] notas, int total) {
        if (total == 0) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        int mejor = 0;
        int peor = 0;
        for (int i = 1; i < total; i++) {
            if (calcularDefinitiva(notas, i) > calcularDefinitiva(notas, mejor)) mejor = i;
            if (calcularDefinitiva(notas, i) < calcularDefinitiva(notas, peor)) peor = i;
        }
        System.out.printf("Mejor promedio: %s (%s) - %.2f%n",
                nombres[mejor], codigos[mejor], calcularDefinitiva(notas, mejor));
        System.out.printf("Peor promedio: %s (%s) - %.2f%n",
                nombres[peor], codigos[peor], calcularDefinitiva(notas, peor));
    }

    // ==================== CASO 5: CONTROL DE PRODUCCION ====================

    static void ejecutarProduccion() {
        final int MAX_OPERARIOS = 10;
        final int DIAS = 5;
        String[] codigos = new String[MAX_OPERARIOS];
        String[] nombres = new String[MAX_OPERARIOS];
        int[][] produccion = new int[MAX_OPERARIOS][DIAS];
        int totalOperarios = 0;
        int opcion;

        do {
            System.out.println("\n--- CONTROL DE PRODUCCION ---");
            System.out.println("1. Registrar operario e ingresar produccion de 5 dias");
            System.out.println("2. Mostrar produccion semanal por operario");
            System.out.println("3. Identificar mejor operario");
            System.out.println("4. Mostrar promedio general de produccion");
            System.out.println("5. Detectar dias con baja produccion");
            System.out.println("0. Volver al menu principal");
            opcion = leerEntero("Seleccione: ", 0, 5);

            switch (opcion) {
                case 1:
                    totalOperarios = registrarOperario(codigos, nombres, produccion,
                            totalOperarios, MAX_OPERARIOS, DIAS);
                    break;
                case 2:
                    mostrarProduccionSemanal(codigos, nombres, produccion, totalOperarios, DIAS);
                    break;
                case 3:
                    mostrarMejorOperario(codigos, nombres, produccion, totalOperarios, DIAS);
                    break;
                case 4:
                    mostrarPromedioProduccion(produccion, totalOperarios, DIAS);
                    break;
                case 5:
                    detectarDiasBajaProduccion(produccion, totalOperarios, DIAS);
                    break;
                case 0:
                    System.out.println("Regresando al menu principal...");
                    break;
            }
        } while (opcion != 0);
    }

    static int registrarOperario(String[] codigos, String[] nombres, int[][] produccion,
                                 int total, int limite, int dias) {
        if (total == limite) {
            System.out.println("Se alcanzo el limite de 10 operarios.");
            return total;
        }
        String codigo = leerTexto("Codigo del operario: ");
        for (int i = 0; i < total; i++) {
            if (codigos[i].equalsIgnoreCase(codigo)) {
                System.out.println("Ese codigo ya esta registrado.");
                return total;
            }
        }
        codigos[total] = codigo;
        nombres[total] = leerTexto("Nombre del operario: ");
        for (int j = 0; j < dias; j++) {
            produccion[total][j] = leerEntero("Produccion del dia " + (j + 1) + ": ", 0, 100000000);
        }
        System.out.println("Operario y produccion registrados correctamente.");
        return total + 1;
    }

    static int calcularProduccionSemanal(int[][] produccion, int posicion, int dias) {
        int suma = 0;
        for (int j = 0; j < dias; j++) suma += produccion[posicion][j];
        return suma;
    }

    static void mostrarProduccionSemanal(String[] codigos, String[] nombres, int[][] produccion,
                                         int total, int dias) {
        if (total == 0) {
            System.out.println("No hay operarios registrados.");
            return;
        }
        System.out.println("\nCODIGO | OPERARIO | PRODUCCION SEMANAL");
        for (int i = 0; i < total; i++) {
            System.out.printf("%s | %s | %d unidades%n",
                    codigos[i], nombres[i], calcularProduccionSemanal(produccion, i, dias));
        }
    }

    static void mostrarMejorOperario(String[] codigos, String[] nombres, int[][] produccion,
                                     int total, int dias) {
        if (total == 0) {
            System.out.println("No hay operarios registrados.");
            return;
        }
        int mejor = 0;
        for (int i = 1; i < total; i++) {
            if (calcularProduccionSemanal(produccion, i, dias)
                    > calcularProduccionSemanal(produccion, mejor, dias)) mejor = i;
        }
        System.out.printf("Mejor operario de la semana: %s (%s), con %d unidades.%n",
                nombres[mejor], codigos[mejor],
                calcularProduccionSemanal(produccion, mejor, dias));
    }

    static void mostrarPromedioProduccion(int[][] produccion, int total, int dias) {
        if (total == 0) {
            System.out.println("No hay datos para calcular el promedio.");
            return;
        }
        int suma = 0;
        for (int i = 0; i < total; i++) {
            suma += calcularProduccionSemanal(produccion, i, dias);
        }
        System.out.printf("Promedio general semanal por operario: %.2f unidades%n",
                (double) suma / total);
    }

    static void detectarDiasBajaProduccion(int[][] produccion, int total, int dias) {
        if (total == 0) {
            System.out.println("No hay datos para analizar.");
            return;
        }
        // Se considera baja produccion un dia cuyo promedio sea inferior al 50% del
        // promedio diario general de los cinco dias registrados.
        double sumaGeneral = 0;
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < dias; j++) sumaGeneral += produccion[i][j];
        }
        double promedioDiarioGeneral = sumaGeneral / (total * dias);
        double limiteBajo = promedioDiarioGeneral * 0.5;

        boolean encontro = false;
        for (int j = 0; j < dias; j++) {
            int sumaDia = 0;
            for (int i = 0; i < total; i++) sumaDia += produccion[i][j];
            double promedioDia = (double) sumaDia / total;
            if (promedioDia < limiteBajo) {
                System.out.printf("Dia %d con baja produccion. Promedio: %.2f unidades.%n",
                        j + 1, promedioDia);
                encontro = true;
            }
        }
        if (!encontro) System.out.println("No se detectaron dias con baja produccion.");
    }
}
