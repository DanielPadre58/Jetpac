package jetpac.app;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import jetpac.astro.Astronauta;
import jetpac.mundo.Mundo;
import prof.jogos2D.util.*;

/**
 * A classe que controla todo o jogo do JetPac
 */
public class Jetpac extends JFrame {

	// elementos do jogo
	private Mundo mundo; // representa o mundo onde se joga
	private int nivel; // nível actual do jogo
	private int pontuacao; // pontuação atual
	private int vidas; // número de vidas

	// último nível suportado
	private static final int lastLevel = 8;

	// leitor do teclado
	private SKeyboard teclado;

	// Os vários elementos visuais do jogo
	private JPanel jContentPane = null;
	private JPanel gameArea = null;
	private JPanel statusPane = null;

	// imagens usadas para melhorar as animações
	private Image ecran; // o ecran onde se desenha o mundo
	private Image statusBarImg; // a barra de status

	// fontes para escrever a pontuação, nível e vidas
	private Font livesFont = new Font("Roman", Font.BOLD, 20);
	private Font levelFont = new Font("Roman", Font.BOLD, 28);

	// isto é para não dar warnings
	private static final long serialVersionUID = 1L;

	/**
	 * construtor da aplicação
	 */
	public Jetpac() {
		setTitle("JetPac by ESTertaiment");
		initialize(); // inicializar a janela do jogo
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// configurar o teclado
		teclado = new SKeyboard();

		startGame();
	}

	/**
	 * Método que começa o jogo
	 */
	private void startGame() {
		// configuração inicial
		nivel = 1;
		pontuacao = 0;
		vidas = 5;

		// jogar o nível
		playLevel();
	}

	/**
	 * Jogar um dado nível
	 */
	private void playLevel() {
		mundo = readLevel(nivel); // ler os ficheiro do nível
		if (mundo == null)
			return;

		// fazer o reset do nível, para ficar tudo reiniciado
		resetLevel();
	}

	/**
	 * inicia/reinicia um nível
	 */
	private void resetLevel() {
		// iniciar o mundo
		mundo.play();

		// Criar o atualizador que vai atualizar o jogo 30x por segundo
		Actualizador actualiza = new Actualizador();
		actualiza.start();
	}

	/**
	 * Lê as informações do nível no respectivo ficheiro.
	 * O ficheiro dos níveis está no diretório levels e tem a terminação txt.
	 *
	 * @param nivel o nível a ler
	 * @return o mundo representado neste nível
	 */
	private Mundo readLevel(int level) {
		String file = "data/levels/level" + level + ".txt"; // ficehiro onde está o nível
		String dirArt = "data/art/"; // diretório onde estão as imagens dos elementos

		// criar o leitor de mundos para ler o mundo
		WorldReader wr = new WorldReader(dirArt);
		return wr.lerMundo(file);
	}

	/**
	 * método que vai ser usado para desenhar os componentes do jogo.
	 *
	 * @param g elemento onde se vai desenhar.
	 */
	private void drawGameArea(Graphics2D g) {
		// passar para graphics2D pois este é mais avançado
		Graphics2D ge = (Graphics2D) ecran.getGraphics();

		// desenhar o mundo
		mundo.draw(ge);

		// está tudo desenhado na imagem auxiliar, desenhar essa imagem no ecrán
		g.drawImage(ecran, 0, 0, null);
	}

	/**
	 * método que vai ser usado para desenhar a barra de estados
	 *
	 * @param g elemento onde se vai desenhar.
	 */
	private void drawStatusBar(Graphics2D g) {
		// desenhar o fundo da imagem
		g.drawImage(statusBarImg, 0, 0, null);

		// desenhar a percentagem de fuel
		int perc = mundo.getFuelPercentage();
		if (perc < 60)
			g.setColor(Color.RED);
		else if (perc < 90)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.GREEN);
		g.fillRect(180, 11, perc * 2, 20);

