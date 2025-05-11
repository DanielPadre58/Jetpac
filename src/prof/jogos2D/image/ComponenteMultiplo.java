package prof.jogos2D.image;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class ComponenteMultiplo extends ComponenteSimples {

	private Vector<ComponenteVisual> imagens = new Vector<ComponenteVisual>();
	private Point posicao = new Point();
	private Rectangle bounds = new Rectangle();
	
	public ComponenteMultiplo() {
		
	}

	public void addComponente( Point p, ComponenteVisual img ){
		img.setPosicao(p);
		imagens.add( img );
		
		//if( bounds == null ){
		//	bounds = new Rectangle( p.x, p.y, img.getComprimento(), img.getAltura() );
		//}
		//else 
			recalculaBounds();
		/*
		if( bounds.x > p.x ){
			bounds.width +=  (bounds.x - p.x);
			bounds.x = p.x;
		}
		if( bounds.getMaxX() < p.x + img.getComprimento() ){
			bounds.width += (p.x + img.getComprimento() - bounds.getMaxX() );			
		}
		
		if( bounds.y > p.y ){
			bounds.height +=  (bounds.y - p.y);
			bounds.y = p.y;
		}
		if( bounds.getMaxY() < p.y + img.getAltura() ){
			bounds.height += (p.y + img.getAltura() - bounds.getMaxY() );			
		}
		*/
	}
	
	public void removeComponente( ComponenteVisual img ){
		imagens.remove( img );
		recalculaBounds();
	}
	
	public void desenhar(java.awt.Graphics g) {
		for( ComponenteVisual c : imagens )
			c.desenhar( g );
	}
	
	@Override
	public void setPosicao(Point p) {
		int dx = p.x - posicao.x;
		int dy = p.y - posicao.y;
		
		posicao = p;
		for( ComponenteVisual c : imagens ){
			Point pi = c.getPosicao();
			c.setPosicao( new Point(pi.x + dx, pi.y + dy) );
		}
		bounds.translate(dx, dy);
		//System.out.println( "setPosicao  " + bounds );

	}
	
	@Override
	public Point getPosicao() {
		return posicao;
	}
	
	@Override
	public void setAngulo(double angulo) {
		super.setAngulo( angulo );
		for( ComponenteVisual c : imagens ){
			c.setAngulo(angulo);
		}
	}
	
	@Override
	public Rectangle getBounds() {
		if( bounds == null )
			return new Rectangle();
		return bounds;
	}
	
	private void recalculaBounds(){
		Point left = new Point(posicao.x, posicao.y);
		Point right = new Point(posicao.x, posicao.y);
		
		for( ComponenteVisual i : imagens ){
			if( left.x > i.getPosicao().x )
				left.x = i.getPosicao().x;
			else if( right.x < i.getPosicao().x + i.getComprimento() )
				right.x = i.getPosicao().x + i.getComprimento();
			if( left.y > i.getPosicao().y )
				left.y = i.getPosicao().y;
			else if( right.y < i.getPosicao().y  + i.getAltura() )
				right.y = i.getPosicao().y + i.getAltura();
		}
		
		bounds = new Rectangle( left.x, left.y, right.x-left.x, right.y-left.y); 
	}
	
	@Override
	public double getAngulo() {
		return imagens.get(0).getAngulo();
	}
	
	@Override
	public int getAltura() {
		return getBounds().height;
	}
	
	@Override
	public int getComprimento() {
		return getBounds().width;
	}
	
	@Override
	public Point getPosicaoCentro() {
		Rectangle r = getBounds();
		return new Point( (int)r.getCenterX(), (int)r.getCenterY() );
	}
	
	@Override
	public Image getSprite() {
		return imagens.get(0).getSprite();
	}
	
	@Override
	public void setPausa(boolean pausa) {
		super.setPausa(pausa);
		for( ComponenteVisual c : imagens )
			c.setPausa( pausa );
	}
	
	@Override
	public void rodar(double angulo) {
		super.rodar(angulo);
		setAngulo( super.getAngulo() );
	}
	
	@Override
	public int numCiclosFeitos() {
		return 0;
	}
	
	@Override
	public boolean eCiclico() {
		for( ComponenteVisual c : imagens )
			if( !c.eCiclico() )
				return false;
		return true;
	}
	
	@Override
	public void inverter() {
		for( ComponenteVisual c : imagens )
			c.inverter();
	}
	
	@Override
	public void setCiclico(boolean ciclico) {
		for( ComponenteVisual c : imagens )
			c.setCiclico(ciclico);
	}
	
	@Override
	public void setPosicaoCentro(Point p) {
		Point c = getPosicaoCentro();
		int dx = p.x - c.x;
		int dy = p.y - c.y;
		setPosicao( new Point(posicao.x+dx, posicao.y + dy) );
	}
	
	public ComponenteMultiplo clone() {
		ComponenteMultiplo cm = new ComponenteMultiplo();
		for( ComponenteVisual cv : imagens )
			cm.addComponente( cv.getPosicao() != null? (Point)cv.getPosicao().clone(): null, cv.clone() );
		return cm;
	}
}
