package prof.jogos2D.util;

import java.util.Random;

/** classe auxiliar que devolve alguns n�meros aleat�rios
 * Tendo esta classe, todas as partes do jogo usam o mesmo gerador
 * de n�meros aleat�rios e assim at� se poderiam repetir jogos usando
 * a mesma semente na gera��o dos n�meros (ainda n�o implementada)
 * 
 * @author F. S�rgio Barbosa
 */
public class RandomGenerator {

	public static Random random = new Random();
	
	public static void startGenerating( long seed ){
		random = new Random( seed );
	}

	/** devolve um n�mero inteiro aleat�rio */
	public static int nextInt() {
		return random.nextInt();
	}

	/** devolve um n�mero aleat�rio desde 0 at� bound (exclusivo) */
	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}

	/**
	 * retorna um n�mero aleat�rio (distribui��o uniforme) entre a gama ini (inclusiv�)
	 * e end (exclusive)
	 * @param ini n�mero minimo (inclusive) a sair
	 * @param end n�mero m�ximo (exclusive) a sair
	 * @return
	 */
	public static int nextRange( int ini, int end ){
		return ini + random.nextInt( end - ini );
	}
	
	/** devolve um booleano aleat�rio */
	public static boolean nextBoolean() {
		return random.nextBoolean();
	}

	/** devolve um n�mero float aleat�rio (entre 0 e 1)*/
	public static float nextFloat() {
		return random.nextFloat();
	}

	/** devolve um n�mero double aleat�rio (entre 0 e 1)*/
	public static double nextDouble() {
		return random.nextDouble();
	}
}
