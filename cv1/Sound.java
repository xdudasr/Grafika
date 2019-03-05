package cv1;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**pusta hlasovu vystrahu podla toho ktori senzor zaznamenal nebezpecnu hodnotu */
public class Sound {
	public void play(String name) {

		try {
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(name).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			/** odchytenie vynimiek*/
		} catch (Exception ex) {
			System.out.println("Chyba pri spusteny poplachu");
			ex.printStackTrace();
		}
	}
}