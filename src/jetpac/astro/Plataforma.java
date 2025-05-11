package jetpac.astro;

import java.awt.Rectangle;

import jetpac.mundo.WorldElementDefault;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

/**
 * Esta classe é responsável pelas plataformas do jogo
 * 
 */
public class Plataforma extends WorldElementDefault {

	public static enum LocalToque {
		NONE, TOP, RIGHT, BOTTOM, LEFT
	}

	/**
	 * cria uma plataforma
	 * 
	 * @param img imagem da plataforma
	 */
	public Plataforma(ComponenteVisual img) {
		super(img);
	}

	/**
	 * Testa se um rectangulo bate na plataforma. retorna um vetor com a direção de
	 * ricochete. Se a direção for (0,0) é porque não há embate. Se for (0,y) é
	 * porque bateu em cima (y negativo) ou em baixo (y positivo).
	 * Se for (x,0) é porque bateu no lado esquerdo (x negativo) ou do lado direito
	 * (x positivo)
	 * 
	 * @param recto o rectântuglo a testar se bate na plataforma
	 * @return um vetor com a direção de ricochete
	 */
	public Vector2D hitV(Rectangle recto) {
		Rectangle r = getBounds();
		Rectangle inter = r.intersection(recto);
		if (inter.isEmpty())
			return new Vector2D(0, 0);
		// topo
		if (inter.height <= 6 && inter.y == r.y) {
			return new Vector2D(0, -inter.height);
		}
		// bateu na parte inferior
		else if (inter.height <= 6 && inter.y == recto.y) {
			return new Vector2D(0, inter.height);
		}
		// bateu no lado esquerdo
		else if (inter.x == r.x) {
			return new Vector2D(-inter.width, 0);
		}
		// bateu no lado direito
		else {
			return new Vector2D(inter.width, 0);
		}
	}

	/**
	 * atualiza a plataforma updates the platform
	 */
	public void update() {
		// neste caso não faz nada
	}
}
