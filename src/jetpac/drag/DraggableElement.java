package jetpac.drag;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import jetpac.astro.Astronauta;
import jetpac.astro.Plataforma;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

public class DraggableElement implements Draggable {
    /** Tempo que demora até poder pegar outra vez no objeto */
	protected static final int TEMPO_ENTRE_APANHADAS = 30;

    protected State estado = State.FALLING;
	protected Mundo world;
	protected ComponenteVisual imagem;
	protected int offsetX, offsetY; // offset da posição do astronauta quando a ser arrastado
	protected int nextDrag = 0;

    public DraggableElement(){}

    @Override
    public void draw(Graphics2D g) {
		imagem.desenhar(g);
	}

    @Override
    public Rectangle getBounds() {
		return imagem.getBounds();
	}

    @Override
    public boolean isActive() {
		return estado != State.DELIVERED;
	}

    @Override
    public boolean isDraggable() {
		return (estado == State.FALLING || estado == State.REST) && nextDrag == 0;
	}

    @Override
    public boolean isFalling() {
		return estado == State.FALLING;
	}

    @Override
    public void move(int dx, int dy) {
		imagem.getPosicao().translate(dx, dy);
	}

    @Override
    public void release() {
		setEstado(State.FALLING);
		nextDrag = TEMPO_ENTRE_APANHADAS;
	}

    @Override
    public void setWorld(Mundo w) {
		world = w;
	}

    @Override
    public Mundo getWorld() {
		return world;
	}

    @Override
    public void update(){
        Astronauta astronauta = getWorld().getAstronaut();
		if (estado == State.DRAGGED) {
			updatePosicao(astronauta);
			testDropZone(astronauta);
			return;
		}

        if (nextDrag > 0)
			nextDrag--;

        if (estado == State.FALLING || estado == State.DROPING) {
			updateFall();
		}

        verSeFoiApanhado(astronauta);

        // já chegou à nave?
		if (getWorld().getMainSpaceship().getBounds().intersects(getBounds())) {
			delivered();
		}
    }

    @Override
    public void verSeFoiApanhado(Astronauta astronauta) {
		// se já tem alguma coisa ou este não é apanhável, não apanha
		if (!astronauta.isDragging() && isDraggable()) {
			if (astronauta.getBounds().intersects(imagem.getBounds())) {
				apanhado(astronauta);
			}
		}
	}

    @Override
    public void apanhado(Astronauta astronauta) {
		Rectangle astroBounds = astronauta.getBounds();
		astronauta.pickItem(this);
		offsetX = (astroBounds.width - getBounds().width) / 2;
		offsetY = (astroBounds.height - getBounds().height);
		estado = State.DRAGGED;
	}

    @Override
    public void updateFall() {
		move(0, 2);
		for (Plataforma p : getWorld().getPlatforms()) {
			Vector2D toqueV = p.hitV(getBounds());
			// se bateu em algum lado pára
			if (toqueV.x != 0 || toqueV.y != 0) {
				move((int) toqueV.x, (int) toqueV.y);
				estado = State.REST;
			}
		}
	}

    /**
	 * atualiza a posição em função do astronauta
	 *
	 * @param astronauta o astronauta
	 */
    @Override
    public void updatePosicao(Astronauta astronauta) {
		Point pos = astronauta.getPosition();
		imagem.setPosicao(new Point(pos.x + offsetX, pos.y + offsetY));
	}

    /**
	 * Verifica se ao ser arrastada pelo astronauta está sobre a drop zone
	 *
	 * @param astronauta o astronauta
	 */
    @Override
    public void testDropZone(Astronauta astronauta) {
		// verificar se está na drop zone
		Rectangle dropArea = getWorld().getMainSpaceship().getDropArea();
		if (dropArea.intersects(getBounds())) {
			astronauta.drop();
			estado = State.DROPING;
			// centrar em relação à dropArea
			Point oldPos = imagem.getPosicaoCentro();
			imagem.setPosicaoCentro(new Point(dropArea.x + dropArea.width / 2, oldPos.y));
		}
	}

    @Override
    public State getEstado() {
		return estado;
	}

    @Override
	public void setEstado(State estado) {
		this.estado = estado;
	}

    @Override
	public ComponenteVisual getImagem() {
		return imagem;
	}

    @Override
	public void delivered() {
		setEstado(State.DELIVERED);
	}
}