		// desenhar as vidas, nível e pontuação
		g.setColor(Color.white);
		g.setFont(livesFont);
		g.drawString("" + vidas, 55, 25);
		g.setFont(levelFont);
		g.drawString("" + nivel, 472, 32);
		g.drawString("" + pontuacao, 618, 32);
	}

	/**
	 * método chamado a cada ciclo de processamento para atualizar os elementos do
	 * jogo. Atenção! Este método NÃO desenha nada. Usar o método drawGameArea para
	 * isso.
	 */
	private void updateGame() {
		Astronauta astro = mundo.getAstronaut();

		// ver as teclas premidas
		// subir?
		astro.setRising(teclado.estaPremida(KeyEvent.VK_Q));
		// disparar?
		astro.setShooting(teclado.estaPremida(KeyEvent.VK_A));
		// a andar? se sim para que lado?
		if (teclado.estaPremida(KeyEvent.VK_N)) {
			astro.setDirection(Astronauta.LEFT);
			astro.setWalking(true);
		} else if (teclado.estaPremida(KeyEvent.VK_M)) {
			astro.setDirection(Astronauta.RIGHT);
			astro.setWalking(true);
		} else
			astro.setWalking(false);

		if (teclado.estaPremida(KeyEvent.VK_Z))
			astro.drop();

		// actualizar mundo e ver quanto se pontuou neste ciclo
		// update world and add the score of this cicle
		pontuacao += mundo.update();
	}

	/**
	 * Classe responsável pela criação da thread que vai actualizar o mundo de x em
	 * x tempo
	 */
	class Actualizador extends Thread {
		public void run() {
			long mili = System.currentTimeMillis();
			long target = mili + ReguladorVelocidade.getIntervaloStandard();
			do {
				updateGame();
				gameArea.repaint();
				statusPane.repaint();
				// esperar 30 milisegundos o que dá umas 32 frames por segundo
				while (mili < target)
					mili = ReguladorVelocidade.tempoRelativo();
				target = mili + ReguladorVelocidade.getIntervaloStandard();
				// enquanto o mundo não estiver completo ou acabado
			} while (!mundo.isCompleted() && !mundo.isOver());
			// se o nível estiver completo passa ao próximo
			if (mundo.isCompleted()) {
				if (nivel == lastLevel) {
					opcoesFinais("Ganhou o jogo! Que deseja fazer?", "GANHOU, GANHOU, GANHOU");
				} else {
					nivel++;
					playLevel();
				}
			} else {
				// se perdeu uma vida, atualizar as vidas e recomeçar ou terminar o jogo
				vidas--;
				if (vidas == 0) {
					opcoesFinais("Game Over! Que deseja fazer?", "GAME OVER");
				} else
					resetLevel();
			}
		}
	}

	/**
	 * Apresenta as opções finais, quer tenha ganho ou perdido o jogo
	 *
	 * @param msg   a mensagem a mostrar
	 * @param title o título da janela de mensagem
	 */
	private void opcoesFinais(String msg, String title) {
		// as escolhas são: recomeçar do 1º nível ou sair
		String escolhas[] = { "Voltar ao 1º nível", "Terminar Jogo" };
		int resposta = JOptionPane.showOptionDialog(Jetpac.this, msg,
				title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				escolhas, escolhas[0]);
		switch (resposta) {
			case 0:
				startGame();
				break;
			case 1:
				System.exit(0);
		}
	}

	/**
	 * Este método inicializa a zonaJogo
	 */
	private JPanel getGameArea() {
		if (gameArea == null) {
			gameArea = new JPanel() {
				public void paintComponent(Graphics g) {
					drawGameArea((Graphics2D) g);
				}
			};
			Dimension d = new Dimension(1000, 690);
			gameArea.setPreferredSize(d);
			gameArea.setSize(d);
			gameArea.setMinimumSize(d);
			gameArea.setBackground(Color.BLACK);
		}
		return gameArea;
	}

	/**
	 * vai inicializar a aplicação
	 */
	private void initialize() {
		// criar a imagem para melhorar as animações e configurá-la para isso mesmo
		ecran = new BufferedImage(1000, 750, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D ge = (Graphics2D) ecran.getGraphics();
		ge.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ge.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		// ler a imagem para a barra de estado
		try {
			statusBarImg = ImageIO.read(new File("data/art/statusbar.gif"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Falta ficheiro data/art/statusbar.gif", "ERRO",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// características da janela
		this.setLocationRelativeTo(null);
		this.setContentPane(getJContentPane());
		this.setTitle("JetPac by ESTertainement");
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	/**
	 * métodos auxiliares para configurar a janela
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getStatusBar(), BorderLayout.NORTH);
			jContentPane.add(getGameArea(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JPanel getStatusBar() {
		if (statusPane == null) {
			statusPane = new JPanel() {
				public void paintComponent(Graphics g) {
					drawStatusBar((Graphics2D) g);
				}
			};
			Dimension d = new Dimension(statusBarImg.getWidth(null), statusBarImg.getHeight(null));
			statusPane.setPreferredSize(d);
			statusPane.setSize(d);
			statusPane.setMinimumSize(d);
		}
		return statusPane;
	}

	public static void main(String args[]) {
		Jetpac ce = new Jetpac();
		ce.setVisible(true);
	}
}
