package prof.jogos2D.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Classe que permite saber quais as teclas que est�o premidas, em cada momento.
 * @author F. S�rgio Barbosa
 */
public class SKeyboard {

	// conjunto com as teclas premidas 
	private HashSet<Integer> teclas = new HashSet<Integer>();
	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 */ 
	public SKeyboard( ) {				
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
				new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if( e.getID() == KeyEvent.KEY_PRESSED )
							teclas.add( e.getKeyCode() );
						else if( e.getID() == KeyEvent.KEY_RELEASED )
							teclas.remove( e.getKeyCode() );
						return false;
					}
				}
		);		
	}
	
	/**
	 * Indica se uma dada tecla est� premida
	 * @param keyCode o c�digo da tecla que se pretende ver se est� premida
	 * @return true se a tecla est� premida, false caso contr�rio
	 */
	public boolean estaPremida( int keyCode ){
		return teclas.contains( keyCode );
	}
	
	/** indica as teclas premidas
	 * @return uma lista com os c�digos das teclas premidas
	 */
	public Collection<Integer> getTeclasPremidas() {
		return Collections.unmodifiableCollection( teclas );
	}
	
	/** indica se h� alguma tecla premida 
	 * @return true, se pelo menos uma tecla estiver premida
	 */
	public boolean temTeclaPremida() {
		return teclas.size() > 0;
	}
	
	/** reinicia as teclas. Isto significa que todas as teclas ficam
	 * como n�o estando pressionadas. Vai obrigar a libertar e voltar a 
	 * pressionar as teclas que estiverem premidas para que corresponda
	 * �s a��es do teclado. � �til quando se muda de janela ativa
	 * por algum momento. 
	 */
	public void limpaTeclas() {
		teclas.clear();
	}
}
