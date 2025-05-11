package jetpac.generator;

import java.awt.Point;

import jetpac.drag.Tesouro;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Esta classe armazena a informação sobre cada um dos tipos de tesouro a criar
 * num dado nível e que permitirá ao gerador de tesouros criar os tesouros
 * corretos
 */
public class TreasureInfo {

	private int probability; // probabilidade de sair este tipo de tesouro
	private int lifeTime; // duração do tesouro
	private int score; // pontuação de cada tesouro
	private ComponenteVisual img; // imagem de cada tesouro

	/**
	 * Cria a informação para um tesouro
	 * 
	 * @param probability probabilidade de sair o tesouro
	 * @param lifeTime    tempo de vida do tesouro
	 * @param score       pontuação do tesouro
	 * @param img         imagem do tesouro
	 */
	public TreasureInfo(int probability, int lifeTime, int score, ComponenteVisual img) {
		this.probability = probability;
		this.lifeTime = lifeTime;
		this.score = score;
		this.img = img;
	}

	/**
	 * Cria um tesouro de acordo com estas especificações
	 * 
	 * @param p coordenada onde colocar o tesouro
	 * @return o tesouro criado
	 */
	public Tesouro createTresure(Point p) {
		return new Tesouro(p, lifeTime, score, img);
	}

	/**
	 * retorna a probabilidade deste tipo de tesouro
	 * 
	 * @return a probabilidade deste tipo de tesouro
	 */
	public int getProbability() {
		return probability;
	}

	/**
	 * define a probabilidade deste tipo de tesouro
	 * 
	 * @param probability a probabilidade deste tipo de tesouro
	 */
	public void setProbabilidade(int probability) {
		this.probability = probability;
	}

	/**
	 * retorna o tempo de vida do tesouro
	 * 
	 * @return o tempo de vida do tesouro
	 */
	public int getLifeTime() {
		return lifeTime;
	}

	/**
	 * define o tempo de vida do tipo de tesouro
	 * 
	 * @param lifeTime o tempo de vida do tipo de tesouro
	 */
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	/**
	 * retorna a pontuação deste tipo de tesouro
	 * 
	 * @return a pontuação deste tipo de tesouro
	 */
	public int getScore() {
		return score;
	}

	/**
	 * define a pontuação para este tipo de tesouro
	 * 
	 * @param score a pontuação a usar
	 */
	public void setPontos(int score) {
		this.score = score;
	}

	/**
	 * retorna a imagem a usar para este tipo de tesouro
	 * 
	 * @return a imagem a usar para este tipo de tesouro
	 */
	public ComponenteVisual getImg() {
		return img;
	}
}
