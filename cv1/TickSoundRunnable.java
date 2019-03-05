package cv1;


public class TickSoundRunnable extends SoundRunnable {
    public TickSoundRunnable(String path) {
        super(path);
    }

    @Override
    public void run() {
        try {
            while (true) {
                clip.setFramePosition(0);
                clip.start();
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
