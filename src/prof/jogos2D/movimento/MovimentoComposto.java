package prof.jogos2D.movimento;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import prof.jogos2D.util.Vector2D;

public class MovimentoComposto implements Movimento {

	private ArrayList<Movimento> movimentos = new ArrayList<Movimento>();
	private int movIdx;
	private Movimento atual;
	
	public MovimentoComposto() {		
	}

	public void addMovimento( Movimento m ) {
		movimentos.add( m );
	}
	
	public void removeMovimento( Movimento m ) {
		movimentos.remove( m );
	}
	
	public List<Movimento> getMovimentos(){
		return Collections.unmodifiableList( movimentos );
	}
	
	@Override
	public void startMovimento(Double p) {
		movIdx = 0;
		atual = movimentos.get(movIdx);
		atual.startMovimento(p);		
	}

	@Override
	public void move(double vel) {
		if( atual.estaFim() ) {
			movIdx++;
			if( movIdx >= movimentos.size() ) {
				movIdx = movimentos.size();
				return;
			}
			Point2D.Double posAviao = atual.getPosicao();
			atual = movimentos.get( movIdx );
			atual.startMovimento( posAviao );
		}
		
		atual.move( vel);
	}
	
	@Override
	public Double getNextPoint(double vel) {
		return atual.getNextPoint(vel);
	}

	@Override
	public Double getPosicao() {
		return atual.getPosicao();
	}

	@Override
	public Vector2D getDirecao() {
		return atual.getDirecao();
	}

	@Override
	public boolean estaFim() {
		return movimentos.get( movimentos.size()-1).estaFim();
	}

	@Override
	public double getAngulo() {
		return atual.getAngulo();
	}

	public Movimento clone() {
		MovimentoComposto mc = new MovimentoComposto();
		for( Movimento m : movimentos )
			mc.addMovimento( m.clone( ) );
		return mc;
	}
}
