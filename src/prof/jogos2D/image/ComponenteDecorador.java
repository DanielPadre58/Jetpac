package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class ComponenteDecorador implements ComponenteVisual {

	private ComponenteVisual decorado;

	public ComponenteDecorador( ComponenteVisual dec ) {
		decorado = dec;
	}

	@Override
	public void desenhar(Graphics g) {
		decorado.desenhar( g );
	}

	@Override
	public int getAltura() {
		return decorado.getAltura();
	}
	
	@Override
	public double getAngulo() {
		return decorado.getAngulo();
	}
	
	@Override
	public Rectangle getBounds() {
		return decorado.getBounds();
	}
	
	@Override
	public int getComprimento() {
		return decorado.getComprimento();
	}
	
	@Override
	public Point getPosicao() {
		return decorado.getPosicao();
	}
	
	@Override
	public Point getPosicaoCentro() {
		return decorado.getPosicaoCentro();
	}
	
	@Override
	public Image getSprite() {
		return decorado.getSprite();
	}
	
	@Override
	public void rodar(double angulo) {
		decorado.rodar(angulo);
	}
	
	@Override
	public int numCiclosFeitos() {
		return decorado.numCiclosFeitos();
	}
	
	@Override
	public void setAngulo(double angulo) {
		decorado.setAngulo(angulo);
	}
	
	@Override
	public void setPosicao(Point p) {
		decorado.setPosicao(p);
	}
	
	@Override
	public void setPosicaoCentro(Point p) {
		decorado.setPosicaoCentro(p);
	}
	
	@Override
	public void setSprite(Image sprite) {
		decorado.setSprite(sprite);
	}
	
	@Override
	public void setPausa(boolean pausa) {
		decorado.setPausa( pausa );		
	}

	@Override
	public boolean estaPausa() {
		return decorado.estaPausa();
	}
	
	@Override
	public void reset() {
		decorado.reset();
	}
	
	@Override
	public void inverter() {
		decorado.inverter();		
	}
	
	@Override
	public boolean eCiclico() {
		return decorado.eCiclico();
	}
	
	@Override
	public void setCiclico(boolean ciclico) {
		decorado.setCiclico(ciclico);
	}
	
	/**
	 * retorna o componente que est� a ser decorado.
	 * @return o componente que est� a ser decorado.
	 */
	protected ComponenteVisual getDecorado(){
		return decorado;
	}
	
	/**
	 * clona um elemento decorador.
	 * O elemento decorado é partilhado entre os clones
	 */
	public ComponenteDecorador clone( ){
		ComponenteDecorador cd = new ComponenteDecorador( decorado.clone() );
		return cd;
	}
}
