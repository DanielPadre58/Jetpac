package prof.jogos2D.util;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Classe que permite saber quais as teclas que estão premidas, em cada momento.
 * @author F. Sérgio Barbosa
 */
public class SKeyboard_old {
		
	private Component owner;                        // quem é o dono deste leitor de teclado
	private boolean asTeclas[] = new boolean[ 200 ]; // serão 200 suficientes??
	
	private MyKeyListener leitorTeclas = new MyKeyListener();
	private MyFocusListener leitorFocus  = new MyFocusListener();
	
	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 * @param owner
	 */ 
	public SKeyboard_old( ) {				
		this( KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() );		
	}

	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 * @param owner
	 */
	public SKeyboard_old( JFrame owner ) {				
		setOwner(owner);
		owner.addWindowListener( new MyWindowListener() );
	}
	
	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 * @param owner
	 */ 
	public SKeyboard_old( Component owner ) {				
		setOwner(owner);
	}
	/**
	 * Indica se uma dada tecla está premida
	 * @param keyCode o código da tecla que se pretende ver se está premida
	 * @return true se a tecla está premida, false caso contrário
	 */
	public boolean estaPremida( int keyCode ){
		return asTeclas[ keyCode ];
	}
	
	/** indica as teclas premidas
	 * @return um array com os códigos das teclas premidas
	 */
	public int[] getTeclasPremidas() {
		int teste[] = new int[ asTeclas.length ];
		int total = 0;
		for( int i=0; i < teste.length; i++ )
			if( asTeclas[i] ) {
				teste[total] = i;
				total++;
			}
		int premidas[] = new int[total];
		for( int i=0; i < total; i++ )
			premidas[i]=teste[i];
		return premidas;
	}
	
	/** indica se há alguma tecla premida 
	 * @return true, se pelo menos uma tecla estiver premida
	 */
	public boolean temTeclaPremida() {
		for( int i=0; i < asTeclas.length; i++ ) {
			if( asTeclas[i] )
				return true;
		}
		return false;
	}
	
	/** reinicia as teclas. Isto significa que todas as teclas ficam
	 * como não estando pressionadas. Vai obrigar a libertar e voltar a 
	 * pressionar as teclas que estiverem premidas para que corresponda
	 * às ações do teclado. É útil quando a janela perde o focus
	 * por algum momento. 
	 */
	public void limpaTeclas() {
		for( int i=0; i < asTeclas.length; i++ )
			asTeclas[i] = false;
	}
	
	/**
	 * Como este keyboard tem de estar sempre associado ao componente 
	 * com o focus se o focus mudar este também tem de mudar
	 * @param newOwner novo elemento com o focus
	 */
	public void setOwner(Component newOwner ) {
		if( owner != null ){
			owner.removeKeyListener( leitorTeclas );
			owner.removeFocusListener( leitorFocus );		
		}
		owner = newOwner;
		if( owner != null ){
			if( !owner.hasFocus() )
				owner.requestFocus();
			owner.addKeyListener( leitorTeclas );		
			owner.addFocusListener( leitorFocus );
		}
	}
	

	private class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent key) {
			asTeclas[ key.getKeyCode() ] = true;
		}

		public void keyReleased(KeyEvent key) {
			asTeclas[ key.getKeyCode() ] = false;
		}		
	}
	
	private class MyFocusListener extends FocusAdapter {		
		public void focusLost(FocusEvent e) {						
			setOwner( e.getOppositeComponent() );
		}
	}
	
	private class MyWindowListener extends WindowAdapter {
		@Override
		public void windowActivated(WindowEvent e) {
			super.windowActivated(e);
			setOwner( e.getWindow() );
		}
	}
}
