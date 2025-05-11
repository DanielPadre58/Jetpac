package prof.jogos2D.elemento;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que implementa o comportamento b�sico de um elemento gr�fico
 * 
 * @author F. S�rgio Barbosa
 */
public abstract class ElementoGraficoDefault implements ElementoGrafico {

	private ComponenteVisual imagem;
	
	/** Cria e inicializa o elemento gr�fico
	 * 
	 * @param image imagem associado ao elemento gr�fico
	 */
	public ElementoGraficoDefault( ComponenteVisual image) {
		setImage( image );
	}
	
	@Override
	public void desenhar(Graphics2D g) {
		imagem.desenhar( g );
	}

	@Override
	public ComponenteVisual getImage() {
		return imagem;
	}

	@Override
	public void setImage(ComponenteVisual image) {
		imagem = image;
	}

	@Override
	public void setPosicao(Point p) {
		imagem.setPosicao( p );
	}

	@Override
	public Point getPosicao() {
		return imagem.getPosicao();
	}
	
	@Override
	public Point getPosicaoCentro() {
		return imagem.getPosicaoCentro();
	}
	
	@Override
	public void setPosicaoCentro(Point p) {
		Rectangle r = getBounds();
		setPosicao( new Point( p.x - r.width/2, p.y - r.height/2 ) );		
	}
	
	@Override
	public Rectangle getBounds() {
		return imagem.getBounds();
	}
}
