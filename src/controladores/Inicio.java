package controladores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Inicio {
    private static int ballX = 345; // Posición inicial de la bola
    private static int ballY = 525; // Cambiar a 525 para que empiece desde debajo de los ladrillos
    private static int ballXSpeed = 3; // Velocidad de la bola en X
    private static int ballYSpeed = -3; // Velocidad de la bola en Y
    private static int paddleX = 310; // Posición de la barra inferior
    private static int paddleYTop = 50; // Posición de la barra superior
    private static final int PADDLE_WIDTH = 150; // Ancho de las barras
    private static final int PADDLE_SPEED = 20; // Velocidad de las barras

    // Matriz para los ladrillos
    private static final int MAX_BRICKS = 50; // Número máximo de ladrillos
    private static int currentBrickCount = 30; // Contador de ladrillos actuales
    private static boolean[] bricks = new boolean[MAX_BRICKS]; // Inicializar la matriz de ladrillos (máx 50)
    private static int lives = 2; // Contador de vidas
    private static boolean isBallLaunched = false; // Controla si la bola ha sido lanzada
    private static int score = 0; // Variable para el puntaje

    private static final int BRICK_WIDTH = 80; // Ancho para los ladrillos
    private static final int BRICK_HEIGHT = 30; // Alto para los ladrillos
    private static final int BRICK_SPACING = 5; // Espacio entre ladrillos
    private static final int BALL_DIAMETER = 30; // Diámetro de la bola
    private static final int GAME_TIME_LIMIT = 300; // Límite de tiempo en segundos
    private static int timeRemaining = GAME_TIME_LIMIT; // Tiempo restante en segundos

    public static void main(String[] args) {
        // Crear la pantalla de inicio
        JFrame startFrame = new JFrame("Pantalla de Inicio");
        startFrame.setSize(400, 200);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLocationRelativeTo(null);
        
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new FlowLayout());

        // Menú desplegable para seleccionar el nivel
        String[] niveles = {"Básico", "Medio"};
        JComboBox<String> nivelComboBox = new JComboBox<>(niveles);
        nivelComboBox.setSelectedItem("Básico"); // Selección por defecto
        
        // Botón para iniciar la partida
        JButton startButton = new JButton("Iniciar Partida");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose(); // Cerrar la pantalla de inicio
                iniciarJuego(nivelComboBox.getSelectedItem().toString()); // Iniciar el juego con el nivel seleccionado
            }
        });

        startPanel.add(nivelComboBox);
        startPanel.add(startButton);
        startFrame.add(startPanel);
        
        startFrame.setVisible(true); // Mostrar la pantalla de inicio
    }

    private static void iniciarJuego(String nivel) {
        // Inicializar la matriz de ladrillos
        for (int i = 0; i < currentBrickCount; i++) {
            bricks[i] = true; // Todos los ladrillos están presentes al inicio
        }

        // Crear un JFrame básico para iniciar la aplicación
        JFrame frame = new JFrame("Brick Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Sin bordes

        // Hacer que la ventana sea de pantalla completa
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame); // Establecer en pantalla completa
        } else {
            frame.setSize(800, 600); // Tamaño por defecto si no se soporta pantalla completa
        }

        // Crear un panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un panel para contener los elementos gráficos del juego
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Fondo
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Barra inferior del jugador
                g.setColor(Color.GREEN);
                g.fillRect(paddleX, getHeight() - 50, PADDLE_WIDTH, 10); // Barra inferior

                // Barra superior del jugador
                g.fillRect(paddleX, paddleYTop, PADDLE_WIDTH, 10); // Barra superior

                // Pelota
                g.setColor(Color.RED);
                g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER); // Usar el nuevo diámetro de la bola

                // Ladrillos en dos filas
                g.setColor(Color.WHITE);
                int startX = (getWidth() - (BRICK_WIDTH * 5 + BRICK_SPACING * 4)) / 2 + 20; // Centrar los ladrillos
                for (int i = 0; i < currentBrickCount; i++) {
                    if (bricks[i]) { // Solo dibujar si el ladrillo está presente
                        int row = i / 5; // Determinar la fila (5 ladrillos por fila)
                        int column = i % 5; // Determinar la columna
                        int brickX = startX + column * (BRICK_WIDTH + BRICK_SPACING); // Posición en X
                        int brickY = 100 + row * (BRICK_HEIGHT + BRICK_SPACING); // Bajar los ladrillos
                        g.fillRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        g.setColor(Color.BLACK);
                        g.drawRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                        g.setColor(Color.WHITE);
                    }
                }

                // Mostrar vidas
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Serif", Font.BOLD, 20));
                g.drawString("Vidas: " + lives, getWidth() - 150, 30); // Ajustar la posición

                // Mostrar puntaje en la esquina superior izquierda
                g.setColor(Color.CYAN);
                g.setFont(new Font("Serif", Font.BOLD, 20));
                g.drawString("Score: " + score, 20, 30); // Coordenadas ajustadas

                // Mostrar tiempo restante en segundos
                g.setColor(Color.MAGENTA);
                g.drawString("Tiempo: " + timeRemaining + "s", getWidth() - 300, 30); // Mostrar tiempo en segundos
            }
        };

        // Agregar botón de cerrar
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(40, 30)); // Tamaño del botón
        closeButton.setFocusable(false);
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Cerrar el juego
            }
        });

        // Crear un panel para el botón de cerrar
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.add(closeButton);

        // Agregar los paneles al panel principal
        mainPanel.add(closePanel, BorderLayout.NORTH); // Panel de cerrar en la parte superior
        mainPanel.add(gamePanel, BorderLayout.CENTER); // Panel del juego en el centro

        // Timer para el bucle de juego
        Timer timer = new Timer(1000, new ActionListener() { // Cambiar a 1000 ms para contar cada segundo
            @Override
            public void actionPerformed(ActionEvent e) {
                // Decrementar el tiempo restante
                if (timeRemaining > 0) {
                    timeRemaining--; // Decrementar el tiempo en 1 segundo
                } else {
                    // Si se acaba el tiempo
                    JOptionPane.showMessageDialog(frame, "¡Tiempo agotado! Tu puntaje final es: " + score);
                    System.exit(0); // Terminar el juego
                }
            }
        });

        // Iniciar el timer
        timer.start();

        // Timer para añadir ladrillos cada 15 segundos
        Timer brickTimer = new Timer(15000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Comprobar si se pueden añadir más ladrillos
                if (currentBrickCount < MAX_BRICKS) { // Limitar a un máximo de 50 ladrillos
                    for (int i = currentBrickCount; i < currentBrickCount + 5 && i < MAX_BRICKS; i++) {
                        bricks[i] = true; // Añadir ladrillos
                    }
                    currentBrickCount += 5; // Incrementar el contador de ladrillos actuales
                }
            }
        });

        // Iniciar el timer de ladrillos
        brickTimer.start();

        // Agregar KeyListener para mover las barras y lanzar la bola
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // Mover ambas barras a la izquierda
                    if (paddleX > 0) {
                        paddleX -= PADDLE_SPEED; // Usar la nueva velocidad
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // Mover ambas barras a la derecha
                    if (paddleX < frame.getWidth() - PADDLE_WIDTH) {
                        paddleX += PADDLE_SPEED; // Usar la nueva velocidad
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Lanzar la bola
                    if (!isBallLaunched) {
                        isBallLaunched = true; // Lanzar la bola
                    }
                }
            }
        });

        // Timer para la lógica del juego
        Timer gameTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar la posición de la bola solo si ha sido lanzada
                if (isBallLaunched) {
                    ballX += ballXSpeed;
                    ballY += ballYSpeed;

                    // Colisiones con los bordes
                    if (ballX <= 0 || ballX >= frame.getWidth() - BALL_DIAMETER) {
                        ballXSpeed = -ballXSpeed; // Rebotar en los bordes laterales
                    }

                    // Comprobar si la bola toca la parte superior (barra superior)
                    if (ballY <= paddleYTop + 10 && ballX + BALL_DIAMETER > paddleX && ballX < paddleX + PADDLE_WIDTH) {
                        // Si toca la barra superior, se considera que ha perdido
                        lives--; // Restar una vida
                        // Reiniciar la bola justo debajo de los ladrillos
                        ballX = frame.getWidth() / 2 - BALL_DIAMETER / 2; // Centrar la bola
                        ballY = 525; // Reiniciar la posición vertical justo debajo de los ladrillos
                        ballYSpeed = -3; // Dirección hacia arriba
                        isBallLaunched = false; // La bola no ha sido lanzada

                        // Comprobar si se han perdido todas las vidas
                        if (lives <= 0) {
                            JOptionPane.showMessageDialog(frame, "¡Juego terminado! No te quedan vidas.");
                            System.exit(0); // Terminar el juego
                        }
                    }

                    // Comprobar colisiones con la barra inferior
                    if (ballY + BALL_DIAMETER >= gamePanel.getHeight() - 50 && ballX + BALL_DIAMETER > paddleX && ballX < paddleX + PADDLE_WIDTH) {
                        // Colisión con la barra inferior
                        ballY = gamePanel.getHeight() - 50 - BALL_DIAMETER; // Colocar la bola justo encima de la barra
                        ballYSpeed = -ballYSpeed; // Rebotar en la barra
                    }

                    boolean hitBrick = false; // Variable para verificar si se ha golpeado un ladrillo
                    // Comprobar colisiones con los ladrillos
                    for (int i = 0; i < currentBrickCount; i++) {
                        if (bricks[i]) { // Si el ladrillo está presente
                            int row = i / 5; // Determinar la fila (5 ladrillos por fila)
                            int column = i % 5; // Determinar la columna
                            int brickX = (gamePanel.getWidth() - (BRICK_WIDTH * 5 + BRICK_SPACING * 4)) / 2 + column * (BRICK_WIDTH + BRICK_SPACING);
                            int brickY = 100 + row * (BRICK_HEIGHT + BRICK_SPACING); // Ajustar la posición vertical

                            // Comprobar colisiones
                            if (ballX + BALL_DIAMETER > brickX && ballX < brickX + BRICK_WIDTH && ballY + BALL_DIAMETER > brickY && ballY < brickY + BRICK_HEIGHT) {
                                // Romper el ladrillo
                                bricks[i] = false; // Romper el ladrillo
                                ballYSpeed = -ballYSpeed; // Rebotar la bola
                                score += 15; // Aumentar el puntaje a 15 por ladrillo

                                // Limitar el puntaje máximo a 500
                                if (score > 500) {
                                    score = 500; // Establecer el puntaje máximo
                                }

                                hitBrick = true; // Marcar que se ha golpeado un ladrillo
                                break; // Salir del bucle para evitar romper múltiples ladrillos
                            }
                        }
                    }

                    // Verificar si se han roto todos los ladrillos
                    boolean allBricksBroken = true; // Variable para verificar si todos los ladrillos están rotos
                    for (int i = 0; i < currentBrickCount; i++) {
                        if (bricks[i]) { // Si el ladrillo está presente
                            allBricksBroken = false; // Si algún ladrillo está presente
                            break; // Salir del bucle
                        }
                    }

                    // Verificar si se han roto todos los ladrillos
                    if (allBricksBroken) {
                        JOptionPane.showMessageDialog(frame, "¡Enhorabuena, has ganado! Tu puntaje final es: " + score);
                        System.exit(0); // Terminar el juego
                    }

                    // Comprobar si la bola cae sin golpear la barra
                    if (ballY > gamePanel.getHeight()) {
                        lives--; // Restar una vida
                        // Reiniciar la bola justo debajo de los ladrillos
                        ballX = frame.getWidth() / 2 - BALL_DIAMETER / 2; // Centrar la bola
                        ballY = 525; // Reiniciar la posición vertical justo debajo de los ladrillos
                        ballYSpeed = -3; // Dirección hacia arriba
                        isBallLaunched = false; // La bola no ha sido lanzada

                        // Comprobar si se han perdido todas las vidas
                        if (lives <= 0) {
                            JOptionPane.showMessageDialog(frame, "¡Juego terminado! No te quedan vidas.");
                            System.exit(0); // Terminar el juego
                        }
                    }
                }

                // Redibujar el panel
                gamePanel.repaint();
            }
        });

        // Iniciar el timer del juego
        gameTimer.start();

        // Agregar el panel principal al marco
        frame.add(mainPanel);
        frame.setVisible(true); // Mostrar el marco
    }
}
