package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class ComponenteMultiVisual extends ComponenteSimples {

	// podia-se usar um hashmap, mas devem ser poucos visuais a serem usados 
	private ArrayList<VisualComNome> visuais = new ArrayList<VisualComNome>();
	private VisualComNome atual = null;
	
	public ComponenteMultiVisual() {
		setPosicao( new Point(0,0) );
	}

	public void addComponenteVisual( String nome, ComponenteVisual visual ) {
		VisualComNome novo = new VisualComNome(nome, visual);
		if( visuais.size() > 0 )
			visual.setPosicaoCentro( getPosicaoCentro() );
		else {
			atual = novo;
		}
		visuais.add( novo );
	}
	
	@Override
	public void setPosicao(Point p) {
		super.setPosicao(p);
		for( VisualComNome v : visuais )
			v.visual.setPosicao( p );
	}

	@Override
	public void setPosicaoCentro(Point p) {
		super.setPosicaoCentro(p);
		for( VisualComNome v : visuais )
			v.visual.setPosicaoCentro( p );
	}
	
	@Override
	public void desenhar(Graphics g) {
		atual.visual.desenhar(g);
	}
	
	public void setVisualAtual( String nome ) {
		for( VisualComNome v : visuais )
			if( nome.equals( v.nome ) ){
				atual = v;
				return;
			}
	}
	
	public String getVisualAtual( ) {
		return atual!=null? atual.nome: null;
	}
	
	public String[] getNomesVisuais() {
		String nomes[] = new String[ visuais.size() ];
		for( int i=0; i < nomes.length; i++ )
			nomes[i] = visuais.get(i).nome;
		
		return nomes;
	}
	
	public ComponenteVisual getComponenteVisual( String nome ) {
		for( VisualComNome v : visuais )
			if( nome.equals( v.nome ) ){
				return v.visual;
			}
		return null;
	}
	
	public ComponenteVisual getComponenteVisualAtual( ) {
		return atual!=null? atual.visual: null;
	}
	
	@Override
	public ComponenteMultiVisual clone() {
		ComponenteMultiVisual cv = new ComponenteMultiVisual();
		for( VisualComNome v : visuais )
			cv.addComponenteVisual( v.nome, v.visual.clone() );
		cv.setPosicaoCentro( cv.getPosicaoCentro() );
		if( atual != null )
			cv.setVisualAtual( atual.nome );
		return cv;
	}
	
	@Override
	public int getAltura() {
		return atual.visual.getAltura();
	}

	@Override
	public int getComprimento() {
		return atual.visual.getComprimento();
	}

	@Override
	public Point getPosicao() {
		return atual.visual.getPosicao();
	}

	@Override
	public Point getPosicaoCentro() {
		return atual.visual.getPosicaoCentro();
	}

	@Override
	public Rectangle getBounds() {
		return atual.visual.getBounds();
	}

	@Override
	public Image getSprite() {
		return atual.visual.getSprite();
	}

	@Override
	public void setSprite(Image sprite) {
		atual.visual.setSprite(sprite);
	}

	@Override
	public void rodar(double angulo) {
		for( VisualComNome v : visuais )
			v.visual.rodar(angulo);
	}

	@Override
	public void setAngulo(double angulo) {
		for( VisualComNome v : visuais )
			v.visual.setAngulo(angulo);
	}

	@Override
	public double getAngulo() {
		return atual.visual.getAngulo();
	}

	@Override
	public int numCiclosFeitos() {
		return atual.visual.numCiclosFeitos();
	}

	@Override
	public boolean eCiclico() {
		return atual.visual.eCiclico();
	}

	@Override
	public void setCiclico(boolean ciclico) {
		atual.visual.setCiclico(ciclico);
	}

	@Override
	public void reset() {
		atual.visual.reset();
	}
	
	@Override
	public void inverter() {		
		atual.visual.inverter();
	}
	
	@Override
	public void setPausa(boolean pausa) {
		super.setPausa(pausa);
		atual.visual.setPausa( pausa );
	}

	private class VisualComNome {
		String nome;
		ComponenteVisual visual;
		
		public VisualComNome(String nome, ComponenteVisual visual) {
			this.nome = nome;
			this.visual = visual;
		}
	}
}
