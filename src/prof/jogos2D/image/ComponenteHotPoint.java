package prof.jogos2D.image;

import java.awt.Point;

/**
 * Classe que representa um componente que possui um hot point, isto é, um ponto de interesse.
 * Este ponto pode ser uma indicação de onde está o "chão" do componente,
 * onde é para simular um tiro, etc
 */
public class ComponenteHotPoint extends ComponenteDecorador {

	// distâncias em x e y do hot point em relação ao centro da imagem 
	private int offsetX, offsetY;
	
	/** Cria um componente que decora outro com um hot point
	 * @param dec componente a decorar
	 * @param offsetX distância do hot point em relação ao centro da imagem no eixo dos x
	 * @param offsetY distância do hot point em relação ao centro da imagem no eixo dos y
	 */
	public ComponenteHotPoint( ComponenteVisual dec, int offsetX, int offsetY ) {
		super( dec );
		this.offsetX = offsetX;
		this.offsetY = offsetY;		
	}
	
	public Point getHotPoint( ) {
		Point p = getDecorado().getPosicaoCentro();
		return new Point( p.x + offsetX, p.y + offsetY );
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public ComponenteHotPoint clone() {
		return new ComponenteHotPoint( getDecorado().clone(), offsetX, offsetY );
	}
}
