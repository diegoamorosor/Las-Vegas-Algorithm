package las.vegas.laberinto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Laberinto_LasVegas {

    private int width;
    private int height;
    private char[][] laberinto;
    private Punto inicio;
    private Punto fin;

    public Laberinto_LasVegas(int width, int height) {
        this.width = width;
        this.height = height;
        this.laberinto = new char[height][width];
    }

    public char[][] generarLaberinto() {
        inicializaLaberinto();
        Random random = new Random();

        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int endX = random.nextInt(width);
        int endY = random.nextInt(height);

        inicio = new Punto(startX, startY);
        fin = new Punto(endX, endY);

        boolean success = generarLaberinto_LasVegas(startX, startY, endX, endY, random);

        if (!success) {
            return generarLaberinto(); 
        }

        laberinto[inicio.y][inicio.x] = 'S';
        laberinto[fin.y][fin.x] = 'M';

        return laberinto;
    }

    private boolean generarLaberinto_LasVegas(int startX, int startY, int endX, int endY, Random random) {
        List<Punto> path = new ArrayList<>();
        path.add(new Punto(startX, startY));

        while (!path.isEmpty()) {
            Punto current = path.get(path.size() - 1);
            int x = current.x;
            int y = current.y;

            List<Punto> vecinos = getVecinos(x, y);
            if (vecinos.isEmpty()) {
                if (x == endX && y == endY) {
                    return true; 
                }
                path.remove(path.size() - 1);
                continue;
            }

            Punto next = vecinos.get(random.nextInt(vecinos.size()));
            int nx = next.x;
            int ny = next.y;

            laberinto[ny][nx] = '.';
            laberinto[y + (ny - y) / 2][x + (nx - x) / 2] = '.';
            path.add(next);
        }

        return false; 
    }

    private void inicializaLaberinto() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                laberinto[i][j] = '#';
            }
        }
    }

    private List<Punto> getVecinos(int x, int y) {
        List<Punto> neighbors = new ArrayList<>();

        if (x >= 2 && laberinto[y][x - 2] == '#') {
            neighbors.add(new Punto(x - 2, y));
        }
        if (x < width - 2 && laberinto[y][x + 2] == '#') {
            neighbors.add(new Punto(x + 2, y));
        }
        if (y >= 2 && laberinto[y - 2][x] == '#') {
            neighbors.add(new Punto(x, y - 2));
        }
        if (y < height - 2 && laberinto[y + 2][x] == '#') {
            neighbors.add(new Punto(x, y + 2));
        }

        return neighbors;
    }

    private static class Punto {
        int x;
        int y;

        public Punto(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public String printLaberintoOriginal() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : laberinto) {
            for (char cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String printLaberintoSolucion() {
        boolean[][] visited = new boolean[height][width];
        Camino(inicio.x, inicio.y, visited);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (laberinto[i][j] == 'S' || laberinto[i][j] == 'M' || visited[i][j]) {
                    sb.append("\u001B[32m").append(laberinto[i][j]).append("\u001B[0m ");
                } else {
                    sb.append(laberinto[i][j]).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean Camino(int x, int y, boolean[][] visited) {
        if (x < 0 || x >= width || y < 0 || y >= height || laberinto[y][x] == '#' || visited[y][x]) {
            return false;
        }

        visited[y][x] = true;

        if (laberinto[y][x] == 'M') {
            return true;
        }

        if (Camino(x - 1, y, visited)) return true; // Move Der
        if (Camino(x + 1, y, visited)) return true; // Move right
        if (Camino(x, y - 1, visited)) return true; // Move up
        if (Camino(x, y + 1, visited)) return true; // Move down

        return false;
    }

   
}
