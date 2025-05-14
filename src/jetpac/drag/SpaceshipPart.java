package jetpac.drag;

import java.awt.Point;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa as partes da nave. Cada parte tem uma ordem em que deve
 * ser colocada na nave
 */
public class SpaceshipPart extends DraggableElementDefault {

	private int partIdx; // ordem da parte da nave

	/**
	 * Cria uma parte da nave
	 *
	 * @param partIdx o índice desta parte
	 * @param pos     posição onde aparece a parte
	 * @param img     imagem da parte
	 */
	public SpaceshipPart(int partIdx, Point pos, ComponenteVisual img) {
		this.imagem = img;
		imagem.setPosicao(pos);
		this.partIdx = partIdx;
	}

    @Override
	public boolean isDraggable() {
		return super.isDraggable() && partIdx == getWorld().getMainSpaceship().getNextPartDue();
	}

	/**
	 * indica o número da parte da nave
	 *
	 * @return o número da parte da nave
	 */
	public int getPartIdx() {
		return partIdx;
	}

    @Override
	public void delivered() {
		super.delivered();
		getWorld().getMainSpaceship().addParte(this);
	}
}
