package jetpac.drag;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jetpac.astro.Astronauta;
import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;

public interface Draggable {

    void draw(Graphics2D g);

    Rectangle getBounds();

    boolean isActive();

    boolean isDraggable();

    boolean isFalling();

    void move(int dx, int dy);

    void release();

    void setWorld(Mundo w);

    Mundo getWorld();

    void update();

    void verSeFoiApanhado(Astronauta astronauta);

    void apanhado(Astronauta astronauta);

    void updateFall();

    void updatePosicao(Astronauta astronauta);

    void testDropZone(Astronauta astronauta);

    State getEstado();

    void setEstado(State estado);

    ComponenteVisual getImagem();

    void delivered();
}
