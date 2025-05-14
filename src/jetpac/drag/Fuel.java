package jetpac.drag;

import java.awt.Point;

import prof.jogos2D.image.ComponenteVisual;

/**
 * classe que representa o combustível a ser carregado para a nave
 */
public class Fuel extends DraggableElementDefault {

	public Fuel(Point p, ComponenteVisual imagem) {
		this.imagem = imagem;
		imagem.setPosicao(p);
    }

	public void delivered() {
		super.delivered();
		getWorld().getFuelGen().fuelDelivered();
	}
}
