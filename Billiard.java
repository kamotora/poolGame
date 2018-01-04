package billiard;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Billiard {
    
    static boolean soundOn = false;
    public static void main(String[] args) throws IOException, LWJGLException, InterruptedException, LineUnavailableException {
        
        engine.path();
        
        init(true);
        //Разрешение экрана в оконном режиме можно задать в методы init();
    }
    
    /**
     * Создание полноэкранного или оконного игрового окна и запуск меню.
     * 
     * @param isFullScreen
     * @throws LWJGLException
     * @throws IOException
     * @throws InterruptedException
     * @throws LineUnavailableException
     */
    public static void init(boolean isFullScreen) throws LWJGLException, IOException, InterruptedException, LineUnavailableException{
        
        if(Display.isCreated()) Display.destroy();
        
        if(isFullScreen) engine.createWindow_FullScreen();
        else engine.createWindow_Windowed(1280, 720); //Можно задать свое разрешение экрана.
        
        gameMenu();
    }
    
    //Возвращает путь к директории проекта.
    private static String getRootDir(){
        
        return new File(".").getAbsoluteFile().getParentFile().getAbsolutePath();
    }
    
    /**
     * Игровое меню.
     * 
     * @throws IOException
     * @throws LWJGLException
     * @throws InterruptedException
     * @throws LineUnavailableException
     */
    public static void gameMenu() throws IOException, LWJGLException, InterruptedException, LineUnavailableException{

        //Запуск стартовой мелодии
        Clip music = engine.clip(getRootDir()+"/res/music.wav");
        FloatControl vc2 = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        vc2.setValue(-30f);
        music.setFramePosition(0);
        if(soundOn && !music.isActive()) music.start();
        
        //Обновление клавиатуры, чтобы нажатые клавишы до входа в меню не учитывались.
        Keyboard.destroy();
        Keyboard.create();
        
        // Создаем объекты через текстуры и координаты.
        obj2D background = new obj2D();
        background.createObjectQuad(-1, -1, 2, 2);
        background.bindTextureFromFile("background.png");

        //Стол
        obj2D table = new obj2D();
        table.createObjectQuad(-1, -1, 2, 2);
        table.bindTextureFromFile("table.png");

        //Кнопки меню
        obj2D mode1 = new obj2D();
        mode1.createObjectQuad(-0.7f, 0f, 0.3f, 0.5f);
        mode1.bindTextureFromFile("mode1.png");
        
        obj2D wind = new obj2D();
        wind.createObjectQuad(-0.15f, 0f, 0.3f, 0.4f);
        wind.bindTextureFromFile("windowed.png");
        
        obj2D fs = new obj2D();
        fs.createObjectQuad(-0.15f, -0.4f, 0.3f, 0.4f);
        fs.bindTextureFromFile("fullscreen.png");
        
        obj2D exit = new obj2D();
        exit.createObjectQuad(0.4f, -0.4f, 0.3f, 0.4f);
        exit.bindTextureFromFile("exit.png");
        
        obj2D snd = new obj2D();
        snd.createObjectQuad(0.4f, 0f, 0.3f, 0.4f);
        if (!music.isActive()) snd.bindTextureFromFile("sndOff.png");
        else snd.bindTextureFromFile("sndOn.png");
        
        //При нажатии Escape в меню - выход из игры.
        while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            
            // Выход при нажатии на крестик.
            if(Display.isCloseRequested()) System.exit(0);

            //Отрисовка обьектов
            background.drawObjectQuad(true);
            table.drawObjectQuad(true);
            
            //Триггеры на наводку, действия при нажатии на объект и заполнение цветом объекта при наведении на него.
            if(mode1.isHover()){
                
                mode1.fillColor(true);

                //Запускаем при нажатии
                if(Mouse.isButtonDown(0))training();
            }
            else mode1.drawObjectQuad(true);
            
            if(wind.isHover()){
                
                wind.fillColor(true);
                if(Mouse.isButtonDown(0)){
                    
                    music.stop();
                    music.close();
                    init(false);
                }
            }
            else wind.drawObjectQuad(true);
            
            if(fs.isHover()){
                
                fs.fillColor(true);
                if(Mouse.isButtonDown(0)) {
                    
                    music.stop();
                    music.close();
                    init(true);
                }
            }
            else fs.drawObjectQuad(true);
            
            if(snd.isHover()){
                
                snd.fillColor(true);
                if(Mouse.isButtonDown(0)){
                    
                    if(music.isActive()) {
                        
                        snd.bindTextureFromFile("sndOff.png");
                        music.stop();
                    }
                    else {
                        
                        snd.bindTextureFromFile("sndOn.png");
                        music.start();
                    }
                    Mouse.destroy();
                    Mouse.create();
                }
            }
            else snd.drawObjectQuad(true);
            
            if(exit.isHover()){
                
                exit.fillColor(true);
                if(Mouse.isButtonDown(0)) {
                    
                    System.exit(0);
                }
            }
            else exit.drawObjectQuad(true);
            
            Thread.sleep(1);
            Display.update();
        }
    }
    
    /**
     * Режим тренировки.
     * 
     * @throws IOException
     * @throws LWJGLException
     * @throws InterruptedException
     * @throws LineUnavailableException
     */
    public static void training() throws IOException, LWJGLException, InterruptedException, LineUnavailableException {
        
        obj2D background = new obj2D();
        background.createObjectQuad(-1, -1, 2, 2);
        background.bindTextureFromFile("background.png");
        
        obj2D table = new obj2D();
        table.createObjectQuad(-1, -1, 2, 2);
        table.bindTextureFromFile("table.png");
        billiardsBall[] balls = engine.createBalls();
        
        Texture tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/kiy.png"));
        
        //Блок временных переменных. Некоторые можно удалить, но код будет длиннее и менее понятен.
        float kiyXcoord;
        float kiyYcoord;
        float angle = 13;
        float transF = balls[0].getPosX()*-1-0.03f;
        float power = 1;
        boolean plus_power = true;
        boolean ready = false;
        boolean removeWhite = false;
        objKiy kiy = new objKiy();
        
        //Загружаем звуки и регулируем их громкость.
        Clip soundShot = engine.clip(getRootDir()+"/res/shot.wav");
        FloatControl vc = (FloatControl) soundShot.getControl(FloatControl.Type.MASTER_GAIN);
        vc.setValue(-10f);
        soundShot.setFramePosition(20000);
        
        //Основной цикл. Прекращается нажатием кнопки ESCAPE. При прекращении закрывается игра.
        while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            
            // Выход при нажатии на крестик.
            if(Display.isCloseRequested()) System.exit(0);
            
            //Основной блок отрисовки.
            background.drawObjectQuad(true);
            table.drawObjectQuad(true);
            engine.moveBalls(balls, soundShot, removeWhite);
            engine.drawBalls(balls);
            
            // Запускаем процесс восстановления белого шара, если он был забит.
            if(!balls[0].isAlive() && !removeWhite) {
                
                removeWhite = true;
            }
            
            // Если забит черный шар - выходим в меню.
            if(!balls[5].isAlive()){

                gameMenu();
            }
            
            // Установка белого шара в новое положение.
            if(removeWhite){
                
                // Возвращаем шару текстуру.
                if(!balls[0].isAlive()) balls[0].createBall(0, 0, balls[0].WHITE);
                //Оставляем шар на месте при нажатии пробела или левой кнопки мыши.
                if((Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.isButtonDown(0) && balls[0].getPosY() < 0.525f && balls[0].getPosY() > -0.585f && balls[0].getPosX() < 0.75f && balls[0].getPosX() > -0.80f)) {
                    
                    removeWhite = false;
                    Keyboard.destroy();
                    Keyboard.create();
                }
                engine.removeWhite(balls[0]);
            }
            
            //Управление кием если шары не движутся.
            if(!engine.ballsIsRolling(balls) && !removeWhite){
                
                //Управляем кием. Стрелка вправо - вращение вправо, влево - вращение влево. Пробел (зажать) - удар.
                if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))angle+=0.1f;
                if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) angle-=0.1f;   
                if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                    
                    ready = true;

                    //Увеличиваем или уменьшаем мощность удара
                    if(plus_power){
                        
                        power+=0.5f;
                        transF-=0.0005;
                        if(transF<(balls[0].getPosX()*-1-0.03f)-0.15f)plus_power=false;
                    } else {
                        
                        if(power>1)power-=0.5f;
                        transF+=0.0005;
                        if(transF>(balls[0].getPosX()*-1-0.03f)+0.01f)plus_power=true;
                    }
                }
                else if(!ready) transF = balls[0].getPosX() * -1 - 0.03f; //Ставим кий на нужную позицию, если удар уже нанесен.
                
                kiyXcoord = (float)(-1 * Math.cos(Math.toRadians(angle)));
                kiyYcoord = (float)(-1 * Math.sin(Math.toRadians(angle)));
                
                if(angle==90 || angle==180) kiyXcoord = 0f;

                //Удар кием.
                if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && ready){
                    
                    if(transF<(balls[0].getPosX()*-1-0.015)){
                        
                        transF+=0.003;
                    } else {
                        
                        ready = false;
                        
                        // Удар.
                        float shotX = kiyXcoord * 0.0001f * -1 * power;
                        float shotY = kiyYcoord * 0.0001f * -1 * power;
                        
                        transF = balls[0].getPosX() * -1 - 0.03f;
                        balls[0].setSpeed(shotX, shotY);
                        
                        power = 1;
                    }
                }
                //Отрисовка кия.
                kiy.create(tex, angle, transF, balls[0]);
                kiy.draw();
            }
            
            //Обновленик экрана. Фиксированное количество кадров через простой в 1м необходимо для корректировки скорости игры и меньшей загруженности процессора.
            Thread.sleep(1);
            Display.update();
        }
        //Возвращаемся в меню при нажатии Escape.
        Keyboard.destroy();
        Keyboard.create();
        gameMenu();
    }
}
