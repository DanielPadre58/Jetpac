package prof.jogos2D.movimento;

import prof.jogos2D.util.Vector2D;

public class MovimentoRe extends MovimentoDecorator {

	public MovimentoRe(Movimento m) {
		super(m);
	}

	@Override
	public double getAngulo() {
		return super.getAngulo() + Math.PI;
	}
	
	@Override
	public Vector2D getDirecao() {
		Vector2D v = super.getDirecao();
		v.x = -v.x;
		v.y = -v.y;
		return v;
	}
	
	public Movimento clone() {
		return new MovimentoRe( super.clone() );
	}
}
