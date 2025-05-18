package jetpac.astro;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.enemy.InimigoDefault;
import jetpac.mundo.WorldElementDefault;

/**
 * Classe que representa um raio laser
 */
public class Laser extends WorldElementDefault {

	private int lifeTime; // duração do laser
	private int range; // alcance do laser

	// estilo de linha do laser
	private static final Stroke laserStyle = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final Color color; // cor do laser
	private final Line2D.Double laser; // linha do laser no écran

	private boolean dead = false; // se o laser está desativo

	/**
	 * Cria um laser
	 * 
	 * @param pos     posição inical do laser
	 * @param alcance alcance do laser
	 */
	public Laser(Point pos, int alcance) {
		super(null);
		lifeTime = 10;
		range = alcance / lifeTime;
		Color colors[] = { Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN };
		color = colors[ThreadLocalRandom.current().nextInt(colors.length)];
		laser = new Line2D.Double(pos.x, pos.y, pos.x + range, pos.y);
	}

	/**
	 * desenha a linha do laser
	 */
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(laserStyle);
		g.draw(laser);
	}

	/**
	 * atualiza o laser em cada ciclo de processamento
	 */
	public void update() {
		// movimentar o laser
		laser.x2 += range;
		laser.x1 += (range * (lifeTime > 8 ? 0 : 1));

		if (laser.x2 > getWorld().getWidth())
			laser.x2 = getWorld().getWidth();
		if (laser.x1 > getWorld().getWidth())
			laser.x1 = getWorld().getWidth();

		// ver se ainda está ativo
		lifeTime--;
		if (lifeTime <= 0) {
			die();
			return;
		}

		// ver se está a bater nas plataformas
		for (Plataforma f : getWorld().getPlatforms()) {
			Rectangle r = f.getBounds();
			if (r.intersectsLine(laser)) {
				laser.x2 = laser.x1 <= r.x ? r.x : r.x + r.width;
				range = 0;
			}
		}
		// ver se bate nos inimigos
		for (InimigoDefault e : getWorld().getEnemies()) {
			Rectangle r = e.getImagem().getBounds();
			if (!e.isDying() && r.intersectsLine(laser)) {
				laser.x2 = laser.x1 <= r.x ? r.x : r.x + r.width;
				e.die();
				// adicionar pontos a este ciclo
				getWorld().addCiclePoints(e.getScore());
			}
		}
	}

	/**
	 * desativa o laser
	 */
	public void die() {
		dead = true;
	}

	/**
	 * testa se o laser está desativo
	 * 
	 * @return true, se o laser está ativo
	 */
	public boolean isDead() {
		return dead;
	}

}
