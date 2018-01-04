package billiard;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class engine {
    
    //Возвращает путь к директории проекта.
    private static String getRootDir(){
        
        return new File(".").getAbsoluteFile().getParentFile().getAbsolutePath();
    }
    
    /**
     * Подключение библиотек OpenGL
     * 
     * @throws IOException
     */
    public static void path()throws IOException{
        
        String osName = System.getProperty("os.name").toLowerCase().substring(0, System.getProperty("os.name").indexOf(' '));
        String osBit = System.getProperty("sun.arch.data.model");
        
        if (osBit.equals("32")) osBit = "86";
        
        System.setProperty("org.lwjgl.librarypath", new File(getRootDir().concat("/lib/native/").concat(osName).concat("/x").concat(osBit)).getAbsolutePath());
        System.setProperty("org.newdawn.librarypath", new File(getRootDir().concat("/lib/native/").concat(osName).concat("/x").concat(osBit)).getAbsolutePath());
        System.setProperty("com.xuggle.xuggler.librarypath", new File(getRootDir().concat("/lib/native/").concat(osName).concat("/x").concat(osBit)).getAbsolutePath());
    }
    
    /**
     * Создать полноэкранное окно, использую разрешение экрана рабочего стола.
     * Create FullScreen Window.
     * 
     * @throws LWJGLException
     */
    public static void createWindow_FullScreen() throws LWJGLException{
        
        Display.setTitle("Billiards");
        
        Display.setFullscreen(true);
        DisplayMode desktopDispMode = Display.getDesktopDisplayMode();
        Display.setDisplayMode(desktopDispMode);
        
        Display.create();
        
        enableAlpha();
    }
    
    /** 
     * Включить поддержку прозрачности в текстурах.
     * Turn on alpha in textures.
     */
    public static void enableAlpha(){
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    /**
     * Создать окно, задав размер в пикселях.
     * Create Window.
     * 
     * @param width
     * @param height
     * @throws LWJGLException
     */
    public static void createWindow_Windowed(int width, int height) throws LWJGLException{
        
        Display.setTitle("billiards");
        
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(width, height));
        
        Display.create();
        
        enableAlpha();
    }

    /**
     * Расставить шары на столе и передать их как массив объектов для последующей работы с ними.
     * 
     * @return
     * @throws java.io.IOException
     */
    public static billiardsBall[] createBalls() throws IOException{
        
        billiardsBall[] balls = new billiardsBall[16];
        for(short i=0;i<16;i++){
            
            switch(i){
                case 0:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(-0.5f, 0f, balls[i].WHITE);
                    break;
                case 1:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.35f, 0, balls[i].BLUE);
                    break;
                case 2:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.41f, 0.04f, balls[i].RED);
                    break;
                case 3:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.41f, -0.04f, balls[i].BLUE);
                    break;
                case 4:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.47f, 0.08f, balls[i].BLUE);
                    break;
                case 5:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.47f, 0f, balls[i].BLACK);
                    break;
                case 6:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.47f, -0.08f, balls[i].RED);
                    break;
                case 7:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.53f, -0.12f, balls[i].RED);
                    break;
                case 8:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.53f, -0.04f, balls[i].BLUE);
                    break;
                case 9:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.53f, 0.04f, balls[i].RED);
                    break;
                case 10:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.53f, 0.12f, balls[i].BLUE);
                    break;
                case 11:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.59f, 0.16f, balls[i].BLUE);
                    break;
                case 12:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.59f, 0.08f, balls[i].RED);
                    break;
                case 13:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.59f, 0f, balls[i].RED);
                    break;
                case 14:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.59f, -0.08f, balls[i].BLUE);
                    break;
                case 15:
                    balls[i] = new billiardsBall();
                    balls[i].createBall(0.59f, -0.16f, balls[i].RED);
                    break;
            }
        }
            
        return balls;
    }
    
    /**
     * Отрисовка всех шаров.
     * 
     * @param balls
     */
    public static void drawBalls(billiardsBall[] balls){
        
        for(billiardsBall current: balls){
            
            current.drawBall();
        }
    }
    
    /**
     * Возвращает истину, если хотя-бы один шар не остановлен.
     * 
     * @param balls
     * @return
     */
    public static boolean ballsIsRolling(billiardsBall[] balls){
        
        for(billiardsBall current: balls){
            
            if(current.getSpeedX()!=0 || current.getSpeedY()!=0) return true;
        }
        return false;
    }
    
    /** 
     * Обработка столновений и передвижение шаров.
     *
     * @param balls
     * @param sound
     * @param blocked
     * @throws javax.sound.sampled.LineUnavailableException
     * @throws java.io.IOException
     */
    public static void moveBalls(billiardsBall[] balls, Clip sound, boolean blocked) throws LineUnavailableException, IOException{
        
        for(int i=0;i<balls.length;i++){
            
            //Если шар попал в лунку - уничтожаем его и переходим к следующему шагу цикла.
            if(balls[i].isGoal()){
                
                balls[i].destroy();
                continue;
            }
            
            for(int j=0;j<balls.length;j++){
                
                //Если шар попал в лунку - уничтожаем его и переходим к следующему шагу цикла.
                if(balls[i].isGoal()){
                    
                    balls[i].destroy();
                    continue;
                }
                
                // Работаем с шарами только если они оба не уничтожены. 
                if(balls[i].isAlive() && balls[j].isAlive()){
                    
                    if(i==j)continue;

                    // Проверка на столкновение. Если столкнулись то производится расчет скорости и направления.
                    float size = balls[i].getSize();

                    //Если расстояние между шарами мало
                    if(Math.abs(balls[i].getPosX() - balls[j].getPosX()) < size - 0.005f && Math.abs(balls[i].getPosY() - balls[j].getPosY()) < size + 0.005f) {

                        //Звук столкновения.
                        sound.stop();
                        sound.setFramePosition(45000);
                        sound.start();

                        float vectorX1 = 0; float vectorY1 = 0; // шар J
                        float vectorX2 = 0; float vectorY2 = 0; // шар I

                        //Тут какая-то непонятная физика соударения, взятая с интернета
                        if(!blocked){
                            // Работаем с шаром J. Получаем направление шара в виде векторов и расчитываем скорость.
                            
                            if(balls[j].getSpeedX()<0.005f && balls[i].getSpeedX()<0.005f) vectorX1 = (balls[j].getPosX() - balls[i].getPosX()) * (Math.abs(balls[i].getSpeedX())*15 + 0.001f);
                            else vectorX1 = (balls[j].getPosX() - balls[i].getPosX()) * (Math.abs(balls[i].getSpeedX())*15);
                            
                            if(balls[j].getSpeedY()<0.01f && balls[i].getSpeedY()<0.01f) vectorY1 = (balls[j].getPosY() - balls[i].getPosY()) * (Math.abs(balls[i].getSpeedY())*15 + 0.001f);
                            else vectorY1 = (balls[j].getPosY() - balls[i].getPosY()) * (Math.abs(balls[i].getSpeedY())*15);

                            // Работаем с шаром I
                            if(vectorX1>vectorY1){
                                vectorX2 = vectorX1 * -1f;
                                vectorY2 = vectorY1;
                            } else{
                                vectorX2 = vectorX1;
                                vectorY2 = vectorY1 * -1f;
                            }
                        }
                        else {
                            
                            balls[0].setPosX(0f);
                            balls[0].setPosY(0f);
                        }
                        //Выставляем вектора и по ним двигаем оба шара.
                        balls[i].setSpeed(vectorX2, vectorY2);
                        balls[j].setSpeed(vectorX1, vectorY1);

                        balls[j].moveBall();
                        balls[i].moveBall();
                    }
                }
            }
        }
        //Двигаем все шары.
        for(billiardsBall current: balls) current.moveBall();
    }
    
    /**
     * Управление белым шаром в момент восстановления.
     * 
     * @param white
     */
    public static void removeWhite(billiardsBall white){
        
        //Управление.
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            if(white.getPosX() < 0.75f && white.getPosX() > -0.80f) white.setPosX(white.getPosX()+0.001f);
            else white.setPosX(0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            if(white.getPosX() < 0.75f && white.getPosX() > -0.80f) white.setPosX(white.getPosX()-0.001f);
            else white.setPosX(0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            if(white.getPosY() < 0.525f && white.getPosY() > -0.585f) white.setPosY(white.getPosY()+0.001f);
            else white.setPosY(0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            if(white.getPosY() < 0.525f && white.getPosY() > -0.585f) white.setPosY(white.getPosY()-0.001f);
            else white.setPosY(0);
        }
        
        if(Mouse.next()){
            
            if(white.getPosY() < 0.525f && white.getPosY() > -0.585f && white.getPosX() < 0.75f && white.getPosX() > -0.80f){
                
                white.setPosX(engine.getFloatX(Mouse.getX()));
                white.setPosY(engine.getFloatY(Mouse.getY()));
            }
            else {
                
                white.setPosX(0);
                white.setPosY(0);
            }
        }
    }
    
    /**
     * Метод нужен для метода clip.
     * 
     * @param track
     * @return
     * @throws IOException
     */
    public static AudioInputStream InputAud(String track)throws IOException{
        
        File aud = new File(track); //.wav формат только.
        AudioInputStream InputAud=null;
        
        try {
            InputAud = AudioSystem.getAudioInputStream(aud);
        } catch (UnsupportedAudioFileException | IOException ex) {return null;}
        
        return InputAud;
    }
    
    /**
     * Получение объекта Clip, через который можно работать с музыкой и звуками. Поддерживается только формат .wav
     * 
     * @param track
     * @return
     * @throws LineUnavailableException
     * @throws IOException
     */
    public static Clip clip(String track)throws LineUnavailableException,IOException{
        
        AudioInputStream input = InputAud(track);
        Clip clip=AudioSystem.getClip();
        clip.setFramePosition(0);
        try {
            clip.open(input);
        } catch (IOException ex) {return null;}
        
        return clip;
    }
    
    /**
     * Получения координаты из пикселя по оси X
     * 
     * @param x
     * @return
     */
    public static float getFloatX(int x){
        
        return (float)x/(float)Display.getWidth()*2-1;
    }
    
    /**
     * Получения координаты из пикселя по оси Y
     * 
     * @param y
     * @return
     */
    public static float getFloatY(int y){
        
        return (float)y/(float)Display.getHeight()*2-1;
    }
}
