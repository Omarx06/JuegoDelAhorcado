import java.util.*;

public class Ahorcado {
    private static HashMap<String, Integer> jugadores = new HashMap<>();
    private static HashSet<Character> letrasUsadas = new HashSet<>();
    private static String fraseActual;
    private static char[] fraseOculta;
    private static List<String> listaJugadores = new ArrayList<>(); // Para mantener el orden de los jugadores
    private static int jugadorActual = 0; // Índice del jugador actual

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Configuración del juego
        System.out.println("Bienvenido al juego del Ahorcado");
        System.out.print("¿Cuántos puntos necesitas para ganar? ");
        int puntosParaGanar = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        System.out.print("¿Cuántos jugadores participarán (2-4)? ");
        int numJugadores = scanner.nextInt();
        scanner.nextLine();

        // Registrar jugadores
        for (int i = 1; i <= numJugadores; i++) {
            System.out.print("Ingresa el nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            jugadores.put(nombre, 0); // Inicializar puntos en 0
            listaJugadores.add(nombre); // Añadir a la lista de jugadores
        }

        // Bucle del juego por rondas
        while (!hayGanador(puntosParaGanar)) {
            iniciarRonda();
            jugarRonda(scanner);
        }

        System.out.println("¡El juego ha terminado!");
    }

    // Función para iniciar una nueva ronda
    private static void iniciarRonda() {
        letrasUsadas.clear(); // Limpiar las letras usadas de la ronda anterior
        fraseActual = Frases.obtenerFraseAleatoria(); // Obtener frase aleatoria desde la clase Frases
        fraseOculta = ocultarFrase(fraseActual); // Convertir la frase en guiones bajos
        System.out.println("Nueva frase: " + new String(fraseOculta));
    }

    // Función para ocultar la frase con guiones
    private static char[] ocultarFrase(String frase) {
        char[] oculta = new char[frase.length()];
        for (int i = 0; i < frase.length(); i++) {
            if (frase.charAt(i) == ' ') {
                oculta[i] = ' '; // Mantener los espacios
            } else {
                oculta[i] = '_'; // Reemplazar las letras por guiones
            }
        }
        return oculta;
    }

    // Función para jugar una ronda
    private static void jugarRonda(Scanner scanner) {
        boolean fraseCompletada = false;

        while (!fraseCompletada) {
            // Usamos el índice jugadorActual para saber a quién le toca
            String jugador = listaJugadores.get(jugadorActual);
            System.out.println("Turno de " + jugador);
            boolean sigueTurno = true;  // Variable para seguir el turno del jugador

            while (sigueTurno) {
                System.out.print("Adivina una letra: ");
                char letra = scanner.nextLine().toLowerCase().charAt(0);

                // Verificar si la letra ya fue usada
                if (letrasUsadas.contains(letra)) {
                    System.out.println("Esa letra ya fue usada.");
                    sumarPuntos(jugador, -3); // Penalización por repetir letra
                } else {
                    letrasUsadas.add(letra); // Marcar la letra como usada
                    if (fraseActual.toLowerCase().contains(String.valueOf(letra))) {
                        revelarLetra(letra); // Revelar la letra en la frase oculta
                        int apariciones = contarApariciones(fraseActual, letra);
                        sumarPuntos(jugador, apariciones * 3); // 3 puntos por cada aparición

                        System.out.println("Frase actual: " + new String(fraseOculta));

                        // Verificar si la frase ya fue adivinada
                        if (new String(fraseOculta).equalsIgnoreCase(fraseActual)) {
                            System.out.println("¡" + jugador + " ha adivinado la frase!");
                            sumarPuntos(jugador, 5); // 5 puntos por ganar la ronda
                            fraseCompletada = true;
                            break;
                        }
                    } else {
                        System.out.println("La letra no está en la frase.");
                        sumarPuntos(jugador, -1); // Penalización por letra incorrecta
                        sigueTurno = false; // Pasar al siguiente jugador
                    }
                }
            }

            // Cambiar al siguiente jugador si no se ha completado la frase
            if (!fraseCompletada) {
                jugadorActual = (jugadorActual + 1) % listaJugadores.size(); // Ciclo de turnos
            }

            // Si la frase fue completada, salir del bucle
            if (fraseCompletada) {
                break;
            }
        }
    }

    // Función para sumar puntos a un jugador
    private static void sumarPuntos(String jugador, int puntos) {
        jugadores.put(jugador, jugadores.get(jugador) + puntos);
        System.out.println("Puntos de " + jugador + ": " + jugadores.get(jugador));
    }

    // Función para contar cuántas veces aparece una letra en la frase
    private static int contarApariciones(String frase, char letra) {
        int count = 0;
        for (char c : frase.toLowerCase().toCharArray()) {
            if (c == letra) {
                count++;
            }
        }
        return count;
    }

    // Función para revelar la letra en la frase oculta
    private static void revelarLetra(char letra) {
        for (int i = 0; i < fraseActual.length(); i++) {
            if (fraseActual.toLowerCase().charAt(i) == letra) {
                fraseOculta[i] = fraseActual.charAt(i);
            }
        }
    }

    // Función para verificar si hay un ganador
    private static boolean hayGanador(int puntosParaGanar) {
        for (int puntos : jugadores.values()) {
            if (puntos >= puntosParaGanar) {
                return true;
            }
        }
        return false;
    }
}
