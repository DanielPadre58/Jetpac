package jetpac.app;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import jetpac.astro.Astronauta;
import jetpac.astro.Plataforma;
import jetpac.astro.Spaceship;
import jetpac.drag.SpaceshipPart;
import jetpac.generator.EnemyGenerator;
import jetpac.generator.FuelGenerator;
import jetpac.generator.TreasureGenerator;
import jetpac.generator.TreasureInfo;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.*;

/**
 * Classe que faz a leitura dos ficheiros dos níveis
 */
public class WorldReader {

	// o diretório onde estão os ficheiros com as imagens
	private String artDir;

	// o mundo a ser criado
	private Mundo world;

	// linha a ler, para indicar onde se deu o erro
	private int linhaAtual;

	/**
	 * Cria a WorldReader com que vai ler as imagens no directório especificado
	 * 
	 * @param artDir the folder where the art files are
	 */
	public WorldReader(String artDir) {
		this.artDir = artDir;
	}

	/**
	 * Lê o mundo especificado no ficheiro indicado. Para saber o formato do
	 * ficheiro ler o ficheiro do nível 1
	 * 
	 * @param file o ficheiro com o nível.
	 * @return o mundo correspondente ao nível lido.
	 */
	public Mundo lerMundo(String file) {
		linhaAtual = 0;
		// cria um mundo novo, vazio
		world = new Mundo();

		try {
			// abrir o ficheiro do nível
			BufferedReader in = new BufferedReader(new FileReader(file));

			// ler uma linha
			String line = leProximaInformacao(in);
			while (line != null) {
				// separar o = do resto da informação da linha
				String cmd[] = line.split("=");

				String tipoInfo = cmd[0]; // tipoInfo é o comando (lado esquerdo do =)
				String data = cmd[1]; // data é a informação a ler

				// ver qual a informação e processá-la
				if (tipoInfo.startsWith("mundo"))
					LerMundoConfig(data);
				else if (tipoInfo.startsWith("nave"))
					lerNave(data, in);
				else if (tipoInfo.startsWith("astronauta"))
					lerAstronauta(data);
				else if (tipoInfo.startsWith("fuel"))
					lerFuel(data);
				else if (tipoInfo.startsWith("plataformas"))
					lerPlataformas(data, in);
				else if (tipoInfo.startsWith("inimigos"))
					lerInimigos(data);
				else if (tipoInfo.startsWith("tesouros"))
					lerTesouros(data, in);
				line = leProximaInformacao(in);
			}
		} catch (Exception e) {
			// caso tenha acontecido algo de errado ao ler o ficheiro de nível
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro na leitura do ficheiro " + file + " linha " + linhaAtual, "ERRO",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return world;
	}

	/**
	 * lê a informação sobre um ficheiro de imagem
	 * 
	 * @param info   a informação a processar.
	 * @param offset onde começar a ler a informação.
	 * @return a imagem correspondente à info.
	 * @throws IOException se algo ocorrer na leitura do ficheiro
	 */
	private ComponenteVisual lerImagem(String info[], int offset, Point p) throws IOException {
		String imgFile = artDir + info[offset];
		int nFrames = Integer.parseInt(info[offset + 1]);
		int delay = Integer.parseInt(info[offset + 2]);
		if (nFrames == 0)
			return new ComponenteSimples(p, imgFile);
		else
			return new ComponenteAnimado(p, imgFile, nFrames, delay);
	}

	/**
	 * lê a configuração do mundo
	 * 
	 * @param data a linha com a informação sobre o mundo
	 * @throws IOException
	 */
	private void LerMundoConfig(String data) throws IOException {
		String info[] = data.split(",");
		int w = Integer.parseInt(info[0]); // comprimento e altura
		int h = Integer.parseInt(info[1]);
		world.setDimensions(w, h);
		world.setBackground(lerImagem(info, 2, new Point()));
	}

	/**
	 * lê a configuração da nave
	 * 
	 * @param data linha com a informação.
	 * @param in   resto das linhas com a info.
	 * @throws IOException
	 */
	private void lerNave(String data, BufferedReader in) throws IOException {
		int nParts = Integer.parseInt(data);
		// ler a info da nave
		String info[] = leProximaInformacao(in).split(",");
		int x = Integer.parseInt(info[0]);
		int y = Integer.parseInt(info[1]);
		ComponenteVisual img = lerImagem(info, 2, new Point(x, y));
		Spaceship nave = new Spaceship(nParts, img);
		world.addSpaceship(nave);
		// ler a info das partes da nave
		for (int i = 1; i < nParts; i++) {
			info = leProximaInformacao(in).split(",");
			x = Integer.parseInt(info[0]);
			y = Integer.parseInt(info[1]);
			img = lerImagem(info, 2, new Point(x, y));
			SpaceshipPart parte = new SpaceshipPart(i, new Point(x, y), img);
			world.addSpaceshipPart(parte);
		}
	}

	/**
	 * lê a informação do astronauta
	 * 
	 * @param data linha com a informação
	 * @throws IOException
	 */
	private void lerAstronauta(String data) throws IOException {
		String info[] = data.split(",");
		int x = Integer.parseInt(info[0]);
		int y = Integer.parseInt(info[1]);
		String file = artDir + info[2];
		int dy = Integer.parseInt(info[3]);
		ComponenteMultiAnimado img = new ComponenteMultiAnimado(new Point(x, y), file, 5, 5, 4);
		Astronauta a = new Astronauta(img, new Point(x, y), dy);
		world.setAstronauta(a);
	}

	/**
	 * ler informação sobre o fuel
	 * 
	 * @param data linha com a informação
	 * @throws IOException
	 */
	private void lerFuel(String data) throws IOException {
		String info[] = data.split(",");
		int nFuels = Integer.parseInt(info[0]);
		int minTime = Integer.parseInt(info[1]);
		int maxTime = Integer.parseInt(info[2]);
		ComponenteVisual img = lerImagem(info, 3, new Point());

		// criar o gerador de fuel e associá-lo ao mundo
		FuelGenerator fg = new FuelGenerator(nFuels, minTime, maxTime, img, world);
		world.setFuelGen(fg);
	}

	/**
	 * ler informação sobre plataformas
	 * 
	 * @param data linha com a informação
	 * @param in   restantes linhas de informação
	 * @throws IOException
	 */
	private void lerPlataformas(String data, BufferedReader in) throws IOException {
		int nPlataformas = Integer.parseInt(data);
		for (int i = 0; i < nPlataformas; i++) {
			String info[] = leProximaInformacao(in).split(",");
			int x = Integer.parseInt(info[0]);
			int y = Integer.parseInt(info[1]);
			ComponenteVisual img = lerImagem(info, 2, new Point(x, y));
			Plataforma p = new Plataforma(img);
			world.addPlatform(p);
		}
		// no fim adicionar sempre o teto
		ComponenteVazio teto = new ComponenteVazio(new Rectangle(-10, -10, world.getWidth() + 20, 10));
		world.addPlatform(new Plataforma(teto));
	}

	/**
	 * ler informação sobre inimigos
	 * 
	 * @param data linha com a informação
	 * @throws IOException
	 */
	private void lerInimigos(String data) throws IOException {
		String info[] = data.split(",");

		int maxEnemys = Integer.parseInt(info[0]);
		int enemyVel = Integer.parseInt(info[1]);
		int enemyScore = Integer.parseInt(info[2]);
		ComponenteMultiAnimado img;
		String tipo = info[3];
		String file = artDir + info[4];
		int nFrames = Integer.parseInt(info[5]);
		int delay = Integer.parseInt(info[6]);
		img = new ComponenteMultiAnimado(new Point(), file, 3, nFrames, delay);

		// criar o gerador de inimigos e associá-lo ao mundo
		EnemyGenerator eg = new EnemyGenerator(tipo, maxEnemys, enemyVel, enemyScore, img, world);
		world.setEnemyGen(eg);
	}

	/**
	 * ler informação sobre tesouros
	 * 
	 * @param data linha com a informação
	 * @param in   restante informação
	 * @throws IOException
	 */
	private void lerTesouros(String data, BufferedReader in) throws IOException {
		String info[] = data.split(",");
		int nTesouros = Integer.parseInt(info[0]);
		int minTime = Integer.parseInt(info[1]);
		int maxTime = Integer.parseInt(info[2]);

		TreasureInfo[] tInfo = new TreasureInfo[nTesouros];
		TreasureGenerator tg = new TreasureGenerator(1, minTime, maxTime, tInfo, world);
		for (int i = 0; i < nTesouros; i++) {
			String tinf[] = leProximaInformacao(in).split(",");
			int prob = Integer.parseInt(tinf[0]);
			int dur = Integer.parseInt(tinf[1]);
			int pontos = Integer.parseInt(tinf[2]);
			ComponenteVisual img = lerImagem(tinf, 3, new Point());
			tInfo[i] = new TreasureInfo(prob, dur, pontos, img);
		}
		world.setTreasureGenerator(tg);
	}

	/**
	 * Lê uma linha de informação, ignorando comentários e linhas em branco
	 * 
	 * @param in leitor do ficheiro
	 * @throws IOException
	 */
	private String leProximaInformacao(BufferedReader in) throws IOException {
		String res = null;
		do {
			res = in.readLine();
			linhaAtual++;
			if (res == null)
				return null;
		} while (res.isEmpty() || res.startsWith("#"));

		// limpar os espaços
		char chs[] = new char[res.length()];
		int nchs = 0;
		for (int i = 0; i < res.length(); i++) {
			char ch = res.charAt(i);
			if (!Character.isSpaceChar(ch)) {
				chs[nchs] = ch;
				nchs++;
			}
		}
		res = String.copyValueOf(chs, 0, nchs);
		return res;
	}
}
