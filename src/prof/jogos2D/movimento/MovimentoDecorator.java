package prof.jogos2D.movimento;

import java.awt.geom.Point2D.Double;

import prof.jogos2D.util.Vector2D;

public abstract class MovimentoDecorator implements Movimento {

	private Movimento m;
	
	public MovimentoDecorator(Movimento m) {
		this.m = m;
	}
	
	@Override
	public void startMovimento(Double p) {
		m.startMovimento( p );
	}

	@Override
	public void move(double vel) {
		m.move( vel );
	}
	
	@Override
	public Double getNextPoint(double vel) {
		return m.getNextPoint(vel);
	}

	@Override
	public Double getPosicao() {
		return m.getPosicao();
	}

	@Override
	public Vector2D getDirecao() {
		return m.getDirecao();
	}

	@Override
	public boolean estaFim() {
		return m.estaFim();
	}

	@Override
	public double getAngulo() {
		return m.getAngulo();
	}
	
	public Movimento clone() {
		return m.clone();		
	}
}
