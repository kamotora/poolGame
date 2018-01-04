package billiard;

import java.io.File;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class billiardsBall {
    
    private short type; // Цвет шара в виде ID:
    public final short BLACK = 0, WHITE = 1, BLUE = 2, RED = 3; //ID шаров.
    
    //Координаты бортиков стола.
    private final float borderX1 = 0.75f;
    private final float borderY1 = 0.525f;
    private final float borderX2 = 0.81f;
    private final float borderY2 = 0.585f;
    
    private float posX;
    private float posY;
    private final float size = 0.07f;
    private Texture tex;
    
    //Текущая скорость шара по координатам.
    private float currentSpeedX = 0;
    private float currentSpeedY = 0;
    
    //Возвращает путь к директории проекта.
    private String getRootDir(){
        
        return new File(".").getAbsoluteFile().getParentFile().getAbsolutePath();
    }
    
    /**
     * Создать шар с необходимыми параметрами.
     * 
     * @param posXf
     * @param posYf
     * @param sType
     * @throws IOException
     */
    public void createBall(float posXf, float posYf, short sType) throws IOException{
        
        this.posX = posXf;
        this.posY = posYf;
        this.type = sType;
        
        switch(type){
            
            case BLACK:
                this.tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/black.png"));
                break;
                
            case WHITE:
                this.tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/white.png"));
                break;
                
            case RED:
                this.tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/red.png"));
                break;
                
            case BLUE:
                this.tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/blue.png"));
                break;
        }
    }
    
    /**
     * Отрисовать шар.
     */
    public void drawBall(){
        
        //Рисуем шар только если он создан.
        if(isAlive()){
            GL11.glEnable(GL11.GL_QUADS);
            GL11.glEnable(GL11.GL_TEXTURE_2D);

            tex.bind();

            GL11.glBegin(GL11.GL_QUADS);

            GL11.glVertex2f(posX, posY+size);                   GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(posX+size-0.015f, posY+size);       GL11.glTexCoord2f(1, 1); 
            GL11.glVertex2f(posX+size-0.015f, posY);            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(posX, posY);                        GL11.glTexCoord2f(0, 0);

            GL11.glEnd();

            GL11.glDisable(GL11.GL_QUADS);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }
    
    /**
     * Переместить шар, основываясь на векторе направления и скорости.
     */
    public void moveBall(){
        
        //Двигаем шар только если он существует. Для повышения производительности.
        if(isAlive()){
            //Если скорость около нуля - обнуляем ее.
            if(currentSpeedX<0.00001f && currentSpeedY<0.00001f && currentSpeedX>-0.00001f && currentSpeedY>-0.00001f){

                currentSpeedX=0;
                currentSpeedY=0;
            }

            //Обрабатываем столкновения с бортиками. При столкновении отражаем направление по нужной оси и уменьшаем скорость в 2 раза. (уменьшение скорости можно подстроить)
            if(posY >= borderY1 || posY <= borderY2 * -1){

                currentSpeedY *= -0.5;
                posY += currentSpeedY;
            }
            else if(posX >= borderX1 || posX <= borderX2 * -1){

                currentSpeedX *= -0.5;
                posX += currentSpeedX;
            }

            //Снижение скорости и передвижение шара.
            currentSpeedX = currentSpeedX * 0.999f;
            currentSpeedY = currentSpeedY * 0.999f;     
            posX += currentSpeedX;
            posY += currentSpeedY;
        }
    }
    
    /**
     * Возвращает истину, если шар попал в лунку.
     * 
     * @return
     */
    public boolean isGoal(){
        
        if(borderX1*-1f - 0.03f > posX && borderY2*-1+0.03f > posY) return true; // Левая  нижняя лунка
        
        if(borderX2 - 0.07f< posX && borderY2*-1+0.03f > posY) return true; // Правая нижняя лунка
        
        if(borderX1*-1f - 0.03f > posX && borderY1-0.03f < posY) return true; // Левая верхняя лунка
        
        if(borderX2 - 0.07f< posX && borderY1-0.03f < posY) return true; // Правая верхняя лунка
        
        if(0 - 0.05f < posX && borderY1-0.02f < posY && 0 + 0.05f > posX) return true; // Центральная верхняя лунка
        
        return (0 - 0.05f < posX && borderY2*-1+0.02f > posY && 0 + 0.05f > posX); // Центральная нижняя лунка
    }
    
    /**
     * Возвращает ложь, если шар уничтожен.
     * 
     * @return
     */
    public boolean isAlive(){
        
        // Шар считается уничтоженым, если на нем нет текстуры.
        
        return this.tex!=null;
    }
    
    /**
     * Уничтожение шара.
     */
    public void destroy(){
        
        this.tex=null;
        this.currentSpeedX=0;
        this.currentSpeedY=0;
    }
    
    /**
     * Выставить скорость и вектор.
     * 
     * @param x
     * @param y
     */
    public void setSpeed(float x, float y){
        
        this.currentSpeedX = x;
        this.currentSpeedY = y;
    }
    
    /**
     * Получить скорость по оси Х
     * 
     * @return
     */
    public float getSpeedX(){
    
        return this.currentSpeedX;
    }
    
    /**
     * Получить скорость по оси У
     * 
     * @return
     */
    public float getSpeedY(){
    
        return this.currentSpeedY;
    }
    
    /**
     * Получить позицию по оси Х
     * 
     * @return
     */
    public float getPosX(){
        
        return this.posX;
    }
    
    /**
     * Получить позицию по оси Y
     * @return
     */
    public float getPosY(){
        
        return this.posY;
    }
    
    /**
     * Задать позицию по оси Х
     * 
     * @param newX
     */
    public void setPosX(float newX){
        
        this.posX = newX;
    }
    
    /**
     * Задать позицию по оси Y
     * @param newY
     */
    public void setPosY(float newY){
        
        this.posY = newY;
    }
    
    /**
     * Получить размер шара.
     * @return
     */
    public float getSize(){
        
        return this.size-0.015f;
    }
}
